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
package org.mikeneck.kuickcheck.generator.collection

import org.mikeneck.kuickcheck.CollectionGenerator
import org.mikeneck.kuickcheck.Generator
import org.mikeneck.kuickcheck.generator.IntGenerator
import org.mikeneck.kuickcheck.generator.internal.EmptySizeGenerator
import org.mikeneck.kuickcheck.generator.internal.FixedSizeGenerator
import org.mikeneck.kuickcheck.generator.internal.SizeGenerator

internal interface ContainerGenerator<out T> : CollectionGenerator<T> {
    fun overrideSize(newSize: Int): Generator<T>

    override fun size(newSize: Int): Generator<T> {
        checkSize(newSize)
        return overrideSize(newSize)
    }

    fun fix(newSize: Int): Generator<T>

    override fun fixedSize(newSize: Int): Generator<T> {
        checkSize(newSize)
        return fix(newSize)
    }

    companion object {
        fun checkSize(size: Int): Unit {
            if (size < 0) throw IllegalArgumentException("Size should be larger than or equal to 0.[size: $size]")
        }

        fun sizeGenerator(size: Int): SizeGenerator {
            return when (size) {
                0 -> EmptySizeGenerator()
                else -> IntGenerator(0, size)
            }
        }

        fun sizeGenerator(size: Int, sizeFixed: Boolean): SizeGenerator =
                if (sizeFixed) FixedSizeGenerator(size) else sizeGenerator(size)
    }
}
