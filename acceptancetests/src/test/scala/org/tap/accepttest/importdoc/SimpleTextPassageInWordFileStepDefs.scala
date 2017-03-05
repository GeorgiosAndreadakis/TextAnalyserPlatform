/*
 * MIT License
 *
 * Copyright (c) 2017 Georgios Andreadakis
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.tap.accepttest.importdoc

import cucumber.api.scala.{EN, ScalaDsl}
import org.scalatest.Matchers
import org.tap.application.importdoc.{ApplicationContext, DocImporter}
import org.tap.domain.{Document, DocumentRepository}

import scala.io.Source

/**
  * Implementation for the Cucumber step definitions for the story import text file with a single passage.
  *
  * @author Georgios Andreadakis (georgios@andreadakis-consulting.de)
  */
class SimpleTextPassageInWordFileStepDefs extends ScalaDsl with EN with Matchers {

  private val source = Source.fromInputStream(getClass.getClassLoader.getResourceAsStream("simple-text-passage.docx"))
  private var saveCalled = false

  Given("""^a word file which contains a single text passage$"""){ () =>
    // source already set
  }

  When("""^the user starts the import for the given file$"""){ () =>

    val context = new ApplicationContext(new DocumentRepository {
      override def save(document: Document) = {
        saveCalled = true
      }
    })
    val importer = new DocImporter(context)
    importer.importFile(source)
  }

  Then("""^the file will be imported and the text is in the system available$"""){ () =>
    //// Write code here that turns the phrase above into concrete actions
    source should not be null
    saveCalled shouldBe true
  }

}
