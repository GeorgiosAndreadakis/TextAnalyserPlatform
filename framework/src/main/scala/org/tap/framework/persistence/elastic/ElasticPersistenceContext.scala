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
package org.tap.framework.persistence.elastic

import org.apache.http.HttpHost
import org.elasticsearch.client.{RestClient, RestHighLevelClient}

/**
  * Provides the Elastic specific client context for the persistence operations.
  *
  * @author Georgios Andreadakis (georgios@andreadakis-consulting.de)
  */
class PersistenceContext {

  def execute(operation: PersistenceOperation): Unit = {

    val client: RestHighLevelClient = new RestHighLevelClient(
      RestClient.builder(
        new HttpHost("localhost", 9200, "http"),
        new HttpHost("localhost", 9201, "http")))

    try {

      operation.run(client)

    } finally {
      val some = Option(client)
      some match {
        case Some(_) => client.close()
        case None => println("Error when finally cleaning up, Elastic client is null!")
      }
    }
  }
}

trait PersistenceOperation {
  def run(client: RestHighLevelClient)
}
