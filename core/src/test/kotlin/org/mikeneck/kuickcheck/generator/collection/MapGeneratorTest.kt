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
import org.mikeneck.kuickcheck.boolean
import org.mikeneck.kuickcheck.int
import org.mikeneck.kuickcheck.string

class MapGeneratorTest {

    // map size <= max
    @Test fun generatedMapHasSizeLessThanOrEqualToMax() {
        val generator = MapGenerator(int, string, 10)
        repeat(120) {
            assert(generator().size <= 10)
        }
    }

    // map size >= 0
    @Test fun generatedMapHasSizeMoreThanOrEqualTo0() {
        val generator = MapGenerator(int, string, 10)
        repeat(120) {
            assert(generator().size >= 0)
        }
    }

    // map (init size 0) = 0
    @Test fun generatorInitializedWithSize0AlwaysGeneratesEmptyMap() {
        val generator = MapGenerator(int, string, 0)
        repeat(120) {
            assert(generator().isEmpty())
        }
    }

    // fixed size -> same size
    @Test fun fixedSizedGeneratorGeneratesMapWithSameSize() {
        val generator = MapGenerator(int, string, 45, true)
        repeat(120) {
            assert(generator().size == 45)
        }
    }

    // change size
    @Test fun changeSize() {
        val generator = MapGenerator(int, string, 100).size(10)
        repeat(120) {
            assert(generator().size <= 10)
        }
    }

    // fix size
    @Test fun fixSize() {
        val generator = MapGenerator(int, string, 100).fixedSize(10)
        repeat(120) {
            assert(generator().size == 10)
        }
    }

    // repeat 9 times -> full size
    @Test fun repeat9TimesReturnsFullySizeGenerator() {
        val generator = MapGenerator(intGenerator(9), string, 20, true)
        assert(generator().size == 20)
    }

    // repeat 10 times -> short size
    @Test fun repeat10TimesReturnsShortSizedGenerator() {
        val generator = MapGenerator(intGenerator(10), string, 20, true)
        assert(generator().size < 20)
    }

    @Test fun booleanKey() {
        val generator = MapGenerator(boolean, string, 20, true)
        repeat(120) {
            assert(generator().size == 2)
        }
    }
}
