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

import org.elasticsearch.ElasticsearchStatusException
import org.elasticsearch.client.RestHighLevelClient
import org.elasticsearch.search.SearchHits
import org.tap.domain.Document
import org.tap.framework.persistence.elastic.mapping.DocumentIndexRequestMapper

/**
  * Reading all documents.
  *
  * @author Georgios Andreadakis (georgios@andreadakis-consulting.de)
  */
class ReadAllDocsOperation extends PersistenceOperation {

  private var result: List[Document] = _

  def getResult: Either[Exception,List[Document]] = Right(result)

  override def run(client: RestHighLevelClient): Unit = {
    val mapper = new DocumentIndexRequestMapper(client)
    try {
      val searchResponse = mapper.readAllDocuments
      result = convert(searchResponse.getHits, mapper)
    } catch {
      case _: ElasticsearchStatusException => result = List()
    }
  }

  private def convert(hits: SearchHits, mapper: DocumentIndexRequestMapper): List[Document] = {
    hits.getHits.map(mapper.convert).toList
  }
}
