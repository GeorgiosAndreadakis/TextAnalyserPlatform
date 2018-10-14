/*
 * Copyright (c) 2017 Georgios Andreadakis
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

import org.tap.domain.{DocElement, Document, ElementContainer}


/** The model for a document view. */
case class DocumentViewModel(document: Document) {
  //def bodyElements: ElementContainerViewModel = BodyElements(document)
}


/* View Model Classes. */

/*abstract class ElementViewModel(val docElement: DocElement) {
  def htmlId(): String = docElement.id
  def id(): String = docElement.id
}*/

/*abstract class ElementContainerViewModel(override val docElement: DocElement)
  extends ElementViewModel(docElement)
  with Iterable[ElementViewModel] {

  def elementContainer: ElementContainer

  override def iterator:Iterator[ElementViewModel] = elementContainer.iterator.map(docElem => mapDocElement(docElem))
  def hasChildren: Boolean = elementContainer.hasChildren

  def mapDocElement(docElem: DocElement): ElementViewModel = {
    docElem match {
      //case sectionElem: Section => SectionViewModel(sectionElem)
      //case paragraph: Paragraph => ParagraphViewModel(paragraph)
      //case image: Image => ImageViewModel(image)
      //case tableCell: TableCell => TableCellViewModel(tableCell)
      //case tableRow: TableRow => TableRowViewModel(tableRow)
      //case tableHeaderCell: TableHeaderCell => TableHeaderCellViewModel(tableHeaderCell)
      //case table: Table => TableViewModel(table)
      case elem: DocElement => DummyViewModel(elem)
    }
  }
}

case class BodyElements(document: Document) extends ElementContainerViewModel(document.bodyElements) {
  override def elementContainer: ElementContainer = document.bodyElements
}

case class SectionViewModel(section: Section) extends ElementContainerViewModel(section) {
  val order: Int = section.order
  def numberOfChildren(): Int = section.children.size
  def title(): String = section.title
  override def elementContainer: ElementContainer = section
}

case class ParagraphViewModel(paragraph: Paragraph) extends ElementContainerViewModel(paragraph) {
  def text(): String = paragraph.text
  override def elementContainer: ElementContainer = paragraph
}

case class ImageViewModel(image: Image) extends ElementViewModel(image) {
  def text(): String = image.filename
  def source(): String = image.pathTempFile
}

case class TableViewModel(table: Table) extends ElementContainerViewModel(table) {
  override def elementContainer: ElementContainer = table
}

case class TableRowViewModel(tableRow: TableRow) extends ElementContainerViewModel(tableRow) {
  override def elementContainer: ElementContainer = tableRow
}

case class TableCellViewModel(tableCell: TableCell) extends ElementContainerViewModel(tableCell) {
  def text(): String = tableCell.content
  override def elementContainer: ElementContainer = tableCell
}

case class TableHeaderCellViewModel(tableHeaderCell: TableHeaderCell) extends ElementViewModel(tableHeaderCell) {
  def text(): String = tableHeaderCell.content
}

case class DummyViewModel(override val docElement: DocElement) extends ElementViewModel(docElement) {
}
*/