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
package org.tap.application.importdoc

import org.tap.application.idgeneration.IdGenerator
import org.tap.domain.{Document, DocumentRepository, DocumentSource}

/**
  * Application service responsible for the import of documents.
  *
  * @author Georgios Andreadakis (georgios@andreadakis-consulting.de)
  */
class DocImporter(
       val docuRepo: DocumentRepository,
       val docuParser: DocumentParser,
       val idGenerator: IdGenerator) {

  def importFile(source: DocumentSource): Document = {
    val service = new DocumentImportService with DocumentImportContext {
      override def repository: DocumentRepository = docuRepo
      override def parser: DocumentParser = docuParser
      override def idGenerator: IdGenerator = idGenerator
    }
    service.importFromSource(source)
  }
}

