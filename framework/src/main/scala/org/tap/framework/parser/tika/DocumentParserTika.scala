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
package org.tap.framework.parser.tika

import java.io.PrintWriter

import org.apache.tika.metadata.Metadata
import org.apache.tika.parser.AutoDetectParser
import org.tap.domain.docimport.DocumentParser
import org.tap.domain.{Document, DocumentSource}

/**
  * Implementation of the document parser with Apache Tika.
  */
class DocumentParserTika extends DocumentParser {

  override def parse(source: DocumentSource): Document = {

    val parser = new AutoDetectParser
    val handler = TapContentHandler(new ParseEventCollector)
    val metadata = new Metadata
    val inputStream = source.inputStream
    parser.parse(inputStream, handler, metadata)
    writeToFile(handler.csvContent)
    buildDocument(handler.parseResult)
  }

  private def buildDocument(parseResult: ParseEventCollector): Document = {
    DocumentBuilder(parseResult).buildDocument
  }

  private def writeToFile(content: String): Unit = {
    new PrintWriter("target/csvprotocol.csv") { write(content); close }
  }
}
