package org.mikeneck.kuickcheck.generator

import org.mikeneck.kuickcheck.Generator
import java.math.BigDecimal

class BigDecimalGenerator(val min: BigDecimal, val max: BigDecimal) : Generator<BigDecimal> {
    init {
        if (max < min) throw IllegalArgumentException("Max should be larger than min. [max: $max, min: $min]")
    }

    override fun invoke(): BigDecimal {
        val randomBigDecimal = min + (BigDecimal(Math.random()) * (max - min))
        return randomBigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP)
    }
}