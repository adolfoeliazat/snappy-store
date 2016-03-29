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

import java.util.*;
import java.io.*;
import java.net.UnknownHostException;

import com.gemstone.gemfire.distributed.internal.membership.InternalDistributedMember;
import com.gemstone.gemfire.internal.admin.remote.DistributionLocatorId;
import com.gemstone.gemfire.internal.admin.remote.RemoteTransportConfig;
import com.gemstone.gemfire.internal.i18n.LocalizedStrings;

public class StartupOperation {
  DistributionManager dm;
  RemoteTransportConfig transport;
  Set newlyDeparted;
  
  StartupOperation(DistributionManager dm, RemoteTransportConfig transport) {
    this.dm = dm;
    this.transport = transport;
  }
  
  /** send the startup message and wait for responses.  If timeout is zero,
      this waits forever for all responses to come back.  This method may
      throw jgroups and other io exceptions since it interacts with the
      distribution manager at a low level to send the startup messages.  It
      does this to ensure it knows which recipients didn't receive the message.
      @return whether all recipients could be contacted.  The failure set can be fetched with getFailureSet??
    */
  boolean sendStartupMessage(Set recipients, long timeout, Set interfaces, 
      String redundancyZone, boolean enforceUniqueZone)
            throws InterruptedException, ReplyException,
              java.net.UnknownHostException, IOException
  {
    if (Thread.interrupted()) throw new InterruptedException();
    StartupMessageReplyProcessor proc = new StartupMessageReplyProcessor(dm, recipients);

    StartupMessage msg = new StartupMessage(InternalLocator.getLocatorStrings());
    // to fix bug 46274 no longer multicast the startup message.
    msg.setInterfaces(interfaces);
    msg.setDistributedSystemId(dm.getConfig().getDistributedSystemId());
    msg.setRedundancyZone(redundancyZone);
    msg.setEnforceUniqueZone(enforceUniqueZone);
    msg.setMcastEnabled(transport.isMcastEnabled());
    msg.setMcastPort(dm.getSystem().getOriginalConfig().getMcastPort());
    msg.setMcastHostAddress(dm.getSystem().getOriginalConfig().getMcastAddress());
    msg.setTcpDisabled(transport.isTcpDisabled());
    msg.setRecipients(recipients);
    msg.setReplyProcessorId(proc.getProcessorId());

    this.newlyDeparted = dm.sendOutgoing(msg);  // set of departed jgroups ids
    if (this.newlyDeparted != null && !this.newlyDeparted.isEmpty()) {
      // tell the reply processor not to wait for the recipients that didn't
      // get the message
//      Vector viewMembers = dm.getViewMembers();
      for (Iterator it=this.newlyDeparted.iterator(); it.hasNext(); ) {
        InternalDistributedMember id = (InternalDistributedMember)it.next();
        this.dm.handleManagerDeparture(id, false, LocalizedStrings.StartupOperation_LEFT_THE_MEMBERSHIP_VIEW.toLocalizedString());
        proc.memberDeparted(id, true);
//        if (viewMembers.contains(id)) {
//          dm.getLogger().warning("Peer " + id
//                                 + " left the view before we could send a startup message.");
//        }
      }
    }
    
    if (proc.stillWaiting() && dm.getLoggerI18n().fineEnabled()) {
      dm.getLoggerI18n().fine("Waiting " + timeout + " milliseconds to receive startup responses");
    }
    boolean timedOut = true;
    Set unresponsive = null;
    try {
      timedOut = !proc.waitForReplies(timeout);
    }
    finally {
      if (timedOut) {
        unresponsive = new HashSet();
        proc.collectUnresponsiveMembers(unresponsive);
        if (!unresponsive.isEmpty()) {
          for (Iterator it=unresponsive.iterator(); it.hasNext(); ) {
            InternalDistributedMember um = (InternalDistributedMember)it.next();
            if (!dm.getViewMembers().contains(um)) {
              // Member slipped away and we didn't notice.
              it.remove();
              dm.handleManagerDeparture(um, true, LocalizedStrings.StartupOperation_DISAPPEARED_DURING_STARTUP_HANDSHAKE.toLocalizedString());
            }
            else
            if (dm.isCurrentMember(um)) {
              // he must have connected back to us and now we just
              // need to get his startup response
              dm.getLoggerI18n().warning(
                  LocalizedStrings.StartupOperation_MEMBERSHIP_RECEIVED_CONNECTION_FROM_0_BUT_RECEIVED_NO_STARTUP_RESPONSE_AFTER_1_MS,
                  new Object[] {um, Long.valueOf(timeout)});
            } 
          } // for

          // Tell the dm who we expect to be waiting for...
          this.dm.setUnfinishedStartups(unresponsive);
          
          // Re-examine list now that we have elided the startup problems....
          if (!unresponsive.isEmpty()) {
            dm.getLoggerI18n().warning(
                LocalizedStrings.StartupOperation_MEMBERSHIP_STARTUP_TIMED_OUT_AFTER_WAITING_0_MILLISECONDS_FOR_RESPONSES_FROM_1,
                new Object[] {Long.valueOf(timeout), unresponsive});
          }
        } // !isEmpty
      } // timedOut
    } // finally
    
    boolean problems;
    problems = this.newlyDeparted != null && this.newlyDeparted.size() > 0;
//    problems = problems || 
//        (unresponsive != null && unresponsive.size() > 0);
    return !problems;
  }
}
