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
package org.mikeneck.kuickcheck.generator

import org.mikeneck.kuickcheck.Generator
import java.text.SimpleDateFormat
import java.util.*

internal class DateGenerator(@Suppress("UNUSED_PARAMETER") val min: Date,
                             @Suppress("UNUSED_PARAMETER") val max: Date) : Generator<Date> {

    constructor() : this(-2208988800000, Long.MAX_VALUE)

    constructor(min: Long, max: Long) : this(Date(min), Date(max))

    constructor(min: Date, max: Long) : this(min, Date(max))

    constructor(min: Long, max: Date) : this(Date(min), max)

    init {
        if (max < min) throw IllegalArgumentException("Max date is smaller than min date.[max: $max, min: $min]")
    }

    private val generator: Generator<Long> = LongGenerator(min.time, max.time)

    override fun generate(): Date {
        val time = generator.generate()
        return Date(time)
    }

    internal companion object {

        val allFormat = "yyyy/MM/dd HH:mm:ss.SSS"

        fun today(): DateGenerator {
            val now = Date()
            val fmt = SimpleDateFormat("yyyy/MM/dd")
            val today = fmt.format(now)
            val format = SimpleDateFormat(allFormat)
            val min = format.parse("$today 00:00:00.000")
            val max = format.parse("$today 23:59:59.999")
            return DateGenerator(min, max)
        }

        fun thisMonth(): DateGenerator {
            val now = Date()
            val fmt = SimpleDateFormat("yyyy/MM")
            val thisMonth = fmt.format(now)
            val format = SimpleDateFormat(allFormat)
            val min = format.parse("$thisMonth/01 00:00:00.000")
            val max = format.parse("$thisMonth/${lastDay(now)} 23:59:59.999")
            return DateGenerator(min, max)
        }

        @Suppress("DEPRECATION")
        internal fun lastDay(today: Date): Int {
            val m = today.month + 1
            val y = today.year
            return lastDay(y, m)
        }

        internal fun lastDay(year: Int, month: Int): Int {
            return when (month) {
                in intArrayOf(1, 3, 5, 7, 8, 10, 12) -> 31
                in intArrayOf(4, 6, 9, 11) -> 30
                else -> {
                    if (year % 400 == 0 || (year % 100 != 0 && year % 4 == 0)) 29
                    else 28
                }
            }
        }

        @Suppress("DEPRECATION")
        fun thisYear(): DateGeneratorOfYear = ofYear(Date().year + 1900)

        fun ofMonth(year: Int, month: Month): DateGeneratorOfMonth =
                MonthSpecifiedDateGenerator(year, month)

        fun ofYear(year: Int): DateGeneratorOfYear = YearSpecifiedDateGenerator(year)
    }
}

interface DayFrom : Generator<Date> {
    fun through(day: Int): Generator<Date>
}

interface DayThrough : Generator<Date> {
    fun from(day: Int): Generator<Date>
}

interface DateGeneratorOfMonth : Generator<Date> {
    fun through(day: Int): DayThrough
    fun from(day: Int): DayFrom
}

enum class Month(val number: Int) {
    JANUARY(1), FEBRUARY(2), MARCH(3), APRIL(4), MAY(5), JUNE(6),
    JULY(7), AUGUST(8), SEPTEMBER(9), OCTOBER(10), NOVEMBER(11), DECEMBER(12)
}

internal class MonthSpecifiedDateGenerator(val year: Int, val month: Month, val from: Int = 1, through: Int? = null) : DateGeneratorOfMonth, DayFrom, DayThrough {

    val end: Int

    private val generator: Generator<Date>

    init {
        val last = DateGenerator.lastDay(year, month.number)
        end = through ?: last
        val fmt = SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS")
        val min = fmt.parse(String.format("%4d/%02d/%02d 00:00:00.000", year, month.number, from))
        val max = fmt.parse(String.format("%4d/%02d/%02d 23:59:59.999", year, month.number, end))
        generator = DateGenerator(min, max)
    }

    override fun from(day: Int): DayFrom {
        if (day <= 0) throw IllegalArgumentException("The day is smaller than 1. [$day]")
        if (end < day) throw IllegalArgumentException("The day is larger than the last day. [day: $day, last: $end]")
        return MonthSpecifiedDateGenerator(year, month, day)
    }

