package org.tap.domain

import java.io.{ByteArrayInputStream, InputStream}

import org.scalatest.{FlatSpec, Matchers}


class DocumentSpec extends FlatSpec with Matchers{

  "Given a document with a single paragraph that" should "be found if it contains a text" in {
    // Given
    val p = new Paragraph("Text and documents are the form of knowledge transportation, which is most frequently used. ")
    val doc = new Document(createDummySource)
    // When
    doc.newChild(p)
    //doc.documentCompleted()
    // Then
    doc.findParagraphWithText("the form of") should not be None
  }

  "Given a document with a section that" should "be found if it contains a text" in {
    // Given
    val doc = new Document(createDummySource)
    doc.newChild(new Section(SectionLevel1, "Mapping the Product Story"))
    doc.newChild(new Paragraph("How can someone create a successful product based on a very vague idea?"))
    doc.newChild(new Paragraph("The second paragraph is here."))
    doc.newChild(new Paragraph("The third paragraph is here."))
    doc.newChild(new Section(SectionLevel1, "A User Story Map"))
    doc.newChild(new Paragraph("To get a first impression of the big picture"))
    doc.documentCompleted()
    // When
    val maybeSection = doc.findSectionWithTitle("A User Story Map")
    // Then
    maybeSection should not be None
  }

  private def createDummySource = {
    new DocumentSource() {
      override def inputStream: InputStream = new ByteArrayInputStream("".getBytes)
      override def name: String = "dummy"
    }
  }
}
