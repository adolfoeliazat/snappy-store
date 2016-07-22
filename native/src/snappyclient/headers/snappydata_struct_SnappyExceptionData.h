/**
 * Autogenerated by Thrift Compiler (1.0.0-dev)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */

#ifndef SNAPPYDATA_STRUCT_SNAPPYEXCEPTIONDATA_H
#define SNAPPYDATA_STRUCT_SNAPPYEXCEPTIONDATA_H


#include "snappydata_struct_Decimal.h"
#include "snappydata_struct_Timestamp.h"
#include "snappydata_struct_JSONValue.h"
#include "snappydata_struct_JSONObject.h"
#include "snappydata_struct_BlobChunk.h"
#include "snappydata_struct_ClobChunk.h"
#include "snappydata_struct_ServiceMetaData.h"
#include "snappydata_struct_ServiceMetaDataArgs.h"
#include "snappydata_struct_OpenConnectionArgs.h"
#include "snappydata_struct_ConnectionProperties.h"
#include "snappydata_struct_HostAddress.h"

#include "snappydata_types.h"

namespace io { namespace snappydata { namespace thrift {


class SnappyExceptionData {
 public:

  SnappyExceptionData(const SnappyExceptionData&);
  SnappyExceptionData(SnappyExceptionData&&) noexcept;
  SnappyExceptionData& operator=(const SnappyExceptionData&);
  SnappyExceptionData& operator=(SnappyExceptionData&&) noexcept;
  SnappyExceptionData() : reason(), sqlState(), severity(0) {
  }

  virtual ~SnappyExceptionData() noexcept;
  std::string reason;
  std::string sqlState;
  int32_t severity;

  void __set_reason(const std::string& val);

  void __set_sqlState(const std::string& val);

  void __set_severity(const int32_t val);

  bool operator == (const SnappyExceptionData & rhs) const
  {
    if (!(reason == rhs.reason))
      return false;
    if (!(sqlState == rhs.sqlState))
      return false;
    if (!(severity == rhs.severity))
      return false;
    return true;
  }
  bool operator != (const SnappyExceptionData &rhs) const {
    return !(*this == rhs);
  }

  bool operator < (const SnappyExceptionData & ) const;

  uint32_t read(::apache::thrift::protocol::TProtocol* iprot);
  uint32_t write(::apache::thrift::protocol::TProtocol* oprot) const;

  virtual void printTo(std::ostream& out) const;
};

void swap(SnappyExceptionData &a, SnappyExceptionData &b) noexcept;

inline std::ostream& operator<<(std::ostream& out, const SnappyExceptionData& obj)
{
  obj.printTo(out);
  return out;
}

}}} // namespace

#endif