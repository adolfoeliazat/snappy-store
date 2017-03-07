/*
 * Copyright (c) 2016 SnappyData, Inc. All rights reserved.
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

package com.pivotal.gemfirexd.internal.engine.distributed.message;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.gemstone.gemfire.DataSerializer;
import com.gemstone.gemfire.cache.DiskAccessException;
import com.gemstone.gemfire.cache.RegionDestroyedException;
import com.gemstone.gemfire.cache.execute.FunctionException;
import com.gemstone.gemfire.distributed.DistributedMember;
import com.gemstone.gemfire.distributed.internal.InternalDistributedSystem;
import com.gemstone.gemfire.distributed.internal.ReplyException;
import com.gemstone.gemfire.distributed.internal.membership.InternalDistributedMember;
import com.gemstone.gemfire.internal.HeapDataOutputStream;
import com.gemstone.gemfire.internal.cache.NoDataStoreAvailableException;
import com.gemstone.gemfire.internal.i18n.LocalizedStrings;
import com.gemstone.gemfire.internal.shared.Version;
import com.pivotal.gemfirexd.internal.engine.GfxdConstants;
import com.pivotal.gemfirexd.internal.engine.Misc;
import com.pivotal.gemfirexd.internal.engine.distributed.FunctionExecutionException;
import com.pivotal.gemfirexd.internal.engine.distributed.GfxdDistributionAdvisor;
import com.pivotal.gemfirexd.internal.engine.distributed.GfxdResultCollector;
import com.pivotal.gemfirexd.internal.engine.distributed.SnappyResultHolder;
import com.pivotal.gemfirexd.internal.engine.distributed.utils.GemFireXDUtils;
import com.pivotal.gemfirexd.internal.engine.jdbc.GemFireXDRuntimeException;
import com.pivotal.gemfirexd.internal.iapi.error.StandardException;
import com.pivotal.gemfirexd.internal.shared.common.reference.SQLState;
import com.pivotal.gemfirexd.internal.shared.common.sanity.SanityManager;
import com.pivotal.gemfirexd.internal.snappy.CallbackFactoryProvider;
import com.pivotal.gemfirexd.internal.snappy.LeadNodeExecutionContext;
import com.pivotal.gemfirexd.internal.snappy.SparkSQLExecute;

/**
 * Route query to Snappy Spark Lead node.
 * <p/>
 */
public final class LeadNodeExecutorMsg extends MemberExecutorMessage<Object> {

  private String sql;
  private LeadNodeExecutionContext ctx;
  private transient SparkSQLExecute exec;
  private String schema;

  public LeadNodeExecutorMsg(String sql, String schema, LeadNodeExecutionContext ctx,
      GfxdResultCollector<Object> rc) {
    super(rc, null, false, true);
    StackTraceElement[] st = Thread.currentThread().getStackTrace();
//    System.out.println(Thread.currentThread().getName() + " ABS LeadNodeExecutorMsg");
//    for (StackTraceElement ste : st) {
//      System.out.println(ste);
//    }
    this.schema = schema;
    this.sql = sql;
    this.ctx = ctx;
  }

  /**
   * Default constructor for deserialization. Not to be invoked directly.
   */
  public LeadNodeExecutorMsg() {
    super(true);
  }

  @Override
  public Set<DistributedMember> getMembers() {
    GfxdDistributionAdvisor advisor = GemFireXDUtils.getGfxdAdvisor();
    InternalDistributedSystem ids = Misc.getDistributedSystem();
    if (ids.isLoner()) {
      return Collections.<DistributedMember>singleton(
          ids.getDistributedMember());
    }
    Set<DistributedMember> allMembers = ids.getAllOtherMembers();
    for (DistributedMember m : allMembers) {
      GfxdDistributionAdvisor.GfxdProfile profile = advisor
          .getProfile((InternalDistributedMember)m);
      if (profile != null && profile.hasSparkURL()) {
        Set<DistributedMember> s = new HashSet<DistributedMember>();
        s.add(m);
        return Collections.unmodifiableSet(s);
      }
    }
    throw new NoDataStoreAvailableException(LocalizedStrings
        .DistributedRegion_NO_DATA_STORE_FOUND_FOR_DISTRIBUTION
        .toLocalizedString("SnappyData Lead Node"));
  }

  @Override
  public void postExecutionCallback() {
  }

  @Override
  public boolean isHA() {
    return true;
  }

  @Override
  public boolean optimizeForWrite() {
    return false;
  }

