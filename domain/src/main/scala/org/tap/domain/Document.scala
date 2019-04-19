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
  * Models a document.
  *
  * @author Georgios Andreadakis (georgios@andreadakis-consulting.de)
  */
class Document(id: String) extends Iterable[DocElement]  {

  def this() = this(UUID.randomUUID().toString)

  private var source: DocumentSource = _

  val bodyElements = new RootContainer()
  private val depthFirstElementList = new ListBuffer[DocElement]

  def addChild(elem: DocElement): Unit = {
    bodyElements.addChild(elem)
    elementCreated(elem)
  }

  def findElement(parentId: String): Option[DocElement] = {
    val f: DocElement => Boolean = (elem: DocElement) => elem.getId.equals(parentId)
    find(f)
  }

  def findParagraphWithText(txt: String): Option[Paragraph] = {

    val f: DocElement => Boolean = (elem: DocElement) => {
      elem.isInstanceOf[Paragraph] && elem.asInstanceOf[Paragraph].text.contains(txt)
    }
    find(f).flatMap(elem => Option(elem.asInstanceOf[Paragraph]))
  }

  def findSectionWithTitle(title: String): Option[Section] = {

    val f: DocElement => Boolean = (elem: DocElement) => {
      elem.isInstanceOf[Section] && elem.asInstanceOf[Section].title.equals(title)
    }
    find(f).flatMap(elem => Option(elem.asInstanceOf[Section]))
  }

  override def iterator:Iterator[DocElement] = depthFirstElementList.iterator

  def setSource(source: DocumentSource): Unit = this.source = source
  def getSource: DocumentSource = source
  def getId: String = id

  def firstElement: DocElement = {
    if (bodyElements.hasChildren) {
      depthFirstElementList.head
    } else {
      null
    }
  }

  def elementCreated(docElement: DocElement): Unit = {
    depthFirstElementList += docElement
  }

}
