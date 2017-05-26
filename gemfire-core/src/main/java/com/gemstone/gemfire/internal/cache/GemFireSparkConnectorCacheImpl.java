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
package com.gemstone.gemfire.internal.cache;

import java.util.Map;

import com.gemstone.gemfire.cache.AttributesFactory;
import com.gemstone.gemfire.cache.DataPolicy;
import com.gemstone.gemfire.cache.client.ClientRegionShortcut;
import com.gemstone.gemfire.cache.client.Pool;
import com.gemstone.gemfire.cache.client.PoolFactory;
import com.gemstone.gemfire.cache.query.QueryService;
import com.gemstone.gemfire.distributed.DistributedSystem;

/**
 * Created by ashahid on 3/30/17.
 */
public class GemFireSparkConnectorCacheImpl extends GemFireCacheImpl {

  public static final String gfeGridPropPrefix = "gemfire-grid";
  private Map<String, String> gfeGridMappings = null ;
  public GemFireSparkConnectorCacheImpl(PoolFactory pf, Map<String, String> gfeGridMappings,
      DistributedSystem system, CacheConfig cacheConfig) {
    super(false, pf, system, cacheConfig);
    this.gfeGridMappings = gfeGridMappings;
  }

  public static GemFireCacheImpl create(PoolFactory pf, Map<String, String> gfeGridMappings, DistributedSystem system,
      CacheConfig cacheConfig) {
    return new GemFireSparkConnectorCacheImpl(pf, gfeGridMappings, system, cacheConfig).init();
  }

  @Override
  protected GemFireCacheImpl init() {
    PoolFactory temp = this.clientpf;
    super.init();
    //re assign the clientPf which would otherwise be nullified by the super call
    this.clientpf = temp;
    if (temp != null) {
      this.determineDefaultPool();
    }
    AttributesFactory af = new AttributesFactory();
    af.setDataPolicy(DataPolicy.EMPTY);
    UserSpecifiedRegionAttributes ra = (UserSpecifiedRegionAttributes)af.create();
    ra.requiresPoolName = true;
    this.setRegionAttributes(ClientRegionShortcut.PROXY.toString(), ra);
    this.clientpf = null;
    return this;
  }

  @Override
  protected void checkValidityForPool() {
  }

  public QueryService getRemoteGemFireQueryService() {
    Pool p = getDefaultPool();
    if (p == null) {
      throw new IllegalStateException("Client cache does not have a default pool. " +
          "Use getQueryService(String poolName) instead.");
    } else {
      return p.getQueryService();
    }
  }

}
