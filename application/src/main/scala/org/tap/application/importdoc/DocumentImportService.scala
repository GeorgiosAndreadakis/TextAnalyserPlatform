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
package org.tap.application.importdoc

import org.tap.domain.{Document, DocumentSource}

/**
  * A domain service for importing documents using the collaborators given by the import context.
  *
  * @author Georgios Andreadakis (georgios@andreadakis-consulting.de)
  */
trait DocumentImportService {
  this: DocumentImportContext =>

  def importFromSource(source: DocumentSource): Document = {
    val doc = parser.parse(source)
    repository.save(doc)
    doc
  }
}
