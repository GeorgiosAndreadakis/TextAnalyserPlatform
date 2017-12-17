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
package org.tap.framework.parser

import org.tap.framework.parser.helpers.EventAttributesAdapter
import org.xml.sax.{Attributes, ContentHandler, Locator}

/**
  * Implements the SAX Content Handler for Apache Tika.
  * <p>
  *   The relevant HTML SAX events are pipelined to the ParseResultCollector.
  *   At the end a parse result is built.
  * </p>
  */
case class TapContentHandler(eventCollector: ParseEventCollector) extends ContentHandler {

  def parseResult: ParseEventCollector = {
    eventCollector
  }

  override def characters(ch: Array[Char], start: Int, length: Int): Unit = {
    eventCollector.addCharacters(ch, start, length)
    val str = buildStringPresentation(ch)
    println(s"characters($str, $start, $length)")
  }

  private def buildStringPresentation(ch: Array[Char]) = {
    "''" + String.copyValueOf(ch) + "''"
  }

  override def endElement(uri: String, localName: String, qName: String): Unit = {
    eventCollector.addEndOfElement(uri, localName, qName)
    println(s"endElement($localName)")
  }

  private def buildStringPresentation(atts: Attributes) = {
    EventAttributesAdapter(atts).basicPresentation
  }

  override def startElement(uri: String, localName: String, qName: String, atts: Attributes): Unit = {
    eventCollector.addStartOfElement(uri, localName, qName, atts)
    println(s"startElement($localName, ${buildStringPresentation(atts)})")
  }

  override def endPrefixMapping(prefix: String): Unit = {
    //result.addEndOfPrefixMapping(prefix)
    println(s"endPrefixMapping($prefix)")
  }

  override def startPrefixMapping(prefix: String, uri: String): Unit = {
    //result.addStartOfPrefixMapping(prefix, uri)
    println(s"startPrefixMapping($prefix)")
  }

  override def endDocument(): Unit = {
    //result.addEndOfDocument()
    println("endDocument")
  }

  override def startDocument(): Unit = {
    //result.addStartOfDocument()
    println("startDocument")
  }

  override def setDocumentLocator(locator: Locator): Unit = {
    println(s"setDocumentLocator($locator)")
  }
  override def ignorableWhitespace(ch: Array[Char], start: Int, length: Int): Unit = {
    val str = buildStringPresentation(ch)
    println(s"ignorableWhitespace($str, $start, $length)")
  }
  override def skippedEntity(name: String): Unit = {
    println(s"skippedEntity($name)")
  }
  override def processingInstruction(target: String, data: String): Unit = {
    println(s"processingInstruction($target, $data)")
  }
}