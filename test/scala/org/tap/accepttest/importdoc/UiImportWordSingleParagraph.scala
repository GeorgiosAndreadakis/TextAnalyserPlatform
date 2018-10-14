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
package scala.org.tap.accepttest.importdoc

import cucumber.api.scala.{EN, ScalaDsl}
import org.scalatest.Matchers
import play.api.Application
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.test.{Helpers, TestBrowser, TestServer}


/**
  * Implements the step definitions for the acceptance test case
  * <p>"Importing a simple Word document with a single paragraph"</p>
  * which addresses the UI.
  */
class UiImportWordSingleParagraph extends ScalaDsl
  with EN with Matchers {

  val port = 9009
  val browser: TestBrowser = TestBrowser.of(Helpers.HTMLUNIT)
  val application: Application = new GuiceApplicationBuilder().build()
  val server = TestServer(port, application)

  Given("""^a started import dialog$"""){ () =>
    server.start()
  }

  When("""^the user selects a file containing a single text passage and starts the import$"""){ () =>
    browser.goTo(s"http://localhost:$port")
  }

  Then("""^the file will be imported, the text will be available in the system and the ui shows the single passage$"""){ () =>

    browser.window().title() shouldBe "The Text Analyzer Platform"

    // cleanup
    server.stop()
    browser.quit()
  }

}
