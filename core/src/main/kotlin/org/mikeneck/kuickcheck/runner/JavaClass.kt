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

data class JavaClass(val name: String, val javaClass: Class<*>? = null) {

    fun isNotFound(): Boolean = javaClass == null

    fun isEnum(): Boolean = javaClass?.isEnum ?: false

    fun isInterface(): Boolean = javaClass?.isInterface ?: false

    fun isThrowable(): Boolean {
        var clazz = javaClass ?: Any::class.java
        while (true) {
            when (clazz) {
                Any::class.java -> return false
                RuntimeException::class.java -> return true
                Exception::class.java -> return true
                Throwable::class.java -> return true
            }
            clazz = clazz.superclass
        }
    }

    fun isExcludedClass(): Boolean = inExcludeClass(name)

    fun isObject(): Boolean {
        return if (javaClass != null) javaClass.kotlin.objectInstance != null
        else false
    }

    fun mapToScannable(): Scannable =
            if (isNotFound()) NotFoundClass(name)
            else if (isInterface()) InterfaceClass(name, javaClass!!.kotlin)
            else if (isExcludedClass()) ExcludedClass(name, javaClass!!.kotlin)
            else if (isEnum()) EnumClass(name, javaClass!!.kotlin)
            else if (isThrowable()) ThrowableClass(name, javaClass!!.kotlin)
            else if (isObject()) SingletonClass(name, javaClass!!.kotlin)
            else NormalClass(name, javaClass!!.kotlin)

    companion object {
        val EXCLUDED_NAMES = listOf(
                EndsWith("${'$'}DefaultImpls"),
                EndsWith("Kt"),
                EndsWith("${'$'}WhenMappings"))

        fun inExcludeClass(name: String): Boolean =
                EXCLUDED_NAMES.filter { it.match(name) }.size > 0
    }
}
