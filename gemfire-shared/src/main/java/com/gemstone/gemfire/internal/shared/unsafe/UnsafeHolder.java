/*
 * Copyright (c) 2010-2015 Pivotal Software, Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you
 * may not use this file except in compliance with the License. You
 * may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * permissions and limitations under the License. See accompanying
 * LICENSE file.
 */
/*
 * Changes for SnappyData distributed computational and data platform.
 *
 * Portions Copyright (c) 2017 SnappyData, Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you
 * may not use this file except in compliance with the License. You
 * may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * permissions and limitations under the License. See accompanying
 * LICENSE file.
 */

package com.gemstone.gemfire.internal.shared.unsafe;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

import com.gemstone.gemfire.internal.shared.ChannelBufferFramedInputStream;
import com.gemstone.gemfire.internal.shared.ChannelBufferFramedOutputStream;
import com.gemstone.gemfire.internal.shared.ChannelBufferInputStream;
import com.gemstone.gemfire.internal.shared.ChannelBufferOutputStream;
import com.gemstone.gemfire.internal.shared.InputStreamChannel;
import com.gemstone.gemfire.internal.shared.OutputStreamChannel;
import org.apache.spark.unsafe.Platform;

/**
 * Holder for static sun.misc.Unsafe instance and some convenience methods. Use
 * other methods only if {@link UnsafeHolder#hasUnsafe()} returns true;
 *
 * @author swale
 * @since gfxd 1.1
 */
public abstract class UnsafeHolder {

  private static final class Wrapper {

    static final sun.misc.Unsafe unsafe;
    static final Constructor<?> directBufferConstructor;
    static final Field cleanerField;
    static final Field cleanerRunnableField;
    static final Object javaLangRefAccess;
    static final Method handlePendingRefs;

    static {
      sun.misc.Unsafe v;
      Constructor<?> dbConstructor;
      Field cleaner;
      Field runnableField = null;
      try {
        // try using "theUnsafe" field
        Field field = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
        field.setAccessible(true);
        v = (sun.misc.Unsafe)field.get(null);

        // get the constructor of DirectByteBuffer that accepts a Runnable
        Class<?> cls = Class.forName("java.nio.DirectByteBuffer");
        dbConstructor = cls.getDeclaredConstructor(Long.TYPE, Integer.TYPE);
        dbConstructor.setAccessible(true);

        cleaner = cls.getDeclaredField("cleaner");
        cleaner.setAccessible(true);

        // search for the Runnable field in Cleaner
        Class<?> runnableClass = Runnable.class;
        Field[] fields = sun.misc.Cleaner.class.getDeclaredFields();
        for (Field f : fields) {
          if (runnableClass.isAssignableFrom(f.getType())) {
            if (runnableField == null || f.getName().contains("thunk")) {
              f.setAccessible(true);
              runnableField = f;
            }
          }
        }

      } catch (LinkageError le) {
        throw le;
      } catch (Throwable t) {
        throw new ExceptionInInitializerError(t);
      }
      if (v == null) {
        throw new ExceptionInInitializerError("theUnsafe not found");
      }
      if (runnableField == null) {
        throw new ExceptionInInitializerError(
            "DirectByteBuffer cleaner thunk runnable field not found");
      }
      unsafe = v;
      directBufferConstructor = dbConstructor;
      cleanerField = cleaner;
      cleanerRunnableField = runnableField;

      Method m;
      Object langRefAccess;
      try {
        m = sun.misc.SharedSecrets.class.getMethod("getJavaLangRefAccess");
        m.setAccessible(true);
        langRefAccess = m.invoke(null);
        m = langRefAccess.getClass().getMethod("tryHandlePendingReference");
        m.setAccessible(true);
        m.invoke(langRefAccess);
      } catch (Throwable ignored) {
        langRefAccess = null;
        m = null;
      }
      javaLangRefAccess = langRefAccess;
      handlePendingRefs = m;
    }

    static void init() {
    }
  }

  private static final boolean hasUnsafe;

  static {
    boolean v;
    try {
      Wrapper.init();
      v = true;
    } catch (LinkageError le) {
      le.printStackTrace();
      v = false;
    }
    hasUnsafe = v;
  }

  private UnsafeHolder() {
    // no instance
  }

  public static boolean hasUnsafe() {
    return hasUnsafe;
  }

