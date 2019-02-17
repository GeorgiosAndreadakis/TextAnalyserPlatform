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
package org.tap.accepttest.importdoc.ui

import play.api.Application
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.test.{Helpers, TestBrowser, TestServer}

/**
  * Boilerplatecode neede to drive the UI acceptance tests.
  *
  * @author Georgios Andreadakis (georgios@andreadakis-consulting.de)
  */
case class UiTestDriver(port: Int ) {

  val browser: TestBrowser = TestBrowser.of(Helpers.HTMLUNIT)
  val application: Application = new GuiceApplicationBuilder().build()
  val server = TestServer(port, application)

  def startup(): Unit = {
    server.start()
  }

  def shutdown(): Unit = {
    server.stop()
    browser.quit()
  }
}

