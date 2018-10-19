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
package org.tap.framework.parser.tika

import org.tap.framework.parser.tika.helpers.EventAttributesAdapter
import org.xml.sax.{Attributes, ContentHandler, Locator}

/**
  * Implements the SAX Content Handler for Apache Tika.
  * <p>
  *   The relevant HTML SAX events are pipelined to the ParseResultCollector.
  *   At the end a parse result is built.
  * </p>
  * <p>
  *   There are some log routines which makes a protocol of the recieved SAX events.
  *   These routines does not belong to the responsibility of the content handler really.
  * </p>
  */
case class TapContentHandler(eventCollector: ParseEventCollector) extends ContentHandler {

  private val csv: StringBuilder = StringBuilder.newBuilder

  def csvContent: String = csv.toString()

  def parseResult: ParseEventCollector = {
    eventCollector
  }

  override def characters(ch: Array[Char], start: Int, length: Int): Unit = {
    eventCollector.addCharacters(ch, start, length)
    logCharacters(ch, start, length)
  }

  private def buildStringPresentation(ch: Array[Char]) = {
    val str = String.copyValueOf(ch)
    "'" + str.replaceAll("\n", "") + "'"
  }

  override def endElement(uri: String, localName: String, qName: String): Unit = {
    if (included(localName)) {
      eventCollector.addEndOfElement(uri, localName, qName)
      logEndElement(uri, localName, qName)
    }
  }

  def included(localName: String): Boolean = {
    !"meta".equals(localName)
  }

  override def startElement(uri: String, localName: String, qName: String, atts: Attributes): Unit = {
    if (included(localName)) {
      eventCollector.addStartOfElement(uri, localName, qName, atts)
      logStartElement(uri, localName, qName, atts)
    }
  }

  override def endPrefixMapping(prefix: String): Unit = {
    //result.addEndOfPrefixMapping(prefix)
    //logEndPrefixMapping(prefix)
  }

  override def startPrefixMapping(prefix: String, uri: String): Unit = {
    //result.addStartOfPrefixMapping(prefix, uri)
    //logStartPrefixMapping(prefix, uri)
  }

  override def endDocument(): Unit = {
    //result.addEndOfDocument()
    logEndDocument()
  }

  override def startDocument(): Unit = {
    //result.addStartOfDocument()
    logStartDocument()
  }

  override def setDocumentLocator(locator: Locator): Unit = {
    logSetDocumentLocator(locator)
  }

  override def ignorableWhitespace(ch: Array[Char], start: Int, length: Int): Unit = {
    //logIgnorableWhitespace(ch, start, length)
  }

  override def skippedEntity(name: String): Unit = {
    logSkippedEntity(name)
  }

  override def processingInstruction(target: String, data: String): Unit = {
    logProcessingInstruction(target, data)
  }


  private def logCharacters(ch: Array[Char], start: Int, length: Int): Unit = {
    val str = buildStringPresentation(ch)
    csv.append(s"characters;$str;$start,$length\n")
  }

  private def logEndElement(uri: String, localName: String, qName: String): Unit = {
    csv.append(s"endElement;$localName\n")
  }

  private def buildCsvPresentation(atts: Attributes) = {
    EventAttributesAdapter(atts).csvPresentation
  }

  private def logStartElement(uri: String, localName: String, qName: String, atts: Attributes): Unit = {
    csv.append(s"startElement;$localName;${buildCsvPresentation(atts)}\n")
  }

  private def logEndPrefixMapping(prefix: String): Unit = {
    csv.append(s"endPrefixMapping;$prefix\n")
  }

  private def logStartPrefixMapping(prefix: String, uri: String): Unit = {
    csv.append(s"startPrefixMapping;$prefix\n")
  }

  private def logEndDocument(): Unit = {
    csv.append("endDocument\n")
  }

  private def logStartDocument(): Unit = {
    csv.append("startDocument\n")
  }

  private def logSetDocumentLocator(locator: Locator): Unit = {
    csv.append(s"setDocumentLocator;$locator\n")
  }
  private def logIgnorableWhitespace(ch: Array[Char], start: Int, length: Int): Unit = {
    val str = buildStringPresentation(ch)
    csv.append(s"ignorableWhitespace;$str;$start,$length\n")
  }
  private def logSkippedEntity(name: String): Unit = {
    csv.append(s"skippedEntity;$name\n")
  }
  private def logProcessingInstruction(target: String, data: String): Unit = {
    csv.append(s"processingInstruction;$target;$data\n")
  }
}