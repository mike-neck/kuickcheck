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

internal class StringGenerator(chars: String, length: Int = 50) : Generator<String> {

    val size: Generator<Int>

    val string: Generator<String>

    init {
        size = intSizeGenerator(length)
        string = if (chars.isEmpty()) throw IllegalArgumentException("Generator cannot generate string with initial empty string.")
        else RandomString(chars)
    }

    override fun generate(): String {
        return size.of { string.generate() }.joinToString()
    }
}

private fun intSizeGenerator(length: Int) = if (length <= 0) throw IllegalArgumentException("Length should be larger than 0.[input: $length]")
else if (1 < length) IntGenerator(2, length)
else OneSizedInt1Generator()

private fun <U> Generator<Int>.of(f: (Int) -> U): List<U> = 1.rangeTo(this.generate()).map(f)

internal class OneSizedInt1Generator : Generator<Int> {
    override fun generate(): Int = 1
}

internal class RandomString(chars: String) : Generator<String> {

    val entry: List<Int>

    val indices: Generator<Int>

    init {
        val set: MutableSet<Int> = mutableSetOf()
        var i: Int = 0
        while (i < chars.length) {
            val c = chars[i]
            if (Character.isSurrogate(c)) {
                i++
                val l = chars[i]
                if (Character.isSurrogatePair(c, l) == false) continue
                val cp = Character.toCodePoint(c, l)
                set.add(cp)
            } else {
                set.add(c.toInt())
            }
            i++
        }
        entry = set.toList()
        indices = if (entry.size == 1) OneSizedInt1Generator() else IntGenerator(1, entry.size)
    }

    override fun generate(): String {
        val codePoint = entry[indices.generate() - 1]
        return String(Character.toChars(codePoint))
    }
}
