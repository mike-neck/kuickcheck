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

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.it
import org.mikeneck.kuickcheck.block
import org.mikeneck.kuickcheck.fail

object NotFoundJavaClassSpek : Spek({

    val jClass = JavaClass("org.mikeneck.kuickcheck.Foo")

    it("isNotFound function returns true") {
        assert(jClass.isNotFound())
    }

    it("isEnum function returns true") {
        assert(jClass.isEnum() == false)
    }

    it("isInterface function returns false") {
        assert(jClass.isInterface() == false)
    }

    it("isClosure function returns false") {
        assert(jClass.isClosure() == false)
    }

    it("accessToConstructorProtected function returns false") {
        assert(jClass.accessToConstructorProtected() == false)
    }

    it("accessToMemberProtected function returns false") {
        assert(jClass.accessToMemberProtected() == false)
    }

    it("isThrowable function returns false") {
        assert(jClass.isThrowable() == false)
    }

    it("isObject function returns false") {
        assert(jClass.isObject() == false)
    }

    it("isExcludedClass function returns false") {
        assert(jClass.isExcludedClass() == false)
    }

    it("mapToScannable function returnsNotFoundClass") {
        val scannable = jClass.mapToScannable()
        assert(scannable is NotFoundClass)
    }
})

object DefaultImplsClassSpek : Spek({

    val jClass: JavaClass = block {
        val name = "org.mikeneck.kuickcheck.runner.TestResult${'$'}DefaultImpls"
        val klass = Class.forName(name)
        JavaClass(name, klass)
    }

    it("isNotFound function returns false") {
        assert(jClass.isNotFound() == false)
    }

    it("isEnum function returns false") {
        assert(jClass.isEnum() == false)
    }

    it("isInterface function returns false") {
        assert(jClass.isInterface() == false)
    }

    it("isClosure function returns false") {
        assert(jClass.isClosure() == false)
    }

    it("accessToConstructorProtected function returns true") {
        assert(jClass.accessToConstructorProtected() == true)
    }

    it("accessToMemberProtected function returns true") {
        assert(jClass.accessToMemberProtected() == true)
    }

    it("isThrowable function returns false") {
        assert(jClass.isThrowable() == false)
    }

    it("isExcludeClass function returns true") {
        assert(jClass.isExcludedClass() == true)
    }

    it("mapToScannable function returnsExcludedClass") {
        val scannable = jClass.mapToScannable()
        assert(scannable is ExcludedClass)
    }
})

object WhenMappingsClassSpek : Spek({

    val jClass: JavaClass = block {
        val name = "org.mikeneck.kuickcheck.runner.TestSummary${'$'}WhenMappings"
        val klass = Class.forName(name)
        JavaClass(name, klass)
    }

    it("isNotFound function returns false") {
        assert(jClass.isNotFound() == false)
    }

    it("isInterface function returns false") {
        assert(jClass.isInterface() == false)
    }

    it("isClosure function returns false") {
        assert(jClass.isClosure() == false)
    }

    it("isEnum function returns false") {
        assert(jClass.isEnum() == false)
    }

    it("accessToConstructorProtected function returns true") {
        assert(jClass.accessToConstructorProtected() == true)
    }

    it("accessToMemberProtected function returns true") {
        assert(jClass.accessToMemberProtected() == true)
    }

    it("isThrowable function returns false") {
        assert(jClass.isThrowable() == false)
    }

    it("isObject fails") {
        try {
            jClass.isObject()
            fail()
        } catch (e: UnsupportedOperationException) {
        }
    }

    it("isExcludedClass function returns true") {
        assert(jClass.isExcludedClass() == true)
    }

    it("mapToScannable function returnsExcludedClass") {
        assert(jClass.mapToScannable() is ExcludedClass)
    }
})

object InterfaceClassSpek : Spek({

    val jClass: JavaClass = block {
        val name = "org.mikeneck.kuickcheck.Checker"
        val klass = Class.forName(name)
        JavaClass(name, klass)
    }

    it("isNotFound function returns false") {
        assert(jClass.isNotFound() == false)
    }

    it("isInterface function returns true") {
        assert(jClass.isInterface() == true)
    }

    it("isClosure function returns false") {
        assert(jClass.isClosure() == false)
    }

    it("isEnum function returns false") {
        assert(jClass.isEnum() == false)
    }

    it("accessToConstructorProtected function returns false") {
        assert(jClass.accessToConstructorProtected() == false)
    }

    it("accessToMemberProtected function returns false") {
        assert(jClass.accessToMemberProtected() == false)
    }

    it("isThrowable function fails") {
        try {
            jClass.isThrowable()
            fail()
        } catch (e: IllegalStateException) {
        }
    }

    it("isObject function returns false") {
        assert(jClass.isObject() == false)
    }

    it("isExcludedClass function returns false") {
        assert(jClass.isExcludedClass() == false)
    }

    it("mapToScannable function returnsInterfaceClass") {
        assert(jClass.mapToScannable() is InterfaceClass)
    }
})

