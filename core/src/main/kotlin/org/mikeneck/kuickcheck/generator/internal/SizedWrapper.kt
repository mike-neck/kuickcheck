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

internal interface SizedWrapper<T> {
    val value: T
    val size: Int
}

internal class StringWrapper(override val value: String, override val size: Int) : SizedWrapper<String> {
    override fun toString(): String = value
}

internal class NormalString(override val value: String) : SizedWrapper<String> {
    override val size: Int = 1
    override fun toString(): String = value
}

internal class SurrogateString(override val value: String) : SizedWrapper<String> {
    override val size: Int = 2
    override fun toString(): String = value
}

internal class IntWrapper(override val value: Int, override val size: Int) : SizedWrapper<Int> {
    fun string(): StringWrapper {
        val chars = Character.toChars(value)
        val s = String(chars)
        return StringWrapper(s, size)
    }
}
