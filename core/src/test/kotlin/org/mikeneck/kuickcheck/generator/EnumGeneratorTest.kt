package org.mikeneck.kuickcheck.generator

import org.junit.Test
import org.mikeneck.kuickcheck.enum

class EnumGeneratorTest {
    @Test fun singleValueEnum() {
        1.rangeTo(100).forEach { assert(enum<Single>().invoke() == Single.SINGLE) }
    }

    @Test fun multiValueEnum() {
        1.rangeTo(100).forEach { assert(enum<Suit>().invoke() in Suit.values()) }
    }

    @Test fun multiValueEnumSpecifiedValues() {
        1.rangeTo(100).forEach { assert(enum(Suit.HEARTS, Suit.DIAMONDS).invoke() in listOf(Suit.HEARTS, Suit.DIAMONDS)) }
    }

    @Test(timeout = 1000)
    fun generatorGeneratesTheValuesInRange() {
        val map: Map<Suit, MutableList<Suit>> =
                Suit.values().map { Pair(it, mutableListOf<Suit>()) }.toMap()
        val generator = enum<Suit>()
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