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
package org.tap.framework.parser.tika

import org.xml.sax.Attributes

import scala.collection.mutable.ListBuffer

/**
  * Collects the SAX events and builds a parse result.
  */
case class ParseEventCollector() {

  val events = new ListBuffer[ParseEvent]

  def buildResult: List[ParseEvent] = {
    events.toList
  }

  def addCharacters(ch: Array[Char], start: Int, length: Int): Unit = {
    events += CharactersEvent(new String(ch), start, length)
  }

  def addStartOfElement(uri: String, localName: String, qName: String, atts: Attributes): Unit = {
    events += StartElementEvent(uri, localName, qName, atts)
  }

  def addEndOfElement(uri: String, localName: String, qName: String): Unit = {
    events += EndElementEvent(uri, localName, qName)
  }
}

/**
  * Models one of the parsing events thrown by the Content Handler.
  */
sealed trait ParseEvent {
}
case class StartElementEvent(uri: String, localName: String, qName: String, atts: Attributes) extends ParseEvent {
  override def toString: String = qName
}
case class EndElementEvent(uri: String, localName: String, qName: String) extends ParseEvent {
  override def toString: String = qName
}
case class CharactersEvent(ch: String, start: Int, lenght: Int) extends ParseEvent {
  override def toString: String = s"[$start,$lenght] $ch"
}
