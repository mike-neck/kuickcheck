package org.mikeneck.kuickcheck.api

sealed class Testable {
    abstract val size: Int
    abstract infix operator fun plus(other: Testable): Testable
}

internal object NoTests : Testable() {
    override val size: Int = 0
    override fun plus(other: Testable): Testable = other
}

internal class SingleTest<A>(
        val propertyDescription: PropertyDescription,
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
