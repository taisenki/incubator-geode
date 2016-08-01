/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gemstone.gemfire.internal.cache;

import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import static org.junit.Assert.assertEquals;

import com.gemstone.gemfire.cache.Cache;
import com.gemstone.gemfire.cache.CacheFactory;
import com.gemstone.gemfire.cache.DataPolicy;
import com.gemstone.gemfire.cache.EvictionAction;
import com.gemstone.gemfire.cache.EvictionAttributes;
import com.gemstone.gemfire.cache.PartitionAttributesFactory;
import com.gemstone.gemfire.cache.Region;
import com.gemstone.gemfire.cache.RegionFactory;
import com.gemstone.gemfire.test.junit.categories.IntegrationTest;

/**
 * Test the replace method with an entry that has overflowed to disk.
 */
@Category(IntegrationTest.class)
public class ReplaceWithOverflowJUnitTest {

  private Cache cache;

  @Before
  public void setUp() {
    Properties props = new Properties();
    props.setProperty("mcast-port", "0");
    props.setProperty("log-level", "info");
    cache = new CacheFactory(props).create();
  }
  
  @After
  public void tearDown() {
    if(cache != null && !cache.isClosed()) {
      cache.close();
    }
  }
  @Test
  public void testReplaceWithOverflow() throws InterruptedException { 
    Region region = createRegion();
    region.put("1", "1");
    region.put("2", "2");
    assertEquals(true, region.replace("1", "1", "one"));
    
  }

  private Region createRegion() {
    RegionFactory<Object, Object> rf = cache.createRegionFactory()
        .setEvictionAttributes(EvictionAttributes.createLRUEntryAttributes(1, EvictionAction.OVERFLOW_TO_DISK))
        .setPartitionAttributes(new PartitionAttributesFactory().setTotalNumBuckets(1).create())
        .setDataPolicy(DataPolicy.PARTITION);
    Region region = rf
        .create("ReplaceWithOverflowJUnitTest");
    return region;
  }
}
