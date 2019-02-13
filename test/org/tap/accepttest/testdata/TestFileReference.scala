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


/**
  * Defines references to documents files and expected values to use in the tests.
  *
  * @author Georgios Andreadakis (georgios@andreadakis-consulting.de)
  */
case class TestFileReference(referenceMap: Map[String,ReferencePair]) {
  def find(key: String) : ReferencePair = {
    referenceMap(key)
  }
}

object TestFileReference {

  val WORD_SINGLE_PARAGRAPH = "Word, single Paragraph"

  def build: TestFileReference = {
    val map = Map[String,ReferencePair](
      WORD_SINGLE_PARAGRAPH -> new ReferencePair(
        "simple-text-passage.docx",
        "The central concept of a document store is the notion")
    )
    TestFileReference(map)
  }

  def buildPath(filename: String): String = "test-resources/importdoc/" + filename
}

class ReferencePair(_filename: String, _expectation: String) {
  private val filename = _filename
  private val expectation = _expectation

  def qualifiedPath: String = TestFileReference.buildPath(filename)
  def expected: String = expectation
  def getFilename: String = _filename
}