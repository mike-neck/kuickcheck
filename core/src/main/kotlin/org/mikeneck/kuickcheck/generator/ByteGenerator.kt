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

import org.mikeneck.kuickcheck.Generator
import org.mikeneck.kuickcheck.random.RandomSource

internal class ByteGenerator(val min: Byte = Byte.MIN_VALUE, val max: Byte = Byte.MAX_VALUE) : Generator<Byte> {

    init {
        if (max < min) throw IllegalArgumentException("Max should be larger than min.[max: $max, min: $min]")
    }

    override fun invoke(): Byte = RandomSource.nextByte(min, max)
}

internal class IntBasedByteGenerator(val min: Int = MIN, val max: Int = MAX) : Generator<Byte> {

    init {
        if (max < min) throw IllegalArgumentException("Max should be larger than min. [max: $max, min: $min]")
        if (min < MIN) throw IllegalArgumentException("Min should be larger than 0. [min: $min]")
        if (max > MAX) throw IllegalArgumentException("Max should be smaller than 128. [max: $max]")
    }

    override fun invoke(): Byte = RandomSource.nextInt(min, max).toByte()

    companion object {
        val MIN: Int = 0
        val MAX: Int = 255
    }
}
