/**
 * Autogenerated by Thrift Compiler (0.10.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */

#include "LocatorService.h"

namespace io { namespace snappydata { namespace thrift {


LocatorService_getPreferredServer_args::~LocatorService_getPreferredServer_args() noexcept {
}


uint32_t LocatorService_getPreferredServer_args::read(::apache::thrift::protocol::TProtocol* iprot) {

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
        if (ftype == ::apache::thrift::protocol::T_SET) {
          {
            this->serverTypes.clear();
            uint32_t _size306;
            ::apache::thrift::protocol::TType _etype309;
            xfer += iprot->readSetBegin(_etype309, _size306);
            uint32_t _i310;
            for (_i310 = 0; _i310 < _size306; ++_i310)
            {
              ServerType::type _elem311;
              int32_t ecast312;
              xfer += iprot->readI32(ecast312);
              _elem311 = (ServerType::type)ecast312;
              this->serverTypes.insert(_elem311);
            }
            xfer += iprot->readSetEnd();
          }
          this->__isset.serverTypes = true;
        } else {
          xfer += iprot->skip(ftype);
        }
        break;
      case 2:
        if (ftype == ::apache::thrift::protocol::T_SET) {
          {
            this->serverGroups.clear();
            uint32_t _size313;
            ::apache::thrift::protocol::TType _etype316;
            xfer += iprot->readSetBegin(_etype316, _size313);
            uint32_t _i317;
            for (_i317 = 0; _i317 < _size313; ++_i317)
            {
              std::string _elem318;
              xfer += iprot->readString(_elem318);
              this->serverGroups.insert(_elem318);
            }
            xfer += iprot->readSetEnd();
          }
          this->__isset.serverGroups = true;
        } else {
          xfer += iprot->skip(ftype);
        }
        break;
      case 3:
        if (ftype == ::apache::thrift::protocol::T_SET) {
          {
            this->failedServers.clear();
            uint32_t _size319;
            ::apache::thrift::protocol::TType _etype322;
            xfer += iprot->readSetBegin(_etype322, _size319);
            uint32_t _i323;
            for (_i323 = 0; _i323 < _size319; ++_i323)
            {
              HostAddress _elem324;
              xfer += _elem324.read(iprot);
              this->failedServers.insert(_elem324);
            }
            xfer += iprot->readSetEnd();
          }
          this->__isset.failedServers = true;
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

uint32_t LocatorService_getPreferredServer_args::write(::apache::thrift::protocol::TProtocol* oprot) const {
  uint32_t xfer = 0;
  xfer += oprot->writeStructBegin("LocatorService_getPreferredServer_args");

  xfer += oprot->writeFieldBegin("serverTypes", ::apache::thrift::protocol::T_SET, 1);
  {
    xfer += oprot->writeSetBegin(::apache::thrift::protocol::T_I32, static_cast<uint32_t>(this->serverTypes.size()));
    std::set<ServerType::type> ::const_iterator _iter325;
    for (_iter325 = this->serverTypes.begin(); _iter325 != this->serverTypes.end(); ++_iter325)
    {
      xfer += oprot->writeI32((int32_t)(*_iter325));
    }
    xfer += oprot->writeSetEnd();
  }
  xfer += oprot->writeFieldEnd();

  xfer += oprot->writeFieldBegin("serverGroups", ::apache::thrift::protocol::T_SET, 2);
  {
    xfer += oprot->writeSetBegin(::apache::thrift::protocol::T_STRING, static_cast<uint32_t>(this->serverGroups.size()));
    std::set<std::string> ::const_iterator _iter326;
    for (_iter326 = this->serverGroups.begin(); _iter326 != this->serverGroups.end(); ++_iter326)
    {
      xfer += oprot->writeString((*_iter326));
    }
    xfer += oprot->writeSetEnd();
  }
  xfer += oprot->writeFieldEnd();

  xfer += oprot->writeFieldBegin("failedServers", ::apache::thrift::protocol::T_SET, 3);
  {
    xfer += oprot->writeSetBegin(::apache::thrift::protocol::T_STRUCT, static_cast<uint32_t>(this->failedServers.size()));
    std::set<HostAddress> ::const_iterator _iter327;
    for (_iter327 = this->failedServers.begin(); _iter327 != this->failedServers.end(); ++_iter327)
    {
      xfer += (*_iter327).write(oprot);
    }
    xfer += oprot->writeSetEnd();
  }
  xfer += oprot->writeFieldEnd();

  xfer += oprot->writeFieldStop();
  xfer += oprot->writeStructEnd();
  return xfer;
}


LocatorService_getPreferredServer_pargs::~LocatorService_getPreferredServer_pargs() noexcept {
}


uint32_t LocatorService_getPreferredServer_pargs::write(::apache::thrift::protocol::TProtocol* oprot) const {
  uint32_t xfer = 0;
  xfer += oprot->writeStructBegin("LocatorService_getPreferredServer_pargs");

  xfer += oprot->writeFieldBegin("serverTypes", ::apache::thrift::protocol::T_SET, 1);
  {
    xfer += oprot->writeSetBegin(::apache::thrift::protocol::T_I32, static_cast<uint32_t>((*(this->serverTypes)).size()));
    std::set<ServerType::type> ::const_iterator _iter328;
    for (_iter328 = (*(this->serverTypes)).begin(); _iter328 != (*(this->serverTypes)).end(); ++_iter328)
    {
      xfer += oprot->writeI32((int32_t)(*_iter328));
    }
    xfer += oprot->writeSetEnd();
  }
  xfer += oprot->writeFieldEnd();

  xfer += oprot->writeFieldBegin("serverGroups", ::apache::thrift::protocol::T_SET, 2);
  {
    xfer += oprot->writeSetBegin(::apache::thrift::protocol::T_STRING, static_cast<uint32_t>((*(this->serverGroups)).size()));
    std::set<std::string> ::const_iterator _iter329;
    for (_iter329 = (*(this->serverGroups)).begin(); _iter329 != (*(this->serverGroups)).end(); ++_iter329)
    {
      xfer += oprot->writeString((*_iter329));
    }
    xfer += oprot->writeSetEnd();
  }
  xfer += oprot->writeFieldEnd();

  xfer += oprot->writeFieldBegin("failedServers", ::apache::thrift::protocol::T_SET, 3);
  {
    xfer += oprot->writeSetBegin(::apache::thrift::protocol::T_STRUCT, static_cast<uint32_t>((*(this->failedServers)).size()));
    std::set<HostAddress> ::const_iterator _iter330;
    for (_iter330 = (*(this->failedServers)).begin(); _iter330 != (*(this->failedServers)).end(); ++_iter330)
    {
      xfer += (*_iter330).write(oprot);
    }
    xfer += oprot->writeSetEnd();
  }
  xfer += oprot->writeFieldEnd();

  xfer += oprot->writeFieldStop();
  xfer += oprot->writeStructEnd();
  return xfer;
}


LocatorService_getPreferredServer_result::~LocatorService_getPreferredServer_result() noexcept {
}


uint32_t LocatorService_getPreferredServer_result::read(::apache::thrift::protocol::TProtocol* iprot) {

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
      case 0:
        if (ftype == ::apache::thrift::protocol::T_STRUCT) {
          xfer += this->success.read(iprot);
          this->__isset.success = true;
        } else {
          xfer += iprot->skip(ftype);
        }
        break;
      case 1:
        if (ftype == ::apache::thrift::protocol::T_STRUCT) {
          xfer += this->error.read(iprot);
          this->__isset.error = true;
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

uint32_t LocatorService_getPreferredServer_result::write(::apache::thrift::protocol::TProtocol* oprot) const {

  uint32_t xfer = 0;

  xfer += oprot->writeStructBegin("LocatorService_getPreferredServer_result");

  if (this->__isset.success) {
    xfer += oprot->writeFieldBegin("success", ::apache::thrift::protocol::T_STRUCT, 0);
    xfer += this->success.write(oprot);
    xfer += oprot->writeFieldEnd();
  } else if (this->__isset.error) {
    xfer += oprot->writeFieldBegin("error", ::apache::thrift::protocol::T_STRUCT, 1);
    xfer += this->error.write(oprot);
    xfer += oprot->writeFieldEnd();
  }
  xfer += oprot->writeFieldStop();
  xfer += oprot->writeStructEnd();
  return xfer;
}


LocatorService_getPreferredServer_presult::~LocatorService_getPreferredServer_presult() noexcept {
}


uint32_t LocatorService_getPreferredServer_presult::read(::apache::thrift::protocol::TProtocol* iprot) {

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
      case 0:
        if (ftype == ::apache::thrift::protocol::T_STRUCT) {
          xfer += (*(this->success)).read(iprot);
          this->__isset.success = true;
        } else {
          xfer += iprot->skip(ftype);
        }
        break;
      case 1:
        if (ftype == ::apache::thrift::protocol::T_STRUCT) {
          xfer += this->error.read(iprot);
          this->__isset.error = true;
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


LocatorService_getAllServersWithPreferredServer_args::~LocatorService_getAllServersWithPreferredServer_args() noexcept {
}


uint32_t LocatorService_getAllServersWithPreferredServer_args::read(::apache::thrift::protocol::TProtocol* iprot) {

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
        if (ftype == ::apache::thrift::protocol::T_SET) {
          {
            this->serverTypes.clear();
            uint32_t _size331;
            ::apache::thrift::protocol::TType _etype334;
            xfer += iprot->readSetBegin(_etype334, _size331);
            uint32_t _i335;
            for (_i335 = 0; _i335 < _size331; ++_i335)
            {
              ServerType::type _elem336;
              int32_t ecast337;
              xfer += iprot->readI32(ecast337);
              _elem336 = (ServerType::type)ecast337;
              this->serverTypes.insert(_elem336);
            }
            xfer += iprot->readSetEnd();
          }
          this->__isset.serverTypes = true;
        } else {
          xfer += iprot->skip(ftype);
        }
        break;
      case 2:
        if (ftype == ::apache::thrift::protocol::T_SET) {
          {
            this->serverGroups.clear();
            uint32_t _size338;
            ::apache::thrift::protocol::TType _etype341;
            xfer += iprot->readSetBegin(_etype341, _size338);
            uint32_t _i342;
            for (_i342 = 0; _i342 < _size338; ++_i342)
            {
              std::string _elem343;
              xfer += iprot->readString(_elem343);
              this->serverGroups.insert(_elem343);
            }
            xfer += iprot->readSetEnd();
          }
          this->__isset.serverGroups = true;
        } else {
          xfer += iprot->skip(ftype);
        }
        break;
      case 3:
        if (ftype == ::apache::thrift::protocol::T_SET) {
          {
            this->failedServers.clear();
            uint32_t _size344;
            ::apache::thrift::protocol::TType _etype347;
            xfer += iprot->readSetBegin(_etype347, _size344);
            uint32_t _i348;
            for (_i348 = 0; _i348 < _size344; ++_i348)
            {
              HostAddress _elem349;
              xfer += _elem349.read(iprot);
              this->failedServers.insert(_elem349);
            }
            xfer += iprot->readSetEnd();
          }
          this->__isset.failedServers = true;
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

uint32_t LocatorService_getAllServersWithPreferredServer_args::write(::apache::thrift::protocol::TProtocol* oprot) const {
  uint32_t xfer = 0;
  xfer += oprot->writeStructBegin("LocatorService_getAllServersWithPreferredServer_args");

  xfer += oprot->writeFieldBegin("serverTypes", ::apache::thrift::protocol::T_SET, 1);
  {
    xfer += oprot->writeSetBegin(::apache::thrift::protocol::T_I32, static_cast<uint32_t>(this->serverTypes.size()));
    std::set<ServerType::type> ::const_iterator _iter350;
    for (_iter350 = this->serverTypes.begin(); _iter350 != this->serverTypes.end(); ++_iter350)
    {
      xfer += oprot->writeI32((int32_t)(*_iter350));
    }
    xfer += oprot->writeSetEnd();
  }
  xfer += oprot->writeFieldEnd();

  xfer += oprot->writeFieldBegin("serverGroups", ::apache::thrift::protocol::T_SET, 2);
  {
    xfer += oprot->writeSetBegin(::apache::thrift::protocol::T_STRING, static_cast<uint32_t>(this->serverGroups.size()));
    std::set<std::string> ::const_iterator _iter351;
    for (_iter351 = this->serverGroups.begin(); _iter351 != this->serverGroups.end(); ++_iter351)
    {
      xfer += oprot->writeString((*_iter351));
    }
    xfer += oprot->writeSetEnd();
  }
  xfer += oprot->writeFieldEnd();

  xfer += oprot->writeFieldBegin("failedServers", ::apache::thrift::protocol::T_SET, 3);
  {
    xfer += oprot->writeSetBegin(::apache::thrift::protocol::T_STRUCT, static_cast<uint32_t>(this->failedServers.size()));
    std::set<HostAddress> ::const_iterator _iter352;
    for (_iter352 = this->failedServers.begin(); _iter352 != this->failedServers.end(); ++_iter352)
    {
      xfer += (*_iter352).write(oprot);
    }
    xfer += oprot->writeSetEnd();
  }
  xfer += oprot->writeFieldEnd();

  xfer += oprot->writeFieldStop();
  xfer += oprot->writeStructEnd();
  return xfer;
}


LocatorService_getAllServersWithPreferredServer_pargs::~LocatorService_getAllServersWithPreferredServer_pargs() noexcept {
}


uint32_t LocatorService_getAllServersWithPreferredServer_pargs::write(::apache::thrift::protocol::TProtocol* oprot) const {
  uint32_t xfer = 0;
  xfer += oprot->writeStructBegin("LocatorService_getAllServersWithPreferredServer_pargs");

  xfer += oprot->writeFieldBegin("serverTypes", ::apache::thrift::protocol::T_SET, 1);
  {
    xfer += oprot->writeSetBegin(::apache::thrift::protocol::T_I32, static_cast<uint32_t>((*(this->serverTypes)).size()));
    std::set<ServerType::type> ::const_iterator _iter353;
    for (_iter353 = (*(this->serverTypes)).begin(); _iter353 != (*(this->serverTypes)).end(); ++_iter353)
    {
      xfer += oprot->writeI32((int32_t)(*_iter353));
    }
    xfer += oprot->writeSetEnd();
  }
  xfer += oprot->writeFieldEnd();

  xfer += oprot->writeFieldBegin("serverGroups", ::apache::thrift::protocol::T_SET, 2);
  {
    xfer += oprot->writeSetBegin(::apache::thrift::protocol::T_STRING, static_cast<uint32_t>((*(this->serverGroups)).size()));
    std::set<std::string> ::const_iterator _iter354;
    for (_iter354 = (*(this->serverGroups)).begin(); _iter354 != (*(this->serverGroups)).end(); ++_iter354)
    {
      xfer += oprot->writeString((*_iter354));
    }
    xfer += oprot->writeSetEnd();
  }
  xfer += oprot->writeFieldEnd();

  xfer += oprot->writeFieldBegin("failedServers", ::apache::thrift::protocol::T_SET, 3);
  {
    xfer += oprot->writeSetBegin(::apache::thrift::protocol::T_STRUCT, static_cast<uint32_t>((*(this->failedServers)).size()));
    std::set<HostAddress> ::const_iterator _iter355;
    for (_iter355 = (*(this->failedServers)).begin(); _iter355 != (*(this->failedServers)).end(); ++_iter355)
    {
      xfer += (*_iter355).write(oprot);
    }
    xfer += oprot->writeSetEnd();
  }
  xfer += oprot->writeFieldEnd();

  xfer += oprot->writeFieldStop();
  xfer += oprot->writeStructEnd();
  return xfer;
}


LocatorService_getAllServersWithPreferredServer_result::~LocatorService_getAllServersWithPreferredServer_result() noexcept {
}


uint32_t LocatorService_getAllServersWithPreferredServer_result::read(::apache::thrift::protocol::TProtocol* iprot) {

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
      case 0:
        if (ftype == ::apache::thrift::protocol::T_LIST) {
          {
            this->success.clear();
            uint32_t _size356;
            ::apache::thrift::protocol::TType _etype359;
            xfer += iprot->readListBegin(_etype359, _size356);
            this->success.resize(_size356);
            uint32_t _i360;
            for (_i360 = 0; _i360 < _size356; ++_i360)
            {
              xfer += this->success[_i360].read(iprot);
            }
            xfer += iprot->readListEnd();
          }
          this->__isset.success = true;
        } else {
          xfer += iprot->skip(ftype);
        }
        break;
      case 1:
        if (ftype == ::apache::thrift::protocol::T_STRUCT) {
          xfer += this->error.read(iprot);
          this->__isset.error = true;
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

uint32_t LocatorService_getAllServersWithPreferredServer_result::write(::apache::thrift::protocol::TProtocol* oprot) const {

  uint32_t xfer = 0;

  xfer += oprot->writeStructBegin("LocatorService_getAllServersWithPreferredServer_result");

  if (this->__isset.success) {
    xfer += oprot->writeFieldBegin("success", ::apache::thrift::protocol::T_LIST, 0);
    {
      xfer += oprot->writeListBegin(::apache::thrift::protocol::T_STRUCT, static_cast<uint32_t>(this->success.size()));
      std::vector<HostAddress> ::const_iterator _iter361;
      for (_iter361 = this->success.begin(); _iter361 != this->success.end(); ++_iter361)
      {
        xfer += (*_iter361).write(oprot);
      }
      xfer += oprot->writeListEnd();
    }
    xfer += oprot->writeFieldEnd();
  } else if (this->__isset.error) {
    xfer += oprot->writeFieldBegin("error", ::apache::thrift::protocol::T_STRUCT, 1);
    xfer += this->error.write(oprot);
    xfer += oprot->writeFieldEnd();
  }
  xfer += oprot->writeFieldStop();
  xfer += oprot->writeStructEnd();
  return xfer;
}


LocatorService_getAllServersWithPreferredServer_presult::~LocatorService_getAllServersWithPreferredServer_presult() noexcept {
}


uint32_t LocatorService_getAllServersWithPreferredServer_presult::read(::apache::thrift::protocol::TProtocol* iprot) {

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
      case 0:
        if (ftype == ::apache::thrift::protocol::T_LIST) {
          {
            (*(this->success)).clear();
            uint32_t _size362;
            ::apache::thrift::protocol::TType _etype365;
            xfer += iprot->readListBegin(_etype365, _size362);
            (*(this->success)).resize(_size362);
            uint32_t _i366;
            for (_i366 = 0; _i366 < _size362; ++_i366)
            {
              xfer += (*(this->success))[_i366].read(iprot);
            }
            xfer += iprot->readListEnd();
          }
          this->__isset.success = true;
        } else {
          xfer += iprot->skip(ftype);
        }
        break;
      case 1:
        if (ftype == ::apache::thrift::protocol::T_STRUCT) {
          xfer += this->error.read(iprot);
          this->__isset.error = true;
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

void LocatorServiceClient::getPreferredServer(HostAddress& _return, const std::set<ServerType::type> & serverTypes, const std::set<std::string> & serverGroups, const std::set<HostAddress> & failedServers)
{
  send_getPreferredServer(serverTypes, serverGroups, failedServers);
  recv_getPreferredServer(_return);
}

void LocatorServiceClient::send_getPreferredServer(const std::set<ServerType::type> & serverTypes, const std::set<std::string> & serverGroups, const std::set<HostAddress> & failedServers)
{
  int32_t cseqid = 0;
  oprot_->writeMessageBegin("getPreferredServer", ::apache::thrift::protocol::T_CALL, cseqid);

  LocatorService_getPreferredServer_pargs args;
  args.serverTypes = &serverTypes;
  args.serverGroups = &serverGroups;
  args.failedServers = &failedServers;
  args.write(oprot_);

  oprot_->writeMessageEnd();
  oprot_->getTransport()->writeEnd();
  oprot_->getTransport()->flush();
}

void LocatorServiceClient::recv_getPreferredServer(HostAddress& _return)
{

  int32_t rseqid = 0;
  std::string fname;
  ::apache::thrift::protocol::TMessageType mtype;

  iprot_->readMessageBegin(fname, mtype, rseqid);
  if (mtype == ::apache::thrift::protocol::T_EXCEPTION) {
    ::apache::thrift::TApplicationException x;
    x.read(iprot_);
    iprot_->readMessageEnd();
    iprot_->getTransport()->readEnd();
    throw x;
  }
  if (mtype != ::apache::thrift::protocol::T_REPLY) {
    iprot_->skip(::apache::thrift::protocol::T_STRUCT);
    iprot_->readMessageEnd();
    iprot_->getTransport()->readEnd();
  }
  if (fname.compare("getPreferredServer") != 0) {
    iprot_->skip(::apache::thrift::protocol::T_STRUCT);
    iprot_->readMessageEnd();
    iprot_->getTransport()->readEnd();
  }
  LocatorService_getPreferredServer_presult result;
  result.success = &_return;
  result.read(iprot_);
  iprot_->readMessageEnd();
  iprot_->getTransport()->readEnd();

  if (result.__isset.success) {
    // _return pointer has now been filled
    return;
  }
  if (result.__isset.error) {
    throw result.error;
  }
  throw ::apache::thrift::TApplicationException(::apache::thrift::TApplicationException::MISSING_RESULT, "getPreferredServer failed: unknown result");
}

void LocatorServiceClient::getAllServersWithPreferredServer(std::vector<HostAddress> & _return, const std::set<ServerType::type> & serverTypes, const std::set<std::string> & serverGroups, const std::set<HostAddress> & failedServers)
{
  send_getAllServersWithPreferredServer(serverTypes, serverGroups, failedServers);
  recv_getAllServersWithPreferredServer(_return);
}

void LocatorServiceClient::send_getAllServersWithPreferredServer(const std::set<ServerType::type> & serverTypes, const std::set<std::string> & serverGroups, const std::set<HostAddress> & failedServers)
{
  int32_t cseqid = 0;
  oprot_->writeMessageBegin("getAllServersWithPreferredServer", ::apache::thrift::protocol::T_CALL, cseqid);

  LocatorService_getAllServersWithPreferredServer_pargs args;
  args.serverTypes = &serverTypes;
  args.serverGroups = &serverGroups;
  args.failedServers = &failedServers;
  args.write(oprot_);

  oprot_->writeMessageEnd();
  oprot_->getTransport()->writeEnd();
  oprot_->getTransport()->flush();
}

void LocatorServiceClient::recv_getAllServersWithPreferredServer(std::vector<HostAddress> & _return)
{

  int32_t rseqid = 0;
  std::string fname;
  ::apache::thrift::protocol::TMessageType mtype;

  iprot_->readMessageBegin(fname, mtype, rseqid);
  if (mtype == ::apache::thrift::protocol::T_EXCEPTION) {
    ::apache::thrift::TApplicationException x;
    x.read(iprot_);
    iprot_->readMessageEnd();
    iprot_->getTransport()->readEnd();
    throw x;
  }
  if (mtype != ::apache::thrift::protocol::T_REPLY) {
    iprot_->skip(::apache::thrift::protocol::T_STRUCT);
    iprot_->readMessageEnd();
    iprot_->getTransport()->readEnd();
  }
  if (fname.compare("getAllServersWithPreferredServer") != 0) {
    iprot_->skip(::apache::thrift::protocol::T_STRUCT);
    iprot_->readMessageEnd();
    iprot_->getTransport()->readEnd();
  }
  LocatorService_getAllServersWithPreferredServer_presult result;
  result.success = &_return;
  result.read(iprot_);
  iprot_->readMessageEnd();
  iprot_->getTransport()->readEnd();

  if (result.__isset.success) {
    // _return pointer has now been filled
    return;
  }
  if (result.__isset.error) {
    throw result.error;
  }
  throw ::apache::thrift::TApplicationException(::apache::thrift::TApplicationException::MISSING_RESULT, "getAllServersWithPreferredServer failed: unknown result");
}

bool LocatorServiceProcessor::dispatchCall(::apache::thrift::protocol::TProtocol* iprot, ::apache::thrift::protocol::TProtocol* oprot, const std::string& fname, int32_t seqid, void* callContext) {
  ProcessMap::iterator pfn;
  pfn = processMap_.find(fname);
  if (pfn == processMap_.end()) {
    iprot->skip(::apache::thrift::protocol::T_STRUCT);
    iprot->readMessageEnd();
    iprot->getTransport()->readEnd();
    ::apache::thrift::TApplicationException x(::apache::thrift::TApplicationException::UNKNOWN_METHOD, "Invalid method name: '"+fname+"'");
    oprot->writeMessageBegin(fname, ::apache::thrift::protocol::T_EXCEPTION, seqid);
    x.write(oprot);
    oprot->writeMessageEnd();
    oprot->getTransport()->writeEnd();
    oprot->getTransport()->flush();
    return true;
  }
  (this->*(pfn->second))(seqid, iprot, oprot, callContext);
  return true;
}

void LocatorServiceProcessor::process_getPreferredServer(int32_t seqid, ::apache::thrift::protocol::TProtocol* iprot, ::apache::thrift::protocol::TProtocol* oprot, void* callContext)
{
  void* ctx = NULL;
  if (this->eventHandler_.get() != NULL) {
    ctx = this->eventHandler_->getContext("LocatorService.getPreferredServer", callContext);
  }
  ::apache::thrift::TProcessorContextFreer freer(this->eventHandler_.get(), ctx, "LocatorService.getPreferredServer");

  if (this->eventHandler_.get() != NULL) {
    this->eventHandler_->preRead(ctx, "LocatorService.getPreferredServer");
  }

  LocatorService_getPreferredServer_args args;
  args.read(iprot);
  iprot->readMessageEnd();
  uint32_t bytes = iprot->getTransport()->readEnd();

  if (this->eventHandler_.get() != NULL) {
    this->eventHandler_->postRead(ctx, "LocatorService.getPreferredServer", bytes);
  }

  LocatorService_getPreferredServer_result result;
  try {
    iface_->getPreferredServer(result.success, args.serverTypes, args.serverGroups, args.failedServers);
    result.__isset.success = true;
  } catch (SnappyException &error) {
    result.error = error;
    result.__isset.error = true;
  } catch (const std::exception& e) {
    if (this->eventHandler_.get() != NULL) {
      this->eventHandler_->handlerError(ctx, "LocatorService.getPreferredServer");
    }

    ::apache::thrift::TApplicationException x(e.what());
    oprot->writeMessageBegin("getPreferredServer", ::apache::thrift::protocol::T_EXCEPTION, seqid);
    x.write(oprot);
    oprot->writeMessageEnd();
    oprot->getTransport()->writeEnd();
    oprot->getTransport()->flush();
    return;
  }

  if (this->eventHandler_.get() != NULL) {
    this->eventHandler_->preWrite(ctx, "LocatorService.getPreferredServer");
  }

  oprot->writeMessageBegin("getPreferredServer", ::apache::thrift::protocol::T_REPLY, seqid);
  result.write(oprot);
  oprot->writeMessageEnd();
  bytes = oprot->getTransport()->writeEnd();
  oprot->getTransport()->flush();

  if (this->eventHandler_.get() != NULL) {
    this->eventHandler_->postWrite(ctx, "LocatorService.getPreferredServer", bytes);
  }
}

void LocatorServiceProcessor::process_getAllServersWithPreferredServer(int32_t seqid, ::apache::thrift::protocol::TProtocol* iprot, ::apache::thrift::protocol::TProtocol* oprot, void* callContext)
{
  void* ctx = NULL;
  if (this->eventHandler_.get() != NULL) {
    ctx = this->eventHandler_->getContext("LocatorService.getAllServersWithPreferredServer", callContext);
  }
  ::apache::thrift::TProcessorContextFreer freer(this->eventHandler_.get(), ctx, "LocatorService.getAllServersWithPreferredServer");

  if (this->eventHandler_.get() != NULL) {
    this->eventHandler_->preRead(ctx, "LocatorService.getAllServersWithPreferredServer");
  }

  LocatorService_getAllServersWithPreferredServer_args args;
  args.read(iprot);
  iprot->readMessageEnd();
  uint32_t bytes = iprot->getTransport()->readEnd();

  if (this->eventHandler_.get() != NULL) {
    this->eventHandler_->postRead(ctx, "LocatorService.getAllServersWithPreferredServer", bytes);
  }

  LocatorService_getAllServersWithPreferredServer_result result;
  try {
    iface_->getAllServersWithPreferredServer(result.success, args.serverTypes, args.serverGroups, args.failedServers);
    result.__isset.success = true;
  } catch (SnappyException &error) {
    result.error = error;
    result.__isset.error = true;
  } catch (const std::exception& e) {
    if (this->eventHandler_.get() != NULL) {
      this->eventHandler_->handlerError(ctx, "LocatorService.getAllServersWithPreferredServer");
    }

    ::apache::thrift::TApplicationException x(e.what());
    oprot->writeMessageBegin("getAllServersWithPreferredServer", ::apache::thrift::protocol::T_EXCEPTION, seqid);
    x.write(oprot);
    oprot->writeMessageEnd();
    oprot->getTransport()->writeEnd();
    oprot->getTransport()->flush();
    return;
  }

  if (this->eventHandler_.get() != NULL) {
    this->eventHandler_->preWrite(ctx, "LocatorService.getAllServersWithPreferredServer");
  }

  oprot->writeMessageBegin("getAllServersWithPreferredServer", ::apache::thrift::protocol::T_REPLY, seqid);
  result.write(oprot);
  oprot->writeMessageEnd();
  bytes = oprot->getTransport()->writeEnd();
  oprot->getTransport()->flush();

  if (this->eventHandler_.get() != NULL) {
    this->eventHandler_->postWrite(ctx, "LocatorService.getAllServersWithPreferredServer", bytes);
  }
}

::boost::shared_ptr< ::apache::thrift::TProcessor > LocatorServiceProcessorFactory::getProcessor(const ::apache::thrift::TConnectionInfo& connInfo) {
  ::apache::thrift::ReleaseHandler< LocatorServiceIfFactory > cleanup(handlerFactory_);
  ::boost::shared_ptr< LocatorServiceIf > handler(handlerFactory_->getHandler(connInfo), cleanup);
  ::boost::shared_ptr< ::apache::thrift::TProcessor > processor(new LocatorServiceProcessor(handler));
  return processor;
}

}}} // namespace

