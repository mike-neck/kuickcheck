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
import org.mikeneck.kuickcheck.negativeFloat
import org.mikeneck.kuickcheck.positiveFloat

class FloatGeneratorTest {

    @Test(expected = IllegalArgumentException::class)
    fun ifMaxIsSmallerThanMinIllegalArgumentExceptionWillBeThrown() {
        FloatGenerator(1.125f, 1.124f)
    }

    @Test
    fun generatorGeneratesValuesSmallerThanOrEqualToMax() {
        val generator = FloatGenerator(0.0f, Float.MIN_VALUE)
        (1..120).forEach {
            assert(generator.invoke() <= Float.MIN_VALUE)
        }
    }

    @Test
    fun generatorGeneratesValuesLargerThanOrEqualToMin() {
        val generator = FloatGenerator(0.0f, Float.MIN_VALUE)
        (1..120).forEach {
            assert(generator.invoke() >= 0.0f)
        }
    }

    @Test fun negativeFloatGeneratesNegativeValues() {
        (1..120).forEach {
            val value = negativeFloat.invoke()
            assert(value < 0)
        }
    }

    @Test fun positiveFloatAlwaysLargerThanNegativeFloat() {
        (1..120).forEach {
            val negative = negativeFloat.invoke()
            val positive = positiveFloat.invoke()
            assert(negative < positive)
        }
    }
}
