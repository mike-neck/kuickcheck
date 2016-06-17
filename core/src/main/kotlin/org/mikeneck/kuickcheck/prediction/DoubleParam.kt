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
package org.mikeneck.kuickcheck.prediction

import org.mikeneck.kuickcheck.Checker2
import org.mikeneck.kuickcheck.Generator
import org.mikeneck.kuickcheck.KuickCheck

fun <T, U> doubleParameterPrediction(gen1: Generator<T>, gen2: Generator<U>): DoubleParameterPrediction<T, U> =
        DoubleArgumentPrediction(gen1, gen2)

interface DoubleParameterPrediction<T, U> {
    fun satisfy(predicate: (T, U) -> Boolean): Checker2<T, U>
    fun filter(condition: (T, U) -> Boolean): DoubleParameterPrediction<T, U>
}

class DoubleArgumentPrediction<T, U>
(val gen1: Generator<T>, val gen2: Generator<U>, val repeatTime: Int = KuickCheck.DEFAULT_REPEAT)
: DoubleParameterPrediction<T, U>{

    override fun satisfy(predicate: (T, U) -> Boolean): Checker2<T, U> {
        return object: Checker2<T, U> {
            override fun testData(): Pair<T, U> = Pair(gen1.invoke(), gen2.invoke())
            override fun consume(p: Pair<T, U>): Boolean {
                return predicate.invoke(p.first, p.second)
            }
            override val repeat: Int = repeatTime
        }
    }

    override fun filter(condition: (T, U) -> Boolean): DoubleParameterPrediction<T, U>
            = DoubleArgumentFilteredPrediction(gen1, gen2, condition, repeatTime)
}

class DoubleArgumentFilteredPrediction<T, U>
(val gen1: Generator<T>, val gen2: Generator<U>, val condition: (T, U) -> Boolean, val repeatTime: Int = KuickCheck.DEFAULT_REPEAT)
: DoubleParameterPrediction<T, U> {

    override fun satisfy(predicate: (T, U) -> Boolean): Checker2<T, U> {
        return object: Checker2<T, U> {
            override fun testData(): Pair<T, U> {
                while (true) {
                    val p = Pair(gen1.invoke(), gen2.invoke())
                    if (condition.invoke(p.first, p.second)) return p
                }
            }
            override fun consume(p: Pair<T, U>): Boolean = predicate.invoke(p.first, p.second)
            override val repeat: Int = repeatTime
        }
    }

    override fun filter(condition: (T, U) -> Boolean): DoubleParameterPrediction<T, U> {
        val con = {t: T, u: U ->
            this.condition.invoke(t, u) && condition.invoke(t, u)
        }
        return DoubleArgumentFilteredPrediction(this.gen1, this.gen2, con, this.repeatTime)
    }
}
