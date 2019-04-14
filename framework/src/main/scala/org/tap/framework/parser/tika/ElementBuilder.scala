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

import org.tap.domain.{DocElement, Paragraph, Section}

/**
  * Builders for the document elements.
  */
sealed abstract class ElementBuilder {

  val textBuilder: TextBuilder = new TextBuilder

  def charactersEventReceived(event: CharactersEvent): ElementBuilder = {
    textBuilder.append(event.ch)
    this
  }

  def endElementEventReceived(): DocElement

}

case class ParagraphBuilder(event: StartElementEvent) extends ElementBuilder {
  override def endElementEventReceived(): DocElement = {
    Paragraph(textBuilder.build)
  }
}

case class SectionBuilder(level: Int) extends ElementBuilder {
  override def endElementEventReceived(): DocElement = Section(level, textBuilder.build)
}

case class DummyBuilder() extends ElementBuilder {
  override def endElementEventReceived(): DocElement = {
    null
  }
}

class TextBuilder {
  var stringBuilder: StringBuilder = new StringBuilder
  def append(ch: String):Unit = stringBuilder.append(ch)
  def build: String = {
    val text: String = stringBuilder.toString
    stringBuilder.clear()
    text
  }
}
