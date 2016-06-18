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
package org.mikeneck.kuickcheck.generator.collection

import org.junit.Test
import org.mikeneck.kuickcheck.int
import org.mikeneck.kuickcheck.list
import org.mikeneck.kuickcheck.string

class ListGeneratorTest {

    @Test
    fun size0ListGeneratorGeneratesEmptyList() {
        val generator = ListGenerator(int, 0)
        (1..120).forEach {
            assert(generator().isEmpty())
        }
    }

    @Test fun size1ListGeneratorAlwaysGeneratesListWithSize1() {
        val generator = ListGenerator(int, 1)
        (1..120).forEach {
            assert(generator().size in listOf(0, 1))
        }
    }

    @Test fun generatorGeneratesListWithSizeMoreThan0() {
        val generator = ListGenerator(int, 10)
        (1..120).forEach {
            assert(generator().size >= 0)
        }
    }

    @Test fun generatorGeneratesListWithSizeLessThanMaxPlus1() {
        val generator = ListGenerator(int, 10)
        (1..120).forEach {
            assert(generator().size <= 10)
        }
    }

    @Test(timeout = 1000)
    fun generatorGeneratesListWithSizeLessThanOrEqualToTheSizeSpecified() {
        val generator = ListGenerator(int, 10)
        val map = (0..10).map { Pair(it, mutableListOf<Int>()) }.toMap()
        while (true) {
            val value = generator()
            map[value.size]?.add(value.size)
            if (map.all { it.value.size > 0 }) break
        }
    }

    @Test fun generatedListContainsSpecifiedType() {
        val generator = ListGenerator(string)
        assert(generator().all { it is String })
    }

    @Test fun fixedListGeneratorGeneratesTheSameSizeList() {
        val generator = ListGenerator(int, fixedSize = true)
        (1..120).forEach {
            assert(generator().size == 20)
        }
    }

    @Test fun changingSize() {
        val generator = list(int).size(10)
        (1..120).forEach {
            assert(generator().size in (0..10))
        }
    }

    @Test fun changeToFixedSize() {
        val generator = list(int).fixedSize(10)
        (1..120).forEach {
            assert(generator().size == 10)
        }
    }
}
