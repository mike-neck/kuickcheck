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

internal class MutableMapGenerator<K, V>(
        val keyGenerator: Generator<K>,
        val valueGenerator: Generator<V>,
        override val size: Int = 20,
        override val sizeFixed: Boolean = false) : ContainerGenerator<MutableMap<K, V>> {

    val sizeGenerator: SizeGenerator = ContainerGenerator.sizeGenerator(size, sizeFixed)

    override fun overrideSize(newSize: Int): Generator<MutableMap<K, V>> =
            MutableMapGenerator(keyGenerator, valueGenerator, newSize, sizeFixed)

    override fun fix(newSize: Int): Generator<MutableMap<K, V>> =
            MutableMapGenerator(keyGenerator, valueGenerator, newSize, true)

    override fun invoke(): MutableMap<K, V> {
        val map = mutableMapOf<K, V>()
        val size = sizeGenerator()
        var t = 0
        while (map.size < size && t < ContainerGenerator.THRESHOLD) {
            val k = keyGenerator()
            if (map.containsKey(k)) {
                t++
            } else {
                map[k] = valueGenerator()
                t = 0
            }
        }
        return map
    }
}
