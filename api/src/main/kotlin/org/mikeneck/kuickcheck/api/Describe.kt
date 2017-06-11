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

abstract class Describe(val overview: String, val optionConfig: OptionConfig) {

    constructor(overview: String) : this(overview, NoImplNow)

    abstract val check: Testable

    fun prop(detail: String): Qualifier = object : Qualifier {
        override fun <A : Any> forAll(gen: () -> Gen<A>): PropertyDescriptor<A> = object : PropertyDescriptor<A> {
            override fun satisfy(property: (A) -> Boolean): Testable =
                    SingleTest(PropertyDescription(this@Describe.overview, detail), gen, Executability.RunCase, property)
        }
    }

    companion object {
        val newTest: TestableWrap = TestableWrap(NoTests)
        val noTest: Testable = NoTests
    }
}

interface Qualifier {
    infix fun <A : Any> forAll(gen: () -> Gen<A>): PropertyDescriptor<A>
}

interface PropertyDescriptor<out A> {
    infix fun satisfy(property: (A) -> Boolean): Testable
}

interface OptionConfig

object NoImplNow : OptionConfig

data class TestMetaData(val overview: String, val optionConfig: OptionConfig)
