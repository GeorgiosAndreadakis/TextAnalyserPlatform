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

import org.tap.domain._

/**
  * The model for a document view.
  *
  * @author Georgios Andreadakis (georgios@andreadakis-consulting.de)
  */
case class DocumentViewModel(document: Document) {
  def bodyElements: ElementContainerViewModel = BodyElements(document)
  def filename: String = document.getSource.name
  def docId: String = document.getId
  def htmlId(): String = "doc" + asHtmlId(docId)
  def excerptFirstElement: String = {
    val firstElem = document.firstElement
    val viewElem = ElementViewModel.mapDocElement(firstElem)
    viewElem.excerpt
  }
  def excerptSecondElement: String = {
    val secondElemOpt = document.secondElement
    secondElemOpt match {
      case Some(secElem) => ElementViewModel.mapDocElement(secElem).excerpt
      case None => ""
    }
  }

  def asHtmlId(id: String): String = {
    id.hashCode.abs.toString
  }
}


/* View Model Classes. */
abstract class ElementViewModel(val docElement: DocElement) {
  def htmlId(): String = asHtmlId(id())
  def id(): String = docElement.getId
  def excerpt: String

  def asHtmlId(id: String): String = {
    "elem" + id.hashCode.abs.toString
  }
}

object ElementViewModel {

  def mapDocElement(docElem: DocElement): ElementViewModel = {
    docElem match {
      case null => DummyViewModel(null)
      case paragraph: Paragraph => ParagraphViewModel(paragraph)
      case section: Section => SectionViewModel(section)
      case elem: DocElement => DummyViewModel(elem)
    }
  }
}

abstract class ElementContainerViewModel(override val docElement: DocElement)
  extends ElementViewModel(docElement)
    with Iterable[ElementViewModel] {

  def children = elementContainer.children.map(docElem => ElementViewModel.mapDocElement(docElem))
  def elementContainer: ElementContainer
  def firstElement: Option[ElementViewModel] = {
    elementContainer.firstElement match {
      case Some(x) => Option(ElementViewModel.mapDocElement(x))
      case None => None
    }
  }

  override def iterator:Iterator[ElementViewModel] = {
    elementContainer.iterator.map(docElem => ElementViewModel.mapDocElement(docElem))
  }
  def hasChildren: Boolean = elementContainer.hasChildren
}

case class BodyElements(document: Document) extends ElementContainerViewModel(document.bodyElements) {
  override def elementContainer: ElementContainer = document.bodyElements

  override def excerpt: String = ""
}

case class ParagraphViewModel(paragraph: Paragraph) extends ElementViewModel(paragraph) {
  def text(): String = paragraph.text
  override def excerpt: String = text()
}

case class SectionViewModel(section: Section) extends ElementContainerViewModel(section) {
  override def elementContainer: ElementContainer = section
  override def excerpt: String = s"Section (Level ${section.level.toString}): ${section.title}"
  def title: String = section.title
  def level: String = section.level.level
}

case class DummyViewModel(override val docElement: DocElement) extends ElementViewModel(docElement) {
  override def excerpt: String = ""
}
