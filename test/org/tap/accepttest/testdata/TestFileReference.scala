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
package org.tap.accepttest.testdata

import java.nio.file.{Path, Paths}


/**
  * Defines references to documents files for test purpose.
  *
  * @author Georgios Andreadakis (georgios@andreadakis-consulting.de)
  */
object TestFileReference {

  def buildPath(filename: String): Path = {
    Paths.get(buildPathString(filename))
  }

  def buildPathString(filename: String): String = "test-resources/importdoc/" + filename
}
