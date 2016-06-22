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
import org.mikeneck.kuickcheck.Property
import kotlin.reflect.KCallable
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

object ClassScanner {

    var debug = false

    fun debug(): Unit {
        debug = true
    }

    fun prepareForCheck(): List<TargetProperty> {
        val classes = listClassPaths().map { classPath(it) }
                .flatMap { it.listClasses() }
        val enums: Set<String> = classes.filter { it is EnumClass }.map { it.name }.toSet()
        val notEnumChild = notEnumChild(enums)
        return classes.filter(scanTarget)
                .filter(notEnumChild)
                .flatMap { propertyScan(it) }
    }

    val scanTarget: (Scannable) -> Boolean = {
        when (it) {
            is SingletonClass -> true
            is NormalClass    -> true
            else              -> false
        }
    }

    private fun notEnumChild(set: Set<String>): (Scannable) -> Boolean {
        return {s ->
            if (s.klass == null) false
            else {
                val cls = s.klass as KClass<*>
                val spc = cls.java.superclass
                if (spc == null) true
                else set.contains(spc.canonicalName) == false
            }
        }
    }

    fun listClassPaths(): List<String> {
        val paths = System.getProperty("java.class.path").split(":")
        val javaHomes = Pair(System.getProperty("java.home"), System.getenv("JAVA_HOME"))
        return paths - paths.filter {
            it.startsWith(javaHomes.first) || it.startsWith(javaHomes.second)
        }
    }

    fun classPath(entry: String): ClassPath =
            if (entry.endsWith(".jar")) JarClassPath(entry)
            else FileClassPath(entry)

    fun propertyScan(scannable: Scannable): List<TargetProperty> {
        if (scannable.testable == false) return emptyList()
        val cls = scannable.klass as KClass<*>
        return cls.members.filter { it.isAnnotated(Property::class) }
                .filter { it.returnType.toString().contains(Checker::class.qualifiedName as String) }
                .map {
                    @Suppress("UNCHECKED_CAST")
                    TargetProperty(cls, scannable.objectInstance, it.name, it as KProperty<Checker<*>>)
                }
    }

    internal fun KCallable<*>.isAnnotated(klass: KClass<out Annotation>) =
            this.annotations.map { it.annotationClass }.contains(klass)
}