object EnumClassSpek : Spek({

    val jClass: JavaClass = block {
        val name = "org.mikeneck.kuickcheck.runner.Color"
        val klass = Class.forName(name)
        JavaClass(name, klass)
    }

    it("isNotFound function returns false") {
        assert(jClass.isNotFound() == false)
    }

    it("isInterface function returns false") {
        assert(jClass.isInterface() == false)
    }

    it("isClosure function returns false") {
        assert(jClass.isClosure() == false)
    }

    it("isEnum function returns true") {
        assert(jClass.isEnum() == true)
    }

    it("accessToConstructorProtected function returns false") {
        assert(jClass.accessToConstructorProtected() == false)
    }

    it("accessToMemberProtected function returns true") {
        assert(jClass.accessToMemberProtected() == true)
    }

    it("isThrowable function returns false") {
        assert(jClass.isThrowable() == false)
    }

    it("isObject function returns false") {
        assert(jClass.isObject() == false)
    }

    it("isExcludedClass function returns false") {
        assert(jClass.isExcludedClass() == false)
    }

    it("mapToScannable function returnsEnumClass") {
        assert(jClass.mapToScannable() is EnumClass)
    }
})

object EnumMemberClass : Spek({

    val jClass: JavaClass = block {
        val name = "org.mikeneck.kuickcheck.runner.Color${'$'}PINK"
        val klass = Class.forName(name)
        JavaClass(name, klass)
    }

    it("isNotFound function returns false") {
        assert(jClass.isNotFound() == false)
    }

    it("isInterface function returns false") {
        assert(jClass.isInterface() == false)
    }

    it("isClosure function returns false") {
        assert(jClass.isClosure() == false)
    }

    it("isEnum function returns false") {
        assert(jClass.isEnum() == false)
    }

    it("accessToConstructorProtected function returns true") {
        assert(jClass.accessToConstructorProtected() == true)
    }

    it("accessToMemberProtected function returns true") {
        assert(jClass.accessToMemberProtected() == true)
    }

    it("isThrowable function returns false") {
        assert(jClass.isThrowable() == false)
    }

    it("isObject function returns false") {
        assert(jClass.isObject() == false)
    }

    it("isExcludedClass function returns false") {
        assert(jClass.isExcludedClass() == false)
    }

    it("mapToScannable function returnsNormalClass") {
        assert(jClass.mapToScannable() is EnumMember)
    }
})

object ObjectClass : Spek({

    val jClass: JavaClass = block {
        val name = "org.mikeneck.kuickcheck.KuickCheck"
        val klass = Class.forName(name)
        JavaClass(name, klass)
    }

    it("isNotFound function returns false") {
        assert(jClass.isNotFound() == false)
    }

    it("isInterface function returns false") {
        assert(jClass.isInterface() == false)
    }

    it("isClosure function returns false") {
        assert(jClass.isClosure() == false)
    }

    it("isEnum function returns false") {
        assert(jClass.isEnum() == false)
    }

    it("accessToConstructorProtected function returns false") {
        assert(jClass.accessToConstructorProtected() == false)
    }

    it("accessToMemberProtected function returns false") {
        assert(jClass.accessToMemberProtected() == false)
    }

    it("isThrowable function returns false") {
        assert(jClass.isThrowable() == false)
    }

    it("isObject function returns true") {
        assert(jClass.isObject() == true)
    }

    it("isExcludedClass function returns false") {
        assert(jClass.isExcludedClass() == false)
    }

    it("mapToScannable function returnsSingletonClass") {
        assert(jClass.mapToScannable() is SingletonClass)
    }
})

