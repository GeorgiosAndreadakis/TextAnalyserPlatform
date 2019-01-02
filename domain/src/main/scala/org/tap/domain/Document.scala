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

  val bodyElements = new RootContainer
  private val depthFirstElementList = new ListBuffer[DocElement]

  override def iterator:Iterator[DocElement] = bodyElements.iterator

  def setSource(source: DocumentSource): Unit = this.source = source
  def getSource: DocumentSource = source
  def getId: String = id

  def allElementsInDepthFirstOrder: List[DocElement] = depthFirstElementList.toList

  def elementCreated(docElement: DocElement): Unit = {
    if (docElement.isEmptyDocElement) {
      bodyElements.removeElement(docElement)
    } else {
      bodyElements.addChild(docElement)
      depthFirstElementList += docElement
    }
  }

}
