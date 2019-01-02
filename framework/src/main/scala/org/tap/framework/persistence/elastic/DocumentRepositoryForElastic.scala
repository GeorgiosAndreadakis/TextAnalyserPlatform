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
import org.elasticsearch.ElasticsearchStatusException
import org.elasticsearch.action.admin.indices.refresh.RefreshRequest
import org.elasticsearch.action.index.IndexRequest
import org.elasticsearch.action.search.SearchRequest
import org.elasticsearch.client.{RequestOptions, RestClient, RestHighLevelClient}
import org.elasticsearch.index.query.QueryBuilders
import org.elasticsearch.search.builder.SearchSourceBuilder
import org.elasticsearch.search.{SearchHit, SearchHits}
import org.tap.domain.{Document, DocumentRepository}
import org.tap.framework.DocumentStringSource

import scala.collection.mutable.ListBuffer

/**
  * An implementation of the {@link DocumentRepository} running against EleastiSearch.
  *
  * @author Georgios Andreadakis (georgios@andreadakis-consulting.de)
  */
class DocumentRepositoryForElastic extends DocumentRepository {

  private val docIndex = "documents"

  override def save(document: Document): Unit = {
    saveDoc(document)
  }

  private def saveDoc(doc: Document): Unit = {

    val client: RestHighLevelClient = new RestHighLevelClient(
      RestClient.builder(
        new HttpHost("localhost", 9200, "http"),
        new HttpHost("localhost", 9201, "http")))

    try {

      // Create index request to read all indices
      val jsonMap = new java.util.HashMap[String, AnyRef]
      jsonMap.put("name", doc.getSource.name)
      val indexRequest = new IndexRequest(docIndex, "doc", doc.getId)
      indexRequest.source(jsonMap)

      // Run index request
      client.index(indexRequest, RequestOptions.DEFAULT)

      // refresh
      val refreshRequest = new RefreshRequest(docIndex)
      client.indices().refresh(refreshRequest);

    } finally {
      val some = Option(client)
      some match {
        case Some(_) => client.close()
        case None => println("Error, Elastic client is null")
      }
    }
  }


  def convert(hits: SearchHits): List[Document] = {
    val result = new ListBuffer[Document]
    for (hit <- hits.getHits) {
      result += convert(hit)
    }
    result.toList
  }

  def convert(hit: SearchHit): Document = {
    val doc = new Document(hit.getId)
    val value = hit.getSourceAsMap.get("name").asInstanceOf[String]
    doc.setSource(new DocumentStringSource(value))
    doc
  }

  override def allDocs: List[Document] = {

    val client: RestHighLevelClient = new RestHighLevelClient(
      RestClient.builder(
        new HttpHost("localhost", 9200, "http"),
        new HttpHost("localhost", 9201, "http")))

    try {
      val searchSourceBuilder = new SearchSourceBuilder
      searchSourceBuilder.query(QueryBuilders.matchAllQuery())

      val searchRequest = new SearchRequest(docIndex).source(searchSourceBuilder)
      val searchResponse = client.search(searchRequest, RequestOptions.DEFAULT)

      convert(searchResponse.getHits)
    } catch {
      case ex: ElasticsearchStatusException => List()

    } finally {
      val some = Option(client)
      some match {
        case Some(_) => client.close()
        case None => println("Error, Elastic client is null")
      }
    }
  }
}
