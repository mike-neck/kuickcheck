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

import org.mikeneck.kuickcheck.Checker
import org.mikeneck.kuickcheck.KuickCheck

fun <T> functionalPrediction(function: () -> T) = FunctionalPrediction(function)

class FunctionalPrediction<T>
(val function: () -> T, val repeatTime: Int = KuickCheck.DEFAULT_REPEAT)
: SingleParameterPrediction<T> {

    override fun satisfy(predicate: (T) -> Boolean): Checker<T> {
        return object : Checker<T> {
            override fun testData(): T = function.invoke()
            override fun consume(p: T): Boolean = predicate.invoke(p)
            override val repeat: Int = repeatTime
        }
    }

    override fun whenever(condition: (T) -> Boolean): SingleParameterPrediction<T> {
        return FunctionalFilteredPrediction(function, condition, repeatTime)
    }
}

class FunctionalFilteredPrediction<T>
(val function: () -> T, val condition: (T) -> Boolean, val repeatTime: Int = KuickCheck.DEFAULT_REPEAT)
: SingleParameterPrediction<T> {

    override fun satisfy(predicate: (T) -> Boolean): Checker<T> {
        return object : Checker<T> {
            override fun testData(): T {
                while (true) {
                    val t: T = function.invoke()
                    if (condition.invoke(t)) return t
                }
            }

            override fun consume(p: T): Boolean = predicate.invoke(p)
            override val repeat: Int = repeatTime
        }
    }

    override fun whenever(condition: (T) -> Boolean): SingleParameterPrediction<T> {
        val con: (T) -> Boolean = { t ->
            this.condition.invoke(t) && condition.invoke(t)
        }
        return FunctionalFilteredPrediction(function, con, repeatTime)
    }
}