    override fun through(day: Int): DayThrough {
        val last = DateGenerator.lastDay(year, month.number)
        if (last < day) throw IllegalArgumentException("The day is larger than the last day of the month. [day: $day, last: $last]")
        if (day < from) throw IllegalArgumentException("The day is smaller than the first day. [day: $day, first: $from]")
        return MonthSpecifiedDateGenerator(year, month, from, day)
    }

    override fun generate(): Date {
        return generator.generate()
    }
}

interface DateGeneratorOfYear : Generator<Date> {
    fun january(): DateGeneratorOfMonth
    val JAN: DateGeneratorOfMonth
    fun february(): DateGeneratorOfMonth
    val FEB: DateGeneratorOfMonth
    fun march(): DateGeneratorOfMonth
    val MAR: DateGeneratorOfMonth
    fun april(): DateGeneratorOfMonth
    val APR: DateGeneratorOfMonth
    fun may(): DateGeneratorOfMonth
    val MAY: DateGeneratorOfMonth
    fun june(): DateGeneratorOfMonth
    val JUN: DateGeneratorOfMonth
    fun july(): DateGeneratorOfMonth
    val JUL: DateGeneratorOfMonth
    fun august(): DateGeneratorOfMonth
    val AUG: DateGeneratorOfMonth
    fun september(): DateGeneratorOfMonth
    val SEP: DateGeneratorOfMonth
    fun october(): DateGeneratorOfMonth
    val OCT: DateGeneratorOfMonth
    fun november(): DateGeneratorOfMonth
    val NOV: DateGeneratorOfMonth
    fun december(): DateGeneratorOfMonth
    val DEC: DateGeneratorOfMonth
}

internal class YearSpecifiedDateGenerator(val year: Int) : DateGeneratorOfYear {

    private val generator: Generator<Date>

    init {
        val fmt = SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS")
        val min = fmt.parse("$year/01/01 00:00:00.000")
        val max = fmt.parse("$year/12/31 23:59:59.999")
        generator = DateGenerator(min, max)
    }

    fun month(month: Month): DateGeneratorOfMonth = MonthSpecifiedDateGenerator(year, month)

    override fun january(): DateGeneratorOfMonth = month(Month.JANUARY)

    override val JAN: DateGeneratorOfMonth
        get() = month(Month.JANUARY)

    override fun february(): DateGeneratorOfMonth = month(Month.FEBRUARY)

    override val FEB: DateGeneratorOfMonth
        get() = month(Month.FEBRUARY)

    override fun march(): DateGeneratorOfMonth = month(Month.MARCH)

    override val MAR: DateGeneratorOfMonth
        get() = month(Month.MARCH)

    override fun april(): DateGeneratorOfMonth = month(Month.APRIL)

    override val APR: DateGeneratorOfMonth
        get() = month(Month.APRIL)

    override fun may(): DateGeneratorOfMonth = month(Month.MAY)

    override val MAY: DateGeneratorOfMonth
        get() = month(Month.MAY)

    override fun june(): DateGeneratorOfMonth = month(Month.JUNE)

    override val JUN: DateGeneratorOfMonth
        get() = month(Month.JUNE)

    override fun july(): DateGeneratorOfMonth = month(Month.JULY)

    override val JUL: DateGeneratorOfMonth
        get() = month(Month.JULY)

    override fun august(): DateGeneratorOfMonth = month(Month.AUGUST)

    override val AUG: DateGeneratorOfMonth
        get() = month(Month.AUGUST)

    override fun september(): DateGeneratorOfMonth = month(Month.SEPTEMBER)

    override val SEP: DateGeneratorOfMonth
        get() = month(Month.SEPTEMBER)

    override fun october(): DateGeneratorOfMonth = month(Month.OCTOBER)

    override val OCT: DateGeneratorOfMonth
        get() = month(Month.OCTOBER)

    override fun november(): DateGeneratorOfMonth = month(Month.NOVEMBER)

    override val NOV: DateGeneratorOfMonth
        get() = month(Month.NOVEMBER)

    override fun december(): DateGeneratorOfMonth = month(Month.DECEMBER)

    override val DEC: DateGeneratorOfMonth
        get() = month(Month.DECEMBER)

    override fun generate(): Date = generator.generate()
}
