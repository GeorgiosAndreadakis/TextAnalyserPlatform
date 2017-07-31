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
package org.tap.accepttest.importdoc

import cucumber.api.scala.{EN, ScalaDsl}
import org.scalatest.Matchers
import org.tap.application.importdoc.{ApplicationContext, DocImporter}
import org.tap.domain.{Document, DocumentRepository}

import scala.io.Source

/**
  * The Cucumber step definitions for the story "import text file with a single passage".
  *
  * @author Georgios Andreadakis (georgios@andreadakis-consulting.de)
  */
class SimpleTextPassageInWordFileStepDefs extends ScalaDsl with EN with Matchers {

  private val source = Source.fromInputStream(getClass.getClassLoader.getResourceAsStream("simple-text-passage.docx"))
  private val repo = new DocumentRepositoryTest
  val context = new ApplicationContext(repo)
  val importer = new DocImporter(context)

  Given("""^a word file which contains a single text passage$"""){ () =>
    // source already set
    source should not be null
  }

  When("""^the user starts the import for the given file$"""){ () =>

    importer.importFile(source)
  }

  Then("""^the file will be imported and the text is in the system available$"""){ () =>
    //// Write code here that turns the phrase above into concrete actions
    repo.saveCalled shouldBe true
  }

}

class DocumentRepositoryTest extends DocumentRepository {
  var saveCalled = false
  override def save(document: Document): Unit = {
    saveCalled = true
  }
}