object AnnotationClassSpek : Spek({

    val jClass: JavaClass = block {
        val name = "org.mikeneck.kuickcheck.Property"
        val klass = Class.forName(name)
        JavaClass(name, klass)
    }

    it("isNotFound function returns false") {
        assert(jClass.isNotFound() == false)
    }

    it("isInterface function returns true") {
        assert(jClass.isInterface() == true)
    }

    it("isClosure function returns false") {
        assert(jClass.isClosure() == false)
    }

    it("isEnum function returns false") {
        assert(jClass.isEnum() == false)
    }

    it("accessToConstructorProtected function returns false") {
        assert(jClass.accessToConstructorProtected() == false)
    }

    it("accessToMemberProtected function returns false") {
        assert(jClass.accessToMemberProtected() == false)
    }

    it("isThrowable function fails") {
        try {
            jClass.isThrowable()
            fail()
        } catch (e: IllegalStateException) {
        }
    }

    it("isObject function returns false") {
        assert(jClass.isObject() == false)
    }

    it("isExcludedClass function returns false") {
        assert(jClass.isExcludedClass() == false)
    }

    it("mapToScannable function returnsInterfaceClass") {
        assert(jClass.mapToScannable() is InterfaceClass)
    }
})

object ExceptionClassSpek : Spek({

    val jClass: JavaClass = block {
        val name = "org.mikeneck.kuickcheck.runner.JavaClassException"
        val klass = Class.forName(name)
        JavaClass(name, klass)
    }

    it("isNotFound function returns false") {
        assert(jClass.isNotFound() == false)
    }

    it("isInterface function returns false") {
        assert(jClass.isInterface() == false)
    }

    it("isClosure function returns false") {
        assert(jClass.isClosure() == false)
    }

    it("isEnum function returns false") {
        assert(jClass.isEnum() == false)
    }

    it("accessToConstructorProtected function returns false") {
        assert(jClass.accessToConstructorProtected() == false)
    }

    it("accessToMemberProtected function returns true") {
        assert(jClass.accessToMemberProtected() == true)
    }

    it("isThrowable function returns true") {
        assert(jClass.isThrowable() == true)
    }

    it("isObject function returns false") {
        assert(jClass.isObject() == false)
    }

    it("isExcludedClass function returns false") {
        assert(jClass.isExcludedClass() == false)
    }

    it("mapToScannable function returnsThrowableClass") {
        assert(jClass.mapToScannable() is ThrowableClass)
    }
})

object ApiKtSpek : Spek({

    val jClass: JavaClass = block {
        val name = "org.mikeneck.kuickcheck.APIKt"
        val klass = Class.forName(name)
        JavaClass(name, klass)
    }

    it("isNotFound function returns false") {
        assert(jClass.isNotFound() == false)
    }

    it("isInterface function returns false") {
        assert(jClass.isInterface() == false)
    }

    it("isClosure function returns false") {
        assert(jClass.isClosure() == false)
    }

    it("isEnum function returns false") {
        assert(jClass.isEnum() == false)
    }

    it("accessToConstructorProtected function returns true") {
        assert(jClass.accessToConstructorProtected() == true)
    }

    it("accessToMemberProtected function returns true") {
        assert(jClass.accessToMemberProtected() == true)
    }

    it("isThrowable function returns false") {
        assert(jClass.isThrowable() == false)
    }

    it("isObject function fails") {
        try {
            assert(jClass.isObject() == false)
            fail()
        } catch (e: UnsupportedOperationException) {
        }
    }

    it("isExcludedClass function returns false") {
        assert(jClass.isExcludedClass() == false)
    }

    it("mapToScannable function returnsThrowableClass") {
        assert(jClass.mapToScannable() is KtClass)
    }
})

object ClosureClassSpek : Spek({

    val jClass: JavaClass = block {
        val name = "org.mikeneck.kuickcheck.generator.AllStringGenerator${'$'}invoke${'$'}1"
        val klass = Class.forName(name)
        JavaClass(name, klass)
    }

    it("isNotFound function returns false") {
        assert(jClass.isNotFound() == false)
    }

    it("isInterface function returns false") {
        assert(jClass.isInterface() == false)
    }

    it("isClosure function returns true") {
        assert(jClass.isClosure() == true)
    }

    it("isEnum function returns false") {
        assert(jClass.isEnum() == false)
    }

    it("accessToConstructorProtected function returns true") {
        assert(jClass.accessToConstructorProtected() == true)
    }

    it("accessToMemberProtected function returns true") {
        assert(jClass.accessToMemberProtected() == true)
    }

    it("isThrowable function returns false") {
        assert(jClass.isThrowable() == false)
    }

    it("isObject returns false") {
        assert(jClass.isObject() == false)
    }

    it("isExcludedClass function returns false") {
        assert(jClass.isExcludedClass() == false)
    }

    it("mapToScannable function returnsThrowableClass") {
        assert(jClass.mapToScannable() is Closure)
    }
})

class JavaClassException : RuntimeException()
