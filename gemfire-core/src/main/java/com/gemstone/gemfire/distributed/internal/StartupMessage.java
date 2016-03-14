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
package com.gemstone.gemfire.distributed.internal;

import com.gemstone.gemfire.DataSerializer;
import com.gemstone.gemfire.Instantiator;
import com.gemstone.gemfire.SystemConnectException;
import com.gemstone.gemfire.internal.GemFireVersion;
import com.gemstone.gemfire.internal.InternalDataSerializer;
import com.gemstone.gemfire.internal.InternalDataSerializer.SerializerAttributesHolder;
import com.gemstone.gemfire.internal.InternalInstantiator;
import com.gemstone.gemfire.internal.InternalInstantiator.InstantiatorAttributesHolder;
import com.gemstone.gemfire.internal.SocketCreator;
import com.gemstone.gemfire.internal.i18n.LocalizedStrings;
import com.gemstone.gemfire.internal.tcp.Stub;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Collections;
import java.util.Collection;
import java.util.Properties;
import java.util.Set;

/**
 * A message that is sent to all other distribution manager when
 * a distribution manager starts up.
 */
public final class StartupMessage extends HighPriorityDistributionMessage implements AdminMessageType {

  /** A stub for the direct channel for this manager */
  private Stub directChannel;
  // below added for bug 29005
  // [sumedh] now use getUnderlyingGemFireVersion so that the version returned
  // is that of underlying GemFire product for GemFireXD
  private String version = GemFireVersion.getGemFireVersion();
  private int replyProcessorId;
  private boolean isMcastEnabled;
  private boolean isTcpDisabled;
  private Set interfaces;
  private int distributedSystemId;
  private String redundancyZone;
  private boolean enforceUniqueZone;
  
  // additional fields using StartupMessageData below here...
  private Collection<String> hostedLocators;
  private int mcastPort;
  private String mcastHostAddress; // see InetAddress.getHostAddress() for the format of this string
  
  /**
   * Determine all of the addresses that this host represents.  An empty list
   * will be regarded as an error by all who see it.
   * 
   * @return list of addresses for this host
   * @since 5.7
   */
  public static Set getMyAddresses(DistributionManager dm) {
    try {
      Set addresses =  SocketCreator.getMyAddresses(false);
      return addresses;
    } catch (IllegalArgumentException e) {
      dm.getLoggerI18n().severe(e);
      return Collections.EMPTY_SET;
    }
  }
  
  /** A list of errors that occurs while deserializing this message.
   * See bug 31573. */
  private transient StringBuffer fromDataProblems;
  
  /**
   * Creates new instance for DataSerializer.
   */
  public StartupMessage() {}
  
  /**
   * Creates new instance for StartupOperation.
   * @param hostedLocators
   */
  StartupMessage(Collection<String> hostedLocators) {
    this.hostedLocators = hostedLocators;
  }
  
  ///////////////////////  Instance Methods  ///////////////////////

  /**
   * Sets the id of the distribution manager that is starting up
   */
  void setDirectChannel(Stub directChannel) {
    this.directChannel = directChannel;
  }

  /**
   * Sets the reply processor for this message
   */
  void setReplyProcessorId(int proc) {
    this.replyProcessorId = proc;
  }
  
  /**
   * Sets the mcastEnabled flag for this message
   * @since 5.0
   */
  void setMcastEnabled(boolean flag) {
    isMcastEnabled = flag;
  }
  
  int getMcastPort() {
    return this.mcastPort;
  }
  void setMcastPort(int port) {
    this.mcastPort = port;
  }
  
  String getMcastHostAddress() {
    return this.mcastHostAddress;
  }
  void setMcastHostAddress(InetAddress addr) {
    String hostAddr = null;
    if (addr != null) {
      hostAddr = addr.getHostAddress();
    }
    this.mcastHostAddress = hostAddr;
  }
  
