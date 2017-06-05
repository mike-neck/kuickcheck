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

interface Gen<out A> {
    fun generate(gen: KcGen): Sized<A>

    fun <B> map(f: (A) -> B): Gen<B> = mkGen { gen: KcGen, s: Size -> this@Gen.generate(gen)(s).let(f) }

    fun <B> flatMap(f: (A) -> Gen<B>): Gen<B> = mkGen { gen: KcGen, s: Size ->
        f(this@Gen.generate(gen)(s)).generate(gen)(s)
    }

    fun filter(p: (A) -> Boolean): Gen<A> = mkGen { gen: KcGen, s: Size ->
        val a = this@Gen.generate(gen)(s)
        tailrec fun retry(x: A): A {
            return if (p(x)) x else retry(this@Gen.generate(gen)(s))
        }
        retry(a)
    }

    companion object {
        fun <A> pure(x: A): Gen<A> = mkGen { _, _ -> x }
    }
}
