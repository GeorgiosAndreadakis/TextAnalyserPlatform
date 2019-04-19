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
package org.tap.framework.persistence.elastic.mapping

import org.elasticsearch.action.admin.indices.refresh.{RefreshRequest, RefreshResponse}
import org.elasticsearch.action.delete.DeleteRequest
import org.elasticsearch.action.index.IndexRequest
import org.elasticsearch.action.search.{SearchRequest, SearchResponse}
import org.elasticsearch.client.{RequestOptions, RestHighLevelClient}
import org.elasticsearch.index.query.QueryBuilders
import org.elasticsearch.search.builder.SearchSourceBuilder
import org.elasticsearch.search.{SearchHit, SearchHits}
import org.tap.domain._
import org.tap.framework.DocumentStringSource

import scala.collection.mutable

/**
  * Responsible for mapping {{Document}} instances to the index instances (aka documents).
  *
  * @author Georgios Andreadakis (georgios@andreadakis-consulting.de)
  */
class DocumentIndexRequestMapper(client: RestHighLevelClient) {

  private val docIndexName = "documents"
  private val elementIndexName = "elements"

  private val docIdAttributName = "docId"
  private val filenameAttributName = "filename"


  def readAllDocuments: SearchResponse = {
    val searchSourceBuilder = (new SearchSourceBuilder).query(QueryBuilders.matchAllQuery())
    val searchRequest = new SearchRequest(docIndexName).source(searchSourceBuilder)
    client.search(searchRequest, defaultRequestOptions)
  }

  def convert(hit: SearchHit): Document = {
    val doc = new Document(hit.getId)
    val value = hit.getSourceAsMap.get(filenameAttributName).asInstanceOf[String]
    doc.setSource(new DocumentStringSource(value)) // TODO derive the correct document source type
    addElements(hit, doc)
    doc
  }

  def allElementsForDocId(docId: String): SearchHits = {
    val searchSourceBuilder = (new SearchSourceBuilder).query(QueryBuilders.matchQuery(docIdAttributName, docId))
    val searchRequest = new SearchRequest(elementIndexName).source(searchSourceBuilder)
    val searchResponse = client.search(searchRequest, defaultRequestOptions)
    searchResponse.getHits
  }

  private def addElements(docHit: SearchHit, doc: Document): Unit = {

    val idElementMap = mutable.Map[String,DocElement]()

    // Fill the elements map
    for (hit <- allElementsForDocId(doc.getId).getHits) {
      val elemFromSearchHit = mapperFor(hit).buildElement(hit)
      idElementMap += (elemFromSearchHit.getId -> elemFromSearchHit)
    }

    // Add the children to their parents
    for ((id, elem) <- idElementMap) {
      val parentOpt = idElementMap.get(elem.parentId)
      parentOpt match {
        case Some(parent: ElementContainer) => parent.addChild(elem)
        case None => doc.newChild(elem)
      }
    }
  }

  def deleteDoc(docId: String): Unit = {
    val deleteRequest = new DeleteRequest(docIndexName, "doc", docId)
    client.delete(deleteRequest, defaultRequestOptions)
  }

  def deleteElement(hit: SearchHit, docId: String): Unit = {
    val deleteRequest = new DeleteRequest(elementIndexName, "doc", hit.getId)
    client.delete(deleteRequest, defaultRequestOptions)
  }

  def saveDocument(doc: Document): Unit = {

    val indexRequest = createIndexRequestForDocument(doc)
    client.index(indexRequest, defaultRequestOptions)

    // refresh
    refreshForDocumentIndex
  }

  private def createIndexRequestForDocument(doc: Document): IndexRequest = {
    val jsonMap = new java.util.HashMap[String, AnyRef]
    jsonMap.put(filenameAttributName, doc.getSource.name)
    val indexRequest = new IndexRequest(docIndexName, "doc", doc.getId)
    indexRequest.source(jsonMap)
    indexRequest
  }

  def saveDocElement(elem:DocElement, doc:Document): Unit = {

    // Create index request for storing doc
    val indexRequest = createIndexRequestFor(elem, doc)

    // Run index request
    client.index(indexRequest, defaultRequestOptions)

    // refresh
    refreshForDocumentElementIndex
  }

  def refreshForDocumentIndex: RefreshResponse = {
    refreshIndex(docIndexName)
  }

  def refreshForDocumentElementIndex: RefreshResponse = {
    refreshIndex(elementIndexName)
  }

  private def mapperFor(element: DocElement): DocElementIndexRequestMapper = {
    element match {
      case Paragraph(_,_) => ParagraphMapper()
      case Section(_,_,_) => SectionMapper()
      case _ => throw new IllegalStateException(s"No mapper for element $element defined!")
    }
  }

  private def mapperFor(searchHit: SearchHit): DocElementIndexRequestMapper = {
    val elemType = searchHit.getSourceAsMap.get("type").asInstanceOf[String]
    elemType match {
      case "p" => ParagraphMapper()
      case "h" => SectionMapper()
      case _ => throw new IllegalStateException(s"No mapper for type $elemType defined!")
    }
  }

  private def createIndexRequestFor(elem:DocElement, doc:Document): IndexRequest = {
    mapperFor(elem).mapTo(elem, doc)
  }

  private def refreshIndex(index: String): RefreshResponse = {
    val refreshRequest = new RefreshRequest(index)
    client.indices().refresh(refreshRequest, defaultRequestOptions)
  }

  private def defaultRequestOptions = RequestOptions.DEFAULT
}
