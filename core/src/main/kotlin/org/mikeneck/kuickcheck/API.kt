/*
 * Copyright 2016 Shinya Mochida
 * 
 * Licensed under the Apache License,Version2.0(the"License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,software
 * Distributed under the License is distributed on an"AS IS"BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.mikeneck.kuickcheck

import org.mikeneck.kuickcheck.generator.*
import org.mikeneck.kuickcheck.generator.collection.*
import org.mikeneck.kuickcheck.prediction.*
import org.mikeneck.kuickcheck.runner.ClassScanner
import org.mikeneck.kuickcheck.runner.toSummary
import java.util.*
import kotlin.system.exitProcess

@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class Property

interface Checker<T> {
    fun testData(): T
    fun consume(p: T): Boolean
    val repeat: Int

    infix operator fun times(rep: Int): Checker<T> {
        val checker = this
        return object: Checker<T> {
            override fun testData(): T = checker.testData()
            override fun consume(p: T): Boolean = checker.consume(p)
            override val repeat: Int = rep
        }
    }
}

interface Checker2<F, S>: Checker<Pair<F, S>>

interface Generator<out T> : () -> T

interface CollectionGenerator<out T> : Generator<T> {
    val size: Int
    val sizeFixed: Boolean
    fun size(newSize: Int): Generator<T>
    fun fixedSize(newSize: Int): Generator<T>
}

object KuickCheck {

    @JvmField val DEFAULT_REPEAT = 100

    @JvmStatic fun main(args: Array<String>) {
        if (args.contains("--debug")) ClassScanner.debug()
        val properties = ClassScanner.prepareForCheck()
        println("${properties.size} properties found.")
        val checksToRun = properties.map { it.getExecutor() }
        val summary = checksToRun.map { it.run() }.toSummary()

        summary.printResult()
        summary.printSummary()
        exitProcess(if (summary.testSuccess()) 0 else 1)
    }
}

fun <T> forAll(generator: Generator<T>): SingleParameterPrediction<T> = singleParameterPrediction(generator)

fun <T, U> forAll(gen1: Generator<T>, gen2: Generator<U>): DoubleParameterPrediction<T, U> =
        doubleParameterPrediction(gen1, gen2)

fun <T> forAll(function: () -> T): SingleParameterPrediction<T> = functionalPrediction(function)

val boolean: Generator<Boolean> = BooleanGenerator()

val alwaysTrue: Generator<Boolean> = BooleanGenerator(true)

val alwaysFalse: Generator<Boolean> = BooleanGenerator(false)

val int: Generator<Int> = IntGenerator(Int.MIN_VALUE, Int.MAX_VALUE)

fun int(only: Int): Generator<Int> = IntGenerator(only, only)

val positiveInt: Generator<Int> = IntGenerator(1, Int.MAX_VALUE)

val positiveIntFrom0: Generator<Int> = IntGenerator(0, Int.MAX_VALUE)

val negativeInt: Generator<Int> = IntGenerator(Int.MIN_VALUE, -1)

val negativeIntTo0: Generator<Int> = IntGenerator(Int.MIN_VALUE, 0)

fun int(min: Int, max: Int): Generator<Int> = IntGenerator(min, max)

val long: Generator<Long> = LongGenerator(Long.MIN_VALUE, Long.MAX_VALUE)

val positiveLong: Generator<Long> = LongGenerator(1, Long.MAX_VALUE)

val positiveLongFrom0: Generator<Long> = LongGenerator(0, Long.MAX_VALUE)

val negativeLong: Generator<Long> = LongGenerator(Long.MIN_VALUE, -1)

val negativeLongTo0: Generator<Long> = LongGenerator(Long.MIN_VALUE, 0)

fun long(min: Long, max: Long): Generator<Long> = LongGenerator(min, max)

val char: Generator<Char> = CharGenerator(Character.MIN_VALUE, Character.MAX_VALUE)

fun char(ch: Char): Generator<Char> = CharGenerator(ch, ch)

fun char(vararg chars: Char): Generator<Char> = CharGenerator(chars)

fun char(chars: String): Generator<Char> = CharGenerator(chars)

fun charInRange(c1: Char, c2: Char): Generator<Char> = CharGenerator(c1, c2)

val largeLetterChar: Generator<Char> = charInRange('A', 'Z')

val smallLetterChar: Generator<Char> = charInRange('a', 'z')

val numericChar: Generator<Char> = charInRange('0', '9')

val alphaNumericChar: Generator<Char> = largeLetterChar + smallLetterChar + numericChar

infix operator fun Generator<Char>.plus(o: Generator<Char>): Generator<Char> =
        (this as CharGenerator) + (o as CharGenerator)

val float: Generator<Float> = FloatGenerator()

val positiveFloat: Generator<Float> = FloatGenerator(min = Float.MIN_VALUE)

val positiveFloatFrom0: Generator<Float> = FloatGenerator(min = 0f)

val negativeFloat: Generator<Float> = FloatGenerator(max = -Float.MIN_VALUE)

val negativeFloatTo0: Generator<Float> = FloatGenerator(max = 0f)

fun float(min: Float, max: Float): Generator<Float> = FloatGenerator(min, max)

val double: Generator<Double> = DoubleGenerator()

val positiveDouble: Generator<Double> = DoubleGenerator(min = Double.MIN_VALUE)

val positiveDoubleFrom0: Generator<Double> = DoubleGenerator(min = 0.0)

val negativeDouble: Generator<Double> = DoubleGenerator(max = -Double.MIN_VALUE)

val negativeDoubleTo0: Generator<Double> = DoubleGenerator(max = 0.0)

fun double(min: Double, max: Double): Generator<Double> = DoubleGenerator(min, max)

val string: Generator<String> = AllStringGenerator()

fun string(size: Int): Generator<String> = AllStringGenerator(size)

fun string(str: String): Generator<String> = StringGenerator(str)

fun string(str: String, size: Int): Generator<String> = StringGenerator(str, size)

val date: Generator<Date> = DateGenerator()

fun date(min: Date, max: Date): Generator<Date> = DateGenerator(min, max)

fun date(min: Long, max: Long): Generator<Date> = DateGenerator(min, max)

fun date(min: Long, max: Date): Generator<Date> = DateGenerator(min, max)

fun date(min: Date, max: Long): Generator<Date> = DateGenerator(min, max)

val today: Generator<Date> = DateGenerator.today()

val dayInThisMonth: Generator<Date> = DateGenerator.thisMonth()

val dayInThisYear: DateGeneratorOfYear = DateGenerator.thisYear()

fun dayInTheYear(year: Int): DateGeneratorOfYear = DateGenerator.ofYear(year)

fun <T> list(type: Generator<T>): CollectionGenerator<List<T>> = ListGenerator(type)

fun <T> mutableList(type: Generator<T>): CollectionGenerator<MutableList<T>> =
        MutableListGenerator(type)

fun <T> set(type: Generator<T>): CollectionGenerator<Set<T>> = SetGenerator(type)

fun <T> mutableSet(type: Generator<T>): CollectionGenerator<Set<T>> =
        MutableSetGenerator(type)

fun <K, V> map(key: Generator<K>, value: Generator<V>): CollectionGenerator<Map<K, V>> =
        MapGenerator(key, value)
