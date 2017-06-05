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

/**
 * Randomly generates test values for given type.
 *
 * @param A The type of value to be generated.
 *
 * @since 1.0
 */
interface Gen<out A> {
    /**
     * generates adjustable random value for given type.
     *
     * @param gen Generator of random.
     * @return [Sized]
     * @since 1.0
     */
    fun generate(gen: KcGen): Sized<A>

    /**
     * maps a transform function to another type.
     *
     * @param f transform function from type [A] to [B].
     * @param B the type to be transformed.
     * @return [Gen] - a transformed random value generator.
     * @since 1.0
     */
    fun <B> map(f: (A) -> B): Gen<B> = mkGen { gen: KcGen, s: Size -> this@Gen.generate(gen)(s).let(f) }

    /**
     * maps a transform function to another type with flattening.
     *
     * @param f transform function from type [A] to [B].
     * @param B the type to be transformed.
     * @return [Gen] - a transformed random value generator flattened.
     * @since 1.0
     */
    fun <B> flatMap(f: (A) -> Gen<B>): Gen<B> = mkGen { gen: KcGen, s: Size ->
        f(this@Gen.generate(gen)(s)).generate(gen)(s)
    }

    /**
     * filters a generated value with given predicates.
     *
     * @param p predicates a generated value.
     * @return [Gen] - a random filtered value generator.
     * @since 1.0
     */
    fun filter(p: (A) -> Boolean): Gen<A> = mkGen { gen: KcGen, s: Size ->
        val a = this@Gen.generate(gen)(s)
        tailrec fun retry(x: A): A {
            return if (p(x)) x else retry(this@Gen.generate(gen)(s))
        }
        retry(a)
    }

    companion object {
        /**
         * creates a generator which only generates the given value.
         *
         * @param x the value to be returned.
         * @return [Gen] - the generator only returns the given value.
         */
        fun <A> pure(x: A): Gen<A> = mkGen { _, _ -> x }
    }
}
