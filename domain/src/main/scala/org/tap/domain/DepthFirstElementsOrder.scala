/*
 * Copyright (c) 2019 Georgios Andreadakis
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
  * Implements a sorting on the elements of the given container in a depth first manner
  * according to the document structure.
  *
  * @author Georgios Andreadakis (georgios@andreadakis-consulting.de)
  */
class DepthFirstElementsOrder(elementContainer: ElementContainer) {

  def buildElementList(): List[DocElement] = {
    val listBuffer = new ListBuffer[DocElement]
    if (!elementContainer.isInstanceOf[RootContainer]) {
      listBuffer += elementContainer
    }
    buildElementListRecursive(listBuffer, elementContainer.children.toList)
    listBuffer.toList
  }

  private def buildElementListRecursive(listBuffer: ListBuffer[DocElement], elems: List[DocElement]): Unit = {
    elems match {
      case Nil =>
      case x :: xs if x.isInstanceOf[ElementContainer] =>
        listBuffer += x
        buildElementListRecursive(listBuffer, x.asContainer.children.toList)
        buildElementListRecursive(listBuffer, xs)
      case x :: xs =>
        listBuffer += x
        buildElementListRecursive(listBuffer, xs)
      case _ =>
        throw new IllegalStateException(s"Pattern matching faild for list $elems")
    }
  }
}