  @SuppressWarnings("serial")
  public static abstract class FreeMemory extends AtomicLong implements Runnable {

    protected FreeMemory(long address) {
      super(address);
    }

    protected final long tryFree() {
      // try hard to ensure freeMemory call happens only once
      final long address = get();
      return (address != 0 && compareAndSet(address, 0L)) ? address : 0L;
    }

    protected abstract String objectName();

    @Override
    public void run() {
      final long address = tryFree();
      if (address != 0) {
        Platform.freeMemory(address);
      }
    }
  }

  public interface FreeMemoryFactory {
    FreeMemory newFreeMemory(long address, int size);
  }

  public static int getAllocationSize(int size) {
    // round to word size
    size = ((size + 7) >>> 3) << 3;
    if (size > 0) return size;
    else throw new BufferOverflowException();
  }

  public static ByteBuffer allocateDirectBuffer(int size,
      FreeMemoryFactory factory) {
    final int allocSize = getAllocationSize(size);
    final ByteBuffer buffer = allocateDirectBuffer(
        Platform.allocateMemory(allocSize), allocSize, factory);
    buffer.limit(size);
    return buffer;
  }

  private static ByteBuffer allocateDirectBuffer(long address, int size,
      FreeMemoryFactory factory) {
    try {
      ByteBuffer buffer = (ByteBuffer)Wrapper.directBufferConstructor
          .newInstance(address, size);
      sun.misc.Cleaner cleaner = sun.misc.Cleaner.create(buffer,
          factory.newFreeMemory(address, size));
      Wrapper.cleanerField.set(buffer, cleaner);
      return buffer;
    } catch (Exception e) {
      Platform.throwException(e);
      throw new IllegalStateException("unreachable");
    }
  }

  public static long getDirectBufferAddress(ByteBuffer buffer) {
    return ((sun.nio.ch.DirectBuffer)buffer).address();
  }

  public static ByteBuffer reallocateDirectBuffer(ByteBuffer buffer,
      int newSize, Class<?> expectedClass, FreeMemoryFactory factory) {
    sun.nio.ch.DirectBuffer directBuffer = (sun.nio.ch.DirectBuffer)buffer;
    final long address = directBuffer.address();
    long newAddress = 0L;

    newSize = getAllocationSize(newSize);
    final sun.misc.Cleaner cleaner = directBuffer.cleaner();
    if (cleaner != null) {
      // reset the runnable to not free the memory and clean it up
      try {
        Object freeMemory = Wrapper.cleanerRunnableField.get(cleaner);
        if (expectedClass != null && (freeMemory == null ||
            !expectedClass.isInstance(freeMemory))) {
          throw new IllegalStateException("Expected class to be " +
              expectedClass.getName() + " in reallocate but was " +
              (freeMemory != null ? freeMemory.getClass().getName() : "null"));
        }
        // use the efficient realloc call if possible
        if ((freeMemory instanceof FreeMemory) &&
            ((FreeMemory)freeMemory).tryFree() != 0L) {
          newAddress = getUnsafe().reallocateMemory(address, newSize);
        }
      } catch (IllegalAccessException e) {
        // fallback to full copy
      }
    }
    if (newAddress == 0L) {
      if (expectedClass != null) {
        throw new IllegalStateException("Expected class to be " +
            expectedClass.getName() + " in reallocate but was non-runnable");
      }
      newAddress = Platform.allocateMemory(newSize);
      Platform.copyMemory(null, address, null, newAddress,
          Math.min(newSize, buffer.limit()));
    }
    // clean only after copying is done
    if (cleaner != null) {
      cleaner.clean();
      cleaner.clear();
    }
    return allocateDirectBuffer(newAddress, newSize, factory)
        .order(buffer.order());
  }

