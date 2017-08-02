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
package org.tap.application.importdoc.parser

import java.io.InputStream

import org.apache.tika.metadata.Metadata
import org.apache.tika.parser.{AutoDetectParser, ParseContext}
import org.tap.domain.Document

/**
  * Performs the parsing of a text input stream with the help of the Apache Tika library.
  */
class DocumentParserTika() {

  def parse(inputStream: InputStream): Document = {

    val parser = new AutoDetectParser
    val handler = TapContentHandler(new ParseResultCollector)
    val metadata = new Metadata
    val parseContext = new ParseContext

    parser.parse(inputStream, handler, metadata)
    val parseResult = handler.result.buildResult
    buildDocument(parseResult)
  }

  private def buildDocument(parseResult: ParseResult): Document = {
    DocumentBuilder(parseResult).buildDocument
  }
}
