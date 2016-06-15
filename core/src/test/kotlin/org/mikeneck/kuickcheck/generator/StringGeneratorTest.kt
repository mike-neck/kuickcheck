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
import org.mikeneck.kuickcheck.generator.internal.NormalString

class StringGeneratorTest {

    @Test(expected = IllegalArgumentException::class)
    fun emptyStringCauseIllegalArgumentException() {
        StringGenerator("")
    }

    @Test(expected = IllegalArgumentException::class)
    fun negativeLengthCauseIllegalArgumentException() {
        StringGenerator("a", -1)
    }

    @Test(expected = IllegalArgumentException::class)
    fun length0CauseIllegalArgumentException() {
        StringGenerator("0", 0)
    }

    @Test fun length1GeneratorGeneratesStringWithSize1() {
        val g = StringGenerator("2", 1)
        (1..120).forEach {
            assert(g.generate().length == 1)
        }
    }

    @Test(timeout = 1000)
    fun generatorGeneratesStringInitiallySpecified() {
        val str = "あa𥙿｀*“æ«‘"
        val chars = str.toCharArray().map { Pair(it, mutableListOf<Char>()) }.toMap()
        val generator = StringGenerator(str, 20)
        while (true) {
            val s = generator.generate()
            s.toCharArray().forEach {
                chars[it]?.add(it)
            }
            if (chars.all { it.value.size > 0 }) break
        }
    }
}

class AllStringGeneratorTest {

    @Test(expected = IllegalArgumentException::class)
    fun length0CauseIllegalArgumentException() {
        AllStringGenerator(0)
    }

    @Test(expected = IllegalArgumentException::class)
    fun negativeLengthCauseIllegalArgumentException() {
        AllStringGenerator(-1)
    }

    @Test(timeout = 1000)
    fun generatorGeneratesStringWithinTheLengthSpecified() {
        val generator = AllStringGenerator(10)
        (1..120).forEach {
            val s = generator.generate()
            println(s)
            assert(s.length <= 10, { "[$s] is longer than 10[$it/120]" })
            assert(s.length > 0)
        }
    }
}

class StringWrapperTest {

    @Test fun theSizeOfNormalStringIs1() {
        val ns = NormalString("あ")
        assert(ns.size == 1)
    }
}
