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
import org.mikeneck.kuickcheck.alwaysTrue
import org.mikeneck.kuickcheck.int

class NullableGeneratorTest {

    @Test(timeout = 1000)
    fun nullableGeneratorSometimesGeneratesNull() {
        val generator = int(0, 3).nullable
        while (true) {
            generator() ?: break
        }
    }

    @Test fun nullablePercentage() {
        val generator = alwaysTrue.nullable
        val map = (0..1).map { Pair(it < 1, mutableListOf<Int>()) }.toMap()
        repeat(120) {
            val v = generator()
            if (v == null) map[false]?.add(it) else map[true]?.add(it)
        }
        println(map.map { Pair(it.key, it.value.size) }.toMap())
    }
}
