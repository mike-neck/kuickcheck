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

import org.mikeneck.kuickcheck.TogglableGenerator
import org.mikeneck.kuickcheck.random.RandomSource

/**
 * Generator for [Float].
 *
 * This generator generates value between `min`(inclusive) and `max`(exclusive).
 * Use [toggle] function to change the range's property(`min` to be exclusive, `max` to be inclusive).
 *
 * @property min the minimum value(inclusive) of the generator. `Float.MIN_VALUE` is default.
 * @property max the maximum value(exclusive) of the generator. `Float.MAX_VALUE` is default.
 */
class FloatGenerator(val min: Float = -Float.MAX_VALUE, val max: Float = Float.MAX_VALUE) : TogglableGenerator<Float> {

    init {
        if (max < min) throw IllegalArgumentException("Max should be larger than min.[max: $max, min: $min]")
    }

    override fun invoke(): Float = RandomSource.nextFloat(min, max)

    override fun toggle(): ToggledFloatGenerator = ToggledFloatGenerator(min, max)
}

/**
 * Generator for [Float].
 *
 * This generator generates value between `min`(exclusive) and `max`(inclusive).
 * Use [toggle] function to change the range's property(`min` to be inclusive, `max` to be exclusive).
 *
 * @property min the minimum value(exclusive) of the generator. `Float.MIN_VALUE` is default.
 * @property max the maximum value(inclusive) of the generator. `Float.MAX_VALUE` is default.
 */
class ToggledFloatGenerator(val min: Float = -Float.MAX_VALUE, val max: Float = Float.MAX_VALUE) : TogglableGenerator<Float> {

    init {
        if (max < min) throw IllegalArgumentException("Max should be larger than min.[max: $max, min: $min]")
    }

    override fun invoke(): Float = RandomSource.nextReverseFloat(min, max)

    override fun toggle(): FloatGenerator = FloatGenerator(min, max)
}
