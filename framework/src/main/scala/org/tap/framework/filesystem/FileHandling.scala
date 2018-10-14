/*
 * Copyright (c) 2018 Georgios Andreadakis
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.tap.framework.filesystem

import java.io.{File, FileOutputStream, InputStream}
import java.net.URI
import java.nio.file.{Files, Path, Paths}

import org.apache.commons.lang.StringUtils
import org.apache.tika.io.IOUtils

/**
  * TODO comment this class.
  */
object FileHandling {

  object FileReference {
    def classpathResource(pathName: String): URI = {
      val url = this.getClass().getClassLoader().getResource(pathName)
      url.toURI()
    }

    def findTempFolder(): Path = {
      val systemTemp = System.getProperty("java.io.tmpdir")
      val folder = if (StringUtils.isNotEmpty(systemTemp)) systemTemp else "/tmp/tap"
      val uri = new File(folder).toURI
      val folderPath = Paths.get(uri)
      if (!folderPath.toFile.exists()) {
        folderPath.toFile.createNewFile()
      }
      folderPath
    }
  }

  object FileAccess {
    def newInputStream(pathName: String): InputStream = {
      val path = Paths.get(FileReference.classpathResource(pathName))
      val stream = newInputStream(path)
      stream
    }

    def newInputStream(path: Path): InputStream = {
      val stream = Files.newInputStream(path)
      stream
    }

    def fileAsByteArray(path: Path): Array[Byte] = {
      Files.readAllBytes(path)
    }

    def writeToFile(stream: InputStream, file: File): Unit = {
      var out: FileOutputStream = null
      try {
        out = new FileOutputStream(file)
        IOUtils.copy(stream, out)
      } finally out.close()
    }

  }
}
