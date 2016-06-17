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

class IntGeneratorTest {

    @Test(expected = IllegalArgumentException::class)
    fun maxIsSmallerThanMin() {
        IntGenerator(1, 0)
    }

    @Test
    fun maxEqualsToMin() {
        val generator = IntGenerator(1, 1)
        1.rangeTo(100).forEach { assert(generator.invoke() == 1) }
    }

    @Test
    fun smallRange() {
        val generator = IntGenerator(0, 20)
        1.rangeTo(120).forEach {
            val random = generator.invoke()
            assert(0 <= random && random <= 20, {"random value [$random]"})
        }
    }

    @Test
    fun largeRange() {
        val generator = IntGenerator(Int.MIN_VALUE, Int.MAX_VALUE)
        val list = 1.rangeTo(200).map { generator.invoke() }.sorted()
        assert(Int.MIN_VALUE <= list.first())
        assert(list.last() <= Int.MAX_VALUE)
    }
}
