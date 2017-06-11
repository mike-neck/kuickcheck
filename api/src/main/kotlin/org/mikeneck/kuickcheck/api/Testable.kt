package org.mikeneck.kuickcheck.api

interface Testable {

    val size: Int

    val execute: Executability get() = Executability.RunCase

    infix operator fun plus(other: Testable): Testable

    infix fun ignore(reason: String): Testable

    fun asList(): List<Testable>

    fun runnableCases(): List<Testable> = asList().filter { it.execute is Executability.RunCase }
}

sealed class TestableCase : Testable

internal object NoTests : TestableCase() {

    override val size: Int = 0

    override fun plus(other: Testable): Testable = other

    override fun ignore(reason: String): Testable = this

    override fun asList(): List<Testable> = emptyList()
}

internal class SingleTest<A>(
        val propertyDescription: PropertyDescription,
        val gen: () -> Gen<A>,
        override val execute: Executability,
        val property: (A) -> Boolean) : TestableCase() {

    override fun ignore(reason: String): Testable =
            SingleTest(
                    propertyDescription,
                    gen, Executability.Ignore(reason),
                    property)

    override val size: Int = 1

    override fun plus(other: Testable): Testable = when (other) {
        is TestableWrap -> plus(other.testable)
        is NoTests -> this
        is SingleTest<*> -> TestList(2, listOf(this, other), Executability.RunCase)
        is TestList -> TestList(other.size + 1, other.tests + this, Executability.RunCase)
        else -> throw IllegalArgumentException("not supported ${other::class}")
    }

    override fun asList(): List<Testable> = listOf(this)
}

internal class TestList(override val size: Int, val tests: List<Testable>, override val execute: Executability)
    : TestableCase() {

    override fun ignore(reason: String): Testable = TestList(size, tests, Executability.Ignore(reason))

    override fun plus(other: Testable): Testable = when (other) {
        is TestableWrap -> plus(other.testable)
        is NoTests -> this
        is SingleTest<*> -> TestList(size + other.size, tests + other, Executability.RunCase)
        is TestList -> TestList(size + other.size, tests + other.tests, Executability.RunCase)
        else -> throw IllegalArgumentException("not supported ${other::class}")
    }

    override fun asList(): List<Testable> = tests
}

class TestableWrap(val testable: Testable) : Testable by testable {
    operator fun get(testable: Testable): TestableWrap = TestableWrap(this + testable)
}

sealed class Executability {
    object RunCase : Executability()
    data class Ignore(val reason: String) : Executability()
}
