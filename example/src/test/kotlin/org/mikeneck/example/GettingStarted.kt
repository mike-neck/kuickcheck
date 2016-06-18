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

import org.mikeneck.kuickcheck.*

object GettingStarted {

    @Property
    val positiveValueIsLargerThan0 = forAll(positiveInt).satisfy { it > 0 }

    @Property
    val negativeValueIsSmallerThan0With200Times = forAll(negativeInt).satisfy { it < 0 } * 200

    @Property
    val valueCanBeDevidedBy10IsMultipleOf5 =
            forAll(positiveLongFrom0).filter { it % 10 == 0.toLong() }.satisfy { it % 5 == 0.toLong() }

    @Property
    val `positiveInt x negativeInt becomes negative` =
            forAll(positiveInt, negativeInt).satisfy { l, r -> l * r < 0 }

    @Property
    val twiceReversedListEqualsToOriginal =
            forAll (list(int)).satisfy { it.reversed().reversed() == it }
}
