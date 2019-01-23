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

import cucumber.api.scala.{EN, ScalaDsl}
import org.openqa.selenium.By
import org.scalatest.Matchers
import org.tap.accepttest.testdata.TestFileReference
import play.api.Application
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.test.{Helpers, TestBrowser, TestServer}


/**
  * Implements the step definitions for the acceptance test case
  * <p>"Importing a simple Word document with a single paragraph"</p>
  * which addresses the UI.
  *
  * @author Georgios Andreadakis (georgios@andreadakis-consulting.de)
  */
class SingleParagraphWordDocStepDefsUI
  extends ScalaDsl with EN with Matchers {

  val port = 9009
  val browser: TestBrowser = TestBrowser.of(Helpers.HTMLUNIT)
  val application: Application = new GuiceApplicationBuilder().build()
  val server = TestServer(port, application)


  Given("""^the started document overview$"""){ () =>

    server.start()
    browser.goTo("http://localhost:" + port)

    // Check elements
    browser.window().title() shouldBe "The Text Analyzer Platform"
    browser.find(By.id("fileref")).get(0) should not be null
    browser.find(By.id("uploadSubmit")).get(0) should not be null
  }


  When("""^the user selects a file with a text passage and starts the import$"""){ () =>

    val keys = testreference.qualifiedPath
    browser.find(By.id("fileref")).get(0).keyboard().sendKeys(keys)
    browser.find(By.id("uploadSubmit")).get(0).click()
  }


  Then("""^after the import the document is available in the system and the overview contains the document$"""){ () =>

    browser.window().title() shouldBe "The Text Analyzer Platform"
    val elements = browser.find(By.id("docOverview"))
    val textList = elements.find(By.tagName("a")).textContents()
    assert(textList.contains(testreference.getFilename))

    // cleanup test setup
    server.stop()
    browser.quit()
  }


  private def testreference = {
    TestFileReference.build.find(TestFileReference.WORD_SINGLE_PARAGRAPH)
  }

}
