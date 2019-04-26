package org.tap.domain

import java.io.{ByteArrayInputStream, InputStream}

import org.scalatest.{FlatSpec, Matchers}


class DocumentSpec extends FlatSpec with Matchers{

  "Given a document with a single paragraph that" should "be found if it contains a text" in {
    // Given
    val p = Paragraph("dummy", "Text and documents are the form of knowledge transportation, which is most frequently used. ")
    val doc = new Document("doc2", createDummySource)
    // When
    doc.newChild(p)
    doc.documentCompleted()
    // Then
    doc.findParagraphWithText("the form of") should not be None
  }

  "Given a document with a section that" should "be found if it contains a text" in {
    // Given
    val doc = new Document("doc1", createDummySource)
    doc.newChild(Section("1", SectionLevel1, "Mapping the Product Story"))
    doc.newChild(Paragraph("2", "How can someone create a successful product based on a very vague idea?"))
    doc.newChild(Paragraph("3", "The second paragraph is here."))
    doc.newChild(Paragraph("4", "The third paragraph is here."))
    doc.newChild(Section("5", SectionLevel1, "A User Story Map"))
    doc.newChild(Paragraph("6", "To get a first impression of the big picture"))
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
