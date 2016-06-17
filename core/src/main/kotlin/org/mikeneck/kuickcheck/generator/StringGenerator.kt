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
import org.mikeneck.kuickcheck.generator.internal.*

internal class StringGenerator(chars: String, length: Int = 50) : Generator<String> {

    val size: SizeGenerator

    val string: Generator<SizedWrapper<String>>

    init {
        size = intSizeGenerator(length)
        string = if (chars.isEmpty()) throw IllegalArgumentException("Generator cannot generate string with initial empty string.")
        else RandomString(chars)
    }

    override fun invoke(): String {
        return upto(size) { string.invoke() }.joinToString("")
    }
}

private fun intSizeGenerator(length: Int) = if (length <= 0) throw IllegalArgumentException("Length should be larger than 0.[input: $length]")
else if (1 < length) IntGenerator(2, length)
else OneSizedInt1Generator()

private fun <T : SizedWrapper<U>, U> upto(size: SizeGenerator, f: () -> T): List<T> {
    val s = size.invoke()
    val l = mutableListOf<T>()
    var c = 0
    while (c < s) {
        if (c + 1 == s) {
            while (true) {
                val g = f.invoke()
                if (g.size == 1) {
                    l.add(g)
                    return l.toList()
                }
            }
        }
        val g = f.invoke()
        c += g.size
        l.add(g)
    }
    return l.toList()
}

internal class RandomString(chars: String) : Generator<SizedWrapper<String>> {

    val entry: List<IntWrapper>

    val indices: Generator<Int>

    init {
        val set: MutableSet<IntWrapper> = mutableSetOf()
        var i: Int = 0
        while (i < chars.length) {
            val c = chars[i]
            if (Character.isSurrogate(c)) {
                i++
                val l = chars[i]
                if (Character.isSurrogatePair(c, l) == false) continue
                val cp = Character.toCodePoint(c, l)
                set.add(IntWrapper(cp, 2))
            } else {
                set.add(IntWrapper(c.toInt(), 1))
            }
            i++
        }
        entry = set.toList()
        indices = if (entry.size == 1) OneSizedInt1Generator() else IntGenerator(1, entry.size)
    }

    override fun invoke(): SizedWrapper<String> {
        val codePoint = entry[indices.invoke() - 1]
        return codePoint.string()
    }
}

internal class AllStringGenerator(length: Int = 50) : Generator<String> {

    val size: SizeGenerator =
            if (length <= 0) throw IllegalArgumentException("Length should be larger than 0.[input: $length]")
            else if (length == 1) OneSizedInt1Generator()
            else IntGenerator(1, length)

    override fun invoke(): String {
        return upto(size) { OneSizedAllString.invoke() }.joinToString("")
    }
}

fun Char.intGeneratorTo(endInclusive: Char): Generator<Int> = IntGenerator(this.toInt(), endInclusive.toInt())

internal object OneSizedAllString : Generator<SizedWrapper<String>> {

    val allChars: Generator<Int> = IntGenerator(0, Character.MAX_VALUE.toInt())

    val highSurrogates: Generator<Int> = Char.MIN_HIGH_SURROGATE.intGeneratorTo(Char.MAX_HIGH_SURROGATE)

    val lowSurrogates: Generator<Int> = Char.MIN_LOW_SURROGATE.intGeneratorTo(Char.MAX_LOW_SURROGATE)

    override fun invoke(): SizedWrapper<String> {
        val c = allChars.invoke().toChar()
        return if (Character.isSurrogate(c)) createSurrogateString(c)
        else NormalString(c.toString())
    }

    fun createSurrogateString(c: Char): SizedWrapper<String> {
        return if (Character.isHighSurrogate(c)) stringWithHighSurrogate(c)
        else stringWithLowSurrogate(c)
    }

    fun stringWithHighSurrogate(high: Char): SurrogateString {
        while (true) {
            val l = lowSurrogates.invoke().toChar()
            if (Character.isSurrogatePair(high, l)) {
                val cp = Character.toCodePoint(high, l)
                return SurrogateString(String(Character.toChars(cp)))
            }
        }
    }

    fun stringWithLowSurrogate(low: Char): SurrogateString {
        while (true) {
            val h = highSurrogates.invoke().toChar()
            if (Character.isSurrogatePair(h, low)) {
                val cp = Character.toCodePoint(h, low)
                return SurrogateString(String(Character.toChars(cp)))
            }
        }
    }
}
