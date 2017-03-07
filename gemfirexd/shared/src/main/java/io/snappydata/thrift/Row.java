/**
 * Autogenerated code by Thrift Compiler moved into the compact
 * representation in {@link io.snappydata.thrift.common.ThriftRow} class.
 */

/*
 * Changes for GemFireXD distributed data platform.
 *
 * Portions Copyright (c) 2010-2015 Pivotal Software, Inc. All rights reserved.
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
 * Changes for SnappyData data platform.
 *
 * Portions Copyright (c) 2016 SnappyData, Inc. All rights reserved.
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

package io.snappydata.thrift;

import java.util.List;

@SuppressWarnings("serial")
public final class Row extends io.snappydata.thrift.common.ThriftRow {

  public Row() {
  }

  public Row(List<ColumnDescriptor> metadata) {
    this(metadata, false);
  }

  public Row(List<ColumnDescriptor> metadata, boolean checkOutputParameters) {
    super(metadata, checkOutputParameters);
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public Row(Row other) {
    super(other);
  }

  /**
   * Performs a deep copy on <i>other</i> if copyValues is true else don't copy
   * values.
   */
  public Row(Row other, boolean otherIsEmpty, boolean copyValues) {
    super(other, otherIsEmpty, copyValues);
  }

  @Override
  public Row deepCopy() {
    return new Row(this);
  }

  private void writeObject(java.io.ObjectOutputStream out)
      throws java.io.IOException {
    try {
      write(new org.apache.thrift.protocol.TCompactProtocol(
          new org.apache.thrift.transport.TIOStreamTransport(out)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private void readObject(java.io.ObjectInputStream in)
      throws java.io.IOException, ClassNotFoundException {
    try {
      read(new org.apache.thrift.protocol.TCompactProtocol(
          new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }
}
