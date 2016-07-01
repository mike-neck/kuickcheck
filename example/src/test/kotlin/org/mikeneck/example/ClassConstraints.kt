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

class ClassConstraints {

    val instance: Interface = object : Interface {

        @Property
        override val `a property in interface cannot be evaluated`: Checker<String>
            get() = forAll(string(10)).satisfy { it.length <= 10 }
    }

    companion object {
        @Property
        val `this property can be evaluated` = forAll(string).satisfy { it.length >= 0 }
    }
}

@Property
val `this property cannot be evaluated due to Kotlin reflection limit` =
        forAll(positiveInt).satisfy { it > 0 }

interface Interface {
    @Property
    val `a property in interface cannot be evaluated`: Checker<String>
}
