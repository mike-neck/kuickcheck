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

import org.mikeneck.kuickcheck.Checker
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KProperty
import kotlin.reflect.primaryConstructor

data class TargetProperty (
        val klass: KClass<*>,
        val objectInstance: Boolean,
        val propertyName: String,
        val property: KProperty<Checker<*>>): Comparable<TargetProperty> {

    val packageName: List<String> 

    init {
        packageName = klass.qualifiedName?.split(".")?.dropLast(1)?: emptyList()
    }

    fun getExecutor(): CheckExecutor = CheckExecutor(packageName, klass.simpleName as String, propertyName, getChecker())

    fun getChecker(): Checker<Any> {
        val instance = getInstance()
        @Suppress("UNCHECKED_CAST")
        return property.getter.call(instance) as Checker<Any>
    }

    fun getInstance(): Any {
        if (objectInstance) return klass.objectInstance as Any
        else {
            val constructor = klass.primaryConstructor as KFunction<*>
            return constructor.call() as Any
        }
    }

    override fun compareTo(other: TargetProperty): Int {
        val sp = packageName.iterator()
        val op = other.packageName.iterator()
        while (sp.hasNext() && op.hasNext()) {
            val c = compare(sp.next(), op.next())
            if (c != 0) return c
        }
        if (sp.hasNext()) return -1
        if (op.hasNext()) return 1
        val c = compare(klass.simpleName?: "", other.klass.simpleName?: "")
        if (c != 0) return c
        return compare(propertyName, other.propertyName)
    }

    private fun compare(self: String, other: String): Int = self.compareTo(other)
}
