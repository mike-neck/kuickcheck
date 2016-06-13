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

interface TestResult{
    val packageName: List<String>
    val className: String
    val propertyName: String
    val milliSeconds: Long
    val repeat: Int

    fun joinPackage(): String = packageName.joinToString(".")
    fun consoleWriteWithIndex(color: Color, index: Int) {
        val text = "$index ${asString()}"
        color.writeln(text)
    }
    fun consoleWrite(color: Color): Unit {
        color.writeln(asString())
    }
    fun asString(): String
}

data class Success(
        override val packageName: List<String>,
        override val className: String,
        override val propertyName: String,
        override val milliSeconds: Long,
        override val repeat: Int
): TestResult {
    override fun asString(): String {
        return "${joinPackage()}.$className - $propertyName SUCCESS in [$milliSeconds ms/$repeat times]"
    }
}

data class Failure<T>(
        override val packageName: List<String>,
        override val className: String,
        override val propertyName: String,
        val input: T,
        override val milliSeconds: Long,
        override val repeat: Int
): TestResult {
    override fun asString(): String {
        return """
       |${joinPackage()}.$className - $propertyName FAILURE in [$milliSeconds ms/$repeat times]
       |    The data [ $input ] makes the prediction fail.""".trimMargin()
    }
}

data class Error(
        override val packageName: List<String>,
        override val className: String,
        override val propertyName: String,
        val cause: Throwable,
        override val milliSeconds: Long,
        override val repeat: Int
): TestResult {
    override fun asString(): String {
        val c = cause.stackTrace.map {
            "        ${it.className}.${it.methodName} - ${it.fileName}[${it.lineNumber}]"
        }.joinToString("\n")
        return """
       |${joinPackage()}.$className - $propertyName ERROR in [$milliSeconds ms/$repeat times]
       |    Error occurred...
       |        ${cause.javaClass.kotlin.qualifiedName} - ${cause.message}
       |$c""".trimMargin()
    }
}
