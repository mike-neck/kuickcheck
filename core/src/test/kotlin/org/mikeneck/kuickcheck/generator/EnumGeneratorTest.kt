package org.mikeneck.kuickcheck.generator

import org.junit.Test

class EnumGeneratorTest {
    @Test fun singleValueEnum() {
        1.rangeTo(100).forEach { assert(EnumGenerator(Single::class.java).invoke() == Single.SINGLE) }
    }

    @Test fun multiValueEnum() {
        1.rangeTo(100).forEach { assert(EnumGenerator(Suit::class.java).invoke() in Suit.values()) }
    }

    @Test(timeout = 1000)
    fun generatorGeneratesTheValuesInRange() {
        val map: Map<Suit, MutableList<Suit>> =
                Suit.values().map { Pair(it, mutableListOf<Suit>()) }.toMap()
        val generator = EnumGenerator(Suit::class.java)
        while (true) {
            val value = generator.invoke()
            map[value]?.add(value)
            if (map.all { it.value.size > 0 }) break
        }
    }
}

enum class Single {
    SINGLE
}

enum class Suit {
    CLUBS, DIAMONDS, HEARTS, SPADES
}