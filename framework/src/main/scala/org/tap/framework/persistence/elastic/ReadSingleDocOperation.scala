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

import java.util

import org.elasticsearch.ElasticsearchStatusException
import org.elasticsearch.client.RestHighLevelClient
import org.tap.domain.Document
import org.tap.framework.persistence.elastic.mapping.DocumentIndexRequestMapper

/**
  * Reading a single document.
  *
  * @author Georgios Andreadakis (georgios@andreadakis-consulting.de)
  */
class ReadSingleDocOperation(docId: String) extends PersistenceOperation {

  private var result: Document = _

  def getResult: Either[Exception,Document] = Right(result)

  override def run(client: RestHighLevelClient): Unit = {
    val mapper = new DocumentIndexRequestMapper(client)
    try {
      val getResponse = mapper.readDoc(docId)
      result = convert(getResponse.getSourceAsMap, mapper)
    } catch {
      case e: ElasticsearchStatusException => throw e
    }
  }

  private def convert(sourceMap: util.Map[String,AnyRef], mapper: DocumentIndexRequestMapper): Document = {
    mapper.convert(sourceMap, docId)
  }
}
