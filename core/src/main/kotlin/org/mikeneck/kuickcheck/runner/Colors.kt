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

object Colors {

    val ESC = 27.toChar()

    val BLACK = "[30m"

    val RED = "[31m"

    val GREEN = "[32m"

    val YELLOW = "[33m"

    val BLUE = "[34m"

    val PINK = "[35m"

    val CYAN = "[36m"

    val WHITE = "[37m"

    val CLEAR = "[0m"

    private fun writeln(text: String): Unit = println(text)
    private fun write(text: String): Unit = print(text)

    fun normalln(msg: String) = writeln(msg)
    fun normal(msg: String) = write(msg)
    fun blackln(msg: String) = writeln("$ESC$BLACK$msg$ESC$CLEAR ")
    fun black(msg: String) = write("$ESC$BLACK$msg$ESC$CLEAR ")
    fun redln(msg: String) = writeln("$ESC$RED$msg$ESC$CLEAR ")
    fun red(msg: String) = write("$ESC$RED$msg$ESC$CLEAR ")
    fun greenln(msg: String) = writeln("$ESC$GREEN$msg$ESC$CLEAR ")
    fun green(msg: String) = write("$ESC$GREEN$msg$ESC$CLEAR ")
    fun yellowln(msg: String) = writeln("$ESC$YELLOW$msg$ESC$CLEAR ")
    fun yellow(msg: String) = write("$ESC$YELLOW$msg$ESC$CLEAR ")
    fun blueln(msg: String) = writeln("$ESC$BLUE$msg$ESC$CLEAR ")
    fun blue(msg: String) = write("$ESC$BLUE$msg$ESC$CLEAR ")
    fun pinkln(msg: String) = writeln("$ESC$PINK$msg$ESC$CLEAR ")
    fun pink(msg: String) = write("$ESC$PINK$msg$ESC$CLEAR ")
    fun cyanln(msg: String) = writeln("$ESC$CYAN$msg$ESC$CLEAR ")
    fun cyan(msg: String) = write("$ESC$CYAN$msg$ESC$CLEAR ")
    fun whiteln(msg: String) = writeln("$ESC$WHITE$msg$ESC$CLEAR ")
    fun white(msg: String) = write("$ESC$WHITE$msg$ESC$CLEAR ")
}

enum class Color {
    BLACK {
        override fun writeln(msg: String) = Colors.blackln(msg)
        override fun write(msg: String) = Colors.black(msg)
    },
    RED {
        override fun writeln(msg: String) = Colors.redln(msg)
        override fun write(msg: String) = Colors.red(msg)
    },
    GREEN {
        override fun writeln(msg: String) = Colors.greenln(msg)
        override fun write(msg: String) = Colors.green(msg)
    },
    YELLOW {
        override fun writeln(msg: String) = Colors.yellowln(msg)
        override fun write(msg: String) = Colors.yellow(msg)
    },
    BLUE {
        override fun writeln(msg: String) = Colors.blueln(msg)
        override fun write(msg: String) = Colors.blue(msg)
    },
    PINK {
        override fun writeln(msg: String) = Colors.pinkln(msg)
        override fun write(msg: String) = Colors.pink(msg)
    },
    CYAN {
        override fun writeln(msg: String) = Colors.cyanln(msg)
        override fun write(msg: String) = Colors.cyan(msg)
    },
    WHITE {
        override fun writeln(msg: String) = Colors.whiteln(msg)
        override fun write(msg: String) = Colors.white(msg)
    };

    abstract fun writeln(msg: String): Unit
    abstract fun write(msg: String): Unit
}
