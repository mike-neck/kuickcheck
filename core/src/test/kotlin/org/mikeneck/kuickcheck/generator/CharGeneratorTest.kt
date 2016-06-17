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

import org.mikeneck.kuickcheck.generator.CharGenerator.Companion.charGenerator

class CharGeneratorTest {

    @Test fun charGeneratorConstructedWithCharArrayGeneratesOnlyCharsInArray() {
        val chars = charArrayOf('a', 'A', '0')
        val generator = CharGenerator(chars)
        (1..120).forEach {
            assert(generator.invoke() in chars)
        }
    }

    @Test fun charGeneratorConstructedByCharArrayViaCompanionGeneratesOnlyCharsInArray() {
        val chars = charArrayOf('あ', 'プ', '堀')
        val generator = charGenerator('あ', 'プ', '堀')
        (1..120).forEach {
            assert(generator.invoke() in chars)
        }
    }

    @Test(expected = IllegalArgumentException::class)
    fun theSizeOfCharArrayIs0() {
        CharGenerator(charArrayOf())
    }

    @Test(expected = IllegalArgumentException::class)
    fun illegalOrderOfParameters() {
        CharGenerator('1', '0')
    }

    @Test(expected = IllegalArgumentException::class)
    fun illegalCharParamMin() {
        CharGenerator((-1).toChar(), 2.toChar())
    }

    @Test(expected = IllegalArgumentException::class)
    fun illegalCharParamMax() {
        CharGenerator(1.toChar(), 65536.toChar())
    }

    @Test fun charGeneratorConstructedWithRangeReturnsTheCharInTheRange() {
        val generator = CharGenerator('0', '9')
        val chars = "0123456789".toCharArray()
        (1..120).forEach {
            assert(generator.invoke() in chars)
        }
    }

    @Test fun charGeneratorConstructedByString() {
        val str = "0123456789"
        val generator = CharGenerator(str)
        val chars = str.toCharArray()
        (1..120).forEach {
            assert(generator.invoke() in chars)
        }
    }
}
