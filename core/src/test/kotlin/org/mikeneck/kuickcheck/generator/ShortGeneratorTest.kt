/*
 * Copyright 2016 Shinya Mochida
 * 
 * Licensed under the Apache License,Version2.0(the"License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,software
 * Distributed under the License is distributed on an"AS IS"BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.mikeneck.kuickcheck.generator

import org.junit.Test

class ShortGeneratorTest {

    @Test(expected = IllegalArgumentException::class)
    fun largeMinSmallMaxCauseIllegalArgumentException() {
        ShortGenerator(1, 0)
    }

    @Test fun minEqualsToMaxGeneratorGeneratesSameValue() {
        val generator = ShortGenerator(Short.MIN_VALUE, Short.MIN_VALUE)
        repeat(120) {
            assert(generator() == Short.MIN_VALUE)
        }
    }

    @Test fun largeBound() {
        val generator = ShortGenerator()
        repeat(120) {
            val v = generator()
            assert(v >= Short.MIN_VALUE)
            assert(v <= Short.MAX_VALUE)
        }
    }

    @Test(timeout = 1000)
    fun minAndMaxAreIncluded() {
        val generator = ShortGenerator(-3, 3)
        val map = (-3..3).map { Pair(it.toShort(), mutableListOf<Short>()) }.toMap()
        while (true) {
            val v = generator()
            map[v]?.add(v)
            if (map.all { it.value.size > 0 }) break
        }
    }
}
