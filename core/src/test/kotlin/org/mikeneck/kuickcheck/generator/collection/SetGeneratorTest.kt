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
import org.mikeneck.kuickcheck.*

class SetGeneratorTest {

    // set size <= max
    @Test fun sizeIsSmallerThanOrEqualToTheSizeSpecified() {
        val generator = SetGenerator(int, 10)
        (1..120).forEach {
            assert(generator().size <= 10)
        }
    }

    // set size >= 0
    @Test fun sizeIsLargerThanOrEqualTo0() {
        val generator = SetGenerator(int, 10)
        (1..120).forEach {
            assert(generator().size >= 0)
        }
    }

    // set size(init 0) == 0
    @Test fun generatorInitializedWithSize0GeneratesEmptySet() {
        val generator = SetGenerator(int, 0)
        (1..120).forEach {
            assert(generator().size == 0)
        }
    }

    // fixed size -> same size
    @Test fun fixedSizeGeneratorAlwaysGeneratesTheSameSizeSet() {
        val generator = SetGenerator(int, 14, true)
        (1..120).forEach {
            assert(generator().size == 14)
        }
    }

    // change size
    @Test fun sizeChange() {
        val generator = SetGenerator(int).size(7)
        (1..120).forEach {
            assert(generator().size in (0..7))
        }
    }

    // fix size
    @Test fun fixedSize() {
        val generator = SetGenerator(int).fixedSize(5)
        (1..120).forEach {
            assert(generator().size == 5)
        }
    }

    // generator generates the same element 9 times then another element -> specified size
    @Test fun repeat9Times() {
        val generator = SetGenerator(intGenerator(9), 15, true)
        val size = generator().size
        assert(size == 15, { "$size" })
    }

    // generator generates the same element 10 times -> size will be small
    @Test fun repeat10Times() {
        val generator = SetGenerator(intGenerator(10), 15, true)
        val size = generator().size
        assert(size < 15, { "$size" })
    }

    @Test fun alwaysTrueGenerator() {
        val generator = SetGenerator(elementGenerator = alwaysTrue, sizeFixed = true)
        repeat(10) {
            assert(generator().size == 1)
        }
    }

    @Test fun booleanGenerator() {
        val generator = SetGenerator(elementGenerator = boolean, sizeFixed = true)
        repeat(10) {
            assert(generator().size == 2)
        }
    }

    @Test fun charGeneratorWithRangeNarrowerThanSetGenerator() {
        val generator = SetGenerator(char("abcdefghij"), 20, true)
        repeat(10) {
            assert(generator().size < generator.size)
        }
    }
}

fun intGenerator(repeat: Int): Generator<Int> = object : Generator<Int> {

    val itor: Iterator<Int>

    init {
        val list = listOf(1, 2, 3, 4) + 1.rangeTo(repeat).map { 4 } + (5..50)
        this.itor = list.iterator()
    }

    override fun invoke(): Int = itor.next()
}

class MutableSetGeneratorTest {

    // set size <= max
    @Test fun generatedSetSizeIsSmallerThanOrEqualToMax() {
        val generator = MutableSetGenerator(int, 35)
        repeat(120) {
            assert(generator().size <= 35)
        }
    }

    // set size >= 0
    @Test(timeout = 1000) fun generatorSometimesGeneratesEmptySet() {
        val generator = MutableSetGenerator(int, 10)
        while (true) {
            if (generator().size == 0) break
        }
    }

    // set size(init 0) == 0
    @Test fun generatorInitializedWithSize0GeneratesEmptySet() {
        val generator = MutableSetGenerator(int, 0)
        repeat(120) {
            assert(generator().size == 0)
        }
    }

    // fixed size -> same size
    @Test fun fixedSizedGeneratorGeneratesTheSameSizeSet() {
        val generator = MutableSetGenerator(int, 43, true)
        repeat(120) {
            assert(generator().size == 43)
        }
    }

    // change size
    @Test fun changeSize() {
        val generator = MutableSetGenerator(int, 33).size(10)
        repeat(120) {
            assert(generator().size in (0..10))
        }
    }

    // fix size
    @Test fun fixSize() {
        val generator = MutableSetGenerator(int, 20).fixedSize(10)
        repeat(120) {
            assert(generator().size == 10)
        }
    }

    // generator generates the same element 9 times then another element -> specified size
    @Test fun repeat9Times() {
        val generator = MutableSetGenerator(intGenerator(9), 15, true)
        assert(generator().size == 15)
    }

    // generator generates the same element 10 times -> size will be small
    @Test fun repeat10Times() {
        val generator = MutableSetGenerator(intGenerator(10), 15, true)
        val size = generator().size
        assert(size < 15, { "$size" })
    }

    @Test fun alwaysFalseGenerator() {
        val generator = MutableSetGenerator(elementGenerator = alwaysFalse, sizeFixed = true)
        repeat(120) {
            assert(generator().size == 1)
        }
    }

    @Test fun generatedSetCanModified() {
        val generator = MutableSetGenerator(elementGenerator = char("abcdefghij"), sizeFixed = true)
        val set = generator()
        val size = set.size
        set.add('u')
        assert(set.size == size + 1)
    }
}
