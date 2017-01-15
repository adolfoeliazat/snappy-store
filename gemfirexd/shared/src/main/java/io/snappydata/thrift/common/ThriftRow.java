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

/*
 * Modified to use {@link OptimizedElementArray} from the autogenerated code by
 * Thrift Compiler (1.0.0-dev).
 */
package io.snappydata.thrift.common;

import java.io.IOException;
import java.util.BitSet;
import java.util.List;

import io.snappydata.thrift.ColumnDescriptor;
import io.snappydata.thrift.SnappyException;
import io.snappydata.thrift.SnappyExceptionData;

/**
 * Compact storage for a row in SQL. This extends {@link OptimizedElementArray}
 * to add Thrift specific serialization code.
 */
@SuppressWarnings({"rawtypes", "serial", "unchecked"})
public class ThriftRow extends OptimizedElementArray implements
    org.apache.thrift.TBase<ThriftRow, ThriftRow._Fields>,
    java.io.Serializable, Comparable<ThriftRow> {

  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC =
      new org.apache.thrift.protocol.TStruct("Row");

  private static final org.apache.thrift.protocol.TField VALUES_FIELD_DESC =
      new org.apache.thrift.protocol.TField("values",
          org.apache.thrift.protocol.TType.LIST, (short)1);

  protected BitSet changedColumns;

  // public List<ColumnValue> values; // required

  /**
   * The set of fields this struct contains, along with convenience methods for
   * finding and manipulating them.
   */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    ;

    private final short _thriftId;

    private final String _fieldName;

    _Fields(short thriftId, String fieldName) {
      _thriftId = thriftId;
      _fieldName = fieldName;
    }

    public short getThriftFieldId() {
      return _thriftId;
    }

    public String getFieldName() {
      return _fieldName;
    }
  }

  public ThriftRow() {
    super();
  }

  public ThriftRow(List<ColumnDescriptor> metadata) {
    super(metadata);
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public ThriftRow(ThriftRow other) {
    super(other);
    final BitSet changedCols = other.changedColumns;
    this.changedColumns = changedCols == null ? null : (BitSet)changedCols
        .clone();
  }

  /**
   * Performs a deep copy on <i>other</i> if copyValues is true else don't copy
   * values.
   */
  public ThriftRow(ThriftRow other, boolean copyValues) {
    super(other, copyValues);
    if (copyValues) {
      final BitSet changedCols = other.changedColumns;
      this.changedColumns = changedCols == null ? null : (BitSet)changedCols
          .clone();
    } else {
      this.changedColumns = other.changedColumns;
    }
  }

  @Override
  public ThriftRow deepCopy() {
    return new ThriftRow(this);
  }

  @Override
  public void clear() {
    super.clear();
    this.changedColumns = null;
  }

  @Override
  public void setFieldValue(_Fields field, Object value) {
  }

  @Override
  public Object getFieldValue(_Fields field) {
    throw new IllegalStateException();
  }

  /**
   * Returns true if field corresponding to fieldID is set (has been assigned a
   * value) and false otherwise
   */
  @Override
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    throw new IllegalStateException();
  }

  public final void setChangedColumns(BitSet changedColumns) {
    this.changedColumns = changedColumns;
  }

  protected final int compare(Object o1, Object o2) {
    if (o1 != null) {
      if (o2 != null) {
        return org.apache.thrift.TBaseHelper.compareTo(o1, o2);
      } else {
        return 1;
      }
    } else if (o2 != null) {
      return -1;
    } else {
      return 0;
    }
  }

  public int compareTo(ThriftRow other) {
    int lastComparison = compare(this.primitives, other.primitives);
    if (lastComparison != 0) {
      return lastComparison;
    } else {
      return compare(this.nonPrimitives, other.nonPrimitives);
    }
  }

  @Override
  public _Fields fieldForId(int fieldId) {
    // nothing here
    return null;
  }

  @Override
  public String toString() {
    return "Row( " + super.toString() + " )";
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    if (primitives == null) {
      throw new org.apache.thrift.protocol.TProtocolException("Required "
          + "field 'primitives' was not present! Struct: " + toString());
    }
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

  @Override
  public void read(org.apache.thrift.protocol.TProtocol iprot)
      throws org.apache.thrift.TException {
    org.apache.thrift.protocol.TField schemeField;
    iprot.readStructBegin();
    while (true) {
      schemeField = iprot.readFieldBegin();
      if (schemeField.type == org.apache.thrift.protocol.TType.STOP) {
        break;
      }
      switch (schemeField.id) {
        case 1: // VALUES
          if (schemeField.type == org.apache.thrift.protocol.TType.LIST) {
            org.apache.thrift.protocol.TList list = iprot.readListBegin();
            try {
              readStandardScheme(list.size, iprot);
            } catch (java.sql.SQLException sqle) {
              throw new SnappyException(new SnappyExceptionData(
                  sqle.getMessage(), sqle.getSQLState(),
                  sqle.getErrorCode()), null);
            }
            iprot.readListEnd();
          } else {
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot,
                schemeField.type);
          }
          break;
        default:
          org.apache.thrift.protocol.TProtocolUtil.skip(iprot,
              schemeField.type);
      }
      iprot.readFieldEnd();
    }
    iprot.readStructEnd();

    // check for required fields of primitive type, which can't be checked in
    // the validate method
    validate();
  }

  @Override
  public void write(org.apache.thrift.protocol.TProtocol oprot)
      throws org.apache.thrift.TException {
    validate();

    oprot.writeStructBegin(STRUCT_DESC);
    final BitSet cols = changedColumns;
    final int size = cols == null ? size() : cols.cardinality();
    if (size > 0) {
      oprot.writeFieldBegin(VALUES_FIELD_DESC);
      oprot.writeListBegin(new org.apache.thrift.protocol.TList(
          org.apache.thrift.protocol.TType.STRUCT, size));
      try {
        writeStandardScheme(cols, oprot);
      } catch (IOException ioe) {
        throw new org.apache.thrift.transport.TTransportException(ioe);
      }
      oprot.writeListEnd();
      oprot.writeFieldEnd();
    }
    oprot.writeFieldStop();
    oprot.writeStructEnd();
  }
}
