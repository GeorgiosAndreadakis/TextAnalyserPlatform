package org.tap.framework

import java.io.{ByteArrayInputStream, InputStream}

import org.tap.domain.DocumentSource

/** A string is the source of the document. */
class DocumentStringSource(text: String) extends DocumentSource {

  override def inputStream: InputStream = new ByteArrayInputStream(text.getBytes)
  override def name: String = {
    "String '" + (if ((text != null) && text.length>20 ) text.substring(0, 20) else text) + "'"
  }
}