  /**
   * Change the runnable field of Cleaner using given factory. The "to"
   * argument specifies that target Runnable type that factory will produce.
   * If the existing Runnable already matches "to" then its a no-op.
   * <p>
   * The provided {@link Consumer} is used to apply any action before actually
   * changing the runnable field with the boolean argument indicating whether
   * the current field matches "from" or if it is something else.
   */
  public static void changeDirectBufferCleaner(
      ByteBuffer buffer, int size, Class<? extends FreeMemory> from,
      Class<? extends FreeMemory> to, FreeMemoryFactory factory,
      final Consumer<String> changeOwner) throws IllegalAccessException {
    sun.nio.ch.DirectBuffer directBuffer = (sun.nio.ch.DirectBuffer)buffer;
    final sun.misc.Cleaner cleaner = directBuffer.cleaner();
    if (cleaner != null) {
      // change the runnable
      final Field runnableField = Wrapper.cleanerRunnableField;
      Object runnable = runnableField.get(cleaner);
      // skip if it already matches the target Runnable type
      if (!to.isInstance(runnable)) {
        if (changeOwner != null) {
          if (from.isInstance(runnable)) {
            changeOwner.accept(((FreeMemory)runnable).objectName());
          } else {
            changeOwner.accept(null);
          }
        }
        Runnable newFree = factory.newFreeMemory(directBuffer.address(), size);
        runnableField.set(cleaner, newFree);
      }
    } else {
      throw new IllegalAccessException(
          "ByteBuffer without a Cleaner cannot be marked for storage");
    }
  }

  /**
   * Release explicitly if the passed ByteBuffer is a direct one. Avoid using
   * this directly rather use BufferAllocator.allocate/release where possible.
   */
  public static void releaseIfDirectBuffer(ByteBuffer buffer) {
    if (buffer != null && buffer.isDirect()) {
      releaseDirectBuffer(buffer);
    }
  }

  static void releaseDirectBuffer(ByteBuffer buffer) {
    sun.misc.Cleaner cleaner = ((sun.nio.ch.DirectBuffer)buffer).cleaner();
    if (cleaner != null) {
      cleaner.clean();
      cleaner.clear();
    }
    releasePendingReferences();
  }

  public static void releasePendingReferences() {
    // commented code intended to be invoked by reflection for platforms
    // that may not have the requisite classes (e.g. Mac default JDK)
    /*
    final sun.misc.JavaLangRefAccess refAccess =
        sun.misc.SharedSecrets.getJavaLangRefAccess();
    while (refAccess.tryHandlePendingReference()) ;
    */
    final Method handlePendingRefs = Wrapper.handlePendingRefs;
    if (handlePendingRefs != null) {
      try {
        // retry while helping enqueue pending Cleaner Reference objects
        // noinspection StatementWithEmptyBody
        while ((Boolean)handlePendingRefs.invoke(Wrapper.javaLangRefAccess)) ;
      } catch (Exception ignored) {
        // ignore any exceptions in releasing pending references
      }
    }
  }

  public static sun.misc.Unsafe getUnsafe() {
    return Wrapper.unsafe;
  }

  @SuppressWarnings("resource")
  public static InputStreamChannel newChannelBufferInputStream(
      ReadableByteChannel channel, int bufferSize) throws IOException {
    return (hasUnsafe
        ? new ChannelBufferUnsafeInputStream(channel, bufferSize)
        : new ChannelBufferInputStream(channel, bufferSize));
  }

  @SuppressWarnings("resource")
  public static OutputStreamChannel newChannelBufferOutputStream(
      WritableByteChannel channel, int bufferSize) throws IOException {
    return (hasUnsafe
        ? new ChannelBufferUnsafeOutputStream(channel, bufferSize)
        : new ChannelBufferOutputStream(channel, bufferSize));
  }

  @SuppressWarnings("resource")
  public static InputStreamChannel newChannelBufferFramedInputStream(
      ReadableByteChannel channel, int bufferSize) throws IOException {
    return (hasUnsafe
        ? new ChannelBufferUnsafeFramedInputStream(channel, bufferSize)
        : new ChannelBufferFramedInputStream(channel, bufferSize));
  }

  @SuppressWarnings("resource")
  public static OutputStreamChannel newChannelBufferFramedOutputStream(
      WritableByteChannel channel, int bufferSize) throws IOException {
    return (hasUnsafe
        ? new ChannelBufferUnsafeFramedOutputStream(channel, bufferSize)
        : new ChannelBufferFramedOutputStream(channel, bufferSize));
  }

  /**
   * Checks that the range described by {@code offset} and {@code size}
   * doesn't exceed {@code arrayLength}.
   */
  public static void checkBounds(int arrayLength, int offset, int len) {
    if ((offset | len) < 0 || offset > arrayLength ||
        arrayLength - offset < len) {
      throw new ArrayIndexOutOfBoundsException("Array index out of range: " +
          "length=" + arrayLength + " offset=" + offset + " length=" + len);
    }
  }
}
