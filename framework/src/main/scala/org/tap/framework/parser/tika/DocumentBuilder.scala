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

import org.tap.domain.{Document, DocumentSource}

/**
  * Builds a document instance for a given [[ParseEventCollector]].
  */
case class DocumentBuilder(parseResult: ParseEventCollector, source: DocumentSource) {

  var doc: Document = _
  private var currentElementBuilder: ElementBuilder = _

  def buildDocument: Document = {

    doc = new Document(source)
    currentElementBuilder = RootContainerBuilder(doc)
    parseResult.events.foreach {
      case charactersEvent  : CharactersEvent   => currentElementBuilder.charactersEventReceived(charactersEvent)
      case startElementEvent: StartElementEvent => startElementMatched(startElementEvent)
      case endElementEvent  : EndElementEvent   => endElementMatched(endElementEvent)
      case _: ParseEvent => // ignore other events
    }
    doc.documentCompleted()
    doc
  }

  def startElementMatched(event: StartElementEvent): Unit = {
    val parentBuilder = currentElementBuilder
    event.qName match {
      case "p" => currentElementBuilder = ParagraphBuilder(parentBuilder)
      case "h1" => currentElementBuilder = SectionBuilder("h1", parentBuilder)
      case _ =>
    }
  }

  def endElementMatched(event: EndElementEvent): Unit = event.qName match {
    case "p" => endElementEventReceivedWithBuilderChange()
    case "h1" => currentElementBuilder.endElementEventReceived()
    case _ =>
  }


  ////

  private def endElementEventReceivedWithBuilderChange(): Unit = {
    currentElementBuilder.endElementEventReceived()
    currentElementBuilder = currentElementBuilder.parentBuilder
  }
}
