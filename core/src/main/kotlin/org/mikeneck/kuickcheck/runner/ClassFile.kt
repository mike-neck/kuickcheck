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

import java.nio.file.Path

interface ClassFile {
    fun isNotClosure(): Boolean
    fun toQualifiedClassName(): String
    fun toJavaClass(): JavaClass {
        val name = toQualifiedClassName()
        try {
            val javaClass = Class.forName(name)
            return JavaClass(name, javaClass)
        } catch (e: ClassNotFoundException) {
            return JavaClass(name)
        }
    }

    companion object {
        fun verifyNotClosure(name: String): Boolean {
            return 0.rangeTo(name.length - 1)
                    .map { name.codePointAt(it) }
                    .map(Int::toChar).filter(Char::isDigit).size != name.length
        }

        fun isNotClosure(file: String): Boolean {
            val className = file.removeSuffix(".class")
                    .replace("/", "$").split("$").last()
            return verifyNotClosure(className)
        }
    }
}

internal class RealClassFile(val parent: Path, val file: String): ClassFile {
    override fun isNotClosure(): Boolean = ClassFile.isNotClosure(file)

    override fun toQualifiedClassName(): String {
        return file.removeSuffix(".class")
                .removePrefix("$parent/").replace("/",".")
    }
}

internal class ZipClassFile(val file: String): ClassFile {
    override fun isNotClosure(): Boolean = ClassFile.isNotClosure(file)

    override fun toQualifiedClassName(): String {
        return file.removeSuffix(".class").replace("/", ".")
    }
}
