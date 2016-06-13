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

class TestSummary(val list: List<TestResult>) {

    val classified: Map<Result, List<TestResult>>

    private fun only(result: Result): (TestResult) -> Boolean {
        return {
            when (result) {
                Result.SUCCESS -> it is Success
                Result.FAILURE -> it is Failure<*>
                Result.ERROR   -> it is Error
            }
        }
    }

    fun collectTestResult(list: List<TestResult>, result: Result): Pair<Result, List<TestResult>> =
        Pair(result, list.filter(only(result)))

    init {
        classified = mapOf(
                collectTestResult(list, Result.SUCCESS),
                collectTestResult(list, Result.FAILURE),
                collectTestResult(list, Result.ERROR)
        )
    }

    val property: Int get() = list.size

    val success: Int get() = classified[Result.SUCCESS]?.size?: 0

    val failure: Int get() = classified[Result.FAILURE]?.size?: 0

    val error: Int get() = classified[Result.ERROR]?.size?: 0

    fun printResult() {
        list.forEachIndexed { index, result ->
            val color = when (result) {
                is Success    -> Color.GREEN
                is Failure<*> -> Color.RED
                is Error      -> Color.YELLOW
                else          -> Color.WHITE
            }
            result.consoleWriteWithIndex(color, index + 1)
        }
    }

    fun printSummary() {
        Colors.normal("Run $property checks:")
        Colors.green("Success[$success]")
        Colors.red("Failure[$failure]")
        Colors.yellowln("Error[$error]")
    }

    fun testSuccess(): Boolean = list.size == success
}

enum class Result {
    SUCCESS, FAILURE, ERROR
}

fun List<TestResult>.toSummary(): TestSummary = TestSummary(this)
