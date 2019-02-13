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
import org.tap.domain.Document
import org.tap.framework.DocumentPathSource

/**
  * The Cucumber step definitions for the story "import text file with a single passage".
  *
  * @author Georgios Andreadakis (georgios@andreadakis-consulting.de)
  */
class ImportWordDocStepDefsAPI extends ScalaDsl with EN with Matchers {

  private var testContext: DocImportTestContext = _
  private var document: Document = _

  private def testreference = {
    TestFileReference.build.find(TestFileReference.WORD_SINGLE_PARAGRAPH)
  }


  Given("""^a word file which contains a single text passage$"""){ () =>
    val filename = testreference.getFilename
    val path = testreference.qualifiedPath
    testContext = DocImportTestContext(new DocumentPathSource(filename, Paths.get(path)))
    withClue(s"Source in path '$path' not found: ") {
      testContext.source should not be null
    }
  }

  Given("^the text document ([^\"]*)$"){ (name: String) =>
    val path = TestFileReference.buildPath(name)
    testContext = DocImportTestContext(new DocumentPathSource(name, Paths.get(path)))
    withClue(s"Source in path '$path' not found: ") {
      testContext.source should not be null
    }
  }

  When("""^the user starts the import for the given file$"""){ () =>
    document = testContext.importFile()
  }

  private def findPersistedDoc(document: Document) = {
    testContext.allDocs match {
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

  Then("""^there is a text passage in the system with substring '([^"]*)'$"""){ (substr: String) =>
    document.findParagraphWithText(substr)
  }
}
