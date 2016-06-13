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

class CheckExecutor(val packageName: List<String>, val className: String, val propertyName: String, val checker: Checker<Any>) {

    fun run(): TestResult {
        val watch = StopWatch()
        val time = 1.rangeTo(checker.repeat)
        for (t in time) {
            val input = checker.testData()
            try {
                val result = checker.consume(input)
                if (result == false)
                    return Failure(packageName, className, propertyName, input, watch.stop(), t)
            } catch(e: Throwable) {
                return Error(packageName, className, propertyName, e, watch.stop(), t)
            }
        }
        return Success(packageName, className, propertyName, watch.stop(), checker.repeat)
    }
}

class StopWatch {
    val start = System.nanoTime()
    fun stop(): Long {
        val current = System.nanoTime()
        return (current - start)/ 1000000
    }
}
