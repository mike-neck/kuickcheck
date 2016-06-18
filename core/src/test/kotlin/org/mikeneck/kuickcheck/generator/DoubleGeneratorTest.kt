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
import org.mikeneck.kuickcheck.negativeDouble
import org.mikeneck.kuickcheck.negativeDoubleTo0
import org.mikeneck.kuickcheck.positiveDouble
import org.mikeneck.kuickcheck.positiveDoubleFrom0

class DoubleGeneratorTest {

    @Test(expected = IllegalArgumentException::class)
    fun maxIsSmallerThanMinThenIllegalArgumentException() {
        DoubleGenerator(10.toDouble(), 0.toDouble())
    }

    @Test fun generatorGeneratesValueLessThanOrEqualToMax() {
        val generator = DoubleGenerator(0.0, Double.MIN_VALUE)
        (0..120).forEach {
            val value = generator.invoke()
            assert(value <= Double.MIN_VALUE, { "$it) value[$value] <= 1.0" })
        }
    }

    @Test fun generatorGeneratesValueIncludingMin() {
        val generator = DoubleGenerator(0.0, Double.MIN_VALUE)
        (0..120).forEach {
            val value = generator.invoke()
            assert(value >= 0.0, { "$it) value[$value] >= 0.0" })
        }
    }

    @Test fun positiveDoubleGeneratesValueMoreThan0() {
        (0..120).forEach {
            val value = positiveDouble()
            assert(value > 0.0, { "$it) positiveDouble[$value] > 0.0" })
        }
    }

    @Test fun negativeDoubleGeneratesValueLessThan0() {
        (0..120).forEach {
            val value = negativeDouble()
            assert(value < 0.0, { "$it) negativeDouble[$value] < 0.0" })
        }
    }

    @Test fun `positiveWith0 generates larger than negative`() {
        (0..120).forEach {
            val pos = positiveDoubleFrom0()
            val neg = negativeDouble()
            assert(pos > neg)
        }
    }

    @Test fun `positive generates larger than negativeWith0`() {
        (0..120).forEach {
            val pos = positiveDouble()
            val neg = negativeDoubleTo0()
            assert(pos > neg)
        }
    }
}
