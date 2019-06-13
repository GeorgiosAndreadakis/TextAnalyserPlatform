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
package org.tap.domain

import scala.collection.mutable.ListBuffer

/**
  * The Model for the elements of a document.
  *
  * @author Georgios Andreadakis (georgios@andreadakis-consulting.de)
  */
sealed abstract class DocElement(id: String) {

  var parentId: String = _
  var parent: ElementContainer = _

  def asContainer:ElementContainer = this.asInstanceOf[ElementContainer]
  def asParagraph: Paragraph = this.asInstanceOf[Paragraph]
  def getId: String = id
  def hasParent: Boolean = parentId != null
  def isEmptyDocElement: Boolean
  def print: String
  override def toString:String = {
    print
  }
}

/** A special element container which acts as root container in the document. */
case class RootContainer(id: String, doc: Document) extends ElementContainer(id: String) {
  def this(doc: Document) = this(doc.getId, doc)

  private var elementsInDepthFirstOrder: List[DocElement] = List()
  parent = null
  parentId = null

  override def iterator:Iterator[DocElement] = elementsInDepthFirstOrder.iterator
  def allElementsAsList():List[DocElement] = elementsInDepthFirstOrder
  override def print: String = "RootContainer"

  override def addChild(elem: DocElement): Unit = {
    if (elem != null) {
      children += elem
      elem.parent = this
      elem.parentId = null
    }
  }

  def documentCompleted(): Unit = {
    elementsInDepthFirstOrder = new DepthFirstElementsOrder(this).buildElementList()
  }
}


/** An document element which acts as an container for other elements. */
class ElementContainer(id: String) extends DocElement(id: String) with Iterable[DocElement] {

  val children = new ListBuffer[DocElement]

  def hasChildren: Boolean = children.nonEmpty
  def isEmptyDocElement: Boolean = !hasChildren

  def addAll(elements: List[DocElement]): Unit = {
    for (elem <- elements) addChild(elem)
  }

  def addChild(elem: DocElement): Unit = {
    if (elem != null) {
      children += elem
      elem.parent = this
      elem.parentId = this.getId
    }
  }

  def removeElement(docElement: DocElement): Unit = {
    foreach {
      case paragraph: Paragraph if paragraph.getId == docElement.getId =>
        docElement.parent = null
        docElement.parentId = null
        children -= docElement
      case _ => // nothing to do
    }
  }

  override def iterator:Iterator[DocElement] = children.iterator
  override def print:String = "ElementContainer"
}

/** Models a paragraph of an document. */
case class Paragraph(id: String, text: String) extends DocElement(id: String) {

  override def isEmptyDocElement: Boolean = text.isEmpty
  override def print: String = "P[" + (if (text != null) text else "") + "]"
}

/** Models a section of a document which is also a parent for other elements. */
case class Section(id: String, level: SectionLevel, title: String) extends ElementContainer(id: String) {
  override def toString: String = s"Section (${level.toString}): $title"
}

/** Value object to model a section level. */
case class SectionLevel(level: String) {
  override def toString: String = level
}

object SectionLevel1 extends SectionLevel("h1")
object SectionLevel2 extends SectionLevel("h2")
object SectionLevel3 extends SectionLevel("h3")