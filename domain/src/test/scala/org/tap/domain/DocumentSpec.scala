package org.tap.domain

import org.scalatest.{FlatSpec, Matchers}


class DocumentSpec extends FlatSpec with Matchers{

  "Given a document with a single paragraph that" should "be found if it contains a text" in {
    // Given
    val p = new Paragraph("Text and documents are the form of knowledge transportation, which is most frequently used. ")
    val doc = new Document()
    // When
    doc.newChild(p)
    // Then
    doc.findParagraphWithText("the form of") should not be None
  }

  "Given a document with a section that" should "be found if it contains a text" in {
    // Given
    val doc = new Document()
    doc.newChild(new Section(1, "Mapping the Product Story"))
    doc.newChild(new Paragraph("How can someone create a successful product based on a very vague idea?"))
    doc.newChild(new Paragraph("The second paragraph is here."))
    doc.newChild(new Paragraph("The third paragraph is here."))
    doc.newChild(new Section(1, "A User Story Map"))
    doc.newChild(new Paragraph("To get a first impression of the big picture"))
    // When
    val maybeSection = doc.findSectionWithTitle("A User Story Map")
    // Then
    maybeSection should not be None
  }
}
