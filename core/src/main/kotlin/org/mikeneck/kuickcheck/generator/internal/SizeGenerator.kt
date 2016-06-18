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
package org.mikeneck.kuickcheck.generator.internal

import org.mikeneck.kuickcheck.Generator

internal interface SizeGenerator : Generator<Int> {
    val max: Int
    fun <U> of(f: (Int) -> U): List<U> = 1.rangeTo(this.invoke()).map(f)
}

internal class Size1SizeGenerator : SizeGenerator {
    override fun invoke(): Int = 1
    override val max: Int = 1
}

internal class FixedSizeGenerator(val size: Int) : SizeGenerator {
    override val max: Int = 1
    override fun invoke(): Int = size
}

internal class EmptySizeGenerator : SizeGenerator {
    override fun invoke(): Int = 0
    override val max: Int = 0
}
