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
package com.gemstone.gemfire.management.internal.cli.functions;

import com.gemstone.gemfire.cache.Cache;
import com.gemstone.gemfire.cache.execute.FunctionAdapter;
import com.gemstone.gemfire.cache.execute.FunctionContext;
import com.gemstone.gemfire.internal.InternalEntity;
import com.gemstone.gemfire.internal.cache.tier.sockets.CacheClientNotifier;
import com.gemstone.gemfire.internal.cache.tier.sockets.CacheClientProxy;
import com.gemstone.gemfire.internal.cache.tier.sockets.ClientProxyMembershipID;
import com.gemstone.gemfire.management.internal.cli.CliUtil;
import com.gemstone.gemfire.management.internal.cli.domain.MemberResult;
import com.gemstone.gemfire.management.internal.cli.i18n.CliStrings;

/***
 * Function to close a durable cq
 * @author bansods
 *
 */
public class CloseDurableCqFunction extends FunctionAdapter implements
InternalEntity {

  private static final long serialVersionUID = 1L;

  @Override
  public void execute(FunctionContext context) {

    final Cache cache = CliUtil.getCacheIfExists();
    final String memberNameOrId = CliUtil.getMemberNameOrId(cache.getDistributedSystem().getDistributedMember());
    CacheClientNotifier cacheClientNotifier = CacheClientNotifier.getInstance();
    String [] args = (String []) context.getArguments();
    String durableClientId = args[0];
    String cqName = args[1];

    MemberResult memberResult = new MemberResult(memberNameOrId);
    try {
      if (cacheClientNotifier != null) {
        CacheClientProxy cacheClientProxy = cacheClientNotifier.getClientProxy(durableClientId);
        if (cacheClientProxy != null) {
          if (cacheClientNotifier.closeClientCq(durableClientId, cqName)) {
            memberResult.setSuccessMessage(CliStrings.format(CliStrings.CLOSE_DURABLE_CQS__SUCCESS, cqName, durableClientId));
          } else {
            memberResult.setErrorMessage(CliStrings.format(CliStrings.CLOSE_DURABLE_CQS__UNABLE__TO__CLOSE__CQ, cqName, durableClientId));
          }

        } else {
          memberResult.setErrorMessage(CliStrings.format(CliStrings.NO_CLIENT_FOUND_WITH_CLIENT_ID, durableClientId));
        }
      } else {
        memberResult.setErrorMessage(CliStrings.NO_CLIENT_FOUND);
      }
    } catch (Exception e) {
      memberResult.setExceptionMessage(e.getMessage());
    } finally {
      context.getResultSender().lastResult(memberResult);
    }
  }

  @Override
  public String getId() {
    return CloseDurableCqFunction.class.getName();
  }

}
