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

import org.xml.sax.{Attributes, ContentHandler, Locator}

/**
  * Implements the SAX Content Handler for Apache Tika.
  * <p>
  *   The relevant HTML SAX events are pipelined to the ParseResultCollector.
  *   At the end a parse result is built.
  * </p>
  */
case class TapContentHandler(result: ParseResultCollector) extends ContentHandler {

  override def endPrefixMapping(prefix: String): Unit = {
    result.addEndOfPrefixMapping(prefix)
  }

  override def characters(ch: Array[Char], start: Int, length: Int): Unit = {
    result.addCharacters(ch, start, length)
  }

  override def startPrefixMapping(prefix: String, uri: String): Unit = {
    result.addStartOfPrefixMapping(prefix, uri)
  }

  override def endElement(uri: String, localName: String, qName: String): Unit = {
    result.addEndOfElement(uri, localName, qName)
  }

  override def startElement(uri: String, localName: String, qName: String, atts: Attributes): Unit = {
    result.addStartOfElement(uri, localName, qName, atts)
  }

  override def endDocument(): Unit = {
    result.addEndOfDocument()
  }

  override def startDocument(): Unit = {
    result.addStartOfDocument()
  }

  override def setDocumentLocator(locator: Locator): Unit = {
  }
  override def ignorableWhitespace(ch: Array[Char], start: Int, length: Int): Unit = {
  }
  override def skippedEntity(name: String): Unit = {
  }
  override def processingInstruction(target: String, data: String): Unit = {
  }
}