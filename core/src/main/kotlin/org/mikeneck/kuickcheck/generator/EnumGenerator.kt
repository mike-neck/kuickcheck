package org.mikeneck.kuickcheck.generator

import org.mikeneck.kuickcheck.Generator
import org.mikeneck.kuickcheck.random.RandomSource

class EnumGenerator<E : Enum<E>>(val clazz: Class<E>) : Generator<E> {
    override fun invoke(): E {
        val x = RandomSource.nextInt(clazz.enumConstants.size)
        return clazz.enumConstants[x]
    }
}