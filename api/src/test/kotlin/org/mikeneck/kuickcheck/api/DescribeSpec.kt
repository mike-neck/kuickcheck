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

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.it
import org.mikeneck.kuickcheck.api.Functions.int

object DescribeSpec : Spek({

    group("function chain[prop -> forAll -> satisfy] generates Testable(SingleTest)") {

        it("has 0 when prop is not called.") {
            val desc = object : Describe("no prop call") {
                override val check: Testable = NoTests
            }
            assert(desc.check.size == 0)
        }

        it("has 1 when prop is called once.") {
            val desc = object : Describe("a prop call") {
                override val check: Testable = prop("test prop call") forAll { int } satisfy { it < 100 }
            }
            assert(desc.check.size == 1)
        }

        it("has many when multiple prop is called and concatenated.") {
            val desc = object : Describe("multiple prop call") {
                override val check: Testable get() =
                (prop("first") forAll { int } satisfy { it < 100 }) +
                        (prop("second") forAll { int } satisfy { it > 0 }) +
                        (prop("third") forAll { int } satisfy { it < 0 })
            }
            assert(desc.check.size == 3)
        }

        it("has 2 when 3 prop is called and then 1 declare ignore.") {
            val desc = object : Describe("multiple prop call") {
                override val check: Testable get() =
                (prop("first") forAll { int } satisfy { it < 100 }) +
                        (prop("second") forAll { int } satisfy { it > 0 }) +
                        (prop("ignore") forAll { int } satisfy { it < 0 } ignore "this is fake.")

            }
            assert(desc.check.size == 3)
            assert(desc.check.runnableCases().size == 2)
        }
    }
})
