package org.tap.framework

import java.io.{ByteArrayInputStream, InputStream}

import org.tap.domain.DocumentSource

/** A string is the source of the document. */
class DocumentStringSource(text: String) extends DocumentSource {

  override def inputStream: InputStream = new ByteArrayInputStream(text.getBytes)
  override def name: String = text

}
