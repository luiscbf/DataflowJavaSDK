/*
 * Copyright (C) 2015 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.google.cloud.dataflow.sdk.io.range;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.junit.Assert.assertThat;

import com.google.common.collect.ImmutableList;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

/**
 * A combinatorial test of {@link ByteKeyRange#estimateFractionForKey(ByteKey)}.
 */
@RunWith(Parameterized.class)
public class ByteKeyRangeEstimateFractionTest {
  private static final ByteKey[] TEST_KEYS = ByteKeyRangeTest.RANGE_TEST_KEYS;

  @Parameters(name = "{index}: i={0}, k={1}")
  public static Iterable<Object[]> data() {
    ImmutableList.Builder<Object[]> ret = ImmutableList.builder();
    for (int i = 0; i < TEST_KEYS.length; ++i) {
      for (int k = i + 1; k < TEST_KEYS.length; ++k) {
        ret.add(new Object[] {i, k});
      }
    }
    return ret.build();
  }

  @Parameter(0)
  public int i;

  @Parameter(1)
  public int k;

  @Test
  public void testEstimateFractionForKey() {
    double last = 0.0;
    ByteKeyRange range = ByteKeyRange.of(TEST_KEYS[i], TEST_KEYS[k]);
    for (int j = i; j < k; ++j) {
      ByteKey key = TEST_KEYS[j];
      if (key.isEmpty()) {
        // Cannot compute progress for unspecified key
        continue;
      }
      double fraction = range.estimateFractionForKey(key);
      assertThat(fraction, greaterThanOrEqualTo(last));
      last = fraction;
    }
  }
}
