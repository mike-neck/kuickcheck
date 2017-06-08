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

    /**
     * generator of [Int] from 0 to 99
     */
    val int: Gen<Int> = mkGen { kcGen, _ -> kcGen.nextInt(100) }

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

object Assert {
    fun <A> assert(left: A, right: A, predicate: (A, A) -> Boolean): Unit =
            (Capture(left) to Capture(right)).assert(predicate).whenLeft { throw AssertionError(compare(it)) }

    class Capture<out A>(val item: A)

    private fun <A> Pair<Capture<A>, Capture<A>>.assert(predicate: (A, A) -> Boolean):
            Pair<Pair<Capture<A>, Capture<A>>, Boolean> =
            this.mkPair { it.separation() }.map { predicate.tuple.invoke(it) }

    private fun <A> Pair<Capture<A>, Capture<A>>.separation(): Pair<A, A> = this.first.item to this.second.item
    private inline fun <A, B> A.mkPair(f: (A) -> B): Pair<A, B> = this to f(this)
    private inline fun <A, B, C> Pair<A, B>.map(f: (B) -> C): Pair<A, C> = this.first to f(this.second)
    private val <A, B, C> ((A, B) -> C).tuple: (Pair<A, B>) -> C get() = { p -> this(p.first, p.second) }

    private inline fun <A> Pair<A, Boolean>.whenLeft(f: (A) -> Unit): Unit = if (this.second) Unit else f(this.first)
    private fun <A> compare(pair: Pair<Capture<A>, Capture<A>>): String = pair.separation()
            .let {
                """
right: ${it.second}
left : ${it.first}
"""
            }
}
