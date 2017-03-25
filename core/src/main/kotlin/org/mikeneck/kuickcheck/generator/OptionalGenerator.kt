package org.mikeneck.kuickcheck.generator

import org.mikeneck.kuickcheck.Generator
import org.mikeneck.kuickcheck.random.RandomSource
import java.util.*

class OptionalGenerator<out T>(val generator: Generator<T>) : Generator<Optional<out T>> {
    override fun invoke(): Optional<out T> =
        if (RandomSource.nextInt(20) == 0) Optional.empty() else Optional.of(generator())
}