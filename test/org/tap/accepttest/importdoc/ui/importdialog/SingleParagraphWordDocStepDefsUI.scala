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
package org.tap.accepttest.importdoc.ui.importdialog

import cucumber.api.Scenario
import cucumber.api.scala.{EN, ScalaDsl}
import org.scalatest.Matchers
import org.tap.accepttest.importdoc.ui.UiTestDriver
import org.tap.accepttest.testdata.TestFileReference


/**
  * Implements the step definitions for the acceptance test case
  * <p>"Importing a simple Word document with a single paragraph"</p>
  * which addresses the UI.
  *
  * @author Georgios Andreadakis (georgios@andreadakis-consulting.de)
  */
class SingleParagraphWordDocStepDefsUI extends ScalaDsl with EN with Matchers {

  private val port = 9009
  val uiTestDriver = UiTestDriver(port)
  val importDialogPage = ImportDialogPage(uiTestDriver)

  var filename: String = _


  Before("@ui"){ scenario : Scenario =>
    uiTestDriver.startup()
  }

  After("@ui"){ scenario : Scenario =>
    uiTestDriver.shutdown()
  }


  Given("""^the started document overview$"""){ () =>
    importDialogPage.open()
    // Check elements
    importDialogPage.pageTitle() shouldBe "The Text Analyzer Platform"
    importDialogPage.fileInput() should not be null
    importDialogPage.submitButton() should not be null
  }

  When("""^the user selects the file ([^"]*) and starts the import$"""){ filename: String =>
    this.filename = filename
    importDialogPage.fileInput().keyboard().sendKeys(TestFileReference.pathStringForFile(filename))
    importDialogPage.submitButton().click()
  }

  Then("""^after the import the document is available in the system and the overview shows the document$"""){ () =>
    assert(importDialogPage.docOverviewContainsFilename(filename))
  }

}
