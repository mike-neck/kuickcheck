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

import java.security.SecureRandom
import java.util.*

interface KcGen {
    fun nextBoolean(): Boolean
    fun nextInt(): Int
    fun nextInt(bound: Int): Int
    fun nextLong(): Long
    fun nextDouble(): Double
    fun nextBytes(size: Int): ByteArray
}

internal class JavaUtilRandom(seed: Long? = null) : KcGen {

    val random: Random = seed?.let(::Random) ?: Random()

    override fun nextBoolean(): Boolean = random.nextBoolean()

    override fun nextInt(): Int = random.nextInt()

    override fun nextInt(bound: Int): Int = random.nextInt(bound)

    override fun nextLong(): Long = random.nextLong()

    override fun nextDouble(): Double = random.nextDouble()

    override fun nextBytes(size: Int): ByteArray = ByteArray(size).apply(random::nextBytes)
}

internal class JavaSecureRandom(seed: Long? = null) : KcGen {

    val random: SecureRandom = seed?.let { s -> SecureRandom().also { it.setSeed(s) } } ?: SecureRandom()

    override fun nextBoolean(): Boolean = random.nextBoolean()

    override fun nextInt(): Int = random.nextInt()

    override fun nextInt(bound: Int): Int = random.nextInt(bound)

    override fun nextLong(): Long = random.nextLong()

    override fun nextDouble(): Double = random.nextDouble()

    override fun nextBytes(size: Int): ByteArray = ByteArray(size).apply(random::nextBytes)
}
