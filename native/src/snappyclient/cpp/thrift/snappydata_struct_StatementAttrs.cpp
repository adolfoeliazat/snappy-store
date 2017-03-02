/**
 * Autogenerated by Thrift Compiler (0.10.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */

#include <iosfwd>

#include <thrift/Thrift.h>
#include <thrift/TApplicationException.h>
#include <thrift/protocol/TProtocol.h>
#include <thrift/transport/TTransport.h>

#include <thrift/cxxfunctional.h>
#include "snappydata_struct_StatementAttrs.h"

#include <algorithm>
#include <ostream>

#include <thrift/TToString.h>

namespace io { namespace snappydata { namespace thrift {


StatementAttrs::~StatementAttrs() noexcept {
}


void StatementAttrs::__set_resultSetType(const int8_t val) {
  this->resultSetType = val;
__isset.resultSetType = true;
}

void StatementAttrs::__set_updatable(const bool val) {
  this->updatable = val;
__isset.updatable = true;
}

void StatementAttrs::__set_holdCursorsOverCommit(const bool val) {
  this->holdCursorsOverCommit = val;
__isset.holdCursorsOverCommit = true;
}

void StatementAttrs::__set_requireAutoIncCols(const bool val) {
  this->requireAutoIncCols = val;
__isset.requireAutoIncCols = true;
}

void StatementAttrs::__set_autoIncColumns(const std::vector<int32_t> & val) {
  this->autoIncColumns = val;
__isset.autoIncColumns = true;
}

void StatementAttrs::__set_autoIncColumnNames(const std::vector<std::string> & val) {
  this->autoIncColumnNames = val;
__isset.autoIncColumnNames = true;
}

void StatementAttrs::__set_batchSize(const int32_t val) {
  this->batchSize = val;
__isset.batchSize = true;
}

void StatementAttrs::__set_fetchReverse(const bool val) {
  this->fetchReverse = val;
__isset.fetchReverse = true;
}

void StatementAttrs::__set_lobChunkSize(const int32_t val) {
  this->lobChunkSize = val;
__isset.lobChunkSize = true;
}

void StatementAttrs::__set_maxRows(const int32_t val) {
  this->maxRows = val;
__isset.maxRows = true;
}

void StatementAttrs::__set_maxFieldSize(const int32_t val) {
  this->maxFieldSize = val;
__isset.maxFieldSize = true;
}

void StatementAttrs::__set_timeout(const int32_t val) {
  this->timeout = val;
__isset.timeout = true;
}

void StatementAttrs::__set_cursorName(const std::string& val) {
  this->cursorName = val;
__isset.cursorName = true;
}

void StatementAttrs::__set_possibleDuplicate(const bool val) {
  this->possibleDuplicate = val;
__isset.possibleDuplicate = true;
}

void StatementAttrs::__set_poolable(const bool val) {
  this->poolable = val;
__isset.poolable = true;
}

void StatementAttrs::__set_doEscapeProcessing(const bool val) {
  this->doEscapeProcessing = val;
__isset.doEscapeProcessing = true;
}

void StatementAttrs::__set_pendingTransactionAttrs(const std::map<TransactionAttribute::type, bool> & val) {
  this->pendingTransactionAttrs = val;
__isset.pendingTransactionAttrs = true;
}

uint32_t StatementAttrs::read(::apache::thrift::protocol::TProtocol* iprot) {

  uint32_t xfer = 0;
  std::string fname;
  ::apache::thrift::protocol::TType ftype;
  int16_t fid;

  xfer += iprot->readStructBegin(fname);

  using ::apache::thrift::protocol::TProtocolException;


  while (true)
  {
    xfer += iprot->readFieldBegin(fname, ftype, fid);
    if (ftype == ::apache::thrift::protocol::T_STOP) {
      break;
    }
    switch (fid)
    {
      case 1:
        if (ftype == ::apache::thrift::protocol::T_BYTE) {
          xfer += iprot->readByte(this->resultSetType);
          this->__isset.resultSetType = true;
        } else {
          xfer += iprot->skip(ftype);
        }
        break;
      case 2:
        if (ftype == ::apache::thrift::protocol::T_BOOL) {
          xfer += iprot->readBool(this->updatable);
          this->__isset.updatable = true;
        } else {
          xfer += iprot->skip(ftype);
        }
        break;
      case 3:
        if (ftype == ::apache::thrift::protocol::T_BOOL) {
          xfer += iprot->readBool(this->holdCursorsOverCommit);
          this->__isset.holdCursorsOverCommit = true;
        } else {
          xfer += iprot->skip(ftype);
        }
        break;
      case 4:
        if (ftype == ::apache::thrift::protocol::T_BOOL) {
          xfer += iprot->readBool(this->requireAutoIncCols);
          this->__isset.requireAutoIncCols = true;
        } else {
          xfer += iprot->skip(ftype);
        }
        break;
      case 5:
        if (ftype == ::apache::thrift::protocol::T_LIST) {
          {
            this->autoIncColumns.clear();
            uint32_t _size153;
            ::apache::thrift::protocol::TType _etype156;
            xfer += iprot->readListBegin(_etype156, _size153);
            this->autoIncColumns.resize(_size153);
            uint32_t _i157;
            for (_i157 = 0; _i157 < _size153; ++_i157)
            {
              xfer += iprot->readI32(this->autoIncColumns[_i157]);
            }
            xfer += iprot->readListEnd();
          }
          this->__isset.autoIncColumns = true;
        } else {
          xfer += iprot->skip(ftype);
        }
        break;
      case 6:
        if (ftype == ::apache::thrift::protocol::T_LIST) {
          {
            this->autoIncColumnNames.clear();
            uint32_t _size158;
            ::apache::thrift::protocol::TType _etype161;
            xfer += iprot->readListBegin(_etype161, _size158);
            this->autoIncColumnNames.resize(_size158);
            uint32_t _i162;
            for (_i162 = 0; _i162 < _size158; ++_i162)
            {
              xfer += iprot->readString(this->autoIncColumnNames[_i162]);
            }
            xfer += iprot->readListEnd();
          }
          this->__isset.autoIncColumnNames = true;
        } else {
          xfer += iprot->skip(ftype);
        }
        break;
      case 7:
        if (ftype == ::apache::thrift::protocol::T_I32) {
          xfer += iprot->readI32(this->batchSize);
          this->__isset.batchSize = true;
        } else {
          xfer += iprot->skip(ftype);
        }
        break;
      case 8:
        if (ftype == ::apache::thrift::protocol::T_BOOL) {
          xfer += iprot->readBool(this->fetchReverse);
          this->__isset.fetchReverse = true;
        } else {
          xfer += iprot->skip(ftype);
        }
        break;
      case 9:
        if (ftype == ::apache::thrift::protocol::T_I32) {
          xfer += iprot->readI32(this->lobChunkSize);
          this->__isset.lobChunkSize = true;
        } else {
          xfer += iprot->skip(ftype);
        }
        break;
      case 10:
        if (ftype == ::apache::thrift::protocol::T_I32) {
          xfer += iprot->readI32(this->maxRows);
          this->__isset.maxRows = true;
        } else {
          xfer += iprot->skip(ftype);
        }
        break;
      case 11:
        if (ftype == ::apache::thrift::protocol::T_I32) {
          xfer += iprot->readI32(this->maxFieldSize);
          this->__isset.maxFieldSize = true;
        } else {
          xfer += iprot->skip(ftype);
        }
        break;
      case 12:
        if (ftype == ::apache::thrift::protocol::T_I32) {
          xfer += iprot->readI32(this->timeout);
          this->__isset.timeout = true;
        } else {
          xfer += iprot->skip(ftype);
        }
        break;
      case 13:
        if (ftype == ::apache::thrift::protocol::T_STRING) {
          xfer += iprot->readString(this->cursorName);
          this->__isset.cursorName = true;
        } else {
          xfer += iprot->skip(ftype);
        }
        break;
      case 14:
        if (ftype == ::apache::thrift::protocol::T_BOOL) {
          xfer += iprot->readBool(this->possibleDuplicate);
          this->__isset.possibleDuplicate = true;
        } else {
          xfer += iprot->skip(ftype);
        }
        break;
      case 15:
        if (ftype == ::apache::thrift::protocol::T_BOOL) {
          xfer += iprot->readBool(this->poolable);
          this->__isset.poolable = true;
        } else {
          xfer += iprot->skip(ftype);
        }
        break;
      case 16:
        if (ftype == ::apache::thrift::protocol::T_BOOL) {
          xfer += iprot->readBool(this->doEscapeProcessing);
          this->__isset.doEscapeProcessing = true;
        } else {
          xfer += iprot->skip(ftype);
        }
        break;
      case 17:
        if (ftype == ::apache::thrift::protocol::T_MAP) {
          {
            this->pendingTransactionAttrs.clear();
            uint32_t _size163;
            ::apache::thrift::protocol::TType _ktype164;
            ::apache::thrift::protocol::TType _vtype165;
            xfer += iprot->readMapBegin(_ktype164, _vtype165, _size163);
            uint32_t _i167;
            for (_i167 = 0; _i167 < _size163; ++_i167)
            {
              TransactionAttribute::type _key168;
              int32_t ecast170;
              xfer += iprot->readI32(ecast170);
              _key168 = (TransactionAttribute::type)ecast170;
              bool& _val169 = this->pendingTransactionAttrs[_key168];
              xfer += iprot->readBool(_val169);
            }
            xfer += iprot->readMapEnd();
          }
          this->__isset.pendingTransactionAttrs = true;
        } else {
          xfer += iprot->skip(ftype);
        }
        break;
      default:
        xfer += iprot->skip(ftype);
        break;
    }
    xfer += iprot->readFieldEnd();
  }

  xfer += iprot->readStructEnd();

  return xfer;
}

uint32_t StatementAttrs::write(::apache::thrift::protocol::TProtocol* oprot) const {
  uint32_t xfer = 0;
  xfer += oprot->writeStructBegin("StatementAttrs");

  if (this->__isset.resultSetType) {
    xfer += oprot->writeFieldBegin("resultSetType", ::apache::thrift::protocol::T_BYTE, 1);
    xfer += oprot->writeByte(this->resultSetType);
    xfer += oprot->writeFieldEnd();
  }
  if (this->__isset.updatable) {
    xfer += oprot->writeFieldBegin("updatable", ::apache::thrift::protocol::T_BOOL, 2);
    xfer += oprot->writeBool(this->updatable);
    xfer += oprot->writeFieldEnd();
  }
  if (this->__isset.holdCursorsOverCommit) {
    xfer += oprot->writeFieldBegin("holdCursorsOverCommit", ::apache::thrift::protocol::T_BOOL, 3);
    xfer += oprot->writeBool(this->holdCursorsOverCommit);
    xfer += oprot->writeFieldEnd();
  }
  if (this->__isset.requireAutoIncCols) {
    xfer += oprot->writeFieldBegin("requireAutoIncCols", ::apache::thrift::protocol::T_BOOL, 4);
    xfer += oprot->writeBool(this->requireAutoIncCols);
    xfer += oprot->writeFieldEnd();
  }
  if (this->__isset.autoIncColumns) {
    xfer += oprot->writeFieldBegin("autoIncColumns", ::apache::thrift::protocol::T_LIST, 5);
    {
      xfer += oprot->writeListBegin(::apache::thrift::protocol::T_I32, static_cast<uint32_t>(this->autoIncColumns.size()));
      std::vector<int32_t> ::const_iterator _iter171;
      for (_iter171 = this->autoIncColumns.begin(); _iter171 != this->autoIncColumns.end(); ++_iter171)
      {
        xfer += oprot->writeI32((*_iter171));
      }
      xfer += oprot->writeListEnd();
    }
    xfer += oprot->writeFieldEnd();
  }
  if (this->__isset.autoIncColumnNames) {
    xfer += oprot->writeFieldBegin("autoIncColumnNames", ::apache::thrift::protocol::T_LIST, 6);
    {
      xfer += oprot->writeListBegin(::apache::thrift::protocol::T_STRING, static_cast<uint32_t>(this->autoIncColumnNames.size()));
      std::vector<std::string> ::const_iterator _iter172;
      for (_iter172 = this->autoIncColumnNames.begin(); _iter172 != this->autoIncColumnNames.end(); ++_iter172)
      {
        xfer += oprot->writeString((*_iter172));
      }
      xfer += oprot->writeListEnd();
    }
    xfer += oprot->writeFieldEnd();
  }
  if (this->__isset.batchSize) {
    xfer += oprot->writeFieldBegin("batchSize", ::apache::thrift::protocol::T_I32, 7);
    xfer += oprot->writeI32(this->batchSize);
    xfer += oprot->writeFieldEnd();
  }
  if (this->__isset.fetchReverse) {
    xfer += oprot->writeFieldBegin("fetchReverse", ::apache::thrift::protocol::T_BOOL, 8);
    xfer += oprot->writeBool(this->fetchReverse);
    xfer += oprot->writeFieldEnd();
  }
  if (this->__isset.lobChunkSize) {
    xfer += oprot->writeFieldBegin("lobChunkSize", ::apache::thrift::protocol::T_I32, 9);
    xfer += oprot->writeI32(this->lobChunkSize);
    xfer += oprot->writeFieldEnd();
  }
  if (this->__isset.maxRows) {
    xfer += oprot->writeFieldBegin("maxRows", ::apache::thrift::protocol::T_I32, 10);
    xfer += oprot->writeI32(this->maxRows);
    xfer += oprot->writeFieldEnd();
  }
  if (this->__isset.maxFieldSize) {
    xfer += oprot->writeFieldBegin("maxFieldSize", ::apache::thrift::protocol::T_I32, 11);
    xfer += oprot->writeI32(this->maxFieldSize);
    xfer += oprot->writeFieldEnd();
  }
  if (this->__isset.timeout) {
    xfer += oprot->writeFieldBegin("timeout", ::apache::thrift::protocol::T_I32, 12);
    xfer += oprot->writeI32(this->timeout);
    xfer += oprot->writeFieldEnd();
  }
  if (this->__isset.cursorName) {
    xfer += oprot->writeFieldBegin("cursorName", ::apache::thrift::protocol::T_STRING, 13);
    xfer += oprot->writeString(this->cursorName);
    xfer += oprot->writeFieldEnd();
  }
  if (this->__isset.possibleDuplicate) {
    xfer += oprot->writeFieldBegin("possibleDuplicate", ::apache::thrift::protocol::T_BOOL, 14);
    xfer += oprot->writeBool(this->possibleDuplicate);
    xfer += oprot->writeFieldEnd();
  }
  if (this->__isset.poolable) {
    xfer += oprot->writeFieldBegin("poolable", ::apache::thrift::protocol::T_BOOL, 15);
    xfer += oprot->writeBool(this->poolable);
    xfer += oprot->writeFieldEnd();
  }
  if (this->__isset.doEscapeProcessing) {
    xfer += oprot->writeFieldBegin("doEscapeProcessing", ::apache::thrift::protocol::T_BOOL, 16);
    xfer += oprot->writeBool(this->doEscapeProcessing);
    xfer += oprot->writeFieldEnd();
  }
  if (this->__isset.pendingTransactionAttrs) {
    xfer += oprot->writeFieldBegin("pendingTransactionAttrs", ::apache::thrift::protocol::T_MAP, 17);
    {
      xfer += oprot->writeMapBegin(::apache::thrift::protocol::T_I32, ::apache::thrift::protocol::T_BOOL, static_cast<uint32_t>(this->pendingTransactionAttrs.size()));
      std::map<TransactionAttribute::type, bool> ::const_iterator _iter173;
      for (_iter173 = this->pendingTransactionAttrs.begin(); _iter173 != this->pendingTransactionAttrs.end(); ++_iter173)
      {
        xfer += oprot->writeI32((int32_t)_iter173->first);
        xfer += oprot->writeBool(_iter173->second);
      }
      xfer += oprot->writeMapEnd();
    }
    xfer += oprot->writeFieldEnd();
  }
  xfer += oprot->writeFieldStop();
  xfer += oprot->writeStructEnd();
  return xfer;
}

void swap(StatementAttrs &a, StatementAttrs &b) noexcept {
  using ::std::swap;
  static_assert(noexcept(swap(a, b)), "throwing swap");
  swap(a.resultSetType, b.resultSetType);
  swap(a.updatable, b.updatable);
  swap(a.holdCursorsOverCommit, b.holdCursorsOverCommit);
  swap(a.requireAutoIncCols, b.requireAutoIncCols);
  swap(a.autoIncColumns, b.autoIncColumns);
  swap(a.autoIncColumnNames, b.autoIncColumnNames);
  swap(a.batchSize, b.batchSize);
  swap(a.fetchReverse, b.fetchReverse);
  swap(a.lobChunkSize, b.lobChunkSize);
  swap(a.maxRows, b.maxRows);
  swap(a.maxFieldSize, b.maxFieldSize);
  swap(a.timeout, b.timeout);
  swap(a.cursorName, b.cursorName);
  swap(a.possibleDuplicate, b.possibleDuplicate);
  swap(a.poolable, b.poolable);
  swap(a.doEscapeProcessing, b.doEscapeProcessing);
  swap(a.pendingTransactionAttrs, b.pendingTransactionAttrs);
  swap(a.__isset, b.__isset);
}

StatementAttrs::StatementAttrs(const StatementAttrs& other174) {
  resultSetType = other174.resultSetType;
  updatable = other174.updatable;
  holdCursorsOverCommit = other174.holdCursorsOverCommit;
  requireAutoIncCols = other174.requireAutoIncCols;
  autoIncColumns = other174.autoIncColumns;
  autoIncColumnNames = other174.autoIncColumnNames;
  batchSize = other174.batchSize;
  fetchReverse = other174.fetchReverse;
  lobChunkSize = other174.lobChunkSize;
  maxRows = other174.maxRows;
  maxFieldSize = other174.maxFieldSize;
  timeout = other174.timeout;
  cursorName = other174.cursorName;
  possibleDuplicate = other174.possibleDuplicate;
  poolable = other174.poolable;
  doEscapeProcessing = other174.doEscapeProcessing;
  pendingTransactionAttrs = other174.pendingTransactionAttrs;
  __isset = other174.__isset;
}
StatementAttrs::StatementAttrs( StatementAttrs&& other175) noexcept {
  resultSetType = std::move(other175.resultSetType);
  updatable = std::move(other175.updatable);
  holdCursorsOverCommit = std::move(other175.holdCursorsOverCommit);
  requireAutoIncCols = std::move(other175.requireAutoIncCols);
  autoIncColumns = std::move(other175.autoIncColumns);
  autoIncColumnNames = std::move(other175.autoIncColumnNames);
  batchSize = std::move(other175.batchSize);
  fetchReverse = std::move(other175.fetchReverse);
  lobChunkSize = std::move(other175.lobChunkSize);
  maxRows = std::move(other175.maxRows);
  maxFieldSize = std::move(other175.maxFieldSize);
  timeout = std::move(other175.timeout);
  cursorName = std::move(other175.cursorName);
  possibleDuplicate = std::move(other175.possibleDuplicate);
  poolable = std::move(other175.poolable);
  doEscapeProcessing = std::move(other175.doEscapeProcessing);
  pendingTransactionAttrs = std::move(other175.pendingTransactionAttrs);
  __isset = std::move(other175.__isset);
}
StatementAttrs& StatementAttrs::operator=(const StatementAttrs& other176) {
  resultSetType = other176.resultSetType;
  updatable = other176.updatable;
  holdCursorsOverCommit = other176.holdCursorsOverCommit;
  requireAutoIncCols = other176.requireAutoIncCols;
  autoIncColumns = other176.autoIncColumns;
  autoIncColumnNames = other176.autoIncColumnNames;
  batchSize = other176.batchSize;
  fetchReverse = other176.fetchReverse;
  lobChunkSize = other176.lobChunkSize;
  maxRows = other176.maxRows;
  maxFieldSize = other176.maxFieldSize;
  timeout = other176.timeout;
  cursorName = other176.cursorName;
  possibleDuplicate = other176.possibleDuplicate;
  poolable = other176.poolable;
  doEscapeProcessing = other176.doEscapeProcessing;
  pendingTransactionAttrs = other176.pendingTransactionAttrs;
  __isset = other176.__isset;
  return *this;
}
StatementAttrs& StatementAttrs::operator=(StatementAttrs&& other177) noexcept {
  resultSetType = std::move(other177.resultSetType);
  updatable = std::move(other177.updatable);
  holdCursorsOverCommit = std::move(other177.holdCursorsOverCommit);
  requireAutoIncCols = std::move(other177.requireAutoIncCols);
  autoIncColumns = std::move(other177.autoIncColumns);
  autoIncColumnNames = std::move(other177.autoIncColumnNames);
  batchSize = std::move(other177.batchSize);
  fetchReverse = std::move(other177.fetchReverse);
  lobChunkSize = std::move(other177.lobChunkSize);
  maxRows = std::move(other177.maxRows);
  maxFieldSize = std::move(other177.maxFieldSize);
  timeout = std::move(other177.timeout);
  cursorName = std::move(other177.cursorName);
  possibleDuplicate = std::move(other177.possibleDuplicate);
  poolable = std::move(other177.poolable);
  doEscapeProcessing = std::move(other177.doEscapeProcessing);
  pendingTransactionAttrs = std::move(other177.pendingTransactionAttrs);
  __isset = std::move(other177.__isset);
  return *this;
}
void StatementAttrs::printTo(std::ostream& out) const {
  using ::apache::thrift::to_string;
  out << "StatementAttrs(";
  out << "resultSetType="; (__isset.resultSetType ? (out << to_string(resultSetType)) : (out << "<null>"));
  out << ", " << "updatable="; (__isset.updatable ? (out << to_string(updatable)) : (out << "<null>"));
  out << ", " << "holdCursorsOverCommit="; (__isset.holdCursorsOverCommit ? (out << to_string(holdCursorsOverCommit)) : (out << "<null>"));
  out << ", " << "requireAutoIncCols="; (__isset.requireAutoIncCols ? (out << to_string(requireAutoIncCols)) : (out << "<null>"));
  out << ", " << "autoIncColumns="; (__isset.autoIncColumns ? (out << to_string(autoIncColumns)) : (out << "<null>"));
  out << ", " << "autoIncColumnNames="; (__isset.autoIncColumnNames ? (out << to_string(autoIncColumnNames)) : (out << "<null>"));
  out << ", " << "batchSize="; (__isset.batchSize ? (out << to_string(batchSize)) : (out << "<null>"));
  out << ", " << "fetchReverse="; (__isset.fetchReverse ? (out << to_string(fetchReverse)) : (out << "<null>"));
  out << ", " << "lobChunkSize="; (__isset.lobChunkSize ? (out << to_string(lobChunkSize)) : (out << "<null>"));
  out << ", " << "maxRows="; (__isset.maxRows ? (out << to_string(maxRows)) : (out << "<null>"));
  out << ", " << "maxFieldSize="; (__isset.maxFieldSize ? (out << to_string(maxFieldSize)) : (out << "<null>"));
  out << ", " << "timeout="; (__isset.timeout ? (out << to_string(timeout)) : (out << "<null>"));
  out << ", " << "cursorName="; (__isset.cursorName ? (out << to_string(cursorName)) : (out << "<null>"));
  out << ", " << "possibleDuplicate="; (__isset.possibleDuplicate ? (out << to_string(possibleDuplicate)) : (out << "<null>"));
  out << ", " << "poolable="; (__isset.poolable ? (out << to_string(poolable)) : (out << "<null>"));
  out << ", " << "doEscapeProcessing="; (__isset.doEscapeProcessing ? (out << to_string(doEscapeProcessing)) : (out << "<null>"));
  out << ", " << "pendingTransactionAttrs="; (__isset.pendingTransactionAttrs ? (out << to_string(pendingTransactionAttrs)) : (out << "<null>"));
  out << ")";
}

}}} // namespace
