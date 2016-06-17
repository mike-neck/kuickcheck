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
package org.mikeneck.example

import org.mikeneck.kuickcheck.Property
import org.mikeneck.kuickcheck.forAll
import org.mikeneck.kuickcheck.int
import org.mikeneck.kuickcheck.string

object CustomGenerator {

    @Property
    val personWithLessThan20YearsOldIsChild =
            forAll { Person(name = string(20)(), age = int(0, 19)()) }.satisfy { it.isChild() }

    val personOver20 = {
        val name = string(size = 20)
        val age = int(20, 120)
        Person(name(), age())
    }

    @Property
    val personOver20IsNotChild = forAll(personOver20).satisfy { !it.isChild() }
}

class Person(val name: String, val age: Int) {
    fun isChild(): Boolean = age < 20
}
