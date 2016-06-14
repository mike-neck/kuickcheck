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
package org.mikeneck.kuickcheck.runner

import org.junit.Test
import org.junit.experimental.theories.DataPoints
import org.junit.experimental.theories.Theories
import org.junit.experimental.theories.Theory
import org.junit.runner.RunWith
import org.mikeneck.kuickcheck.InputAndExpect

class ClassFileTest {

    @Test
    fun onlyNumberIsClosure() {
        assert(ClassFile.verifyNotClosure("1") == false)
        assert(ClassFile.verifyNotClosure("2") == false)
    }

    @Test
    fun onlyStringIsNotClosure() {
        assert(ClassFile.verifyNotClosure("GettingStarted") == true)
    }
}

@RunWith(Theories::class)
class IsNotClosure {

    companion object {
        @DataPoints
        @JvmField val entries: Array<InputAndExpect<String, Boolean>> = arrayOf(
                InputAndExpect("com/sample/compile/Bar${'$'}DefaultImpls.class", true),
                InputAndExpect("com/sample/compile/Bar.class", true),
                InputAndExpect("com/sample/compile/Color${'$'}BLACK.class", true),
                InputAndExpect("com/sample/compile/Color${'$'}BLUE.class", true),
                InputAndExpect("com/sample/compile/Color${'$'}WHITE.class", true),
                InputAndExpect("com/sample/compile/Color.class", true),
                InputAndExpect("com/sample/compile/Compile_sampleKt.class", true),
                InputAndExpect("com/sample/compile/Foo${'$'}Companion${'$'}WhenMappings.class", true),
                InputAndExpect("com/sample/compile/Foo${'$'}Companion.class", true),
                InputAndExpect("com/sample/compile/Foo${'$'}upper${'$'}1.class", false),
                InputAndExpect("com/sample/compile/Foo.class", true)
        )
    }

    @Theory fun test(ie: InputAndExpect<String, Boolean>) {
        val classFile = ZipClassFile(ie.input)
        assert(classFile.isNotClosure() == ie.expect)
    }
}
