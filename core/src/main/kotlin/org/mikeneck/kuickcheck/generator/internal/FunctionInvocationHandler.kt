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

import org.mikeneck.kuickcheck.Generator
import java.lang.invoke.MethodHandles
import java.lang.reflect.*
import java.util.*
import kotlin.reflect.KClass

internal class FunctionInvocationHandler<F : Function<R>, R>(
        val functionType: KClass<F>, val resultGenerator: Generator<R>) : InvocationHandler {

    val resultMap: MutableMap<Int, R> = mutableMapOf()

    lateinit var recentParamAndResult: Pair<Array<out Any>, R>

    override fun invoke(proxy: Any?, method: Method?, args: Array<out Any>?): Any? {
        if (proxy == null || method == null) return null
        if (method.declaringClass == Any::class.java) return invokeAnyMethod(proxy, method, args)
        if (java8OrLater() && method.isDefault) return invokeDefaultMethod(proxy, method, args)
        return invokeFunction(args)
    }

    fun invokeAnyMethod(proxy: Any, method: Method, args: Array<out Any>?): Any? {
        return when (method.name) {
            "equals" -> if (args == null || args.size == 0) false else proxy == args[0]
            "hashCode" -> System.identityHashCode(proxy)
            else -> "generated instance of [${functionType.qualifiedName}]"
        }
    }

    fun invokeDefaultMethod(proxy: Any, method: Method, args: Array<out Any>?): Any? {
        val lookup = ctor.newInstance(method.declaringClass, MethodHandles.Lookup.PRIVATE)
        return lookup.unreflectSpecial(method, method.declaringClass)
                .bindTo(proxy)
                .invokeWithArguments(args?.toList() ?: emptyList<Any>())
    }

    fun invokeFunction(args: Array<out Any>?): Any {
        val params = Arrays.hashCode(args)
        return if (resultMap.contains(params)) {
            val result = resultMap[params]
            recentParamAndResult = Pair(args ?: emptyArray<Any>(), result as R)
            result as Any
        } else {
            val result = resultGenerator()
            resultMap[params] = result
            recentParamAndResult = Pair(args ?: emptyArray<Any>(), result)
            result as Any
        }
    }

    companion object {
        fun java8OrLater(): Boolean {
            val javaVersion = System.getProperty("java.version").substring(0..2)
            return when (javaVersion) {
                "1.9" -> true
                "1.8" -> true
                else -> false
            }
        }

        val ctor = findConstructor(MethodHandles.Lookup::class.java, Class::class.java, Int::class.java)

        fun <T> findConstructor(type: Class<T>, vararg paramTypes: Class<*>): Constructor<T> {
            val constructor = type.getDeclaredConstructor(*paramTypes)
            constructor.isAccessible = true
            return constructor
        }

        fun <F : Function<R>, R> createFunction(functionType: KClass<F>, resultGenerator: Generator<R>): F {
            val javaClass = functionType.java
            if (JavaMethod.isFunctionWithSingleAbstractMethod(functionType) == false)
                throw IllegalArgumentException("Given KClass is not interface type or " +
                        "is not single function interface. [$functionType]")
            @Suppress("UNCHECKED_CAST")
            return Proxy.newProxyInstance(
                    javaClass.classLoader,
                    arrayOf(javaClass),
                    FunctionInvocationHandler(functionType, resultGenerator)) as F
        }
    }
}

internal class JavaMethod(
        val name: String,
        val params: Array<Class<*>>,
        val firstParamType: Class<*>?,
        @Suppress("UNUSED")
        val method: Method? = null,
        val modifier: Int? = null) {

    constructor(method: Method) : this(
            method.name,
            method.parameterTypes,
            if (method.parameterTypes.size == 0) null else method.parameterTypes[0],
            method,
            method.modifiers)

    fun isAbstract(): Boolean = if (modifier != null) Modifier.isAbstract(modifier) else false

    fun declaredInAny(): Boolean = this in arrayOf(toString, hashCode, equals)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is JavaMethod) return false

        if (name != other.name) return false
        if (!Arrays.equals(params, other.params)) return false
        if (firstParamType != other.firstParamType) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + Arrays.hashCode(params)
        result = 31 * result + (firstParamType?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        val ps = params.map { it.simpleName }.joinToString(",", "[", "]")
        return "method[$name, param:$ps]"
    }

    companion object {
        val toString: JavaMethod = JavaMethod("toString", arrayOf<Class<*>>(), null)
        val hashCode: JavaMethod = JavaMethod("hashCode", arrayOf<Class<*>>(), null)
        val equals: JavaMethod = JavaMethod("equals", arrayOf(Any::class.java), Any::class.java)

        fun isFunctionWithSingleAbstractMethod(klass: KClass<*>): Boolean {
            val jcl = klass.java
            if (!jcl.isInterface) return false
            val methods = jcl.methods.map(::JavaMethod)
                    .filter(JavaMethod::isAbstract)
                    .filter { !it.declaredInAny() }
            return if (methods.size == 1) true
            else false
        }
    }
}
