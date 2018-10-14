package org.tap.framework

import java.io.InputStream
import java.nio.file.Path

import org.tap.domain.DocumentSource
import org.tap.framework.filesystem.FileHandling.FileAccess

/** A string is the source of the document. */
class DocumentPathSource(path: Path) extends DocumentSource {

  override def inputStream: InputStream = FileAccess.newInputStream(path)
  override def name: String = path.toString
}
