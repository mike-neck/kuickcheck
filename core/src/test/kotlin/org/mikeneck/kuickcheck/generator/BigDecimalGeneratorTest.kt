package org.mikeneck.kuickcheck.generator

import org.junit.Test
import java.math.BigDecimal

class BigDecimalGeneratorTest {
    @Test(expected = IllegalArgumentException::class)
    fun maxIsSmallerThanMin() {
        BigDecimalGenerator(BigDecimal.TEN, BigDecimal.ONE)
    }

    @Test
    fun maxEqualsToMin() {
        val generator = BigDecimalGenerator(BigDecimal.ONE, BigDecimal.ONE)
        1.rangeTo(100).forEach { assert(generator.invoke().compareTo(BigDecimal.ONE) == 0)  }
    }

    @Test(timeout = 1000)
    fun generatorGeneratesTheValuesInRange() {
        val map: Map<Int, MutableList<BigDecimal>> =
                (1..10).map { Pair(it, mutableListOf<BigDecimal>()) }.toMap()
        val generator = BigDecimalGenerator(BigDecimal.ONE, BigDecimal.TEN)
        while (true) {
            val value = generator.invoke()
            assert(value > BigDecimal.ZERO)
            assert(value < BigDecimal(11))
            map[value.toInt()]?.add(value)
            if (map.all { it.value.size > 0 }) break
        }
    }

    @Test
    fun smallRange() {
        val generator = BigDecimalGenerator(BigDecimal.ZERO, BigDecimal(20))
        1.rangeTo(120).forEach {
            val random = generator.invoke()
            assert(BigDecimal.ZERO <= random && random <= BigDecimal(20), {"random value [$random]"})
        }
    }

    @Test
    fun largeRange() {
        val generator = BigDecimalGenerator(BigDecimal(-Double.MAX_VALUE), BigDecimal(Double.MAX_VALUE))
        val list = 1.rangeTo(200).map { generator.invoke() }.sorted()
        assert(BigDecimal(-Double.MAX_VALUE) <= list.first())
        assert(list.last() <= BigDecimal(Double.MAX_VALUE))
    }
}