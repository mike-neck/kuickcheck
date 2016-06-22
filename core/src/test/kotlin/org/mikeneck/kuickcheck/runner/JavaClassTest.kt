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
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith

@RunWith(Enclosed::class)
class JavaClassTest {

    class NotFoundJavaClassTest {

        val jClass = JavaClass("org.mikeneck.kuickcheck.Foo")

        @Test fun isNotFoundReturnsTrue() {
            assert(jClass.isNotFound())
        }

        @Test fun isEnumReturnsTrue() {
            assert(jClass.isEnum() == false)
        }

        @Test fun isInterfaceReturnsFalse() {
            assert(jClass.isInterface() == false)
        }

        @Test fun isReflectionProhibitedReturnsFalse() {
            assert(jClass.isReflectionProhibited() == false)
        }

        @Test fun isThrowableReturnsFalse() {
            assert(jClass.isThrowable() == false)
        }

        @Test fun isObjectReturnsFalse() {
            assert(jClass.isObject() == false)
        }

        @Test fun isExcludedClassReturnsFalse() {
            assert(jClass.isExcludedClass() == false)
        }

        @Test fun mapToScannableReturnsNotFoundClass() {
            val scannable = jClass.mapToScannable()
            assert(scannable is NotFoundClass)
        }
    }

    class DefaultImplsClassTest {

        val jClass: JavaClass
            get() {
                val name = "org.mikeneck.kuickcheck.runner.TestResult${'$'}DefaultImpls"
                val klass = Class.forName(name)
                return JavaClass(name, klass)
            }

        @Test fun isNotFoundReturnsFalse() {
            assert(jClass.isNotFound() == false)
        }

        @Test fun isEnumReturnsFalse() {
            assert(jClass.isEnum() == false)
        }

        @Test fun isInterfaceReturnsFalse() {
            assert(jClass.isInterface() == false)
        }

        @Test fun isReflectionProhibitedReturnsTrue() {
            assert(jClass.isReflectionProhibited() == true)
        }

        @Test fun isThrowableReturnsFalse() {
            assert(jClass.isThrowable() == false)
        }

        @Test fun isExcludeClassReturnsTrue() {
            assert(jClass.isExcludedClass() == true)
        }

        @Test fun mapToScannableReturnsExcludedClass() {
            val scannable = jClass.mapToScannable()
            assert(scannable is ExcludedClass)
        }
    }

    class WhenMappingsClassTest {

        val jClass: JavaClass
            get() {
                val name = "org.mikeneck.kuickcheck.runner.TestSummary${'$'}WhenMappings"
                val klass = Class.forName(name)
                return JavaClass(name, klass)
            }

        @Test fun isNotFoundReturnsFalse() {
            assert(jClass.isNotFound() == false)
        }

        @Test fun isInterfaceReturnsFalse() {
            assert(jClass.isInterface() == false)
        }

        @Test fun isEnumReturnsFalse() {
            assert(jClass.isEnum() == false)
        }

        @Test fun isReflectionProhibitedReturnsTrue() {
            assert(jClass.isReflectionProhibited() == true)
        }

        @Test fun isThrowableReturnsFalse() {
            assert(jClass.isThrowable() == false)
        }

        @Test(expected = UnsupportedOperationException::class)
        fun isObjectReturnsFalse() {
            jClass.isObject()
        }

        @Test fun isExcludedClassReturnsTrue() {
            assert(jClass.isExcludedClass() == true)
        }

        @Test fun mapToScannableReturnsExcludedClass() {
            assert(jClass.mapToScannable() is ExcludedClass)
        }
    }

    class InterfaceClassTest {

        val jClass: JavaClass
            get() {
                val name = "org.mikeneck.kuickcheck.Checker"
                val klass = Class.forName(name)
                return JavaClass(name, klass)
            }

        @Test fun isNotFoundReturnsFalse() {
            assert(jClass.isNotFound() == false)
        }

        @Test fun isInterfaceReturnsTrue() {
            assert(jClass.isInterface() == true)
        }

        @Test fun isEnumReturnsFalse() {
            assert(jClass.isEnum() == false)
        }

        @Test fun isReflectionProhibitedReturnsFalse() {
            assert(jClass.isReflectionProhibited() == false)
        }

        @Test(expected = IllegalStateException::class)
        fun isThrowableReturnsFalse() {
            jClass.isThrowable()
        }

        @Test fun isObjectReturnsFalse() {
            assert(jClass.isObject() == false)
        }

        @Test fun isExcludedClassReturnsFalse() {
            assert(jClass.isExcludedClass() == false)
        }

        @Test fun mapToScannableReturnsInterfaceClass() {
            assert(jClass.mapToScannable() is InterfaceClass)
        }
    }

    class EnumClassTest {

        val jClass: JavaClass
            get() {
                val name = "org.mikeneck.kuickcheck.runner.Color"
                val klass = Class.forName(name)
                return JavaClass(name, klass)
            }

        @Test fun isNotFoundReturnsFalse() {
            assert(jClass.isNotFound() == false)
        }

        @Test fun isInterfaceReturnsFalse() {
            assert(jClass.isInterface() == false)
        }

        @Test fun isEnumReturnsTrue() {
            assert(jClass.isEnum() == true)
        }

        @Test fun isReflectionProhibitedReturnsFalse() {
            assert(jClass.isReflectionProhibited() == false)
        }

        @Test fun isThrowableReturnsFalse() {
            assert(jClass.isThrowable() == false)
        }

        @Test fun isObjectReturnsFalse() {
            assert(jClass.isObject() == false)
        }

        @Test fun isExcludedClassReturnsFalse() {
            assert(jClass.isExcludedClass() == false)
        }

        @Test fun mapToScannableReturnsEnumClass() {
            assert(jClass.mapToScannable() is EnumClass)
        }
    }

