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
import java.math.BigInteger

class IntGenerator(val min: Int, val max: Int): Generator<Int> {

    init {
        if (max < min) throw IllegalArgumentException("Max should be larger than min.[max: $max, min: $min]")
    }

    override fun generate(): Int {
        val generator = BigIntegerGenerator(BigInteger.valueOf(min.toLong()), BigInteger.valueOf(max.toLong()))
        return generator.generate().toInt()
    }
}
