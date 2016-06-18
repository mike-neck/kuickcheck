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
import org.mikeneck.kuickcheck.date
import org.mikeneck.kuickcheck.int
import java.util.*

class MutableListGeneratorTest {

    @Test fun generatorGeneratesListWithSizeLessThanOrEqualToTheSizeSpecified() {
        val generator = MutableListGenerator(int, 10)
        (1..120).forEach {
            assert(generator().size <= 10)
        }
    }

    @Test(timeout = 1000)
    fun generatorSometimesGeneratesEmptyList() {
        val generator = MutableListGenerator(int, 10)
        while (true) {
            if (generator().size == 0) break
        }
    }

    @Test fun generatedListCanBeModified() {
        val generator = MutableListGenerator(int)
        val list = generator()
        val originalSize = list.size
        list.add(10)
        assert(list.size == originalSize + 1)
    }

    @Test fun fixedGeneratorGeneratesSameSizeList() {
        val generator = MutableListGenerator(int, 20, true)
        (1..120).forEach {
            assert(generator().size == 20)
        }
    }

    @Test(timeout = 1000)
    fun changingSize() {
        val generator = MutableListGenerator(int).size(10)
        val map = (0..10).map { Pair(it, mutableListOf<Int>()) }.toMap()
        while (true) {
            val list = generator()
            map[list.size]?.add(list.size)
            if (map.all { it.value.size > 0 }) break
        }
    }

    @Test fun changeFixed() {
        val generator = MutableListGenerator(int).fixedSize(25)
        (1..120).forEach {
            assert(generator().size == 25)
        }
    }

    @Test fun generatedListContainsTypeSpecified() {
        val generator = MutableListGenerator(date, 40, true)
        assert(generator().all { it is Date })
    }
}
