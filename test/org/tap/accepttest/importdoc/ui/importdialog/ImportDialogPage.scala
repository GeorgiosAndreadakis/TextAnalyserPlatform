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
package org.tap.accepttest.importdoc.ui.importdialog

import org.fluentlenium.core.domain.{FluentList, FluentWebElement}
import org.openqa.selenium.By
import org.tap.accepttest.importdoc.ui.UiTestDriver

/**
  * A page object for the import dialog according to the PageObject pattern.
  *
  * @see See [[https://www.martinfowler.com/bliki/PageObject.html]] for more information.
  * @author Georgios Andreadakis (georgios@andreadakis-consulting.de)
  */
case class ImportDialogPage(uiTestDriver: UiTestDriver) {

  def open(): ImportDialogPage = {
    uiTestDriver.browser.goTo("http://localhost:" + uiTestDriver.port)
    this
  }

  def docOverviewContainsFilename(filename: String): Boolean = {
    val elements = findWebElementsById("docOverview")
    val textList = elements.find(By.tagName("a")).textContents()
    textList.contains(filename)
  }

  def fileInput(): FluentWebElement = {
    findWebElementById("fileref")
  }

  def pageTitle(): String = {
    uiTestDriver.browser.window().title()
  }

  def submitButton(): FluentWebElement = {
    findWebElementById("uploadSubmit")
  }


  private def findWebElementById(id: String): FluentWebElement = {
    findWebElementsById(id).first()
  }
  private def findWebElementsById(id: String): FluentList[FluentWebElement] = {
    uiTestDriver.browser.find(By.id(id))
  }
}