  @Override
  protected void execute() throws Exception {
    if (GemFireXDUtils.TraceQuery) {
      SanityManager.DEBUG_PRINT(GfxdConstants.TRACE_QUERYDISTRIB,
              "LeadNodeExecutorMsg.execute: Got sql = " + sql);
    }
    try {
      InternalDistributedMember m = this.getSenderForReply();
      final Version v = m.getVersionObject();
      exec = CallbackFactoryProvider.getClusterCallbacks().getSQLExecute(
          sql, schema, ctx, v);
      SnappyResultHolder srh = new SnappyResultHolder(exec);

      srh.prepareSend(this);
      this.lastResultSent = true;
      this.endMessage();
      if (GemFireXDUtils.TraceQuery) {
        SanityManager.DEBUG_PRINT(GfxdConstants.TRACE_QUERYDISTRIB,
                "LeadNodeExecutorMsg.execute: Sent Last result ");
      }
    } catch (Exception ex) {
      throw getExceptionToSendToServer(ex);
    }
  }

  private static class SparkExceptionWrapper extends Exception {
    public SparkExceptionWrapper(Throwable ex) {
      super(ex.getClass().getName() + ": " + ex.getMessage(), ex.getCause());
      this.setStackTrace(ex.getStackTrace());
    }
  }

  private Exception getExceptionToSendToServer(Exception ex) {
    // Catch all exceptions and convert so can be caught at XD side
    // Check if the exception can be serialized or not
    boolean wrapExcepton = false;
    HeapDataOutputStream hdos = null;
    try {
      hdos = new HeapDataOutputStream();
      DataSerializer.writeObject(ex, hdos);
    } catch (Exception e) {
      wrapExcepton = true;
    } finally {
      if (hdos != null) {
        hdos.close();
      }
    }

    Throwable cause = ex;
    while (cause != null) {
      if (cause.getClass().getName().contains("AnalysisException")) {
        return StandardException.newException(
            SQLState.LANG_UNEXPECTED_USER_EXCEPTION,
            (!wrapExcepton ? cause : new SparkExceptionWrapper(cause)), cause.getMessage());
      } else if (cause.getClass().getName().contains("apache.spark.storage")) {
        return StandardException.newException(
            SQLState.DATA_UNEXPECTED_EXCEPTION,
            (!wrapExcepton ? cause : new SparkExceptionWrapper(cause)), cause.getMessage());
      } else if (cause.getClass().getName().contains("apache.spark.sql")) {
        Throwable nestedCause = cause.getCause();
        while (nestedCause != null) {
          if (nestedCause.getClass().getName().contains("ErrorLimitExceededException")) {
            return StandardException.newException(
                SQLState.LANG_UNEXPECTED_USER_EXCEPTION,
                (!wrapExcepton ? nestedCause : new SparkExceptionWrapper(nestedCause)), nestedCause.getMessage());
          }
          nestedCause = nestedCause.getCause();
        }
        return StandardException.newException(
            SQLState.LANG_UNEXPECTED_USER_EXCEPTION,
            (!wrapExcepton ? cause : new SparkExceptionWrapper(cause)), cause.getMessage());
      }
      cause = cause.getCause();
    }
    return ex;
  }

  @Override
  protected void executeFunction(boolean enableStreaming)
      throws StandardException, SQLException {
    try {
      super.executeFunction(enableStreaming);
    } catch (RuntimeException re) {
      throw handleLeadNodeException(re);
    }
  }

  public static RuntimeException handleLeadNodeException(
      RuntimeException re) {
    Throwable cause = re;
    if (re instanceof GemFireXDRuntimeException ||
        re instanceof FunctionException ||
        re instanceof FunctionExecutionException ||
        re instanceof ReplyException) {
      cause = re.getCause();
    }
    if (cause instanceof RegionDestroyedException) {
      RegionDestroyedException rde = (RegionDestroyedException)cause;
      // don't mark as remote so that no retry is done (SNAP-961)
      // a top-level exception can only have been from lead node itself
      if (rde.isRemote()) rde.setNotRemote();
    }
    if (cause instanceof DiskAccessException) {
      DiskAccessException dae = (DiskAccessException)cause;
      // don't mark as remote so that no retry is done (SNAP-961)
      // a top-level exception can only have been from lead node itself
      if (dae.isRemote()) dae.setNotRemote();
    }
    return re;
  }

  @Override
  protected LeadNodeExecutorMsg clone() {
    final LeadNodeExecutorMsg msg = new LeadNodeExecutorMsg(this.sql, this.schema, this.ctx,
        (GfxdResultCollector<Object>)this.userCollector);
    msg.exec = this.exec;
    return msg;
  }

  @Override
  public byte getGfxdID() {
    return LEAD_NODE_EXN_MSG;
  }

  @Override
  public void fromData(DataInput in) throws IOException, ClassNotFoundException {
    super.fromData(in);
    this.sql = DataSerializer.readString(in);
    this.schema = DataSerializer.readString(in);
    this.ctx = DataSerializer.readObject(in);
  }

  @Override
  public void toData(final DataOutput out) throws IOException {
    super.toData(out);
    DataSerializer.writeString(this.sql, out);
    DataSerializer.writeString(this.schema , out);
    DataSerializer.writeObject(ctx, out);
  }

  public void appendFields(final StringBuilder sb) {
    sb.append("sql: " + sql);
    sb.append("schema: " + schema);
  }
}
