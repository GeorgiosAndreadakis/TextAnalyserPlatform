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

import org.tap.domain.Document

/**
  * Builds a document instance for a given {@link ParseResult}.
  */
case class DocumentBuilder(parseResult: ParseEventCollector) {

  var doc: Document = _
  private var currentElementBuilder: ElementBuilder = _

  def buildDocument: Document = {

    doc = new Document
    currentElementBuilder = DummyBuilder()
    parseResult.events.foreach {
      case charactersEvent  : CharactersEvent   => currentElementBuilder.charactersEventReceived(charactersEvent)
      case startElementEvent: StartElementEvent => startElementMatched(startElementEvent)
      case endElementEvent  : EndElementEvent   => endElementMatched(endElementEvent)
      case _: ParseEvent => // ignore other events
    }
    doc
  }

  def startElementMatched(event: StartElementEvent): Unit = {
    event.qName match {
      case "p" => currentElementBuilder = ParagraphBuilder(event)
      case "h1" => currentElementBuilder = SectionBuilder(1)
      case _ =>
    }
  }

  def endElementMatched(event: EndElementEvent): Unit = event.qName match {
    case "p" => doc.elementCreated(currentElementBuilder.endElementEventReceived())
                currentElementBuilder = DummyBuilder()
    case "h1" =>  doc.elementCreated(currentElementBuilder.endElementEventReceived())
                  currentElementBuilder = DummyBuilder()
    case _ =>
  }
}