  @Override
  public boolean sendViaUDP() {
    return true;
  }
  
  
  /**
   * Sets the tcpDisabled flag for this message
   * @since 5.0
   */
  void setTcpDisabled(boolean flag) {
    isTcpDisabled = flag;
  }

  void setInterfaces(Set interfaces) {
    this.interfaces = interfaces; 
    if (interfaces == null || interfaces.size() == 0) {
      throw new SystemConnectException("Unable to examine network card");
    }
  }
  
  public void setDistributedSystemId(int distributedSystemId) {
    this.distributedSystemId = distributedSystemId;
  }

  public void setRedundancyZone(String redundancyZone) {
    this.redundancyZone = redundancyZone;
  }
  
  public void setEnforceUniqueZone(boolean enforceUniqueZone) {
    this.enforceUniqueZone = enforceUniqueZone;
  }
  
  /**
   * Adds the distribution manager that is started up to the current
   * DM's list of members.
   *
   * This method is invoked on the receiver side
   */
  @Override
  protected void process(DistributionManager dm) {
    String rejectionMessage = null;
    final boolean isAdminDM = dm.getId().getVmKind() == DistributionManager.ADMIN_ONLY_DM_TYPE || dm.getId().getVmKind() == DistributionManager.LOCATOR_DM_TYPE;

    //dm.getLogger().info("theirs: isMcastEnabled="+isMcastEnabled
    //  + " isMcastDiscovery="+isMcastDiscovery
    //  + " isTcpDisabled="+isTcpDisabled);
    //dm.getLogger().info("mine: isMcastEnabled="+dm.getTransport().isMcastEnabled()
    // + " isMcastDiscovery="+dm.getTransport().isMcastDiscovery()
    // + " isTcpDisabled="+dm.getTransport().isTcpDisabled());
    String myVersion = GemFireVersion.getGemFireVersion();
    String theirVersion = this.version;
    int myMajorVersion = GemFireVersion.getMajorVersion(myVersion);
    int theirMajorVersion = GemFireVersion.getMajorVersion(theirVersion);
    int myMinorVersion = GemFireVersion.getMinorVersion(myVersion);
    int theirMinorVersion = GemFireVersion.getMinorVersion(theirVersion);
    // fix for bug 43608
    if (myMajorVersion != theirMajorVersion || myMinorVersion != theirMinorVersion) {
      // now don't reject at this level since it will be handled at
      // JGroups/Connection handshake level
      /*
      rejectionMessage = 
          LocalizedStrings.StartupMessage_REJECTED_NEW_SYSTEM_NODE_0_WITH_PRODUCT_VERSION_1_BECAUSE_THE_EXISTING_DISTRIBUTED_SYSTEM_NODE_2_HAS_A_PRODUCT_VERSION_OF_3
          .toLocalizedString(new Object[] {getSender(), this.version, dm.getId(), GemFireVersion.getGemFireVersion()});
      */
    }
    if (dm.getTransport().isMcastEnabled() != isMcastEnabled) {
      rejectionMessage =
        LocalizedStrings.StartupMessage_REJECTED_NEW_SYSTEM_NODE_0_BECAUSE_ISMCASTENABLED_1_DOES_NOT_MATCH_THE_DISTRIBUTED_SYSTEM_IT_IS_ATTEMPTING_TO_JOIN
        .toLocalizedString(new Object[] {getSender(), isMcastEnabled ? "enabled" : "disabled"});
    }
    else if (isMcastEnabled && dm.getSystem().getOriginalConfig().getMcastPort() != getMcastPort()) {
      rejectionMessage =
        LocalizedStrings.StartupMessage_REJECTED_NEW_SYSTEM_NODE_0_BECAUSE_MCAST_PORT_1_DOES_NOT_MATCH_THE_DISTRIBUTED_SYSTEM_2_IT_IS_ATTEMPTING_TO_JOIN
        .toLocalizedString(new Object[] {getSender(), getMcastPort(), dm.getSystem().getOriginalConfig().getMcastPort()});
    }
    else if (isMcastEnabled && !checkMcastAddress(dm.getSystem().getOriginalConfig().getMcastAddress(), getMcastHostAddress())) {
      rejectionMessage =
        LocalizedStrings.StartupMessage_REJECTED_NEW_SYSTEM_NODE_0_BECAUSE_MCAST_ADDRESS_1_DOES_NOT_MATCH_THE_DISTRIBUTED_SYSTEM_2_IT_IS_ATTEMPTING_TO_JOIN
        .toLocalizedString(new Object[] {getSender(), getMcastHostAddress(), dm.getSystem().getOriginalConfig().getMcastAddress()});
        }
    //else if (dm.getTransport().isMcastDiscovery() != isMcastDiscovery) {
    //  boolean flag = dm.getTransport().isMcastDiscovery();
    //  rejectionMessage = "Rejected new system node " + getSender()
    //   + " because it is " + (isMcastDiscovery? "not " : "")
    //   + "using locators, but existing nodes " + (flag? "are not." : "are.");
    //  dm.getLogger().warning(rejectionMessage);
    //}
    else if (dm.getTransport().isTcpDisabled() != isTcpDisabled) {           
      rejectionMessage =
          LocalizedStrings.StartupMessage_REJECTED_NEW_SYSTEM_NODE_0_BECAUSE_ISTCPDISABLED_1_DOES_NOT_MATCH_THE_DISTRIBUTED_SYSTEM_IT_IS_ATTEMPTING_TO_JOIN
          .toLocalizedString(new Object[] {getSender(), Boolean.valueOf(isTcpDisabled)});
  }
   else if (dm.getDistributedSystemId() != DistributionConfig.DEFAULT_DISTRIBUTED_SYSTEM_ID && distributedSystemId !=  DistributionConfig.DEFAULT_DISTRIBUTED_SYSTEM_ID 
             && distributedSystemId != dm.getDistributedSystemId()) {

     String distributedSystemListener = System
     .getProperty("gemfire.DistributedSystemListener");
     //this check is specific for Jayesh's use case of WAN BootStraping
     if(distributedSystemListener != null){
       if(-distributedSystemId != dm.getDistributedSystemId()){
         rejectionMessage = LocalizedStrings.StartupMessage_REJECTED_NEW_SYSTEM_NODE_0_BECAUSE_DISTRIBUTED_SYSTEM_ID_1_DOES_NOT_MATCH_THE_DISTRIBUTED_SYSTEM_2_IT_IS_ATTEMPTING_TO_JOIN.toLocalizedString(
             new Object[] {getSender(), Integer.valueOf(distributedSystemId), dm.getDistributedSystemId()});
       } 
     }else{
       rejectionMessage = LocalizedStrings.StartupMessage_REJECTED_NEW_SYSTEM_NODE_0_BECAUSE_DISTRIBUTED_SYSTEM_ID_1_DOES_NOT_MATCH_THE_DISTRIBUTED_SYSTEM_2_IT_IS_ATTEMPTING_TO_JOIN.toLocalizedString(
           new Object[] {getSender(), Integer.valueOf(distributedSystemId), dm.getDistributedSystemId()});
     }
   }

    if (this.fromDataProblems != null) {
      dm.getLoggerI18n().info(LocalizedStrings.ONE_ARG, this.fromDataProblems.toString());
    }

    if (rejectionMessage == null) { // change state only if there's no rejectionMessage yet
      if (this.interfaces == null || this.interfaces.size() == 0) {
        final com.gemstone.gemfire.i18n.StringId msg = 
          LocalizedStrings.StartupMessage_REJECTED_NEW_SYSTEM_NODE_0_BECAUSE_PEER_HAS_NO_NETWORK_INTERFACES;
        rejectionMessage = msg.toLocalizedString(getSender());
      }
      else {
        dm.setEquivalentHosts(this.interfaces);
      }
    }

    if (rejectionMessage != null) {
      dm.getLoggerI18n().convertToLogWriter().warning(rejectionMessage);
    }

    if (rejectionMessage == null) { // change state only if there's no rejectionMessage yet
      dm.setRedundancyZone(getSender(), this.redundancyZone);
      dm.setEnforceUniqueZone(this.enforceUniqueZone);
      if (this.hostedLocators != null) {
        dm.addHostedLocators(getSender(), this.hostedLocators);
      }
    }

    StartupResponseMessage m = null;
    //Commenting out. See Bruces note in the StartupMessageData constructor. 
    //Comparisons should use the functionality described in SerializationVersions 
//    if (GemFireVersion.compareVersions(theirVersion,"6.6.2") >= 0) {
      m = new StartupResponseWithVersionMessage(dm, replyProcessorId, getSender(), rejectionMessage, isAdminDM);
//    } else {
//      m = new StartupResponseMessage(dm, replyProcessorId, getSender(), rejectionMessage, isAdminDM);
//    }
    dm.getLoggerI18n().fine("Received StartupMessage from a member with version:"
        +theirVersion+",my version is:"+myVersion);
    dm.putOutgoing(m);
    if (rejectionMessage != null) {
      dm.getMembershipManager().startupMessageFailed(getSender(), rejectionMessage);
    }

    // bug33638: we need to discard this member if they aren't a peer.
    if (rejectionMessage != null)
      dm.handleManagerDeparture(getSender(), false, rejectionMessage);
  }

