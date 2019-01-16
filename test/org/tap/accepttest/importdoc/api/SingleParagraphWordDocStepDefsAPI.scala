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
package org.tap.accepttest.importdoc.api

import java.nio.file.Paths

import cucumber.api.scala.{EN, ScalaDsl}
import org.scalatest.Matchers
import org.tap.accepttest.testdata.TestFileReference
import org.tap.application.importdoc.DocImporter
import org.tap.domain.docimport.DocumentParser
import org.tap.domain.{Document, DocumentRepository, DocumentSource}
import org.tap.framework.DocumentPathSource
import org.tap.framework.parser.tika.DocumentParserTika
import org.tap.framework.persistence.elastic.DocumentRepositoryForElastic

/**
  * The Cucumber step definitions for the story "import text file with a single passage".
  *
  * @author Georgios Andreadakis (georgios@andreadakis-consulting.de)
  */
class SimpleTextPassageInWordFileStepDefs extends ScalaDsl with EN with Matchers {

  private var source: DocumentPathSource = _
  private var document: Document = _
  private val parser: DocumentParser = new DocumentParserTika
  private val docRepo: DocumentRepository = new DocumentRepositoryForElastic

  private def testreference = {
    TestFileReference.build.find(TestFileReference.WORD_SINGLE_PARAGRAPH)
  }


  Given("""^a word file which contains a single text passage$"""){ () =>
    val filename = testreference.getFilename
    val path = testreference.qualifiedPath
    source = new DocumentPathSource(filename, Paths.get(path))
    withClue(s"Source in path '$path' not found: ") {
      source should not be null
    }
  }

  When("""^the user starts the import for the given file$"""){ () =>
    document = new DocImporter(docRepo, parser).importFile(source)
  }

  private def findPersistedDoc(document: Document) = {
    docRepo.allDocs match {
      case Left(_) => Left(None)
      case Right(list: List[Document]) => Right(list.filter(_.getId == document.getId).head)
    }
  }

  Then("""^the file will be imported and the text is in the system available$"""){ () =>

    val docFromDB = findPersistedDoc(document)
    withClue("Document should not be null") {
      docFromDB.right.get should not be null
    }

    withClue("Document misses a paragraph containing defined text") {
      val expectedText = testreference.expected
      docFromDB.right.get.firstElement.asParagraph.text startsWith expectedText
    }
  }
}

class DocumentParserMock extends DocumentParser {
  override def parse(source: DocumentSource): Document = new Document
}