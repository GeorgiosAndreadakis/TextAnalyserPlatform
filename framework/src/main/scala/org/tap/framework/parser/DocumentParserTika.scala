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
package org.tap.framework.parser

import java.io.InputStream

import org.apache.tika.metadata.Metadata
import org.apache.tika.parser.AutoDetectParser
import org.tap.domain.Document
import org.tap.domain.docimport.DocumentParser

/**
  * Implementation of the document parser with Apache Tika.
  */
class DocumentParserTika extends DocumentParser {

  override def parse(inputStream: InputStream) = {

    val parser = new AutoDetectParser
    val handler = TapContentHandler(new ParseResultCollector)
    val metadata = new Metadata
    parser.parse(inputStream, handler, metadata)
    val parseResult = handler.result.buildResult
    buildDocument(parseResult)
  }

  private def buildDocument(parseResult: ParseResult): Document = {
    DocumentBuilder(parseResult).buildDocument
  }
}
