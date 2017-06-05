/*
 * Copyright 2017 Shinya Mochida
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
package org.mikeneck.kuickcheck.api

object Functions {

    val intToChar: (Int) -> Char = { 'a'.toInt().plus(it).toChar() }

    val toUpper: (Char) -> Char = { it.toUpperCase() }

    inline infix operator
    fun <A : Any, B : Any, C : Any> ((A) -> B).plus(crossinline f: (B) -> C): (A) -> C = { a: A -> a.let(this).let(f) }

    fun <A> identity(): (A) -> A = { it }

    val intToGenChar: (Int) -> Gen<Char> = { x: Int ->
        fun add26WhenMinus(n: Int) = if (n < 0) n + 26 else n
        mkGen { gen: KcGen, s: Size -> (gen.nextInt(add26WhenMinus((s.max + x) % 26) + 1) - 1).toChar() }
    }

    val charToGenString: (Char) -> Gen<String> = { c: Char ->
        mkGen { gen: KcGen, s: Size ->
            (1..gen.nextInt(s.max))
                    .map { if (it % 11 == 0) c else gen.nextInt(26).let(intToChar) }
                    .joinToString("")
        }
    }
}
