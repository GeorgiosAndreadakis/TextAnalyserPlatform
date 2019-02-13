/*
 * Copyright (c) 2019 Georgios Andreadakis
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
package org.tap.accepttest.importdoc.api

import org.tap.application.importdoc.DocImporter
import org.tap.domain.docimport.DocumentParser
import org.tap.domain.{Document, DocumentRepository, DocumentSource}
import org.tap.framework.parser.tika.DocumentParserTika
import org.tap.framework.persistence.elastic.DocumentRepositoryForElastic

/**
  * Defines the import collaborators for the document import API test.
  *
  * @author Georgios Andreadakis (georgios@andreadakis-consulting.de)
  */
case class DocImportTestContext(source: DocumentSource) {
  private val parser: DocumentParser = new DocumentParserTika
  private val docRepo: DocumentRepository = new DocumentRepositoryForElastic

  def importFile(): Document = {
    new DocImporter(docRepo, parser).importFile(source)
  }

  def allDocs: Either[Exception,List[Document]] = docRepo.allDocs
}
