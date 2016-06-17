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

internal class CharGenerator(val chars: CharArray) : Generator<Char> {

    init {
        if (chars.size == 0) throw IllegalArgumentException("Generator cannot generate any char because there is no characters specified.")
    }

    constructor(min: Char, max: Char) : this(
            if (min > max) throw IllegalArgumentException("Min[$min] is larger than max[$max].")
            else if (min < Character.MIN_VALUE) throw IllegalArgumentException("Min[$min] is smaller than 0")
            else if (Character.MAX_VALUE < max) throw IllegalArgumentException("Max[$max] is larger than ${Character.MAX_VALUE.toInt()}")
            else if (min == max) charArrayOf(min)
            else min.rangeTo(max).toList().toCharArray()
    )

    constructor(entry: String) : this(entry.toCharArray().sortedArray())

    override fun invoke(): Char {
        val index = IntGenerator(0, chars.size - 1).invoke()
        return chars[index]
    }

    infix operator fun plus(o: CharGenerator): CharGenerator {
        val chars = this.chars + o.chars
        return CharGenerator(chars.toSet().toCharArray())
    }

    companion object {
        fun charGenerator(vararg chars: Char): CharGenerator = CharGenerator(chars)
    }
}
