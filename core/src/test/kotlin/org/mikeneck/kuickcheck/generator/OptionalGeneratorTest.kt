package org.mikeneck.kuickcheck.generator

import org.junit.Test
import org.mikeneck.kuickcheck.alwaysTrue
import org.mikeneck.kuickcheck.int

class OptionalGeneratorTest {
    @Test(timeout = 1000)
    fun optionalGeneratorSometimesGeneratesEmpty() {
        val generator = int(0, 3).optional
        while (true) {
            if (!generator().isPresent) break
        }
    }

    @Test fun optionalPercentage() {
        val generator = alwaysTrue.optional
        val map = (0..1).map { Pair(it < 1, mutableListOf<Int>()) }.toMap()
        repeat(120) {
            val v = generator()
            if (!v.isPresent) map[false]?.add(it) else map[true]?.add(it)
        }
        println(map.map { Pair(it.key, it.value.size) }.toMap())
    }
}