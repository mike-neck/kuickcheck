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
import org.mikeneck.kuickcheck.api.Assert.assert
import org.mikeneck.kuickcheck.api.Functions.charToGenString
import org.mikeneck.kuickcheck.api.Functions.identity
import org.mikeneck.kuickcheck.api.Functions.intToChar
import org.mikeneck.kuickcheck.api.Functions.intToGenChar
import org.mikeneck.kuickcheck.api.Functions.plus
import org.mikeneck.kuickcheck.api.Functions.toUpper
import org.mikeneck.kuickcheck.api.GenMonadicContext.Companion.doing
import org.mikeneck.kuickcheck.api.GenMonadicContext.Companion.pure
import org.mikeneck.kuickcheck.api.GenMonadicContext.Companion.withContext
import org.mikeneck.kuickcheck.api.GenMonadicContext.Companion.withPair
import org.mikeneck.kuickcheck.api.GenMonadicContextSpec.gen
import java.util.*

object GenMonadicContextSpec : Spek({

    val int: LifecycleAware<GenMonadicContext<Unit, Int>> = memoized {
        mkGen { gen: KcGen, s: Size -> gen.nextInt(s.max % 26 + 1) - 1 }.doing()
    }

    val seed = Date().time
    val s = Random(seed).nextInt(10).toLong() + 1L
    val randomLoop = 0L..1000L step s

    val getGen: (Long) -> Pair<KcGen, KcGen> = { gen(it) to gen(it) }

    group("Checking GenMonadicContext's functor laws") {

        it("satisfies associative law(seed: $seed): fmap (g . f) gen = fmap g $ fmap f gen") {
            val oneByOne = int().map(intToChar).map(toUpper)
            val composition = int().map(intToChar + toUpper)

            randomLoop.forEach {
                val (left, right) = getGen(it)
                val size = Size(it.toInt() + 1)

                assert(oneByOne.generate(left)(size) == composition.generate(right)(size))
            }
        }

        it("satisfies identity law(seed: $seed) fmap id gen = gen") {
            val mapped = int().map(identity())
            val original = int()

            randomLoop.forEach {
                val (left, right) = getGen(it)
                val size = Size(it.toInt() + 1)

                assert(mapped.generate(left)(size) == original.generate(right)(size))
            }
        }
    }

    group("Checking GenMonadicContext's Monad laws") {

        it("satisfies left identity law(seed: $seed): return a >>= f == f a") {
            val x = Random(seed).nextInt()

            randomLoop.forEach {
                val (left, right) = getGen(it)
                val size = Size(it.toInt() + 1)

                assert(pure(x)
                        .flatMap(intToGenChar)
                        .generate(left)(size)
                        ==
                        intToGenChar(x).generate(right)(size))
            }
        }

        it("satisfies right identity law(seed: $seed): gen >>= return == gen") {
            randomLoop.forEach {
                val (left, right) = getGen(it)
                val size = Size(it.toInt() + 1)

                val leftExpression = int().flatMap(GenMonadicContext.Companion::pure)
                val rightIdentity = int()

                assert(leftExpression.generate(left)(size) == rightIdentity.generate(right)(size))
            }
        }

        it("satisfies associative law(seed: $seed): (m >>= f) >>= g == m >>= (\\x -> f x >>= g)") {
            val leftAssoc = int()[intToGenChar.withPair()][charToGenString.withPair()]
            val rightAssoc = int()[{ p: Pair<Unit, Int> ->
                intToGenChar.withContext<Int, Char, Unit>()(p)[charToGenString.withPair()]
            }]

            randomLoop.forEach {
                val (left, right) = getGen(it)
                val size = Size(it.toInt() + 1)

                assert(leftAssoc.generate(left)(size), rightAssoc.generate(right)(size)) { l, r -> l == r }
            }
        }
    }
}) {
    fun gen(seed: Long): KcGen = JavaUtilRandom(seed)
}
