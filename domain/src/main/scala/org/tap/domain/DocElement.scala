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

import java.util.UUID

import scala.collection.mutable.ListBuffer

/**
  * Models an element of a document.
  *
  * @author Georgios Andreadakis (georgios@andreadakis-consulting.de)
  */
sealed abstract class DocElement(id: String) {

  def this() = this(UUID.randomUUID().toString)

  var parentId: String = _
  var parent: ElementContainer = _

  def asContainer:ElementContainer = this.asInstanceOf[ElementContainer]
  def asParagraph: Paragraph = this.asInstanceOf[Paragraph]
  def getId: String = id
  def hasParent: Boolean = parent != null
  def isEmptyDocElement: Boolean
  def print: String
}

/** A special element container which acts as root container in the document. */
case class RootContainer(id: String) extends ElementContainer(id: String) {
  def this() = this(UUID.randomUUID().toString)
  parent = null
  parentId = null
}


/**
  * An document element which acts as an container for other elements.
  * @author Georgios Andreadakis (georgios@andreadakis-consulting.de)
  */
class ElementContainer(id: String) extends DocElement(id: String) with Iterable[DocElement] {

  def this() = this(UUID.randomUUID().toString)

  val children = new ListBuffer[DocElement]

  def hasChildren: Boolean = children.nonEmpty
  def hasASingleParagraphChild: Boolean = (children.size == 1) && children.exists(_.isInstanceOf[Paragraph])
  def singleParagraph: Paragraph = children.head.asParagraph
  def isEmptyDocElement: Boolean = !hasChildren

  def orderRecursive(container: ElementContainer): Int = {
    if (container == null)
      0
    else
      1 + orderRecursive(container.parent)
  }

  def calculateOrder: Int = 1 + orderRecursive(this)

  def addAll(elements: List[DocElement]): Unit = {
    for (elem <- elements) {
      addChild(elem)
    }
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

  def addParagraph(paragraph: Paragraph): Unit = addChild(paragraph)

  override def iterator:Iterator[DocElement] = children.iterator
  override def print: String = "ElementContainer[]"
  override def toString: String = print
}

/**
  * Models a paragraph of an document.
  * @author Georgios Andreadakis (georgios@andreadakis-consulting.de)
  */
case class Paragraph(id: String, text: String) extends ElementContainer(id: String) {

  def this(text: String) = this(UUID.randomUUID().toString, text)

  override def isEmptyDocElement: Boolean = {
    text.isEmpty && children.isEmpty
  }
  override def print: String = "P[" + (if (text != null) text else "") + "]"
}

/**
  * Models a section of an document.
  * <p>
  *   The section acts as element container.
  * </p>
  * @author Georgios Andreadakis (georgios@andreadakis-consulting.de)
  */
case class Section(id: String, level: Int, title: String) extends ElementContainer(id: String) {

  def this(level: Int, title: String) = this(UUID.randomUUID().toString, level, title)
}
