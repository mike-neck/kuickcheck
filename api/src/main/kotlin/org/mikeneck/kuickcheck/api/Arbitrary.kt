/*
 * Copyright 2017 Shinya Mochida
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
package org.mikeneck.kuickcheck.api

/**
 * Randomly generates values for tests and shrinks a value causing a test failure.
 *
 * @param A The type of value to be generated.
 *
 * @since 1.0
 */
interface Arbitrary<A> {

    /**
     * Generates values for given type.
     *
     * @since 1.0
     */
    fun arbitrary(): Gen<A>

    /**
     * Shrinks a value causing a test failure.
     *
     * @since 1.0
     */
    fun shrink(original: A): List<A>
}
