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

import org.junit.Test

class ClassScannerTest {

    @Test
    fun scanDefaultImpl() {
        val klass = Class.forName("org.mikeneck.kuickcheck.Checker${'$'}DefaultImpls")
        val kt = klass?.kotlin
        println(klass)
        println(kt)
        val obj = kt?.objectInstance
        println(obj)
    }
}
