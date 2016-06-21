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

class ByteGeneratorTest {

    @Test(expected = IllegalArgumentException::class)
    fun maxSmallerThanMinCauseIllegalArgumentException() {
        ByteGenerator(1, 0)
    }

    @Test fun minIsSameAsMaxThenGeneratorGeneratesSameValue() {
        val generator = ByteGenerator(1, 1)
        repeat(120) {
            assert(generator() == 1.toByte())
        }
    }

    @Test fun largeBound() {
        val generator = ByteGenerator()
        repeat(120) {
            val v = generator()
            assert(v >= Byte.MIN_VALUE)
            assert(v <= Byte.MAX_VALUE)
        }
    }

    @Test(timeout = 1000)
    fun generatorGeneratesValuesInRange() {
        val generator = ByteGenerator(-8, 7)
        val map = (-8..7).map { Pair(it.toByte(), mutableListOf<Byte>()) }.toMap()
        while (true) {
            val b = generator()
            map[b]?.add(b)
            if (map.all { it.value.size > 0 }) break
        }
    }
}

class IntBasedByteGeneratorTest {

    @Test(expected = IllegalArgumentException::class)
    fun maxIsSmallerThanMinThenIllegalArgumentException() {
        IntBasedByteGenerator(1, 0)
    }

    @Test(expected = IllegalArgumentException::class)
    fun minIsSmallerThan0ThenIllegalArgumentException() {
        IntBasedByteGenerator(min = -1)
    }

    @Test(expected = IllegalArgumentException::class)
    fun maxIsLargerThan255ThenIllegalArgumentException() {
        IntBasedByteGenerator(max = 256)
    }

    @Test fun minEqualsToMaxThenGeneratorGeneratesTheSameValues() {
        val generator = IntBasedByteGenerator(12, 12)
        repeat(120) {
            assert(generator() == 12.toByte())
        }
    }

    @Test(timeout = 4000)
    fun intBasedGeneratorGeneratesAllByteRange() {
        val generator = IntBasedByteGenerator()
        val map = (-128..127).map { Pair(it.toByte(), mutableListOf<Byte>()) }.toMap()
        while (true) {
            val b = generator()
            map[b]?.add(b)
            if (map.all { it.value.size > 0 }) break
        }
    }
}
