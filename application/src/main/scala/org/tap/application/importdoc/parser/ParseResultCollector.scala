/*
 * Copyright (c) 2017 Georgios Andreadakis
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
package org.tap.application.importdoc.parser

import org.xml.sax.Attributes

import scala.collection.mutable.ListBuffer

/**
  * Collects the SAX events and builds a parse result.
  *
  * @author Georgios Andreadakis (georgios@andreadakis-consulting.de)
  */
class ParseResultCollector {

  val buf = new ListBuffer[ParseEvent]

  def buildResult: ParseResult = {
    ParseResult(buf.toList)
  }

  def addCharacters(ch: Array[Char], start: Int, length: Int): Unit = {
  }
  def addEndOfDocument(): Unit = {
  }
  def addEndOfElement(uri: String, localName: String, qName: String): Unit = {
  }
  def addEndOfPrefixMapping(prefix: String): Unit = {
  }
  def addStartOfDocument(): Unit = {
  }
  def addStartOfElement(uri: String, localName: String, qName: String, atts: Attributes): Unit = {
  }
  def addStartOfPrefixMapping(prefix: String, uri: String): Unit = {
  }
}
