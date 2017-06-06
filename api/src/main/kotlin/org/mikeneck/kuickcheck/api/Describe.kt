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

abstract class Describe(val description: String, val optionConfig: Nothing) {

    abstract val check: Testable
}

sealed class Testable {
    abstract val size: Int
    abstract infix operator fun plus(other: Testable): Testable
}

internal object NoTests : Testable() {
    override val size: Int = 0
    override fun plus(other: Testable): Testable = other
}

internal class SingleTest<A>(
        val title: String,
        val gen: () -> Gen<A>,
        val property: (A) -> Boolean) : Testable() {
    override val size: Int = 1
    override fun plus(other: Testable): Testable = when (other) {
        is NoTests -> this
        is SingleTest<*> -> TestList(2, listOf(this, other))
        is TestList -> TestList(other.size + 1, other.tests + this)
    }
}

internal class TestList(override val size: Int, val tests: List<Testable>) : Testable() {
    override fun plus(other: Testable): Testable = when (other) {
        is NoTests -> this
        is SingleTest<*> -> TestList(size + other.size, tests + other)
        is TestList -> TestList(size + other.size, tests + other.tests)
    }
}

object KuickCheckApi {

    fun prop(title: String): Qualifier = object : Qualifier {
        override fun <A : Any> forAll(gen: () -> Gen<A>): PropertyDescriptor<A> =
                object : PropertyDescriptor<A> {
                    override fun satisfy(property: (A) -> Boolean): Testable = SingleTest(title, gen, property)
                }

    }
}

interface Qualifier {
    infix fun <A : Any> forAll(gen: () -> Gen<A>): PropertyDescriptor<A>
}

interface PropertyDescriptor<out A> {
    infix fun satisfy(property: (A) -> Boolean): Testable
}
