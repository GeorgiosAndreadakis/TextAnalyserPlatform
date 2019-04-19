package org.tap.domain

import org.scalatest.{FlatSpec, Matchers}


class DocumentSpec extends FlatSpec with Matchers{

  "Given a document with a single paragraph that" should "be found if it contains a text" in {
    // Given
    val p = new Paragraph("Text and documents are the form of knowledge transportation, which is most frequently used. ")
    val doc = new Document()
    // When
    doc.elementCreated(p)
    // Then
    doc.findParagraphWithText("the form of") should not be None
  }

  "Given a document with a section that" should "be found if it contains a text" in {
    // Given
    val doc = new Document()
    doc.elementCreated(new Section(1, "Mapping the Product Story"))
    doc.elementCreated(new Paragraph("How can someone create a successful product based on a very vague idea?"))
    doc.elementCreated(new Paragraph("The second paragraph is here."))
    doc.elementCreated(new Paragraph("The third paragraph is here."))
    doc.elementCreated(new Section(1, "A User Story Map"))
    doc.elementCreated(new Paragraph("To get a first impression of the big picture"))
    // When
    val maybeSection = doc.findSectionWithTitle("A User Story Map")
    // Then
    maybeSection should not be None
  }
}
