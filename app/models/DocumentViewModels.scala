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
package models

import org.tap.domain.{DocElement, Document, ElementContainer, Paragraph}


/**
  * The model for a document view.
  *
  * @author Georgios Andreadakis (georgios@andreadakis-consulting.de)
  */
case class DocumentViewModel(document: Document) {
  def bodyElements: ElementContainerViewModel = BodyElements(document)
  def filename: String = document.getSource.name
}


/* View Model Classes. */
abstract class ElementViewModel(val docElement: DocElement) {
  def htmlId(): String = docElement.id
  def id(): String = docElement.id
}

abstract class ElementContainerViewModel(override val docElement: DocElement)
  extends ElementViewModel(docElement)
    with Iterable[ElementViewModel] {

  def elementContainer: ElementContainer

  override def iterator:Iterator[ElementViewModel] = elementContainer.iterator.map(docElem => mapDocElement(docElem))
  def hasChildren: Boolean = elementContainer.hasChildren

  def mapDocElement(docElem: DocElement): ElementViewModel = {
    docElem match {
      case paragraph: Paragraph => ParagraphViewModel(paragraph)
      case elem: DocElement => DummyViewModel(elem)
    }
  }
}

case class BodyElements(document: Document) extends ElementContainerViewModel(document.bodyElements) {
  override def elementContainer: ElementContainer = document.bodyElements
}

case class ParagraphViewModel(paragraph: Paragraph) extends ElementContainerViewModel(paragraph) {
  def text(): String = paragraph.text
  override def elementContainer: ElementContainer = paragraph
}

case class DummyViewModel(override val docElement: DocElement) extends ElementViewModel(docElement) {
}
