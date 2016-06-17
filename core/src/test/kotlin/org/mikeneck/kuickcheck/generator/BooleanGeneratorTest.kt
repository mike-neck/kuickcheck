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
package org.mikeneck.kuickcheck.generator

import org.junit.Test

class BooleanGeneratorTest {

    @Test fun alwaysTrueBooleanGeneratorReturnsTrue() {
        val generator = BooleanGenerator(true)
        (1..100).forEach {
            assert(generator.invoke() == true)
        }
    }

    @Test fun alwaysFalseBooleanGeneratorReturnsFalse() {
        val generator = BooleanGenerator(false)
        (1..100).forEach {
            assert(generator.invoke() == false)
        }
    }

    @Test(timeout = 1000) fun booleanGeneratorReturnsTrueAndFalse() {
        val generator = BooleanGenerator()
        var t = true
        var f = true
        while (t || f) {
            when (generator.invoke()) {
                true -> t = false
                false -> f = false
            }
        }
        assert(!(t || f) == true)
    }
}