  private static boolean checkMcastAddress(InetAddress myMcastAddr, String otherMcastHostAddr) {
    String myMcastHostAddr = null;
    if (myMcastAddr != null) {
      myMcastHostAddr = myMcastAddr.getHostAddress();
    }
    if (myMcastHostAddr == otherMcastHostAddr) return true;
    if (myMcastHostAddr == null) return false;
    return myMcastHostAddr.equals(otherMcastHostAddr);
  }
  
  public int getDSFID() {
    return STARTUP_MESSAGE;
  }

  @Override
  public void toData(DataOutput out) throws IOException {
    super.toData(out);
    out.writeBoolean(this.directChannel != null);
    if (this.directChannel != null) {
      InternalDataSerializer.invokeToData(this.directChannel, out);
    }
    // for erstwhile license properties
    DataSerializer.writeObject(null, out);
    DataSerializer.writeString(this.version, out);
    out.writeInt(this.replyProcessorId);
    out.writeBoolean(this.isMcastEnabled);
    out.writeBoolean(this.isTcpDisabled);

    // Send a description of all of the DataSerializers and
    // Instantiators that have been registered
    SerializerAttributesHolder[] sahs = InternalDataSerializer.getSerializersForDistribution();
    out.writeInt(sahs.length);
    for (int i = 0; i < sahs.length; i++) {
      DataSerializer.writeNonPrimitiveClassName(sahs[i].getClassName(), out);
      out.writeInt(sahs[i].getId());
    }

    Object[] insts = InternalInstantiator.getInstantiatorsForSerialization();
    out.writeInt(insts.length);
    for (int i = 0; i < insts.length; i++) {
      String instantiatorClassName, instantiatedClassName;
      int id;
      if (insts[i] instanceof Instantiator) {
        instantiatorClassName = ((Instantiator)insts[i]).getClass().getName();
        instantiatedClassName = ((Instantiator)insts[i]).getInstantiatedClass().getName();
        id = ((Instantiator)insts[i]).getId();
      } else {
        instantiatorClassName = ((InstantiatorAttributesHolder)insts[i]).getInstantiatorClassName();
        instantiatedClassName = ((InstantiatorAttributesHolder)insts[i]).getInstantiatedClassName();
        id = ((InstantiatorAttributesHolder)insts[i]).getId();
      }
      DataSerializer.writeNonPrimitiveClassName(instantiatorClassName, out);
      DataSerializer.writeNonPrimitiveClassName(instantiatedClassName, out);
      out.writeInt(id);
    }
    DataSerializer.writeObject(interfaces, out);
    out.writeInt(distributedSystemId);
    DataSerializer.writeString(redundancyZone, out);
    out.writeBoolean(enforceUniqueZone);
    
    StartupMessageData data = new StartupMessageData();
    data.writeHostedLocators(this.hostedLocators);
    data.writeMcastPort(this.mcastPort);
    data.writeMcastHostAddress(this.mcastHostAddress);
    data.toData(out);
  }
  
