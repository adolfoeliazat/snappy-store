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
package com.gemstone.gemfire.internal.cache.persistence.query.mock;

import java.util.concurrent.atomic.AtomicLong;

import com.gemstone.gemfire.internal.cache.CachedDeserializable;
import com.gemstone.gemfire.internal.CloseableIterator;
import com.gemstone.gemfire.internal.cache.persistence.query.IdentityExtractor;
import com.gemstone.gemfire.internal.cache.persistence.query.ResultBag;
import com.gemstone.gemfire.internal.cache.persistence.query.SortKeyExtractor;

/**
 * Mock sorted bag implementation. Uses the mock index map
 * internally, for convenience.
 * @author dsmith
 *
 */
public class SortedResultBagImpl implements ResultBag {
  private final IndexMapImpl map;
  private AtomicLong counter = new AtomicLong();
  private SortKeyExtractor extractor;
  private boolean reverse;
  
  public SortedResultBagImpl(SortKeyExtractor extractor, boolean reverse) {
    this.extractor = extractor == null ? new IdentityExtractor() : extractor;
    map = new IndexMapImpl();
    this.reverse =reverse;
  }

  @Override
  public void add(Object e) {
    map.put(extractor.getSortKey(e), counter.incrementAndGet(), e);
    
  }

  @Override
  public CloseableIterator<CachedDeserializable> iterator() {
    if(reverse) {
      return map.descendingValueIterator();
    } else {
      return map.valueIterator();
    }
  }

  @Override
  public void close() {
    map.destroy();
  }
}
