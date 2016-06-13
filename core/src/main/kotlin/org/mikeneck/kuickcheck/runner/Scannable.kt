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
package org.mikeneck.kuickcheck.runner

import kotlin.reflect.KClass

interface Scannable {
    val name: String
    val validClassName: Boolean
    val klass: KClass<*>?
    val objectInstance: Boolean
}

internal data class SingletonClass(
        override val name: String,
        override val klass: KClass<*>): Scannable {
    override val validClassName: Boolean = true
    override val objectInstance: Boolean = true
}

internal data class NormalClass(
        override val name: String,
        override val klass: KClass<*>): Scannable {
    override val validClassName: Boolean = true
    override val objectInstance: Boolean = false
}

internal data class ExcludedClass(
        override val name: String,
        override val klass: KClass<*>):Scannable {
    override val objectInstance: Boolean = false
    override val validClassName: Boolean = true
}

internal data class NotFoundClass(
        override val name: String): Scannable {
    override val validClassName: Boolean = false
    override val klass: KClass<*>? = null
    override val objectInstance: Boolean = false
}

internal data class EnumClass(
        override val name: String,
        override val klass: KClass<*>): Scannable {
    override val validClassName: Boolean = true
    override val objectInstance: Boolean = false
}

internal data class ThrowableClass(
        override val name: String,
        override val klass: KClass<*>): Scannable {
    override val validClassName: Boolean = true
    override val objectInstance: Boolean = false
}

internal data class InterfaceClass(
        override val name: String,
        override val klass: KClass<*>): Scannable {
    override val validClassName: Boolean = true
    override val objectInstance: Boolean = false
}
