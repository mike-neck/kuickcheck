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
import org.mikeneck.kuickcheck.generator.internal.SizeGenerator

internal class SetGenerator<out T>(
        val elementGenerator: Generator<T>,
        override val size: Int = 20,
        override val sizeFixed: Boolean = false) : ContainerGenerator<Set<T>> {

    val sizeGenerator: SizeGenerator = ContainerGenerator.sizeGenerator(size, sizeFixed)

    override fun overrideSize(newSize: Int): Generator<Set<T>> =
            SetGenerator(elementGenerator, newSize, sizeFixed)

    override fun fix(newSize: Int): Generator<Set<T>> =
            SetGenerator(elementGenerator, newSize, true)

    override fun invoke(): Set<T> {
        val size = sizeGenerator()
        val set = mutableSetOf<T>()
        var cs = 0
        var t = 0
        while (set.size < size && t < THRESHOLD) {
            set.add(elementGenerator())
            val s = set.size
            t = if (cs == s) t + 1 else 0
            cs = s
        }
        return set.toSet()
    }

    companion object {
        val THRESHOLD = 10
    }
}
