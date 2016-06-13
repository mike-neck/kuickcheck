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
import org.mikeneck.kuickcheck.long

class InClass {

    @Property
    val allMultiplesOf4AreMultiplesOf2 = forAll(int).filter { it % 4 == 0 }.satisfy { it % 2 == 0 }

    @Property
    val negativeValueHasSameAbsoluteValue = forAll(long).satisfy { Math.abs(it) == Math.abs(-it) }

    @Property
    val `naturalNumberIsMoreThan0(fail)` = forAll(int(0, 30)).satisfy { it > 0 }

    @Property
    val throwsExceptionIf30Comes = forAll(long(20L, 40L)).satisfy { if (it == 30L) throw  RuntimeException("$it comes.") else true}
}
