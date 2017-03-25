package org.mikeneck.kuickcheck.generator

import org.mikeneck.kuickcheck.Generator
import org.mikeneck.kuickcheck.random.RandomSource

class EnumGenerator<E : Enum<E>>(val values: List<E>) : Generator<E> {
    override fun invoke(): E {
        val x = RandomSource.nextInt(values.size)
        return values[x]
    }
}