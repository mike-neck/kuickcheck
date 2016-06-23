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

import java.nio.file.*
import java.nio.file.attribute.BasicFileAttributes
import java.util.*
import java.util.jar.JarFile

interface ClassPath {
    fun listClasses(): List<Scannable>
}

data class FileClassPath(val path: String): ClassPath {

    override fun listClasses(): List<Scannable> {
        val path = Paths.get(path)
        if (Files.exists(path) == false) return emptyList()
        Files.walkFileTree(path, visitor)
        return visitor.list().map { it.toString() }
                .map { RealClassFile(path, it) }
                .filter(ClassFile::isNotClosure)
                .map(ClassFile::toJavaClass)
                .map(JavaClass::mapToScannable)
    }

    interface DirectoryScanner: FileVisitor<Path> {
        fun list(): List<Path>
    }

    val visitor: DirectoryScanner = object: SimpleFileVisitor<Path>(), DirectoryScanner {
        val list = mutableListOf<Path>()

        override fun list(): List<Path> {
            return list.toList()
        }

        override fun visitFile(file: Path?, attrs: BasicFileAttributes?): FileVisitResult? {
            if (file != null) list.add(file)
            return FileVisitResult.CONTINUE
        }
    }
}

data class JarClassPath(val path: String): ClassPath {

    companion object {
        val KOTLIN = listOf("/kotlin-stdlib/", "/kotlin-runtime/", "/kotlin-reflect/")
    }

    override fun listClasses(): List<Scannable> {
        val kotlinLib = KOTLIN.filter { path.contains(it) }.size > 0
        if (kotlinLib) return emptyList()
        val jarFile = JarFile(path)
        return Collections.list(jarFile.entries()).filter { !it.isDirectory }
                .filter { it.name.startsWith("META-INF") == false }
                .map{ it.name }
                .map(::ZipClassFile)
                .filter(ClassFile::isNotClosure)
                .map(ClassFile::toJavaClass)
                .map(JavaClass::mapToScannable)
                .filter(Scannable::nameProhibited)
    }
}
