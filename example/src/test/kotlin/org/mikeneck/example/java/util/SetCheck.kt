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
package org.mikeneck.example.java.util

import org.mikeneck.kuickcheck.Property
import org.mikeneck.kuickcheck.forAll
import org.mikeneck.kuickcheck.int
import org.mikeneck.kuickcheck.set

object SetCheck {

    @Property
    val elementIsUniqueInSet = forAll(set(int(0, 40)).size(15))
            .satisfy { s -> s.map { i -> s.filter { it == i } }.all { it.size == 1 } }
}
