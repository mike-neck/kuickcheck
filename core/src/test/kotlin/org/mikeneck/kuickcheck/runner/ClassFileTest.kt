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

import java.nio.file.Paths

class ClassFileTest {

    fun qualifiedClassName() {
        val file = RealClassFile(path, classFile)
        assert(file.toQualifiedClassName() == qualifiedClassName)
    }

    fun toJavaClass() {
        val file = RealClassFile(path, classFile)
        val javaClass = file.toJavaClass()
        assert(javaClass.isNotFound() == false)
    }

    companion object {
        val BASE = "core/build/classes/main"
        val classFile = "$BASE/org/mikeneck/kuickcheck/prediction/DoubleArgumentFilteredPrediction.class"
        val path = Paths.get(BASE)
        val qualifiedClassName = "org.mikeneck.kuickcheck.prediction.DoubleArgumentFilteredPrediction"
    }
}

class RealKtClassFileTest {

    fun qualifiedClassName() {
        val file = RealClassFile(path, classFile)
        assert(file.toQualifiedClassName() == qualifiedClassName)
    }

    fun toJavaClass() {
        val file = RealClassFile(path, classFile)
        val javaClass = file.toJavaClass()
        assert(javaClass.isNotFound() == false)
    }

    companion object {
        val BASE = "core/build/classes/main"
        val classFile = "$BASE/org/mikeneck/kuickcheck/prediction/FunctionalPredictionKt.class"
        val qualifiedClassName = "org.mikeneck.kuickcheck.prediction.FunctionalPredictionKt"
        val path = Paths.get(BASE)
    }
}

class ZipClassFileTest {

    fun qualifiedClassName() {
        val file = ZipClassFile(classEntry)
        assert(file.toQualifiedClassName() == qualifiedClass)
    }

    fun toJavaClass() {
        val file = ZipClassFile(classEntry)
        val javaClass = file.toJavaClass()
        assert(javaClass.isNotFound() == false)
    }

    companion object {
        val classEntry = "org/mikeneck/kuickcheck/prediction/DoubleArgumentFilteredPrediction.class"

        val qualifiedClass = "org.mikeneck.kuickcheck.prediction.DoubleArgumentFilteredPrediction"
    }
}
