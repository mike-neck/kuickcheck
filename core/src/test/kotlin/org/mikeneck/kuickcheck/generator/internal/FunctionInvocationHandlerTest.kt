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
package org.mikeneck.kuickcheck.generator.internal

import org.junit.Test
import org.junit.experimental.theories.DataPoints
import org.junit.experimental.theories.Theories
import org.junit.experimental.theories.Theory
import org.junit.runner.RunWith
import org.mikeneck.kuickcheck.Generator
import java.lang.reflect.Method
import java.util.*
import kotlin.reflect.KClass

class JavaMethodTest {

    @Test fun comparableTest() {
        val method = Comparable::class.java.getMethod("compareTo", Any::class.java)
        val javaMethod = JavaMethod(method)
        assert(javaMethod.isAbstract())
    }

    @Test fun implMethod() {
        val method = JavaMethodTest::class.java.getMethod("implMethod")
        val javaMethod = JavaMethod(method)
        assert(javaMethod.isAbstract() == false)
    }
}

@RunWith(Theories::class)
internal class JavaMethodEqualityTest {

    companion object {
        @DataPoints @JvmField val testData = listOf(
                Pair(JavaMethod("toString", arrayOf<Class<*>>(), null),
                        JavaMethodTest::class.java.getMethod("toString")),
                Pair(JavaMethod("equals", arrayOf(Any::class.java), Any::class.java),
                        JavaMethodTest::class.java.getMethod("equals", Any::class.java)),
                Pair(JavaMethod("hashCode", arrayOf<Class<*>>(), null),
                        JavaMethodTest::class.java.getMethod("hashCode"))
        )
    }

    @Theory fun test(data: Pair<JavaMethod, Method>) {
        val method = JavaMethod(data.second)
        assert(method == data.first)
    }

    @Theory fun declaredInAny(data: Pair<JavaMethod, Method>) {
        val method = JavaMethod(data.second)
        assert(method.declaredInAny())
    }
}

private interface Generated<out T> {
    fun generated(): T
}

class FunctionInvocationHandlerTest {

    private val intGenerator = object : Generator<Int>, Generated<Int> {
        var gen: Int = 0
        val random = Random()

        override fun generated(): Int = gen

        override fun invoke(): Int {
            gen = random.nextInt(200)
            return gen
        }
    }

    private val function1 = { s: String -> s.length }

    private val handler: FunctionInvocationHandler<Function1<String, Int>, Int> =
            FunctionInvocationHandler(function1.javaClass.kotlin, intGenerator)

    @Test fun createInstance() {
        assert(handler.hashCode() != 0)
    }

    @Test fun `invokeEquals - false`() {
        val equals = Any::class.java.getMethod("equals", Any::class.java)
        val any = Any()
        val proxy = Any()
        val result = handler(proxy, equals, arrayOf(any)) as Boolean
        assert(result == false)
    }

    @Test fun `invokeEquals - true`() {
        val equals = Any::class.java.getMethod("equals", Any::class.java)
        val proxy = Any()
        val result = handler(proxy, equals, arrayOf(proxy)) as Boolean
        assert(result)
    }

    @Test fun invokeHashCode() {
        val hashCode = Any::class.java.getMethod("hashCode")
        val proxy = Any()
        val result = handler(proxy, hashCode, emptyArray())
        assert(result is Int)
        assert(result == System.identityHashCode(proxy))
    }

    @Test fun invokeToString() {
        val toString = Any::class.java.getMethod("toString")
        val proxy = Any()
        val result = handler(proxy, toString, emptyArray())
        assert(result is String)
        assert((result as String).startsWith("generated"))
    }

    @Test fun invokeFunction() {
        val invoke = function1.javaClass.getMethod("invoke", String::class.java)
        val proxy = Any()
        val result = handler(proxy, invoke, arrayOf("test arg"))
        assert(result is Int)
        assert((result as Int) == intGenerator.generated())
    }
}

class FunctionUtilTest {

    fun intGenerator(): Generator<Int> {
        return object : Generator<Int> {
            val itor = (1..10000000).iterator()
            override fun invoke(): Int = itor.nextInt()
        }
    }

    @Test fun functionType() {
        val f = { s: String, l: Long?, p: (String, Long) -> Boolean -> p(s, l ?: 0) }
        println(f.javaClass)
        println(f.javaClass.interfaces.map { it })
        println(f.javaClass.superclass)
        println(f.javaClass.kotlin)
    }

    @Test fun functionTypeOf() {
        val f = function({ s: String -> }, intGenerator())
        assert(f is Function<Int>)
    }

    @Test fun createFunction() {
        val klass = ({ s: String -> s.length }).javaClass.kotlin
        @Suppress("UNCHECKED_CAST")
        val fClass = klass.java.interfaces.filter { it in functions }.first()?.kotlin as KClass<Function1<String, Int>>

        val generator = intGenerator()
        val f = FunctionInvocationHandler.createFunction(fClass, generator)

        assert(f("tooo") == 1)
    }

    companion object {

        val functions = listOf(
                Function::class.java, Function0::class.java, Function1::class.java, Function2::class.java, Function3::class.java
        )

        @Suppress("UNUSED")
        fun <P1, P2, R> function(@Suppress("UNUSED_PARAMETER") f: (P1, P2) -> Unit, g: Generator<R>): (P1, P2) -> R {
            return { p1, p2 -> g() }
        }

        fun <P1, R> function(@Suppress("UNUSED_PARAMETER") f: (P1) -> Unit, g: Generator<R>): (P1) -> R {
            return { p1 -> g() }
        }
    }
}