  /**
   * Notes a problem that occurs while invoking {@link #fromData}.
   */
  private void fromDataProblem(String s) {
    if (this.fromDataProblems == null) {
      this.fromDataProblems = new StringBuffer();
    }

    this.fromDataProblems.append(s);
    this.fromDataProblems.append("\n\n");
  }

  @Override
  public void fromData(DataInput in)
    throws IOException, ClassNotFoundException {
      
    super.fromData(in);
    boolean hasDirectChannel = in.readBoolean();
    if (hasDirectChannel) {
      this.directChannel = Stub.createFromData(in);
    } else {
      this.directChannel = null;
    }
    // for erstwhile license properties
    DataSerializer.readObject(in);
    this.version = DataSerializer.readString(in);
    this.replyProcessorId = in.readInt();
    this.isMcastEnabled = in.readBoolean();
    this.isTcpDisabled = in.readBoolean();

    int serializerCount = in.readInt();
    for (int i = 0; i < serializerCount; i++) {
      String cName = DataSerializer.readNonPrimitiveClassName(in);

      int id = in.readInt(); // id
      try {
        if (cName != null) {
          // @todo verify that the id is correct
          InternalDataSerializer.register(cName, false, null, null, id);
        }
      } catch (IllegalArgumentException ex) {
        fromDataProblem(
            LocalizedStrings.StartupMessage_ILLEGALARGUMENTEXCEPTION_WHILE_REGISTERING_A_DATASERIALIZER_0
            .toLocalizedString(ex));
      }
    }

    int instantiatorCount = in.readInt();
    for (int i = 0; i < instantiatorCount; i++) {
      String instantiatorClassName = DataSerializer.readNonPrimitiveClassName(in);
      String instantiatedClassName = DataSerializer.readNonPrimitiveClassName(in);
      int id = in.readInt();

      try {
        if (instantiatorClassName != null && instantiatedClassName != null) {
          InternalInstantiator.register(instantiatorClassName, instantiatedClassName, id, false);
        }
      } catch (IllegalArgumentException ex) {
        fromDataProblem(
          LocalizedStrings.StartupMessage_ILLEGALARGUMENTEXCEPTION_WHILE_REGISTERING_AN_INSTANTIATOR_0
          .toLocalizedString(ex));
      }
    } // for
    
    this.interfaces = (Set)DataSerializer.readObject(in);
    this.distributedSystemId = in.readInt();
    this.redundancyZone = DataSerializer.readString(in);
    this.enforceUniqueZone = in.readBoolean();
    
    StartupMessageData data = new StartupMessageData(in, this.version);
    this.hostedLocators = data.readHostedLocators();
    this.mcastPort = data.readMcastPort();
    this.mcastHostAddress = data.readMcastHostAddress();
  }
  
  @Override
  public String toString() {
    return 
      LocalizedStrings.StartupMessage_STARTUPMESSAGE_DM_0_HAS_STARTED_PROCESSOR_1_WITH_DISTRIBUTED_SYSTEM_ID_2
      .toLocalizedString(new Object[]{getSender(), Integer.valueOf(replyProcessorId), this.distributedSystemId});
  }
}
