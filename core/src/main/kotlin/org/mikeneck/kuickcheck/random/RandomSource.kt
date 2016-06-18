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
package org.mikeneck.kuickcheck.random

import java.util.*

object RandomSource {

    val random: Random = Random()

    fun nextFloat(min: Float, max: Float): Float {
        val range = max - min
        return if (range == 0.toFloat()) min else min + range * nextFloat()
    }

    fun nextReverseFloat(min: Float, max: Float): Float {
        val range = min - max
        return if (range == 0.toFloat()) max else max + range * nextFloat()
    }

    private fun nextFloat(): Float = random.nextFloat()

    fun nextDouble(min: Double, max: Double): Double {
        val range = max - min
        return if (range == 0.toDouble()) min else min + range * nextDouble()
    }

    fun nextReverseDouble(min: Double, max: Double): Double {
        val range = min - max
        return if (range == 0.toDouble()) max else max + range * nextDouble()
    }

    private fun nextDouble(): Double = random.nextDouble()
}
