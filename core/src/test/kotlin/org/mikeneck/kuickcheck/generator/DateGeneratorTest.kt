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

import org.junit.Test
import org.junit.experimental.theories.DataPoints
import org.junit.experimental.theories.Theories
import org.junit.experimental.theories.Theory
import org.junit.runner.RunWith
import java.text.SimpleDateFormat
import java.util.*

class DateGeneratorTest {

    @Test(expected = IllegalArgumentException::class)
    fun minIsOlderThanMax() {
        val max = Date(0)
        val min = Date(1)
        DateGenerator(min, max)
    }

    @Test fun minEqualsToMax() {
        val date = Date()
        val generator = DateGenerator(date, date)
        assert(generator.generate() == date)
    }

    @Test fun generatorGeneratesTheDateBetweenMinAndMax() {
        val min = Date(0)
        val max = Date(1280000)
        val generator = DateGenerator(min, max)
        1.rangeTo(120).forEach {
            val d = generator.generate()
            assert(min <= d)
            assert(d <= max)
        }
    }

    @Test fun today() {
        val fmt = SimpleDateFormat("yyyy/MM/dd")
        val now = Date()
        val date = fmt.format(now)
        val today = DateGenerator.today()
        1.rangeTo(120).forEach {
            val g = today.generate()
            val f = fmt.format(g)
            assert(date == f, { "[$it]expected:$date - actual:$f" })
        }
    }

    @Test fun thisMonth() {
        val fmt = SimpleDateFormat("yyyy/MM")
        val now = Date()
        val month = fmt.format(now)
        val thisMonth = DateGenerator.thisMonth()
        1.rangeTo(120).forEach {
            val g = thisMonth.generate()
            val f = fmt.format(g)
            assert(month == f, { "[$it]expected:$month - actual:$f" })
        }
    }
}

@RunWith(Theories::class)
class LastDayTest {

    companion object {
        @DataPoints @JvmField val testData = listOf(
                Pair("2000/02/01", 29),
                Pair("1900/02/01", 28),
                Pair("2016/02/01", 29)
        )
    }

    @Theory fun test(data: Pair<String, Int>) {
        val fmt = SimpleDateFormat("yyyy/MM/dd")
        val date = fmt.parse(data.first)
        val actual = DateGenerator.lastDay(date)
        assert(actual == data.second)
    }
}

class DateGeneratorOfMonthTest {

    @Test(expected = IllegalArgumentException::class)
    fun day0CauseIllegalArgumentException() {
        val generator = DateGenerator.ofMonth(2016, Month.JUNE)
        generator.from(0)
    }

    @Test(expected = IllegalArgumentException::class)
    fun dayLargerThanEndCauseIllegalArgumentException() {
        val generator = DateGenerator.ofMonth(2016, Month.JUNE)
        generator.from(31)
    }

    @Test(expected = IllegalArgumentException::class)
    fun dayLargerThanLastDayCauseIllegalArgumentException() {
        val generator = DateGenerator.ofMonth(2016, Month.JUNE)
        generator.through(31)
    }

    @Test(expected = IllegalArgumentException::class)
    fun daySmallerThanFirstDayCauseIllegalArgumentException() {
        val generator = DateGenerator.ofMonth(2016, Month.JUNE).from(15)
        generator.through(14)
    }

    @Test
    fun generatorGeneratesTheSameMonth() {
        val generator = DateGenerator.ofMonth(2016, Month.JUNE)
        1.rangeTo(120).forEach {
            val date = generator.generate()
            @Suppress("DEPRECATION")
            val month = date.month
            assert(month == 5)
        }
    }

    @Test
    fun generatorGeneratesTheDayInclusive() {
        val generator = DateGenerator.ofMonth(2016, Month.JUNE)
                .from(11).through(19)
        1.rangeTo(120).forEach {
            val date = generator.generate()
            @Suppress("DEPRECATION")
            val day = date.date
            assert(11 <= day, { "$date is after 11." })
            assert(day <= 19, { "$date is before 19." })
        }
    }

    @Test
    fun yearGeneratorGeneratesTheDayOfTheSameYear() {
        val thisMonth = DateGenerator.thisMonth()
        val thisYear = DateGenerator.thisYear()
        1.rangeTo(120).forEach {
            val m = thisMonth.generate()
            val y = thisYear.generate()
            @Suppress("DEPRECATION")
            assert(m.year == y.year)
        }
    }
}
