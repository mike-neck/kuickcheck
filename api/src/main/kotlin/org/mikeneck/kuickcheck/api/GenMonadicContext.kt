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
 * A [Gen] with do notation support.
 *
 * @since 1.0
 */
abstract class GenMonadicContext<C, A>(val gen: Gen<Pair<C, A>>) : Gen<A> {
    override fun generate(gen: KcGen): Sized<A> = object : Sized<A> {
        override fun invoke(size: Size): A = this@GenMonadicContext.gen.generate(gen)(size).second
    }

    /**
     * do notation support.
     *
     * @param f a transform function from type [A] to [B].
     * Its parameter is given from a context [C] and holding type [A].
     * @param B the type to be transformed.
     * @return [Gen] a transformed random value generator.
     * @since 1.0
     */
    operator fun <B> get(f: (Pair<C, A>) -> Gen<B>): GenMonadicContext<Pair<C, A>, B> =
            object : GenMonadicContext<Pair<C, A>, B>(gen.flatMap { p: Pair<C, A> -> f(p).map { b: B -> p to b } }) {}

    companion object {
        internal fun <A, B, C> Pair<A, B>.map(f: (B) -> C): Pair<A, C> = this.first to f(this.second)

        fun <A, B, C> ((A) -> Gen<B>).withPair(): (Pair<C, A>) -> Gen<B> = { (_, a) -> this(a) }

        fun <A, B, C> ((A) -> Gen<B>).withContext(): (Pair<C, A>) -> GenMonadicContext<Pair<C, A>, B> =
                { p: Pair<C, A> ->
                    object : GenMonadicContext<Pair<C, A>, B>(this@withContext(p.second).map { b: B ->
                        p to b
                    }) {}
                }

        fun <A> Gen<A>.doing(): GenMonadicContext<Unit, A> =
                object : GenMonadicContext<Unit, A>(this@doing.map { Unit to it }) {}

        /**
         * creates a generator which only generates the given value.
         *
         * @param x the value to be returned.
         * @return [Gen] - the generator only returns the given value.
         */
        fun <A> pure(x: A): GenMonadicContext<Unit, A> = Gen.pure(x).doing()
    }
}
