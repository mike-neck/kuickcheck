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

abstract class GenMonadicContext<C, A>(val gen: Gen<Pair<C, A>>) : Gen<A> {
    override fun generate(gen: KcGen): Sized<A> = object : Sized<A> {
        override fun invoke(size: Size): A = this@GenMonadicContext.gen.generate(gen)(size).second
    }

    fun <B> __flat__map(f: (A) -> Gen<B>): GenMonadicContext<Pair<C, A>, B> =
            object : GenMonadicContext<Pair<C, A>, B>(flatMap { a: A ->
                mkGen { kcGen, size ->
                    this@GenMonadicContext.gen.generate(kcGen)(size).map(toPairMap(f)).move().map { gb ->
                        gb.generate(kcGen)(size)
                    }
                }
            }) {}

    companion object {
        fun <A, B, C> Pair<Pair<A, B>, C>.flatten(): Triple<A, B, C> =
                Triple(this.first.first, this.first.second, this.second)

        fun <A, B, C> Pair<A, B>.map(f: (B) -> C): Pair<A, C> = this.first to f(this.second)

        fun <A, B> toPairMap(f: (A) -> B): (A) -> Pair<A, B> = { a: A -> a to f(a) }

        fun <A, B, C> Pair<A, Pair<B, C>>.move(): Pair<Pair<A, B>, C> =
                (this.first to this.second.first) to this.second.second
    }
}