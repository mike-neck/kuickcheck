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

import org.mikeneck.kuickcheck.Generator
import org.mikeneck.kuickcheck.generator.internal.FixedSizeGenerator
import org.mikeneck.kuickcheck.generator.internal.SizeGenerator

internal class MutableListGenerator<T>(
        val elementGenerator: Generator<T>,
        override val size: Int = 20,
        override val sizeFixed: Boolean = false) : ContainerGenerator<MutableList<T>> {

    val sizeGenerator: SizeGenerator = if (sizeFixed) FixedSizeGenerator(size)
    else ContainerGenerator.sizeGenerator(size)

    override fun overrideSize(newSize: Int): Generator<MutableList<T>> =
            MutableListGenerator(elementGenerator, newSize, sizeFixed)

    override fun fix(newSize: Int): Generator<MutableList<T>> =
            MutableListGenerator(elementGenerator, newSize, true)

    override fun invoke(): MutableList<T> = sizeGenerator.of { elementGenerator() }.toMutableList()
}
