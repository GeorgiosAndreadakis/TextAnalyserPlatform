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
package org.tap.accepttest.testdata

import com.softwaremill.sttp._

/**
  * Provides helper methods to manipoulate the database for tests.
  *
  * @author Georgios Andreadakis (georgios@andreadakis-consulting.de)
  */
object DatabaseTestDriver {

  def cleanup(): Unit = {

    implicit val backend = HttpURLConnectionBackend()

    var request = sttp.delete(uri"http://localhost:9200/elements")
    var response = request.send()
    println("HTTP Status: " + response.code)

    request = sttp.delete(uri"http://localhost:9200/documents")
    response = request.send()
    println("HTTP Status: " + response.code)
  }
}
