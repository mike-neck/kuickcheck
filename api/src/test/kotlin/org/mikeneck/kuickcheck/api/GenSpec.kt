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

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.lifecycle.LifecycleAware
import org.mikeneck.kuickcheck.api.GenSpec.gen
import org.mikeneck.kuickcheck.api.GenSpec.identity
import org.mikeneck.kuickcheck.api.GenSpec.intToChar
import org.mikeneck.kuickcheck.api.GenSpec.plus
import org.mikeneck.kuickcheck.api.GenSpec.toUpper

object GenSpec : Spek({

    val int: LifecycleAware<Gen<Int>> = memoized {
        mkGen { gen: KcGen -> mkSized { s: Size -> gen.nextInt(s.max % 26 + 1) - 1 } }
    }

    group("Checking Gen's functor laws") {

        it("satisfies associative law: fmap (g . f) gen = fmap g $ fmap f gen") {
            val oneByOne = int().map(intToChar).map(toUpper)
            val composition = int().map(intToChar + toUpper)

            (10L..1000L step 10L).forEach {
                val first = gen(it)
                val second = gen(it)
                val size = Size(it.toInt())
                assert(oneByOne.generate(first)(size) == composition.generate(second)(size))
            }
        }

        it("satisfies identity law: fmap id gen = gen") {
            val mapped = int().map(identity())
            val original = int()

            (10L..1000L step 10L).forEach {
                val first = gen(it)
                val second = gen(it)
                val size = Size(it.toInt())
                assert(mapped.generate(first)(size) == original.generate(second)(size))
            }
        }
    }
}) {
    val intToChar: (Int) -> Char = { 'a'.toInt().plus(it).toChar() }
    val toUpper: (Char) -> Char = { it.toUpperCase() }

    fun <A> identity(): (A) -> A = { it }

    inline infix operator
    fun <A : Any, B : Any, C : Any> ((A) -> B).plus(crossinline f: (B) -> C): (A) -> C = { a: A -> a.let(this).let(f) }

    fun gen(seed: Long): KcGen = JavaUtilRandom(seed)
}