    class EnumMemberClass {

        val jClass: JavaClass
            get() {
                val name = "org.mikeneck.kuickcheck.runner.Color${'$'}PINK"
                val klass = Class.forName(name)
                return JavaClass(name, klass)
            }

        @Test fun isNotFoundReturnsFalse() {
            assert(jClass.isNotFound() == false)
        }

        @Test fun isInterfaceReturnsFalse() {
            assert(jClass.isInterface() == false)
        }

        @Test fun isEnumReturnsFalse() {
            assert(jClass.isEnum() == false)
        }

        @Test fun isReflectionProhibitedReturnsFalse() {
            assert(jClass.isReflectionProhibited() == false)
        }

        @Test fun isThrowableReturnsFalse() {
            assert(jClass.isThrowable() == false)
        }

        @Test fun isObjectReturnsFalse() {
            assert(jClass.isObject() == false)
        }

        @Test fun isExcludedClassReturnsFalse() {
            assert(jClass.isExcludedClass() == false)
        }

        @Test fun mapToScannableReturnsNormalClass() {
            assert(jClass.mapToScannable() is NormalClass)
        }
    }

    class ObjectClass {

        val jClass: JavaClass
            get() {
                val name = "org.mikeneck.kuickcheck.KuickCheck"
                val klass = Class.forName(name)
                return JavaClass(name, klass)
            }

        @Test fun isNotFoundReturnsFalse() {
            assert(jClass.isNotFound() == false)
        }

        @Test fun isInterfaceReturnsFalse() {
            assert(jClass.isInterface() == false)
        }

        @Test fun isEnumReturnsFalse() {
            assert(jClass.isEnum() == false)
        }

        @Test fun isReflectionProhibitedReturnsFalse() {
            assert(jClass.isReflectionProhibited() == false)
        }

        @Test fun isThrowableReturnsFalse() {
            assert(jClass.isThrowable() == false)
        }

        @Test fun isObjectReturnsTrue() {
            assert(jClass.isObject() == true)
        }

        @Test fun isExcludedClassReturnsFalse() {
            assert(jClass.isExcludedClass() == false)
        }

        @Test fun mapToScannableReturnsSingletonClass() {
            assert(jClass.mapToScannable() is SingletonClass)
        }
    }

    class AnnotationClassTest {

        val jClass: JavaClass
            get() {
                val name = "org.mikeneck.kuickcheck.Property"
                val klass = Class.forName(name)
                return JavaClass(name, klass)
            }

        @Test fun isNotFoundReturnsFalse() {
            assert(jClass.isNotFound() == false)
        }

        @Test fun isInterfaceReturnsTrue() {
            assert(jClass.isInterface() == true)
        }

        @Test fun isEnumReturnsFalse() {
            assert(jClass.isEnum() == false)
        }

        @Test fun isReflectionProhibitedReturnsFalse() {
            assert(jClass.isReflectionProhibited() == false)
        }

        @Test(expected = IllegalStateException::class)
        fun isThrowableReturnsFalse() {
            jClass.isThrowable()
        }

        @Test fun isObjectReturnsFalse() {
            assert(jClass.isObject() == false)
        }

        @Test fun isExcludedClassReturnsFalse() {
            assert(jClass.isExcludedClass() == false)
        }

        @Test fun mapToScannableReturnsInterfaceClass() {
            assert(jClass.mapToScannable() is InterfaceClass)
        }
    }

    class ExceptionClassTest {

        val jClass: JavaClass
            get() {
                val name = "org.mikeneck.kuickcheck.runner.JavaClassException"
                val klass = Class.forName(name)
                return JavaClass(name, klass)
            }

        @Test fun isNotFoundReturnsFalse() {
            assert(jClass.isNotFound() == false)
        }

        @Test fun isInterfaceReturnsFalse() {
            assert(jClass.isInterface() == false)
        }

        @Test fun isEnumReturnsFalse() {
            assert(jClass.isEnum() == false)
        }

        @Test fun isReflectionProhibitedReturnsFalse() {
            assert(jClass.isReflectionProhibited() == false)
        }

        @Test fun isThrowableReturnsTrue() {
            assert(jClass.isThrowable() == true)
        }

        @Test fun isObjectReturnsFalse() {
            assert(jClass.isObject() == false)
        }

        @Test fun isExcludedClassReturnsFalse() {
            assert(jClass.isExcludedClass() == false)
        }

        @Test fun mapToScannableReturnsThrowableClass() {
            assert(jClass.mapToScannable() is ThrowableClass)
        }
    }

    class ApiKtTest {

        val jClass: JavaClass
            get() {
                val name = "org.mikeneck.kuickcheck.APIKt"
                val klass = Class.forName(name)
                return JavaClass(name, klass)
            }

        @Test fun isNotFoundReturnsFalse() {
            assert(jClass.isNotFound() == false)
        }

        @Test fun isInterfaceReturnsFalse() {
            assert(jClass.isInterface() == false)
        }

        @Test fun isEnumReturnsFalse() {
            assert(jClass.isEnum() == false)
        }

        @Test fun isReflectionProhibitedReturnsTrue() {
            assert(jClass.isReflectionProhibited() == true)
        }

        @Test fun isThrowableReturnsTrue() {
            assert(jClass.isThrowable() == false)
        }

        @Test(expected = UnsupportedOperationException::class)
        fun isObjectReturnsFalse() {
            assert(jClass.isObject() == false)
        }

        @Test fun isExcludedClassReturnsFalse() {
            assert(jClass.isExcludedClass() == false)
        }

        @Test fun mapToScannableReturnsThrowableClass() {
            assert(jClass.mapToScannable() is KtClass)
        }
    }
}

class JavaClassException : RuntimeException()
