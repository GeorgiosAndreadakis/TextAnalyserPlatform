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

import java.util

import org.elasticsearch.action.admin.indices.refresh.{RefreshRequest, RefreshResponse}
import org.elasticsearch.action.delete.DeleteRequest
import org.elasticsearch.action.get.{GetRequest, GetResponse}
import org.elasticsearch.action.index.IndexRequest
import org.elasticsearch.action.search.{SearchRequest, SearchResponse}
import org.elasticsearch.client.{RequestOptions, RestHighLevelClient}
import org.elasticsearch.common.xcontent.{XContentBuilder, XContentFactory}
import org.elasticsearch.index.query.QueryBuilders
import org.elasticsearch.search.builder.SearchSourceBuilder
import org.elasticsearch.search.{SearchHit, SearchHits}
import org.tap.domain._

import scala.collection.mutable

/**
  * Responsible for mapping {{Document}} instances to the index instances (aka documents).
  *
  * @author Georgios Andreadakis (georgios@andreadakis-consulting.de)
  */
class DocumentIndexRequestMapper(client: RestHighLevelClient) {

  private val docIndexName = "documents"
  private val elementIndexName = "elements"

  private val docIdAttribute = "docId"
  private val elementOrderAttribute = "elementOrder"
  private val docSourceAttribute = "docSource"
  private val docSourceTypeAttribute = "sourceType"
  private val docSourceNameAttribute = "sourceName"


  def readAllDocuments: SearchResponse = {
    val searchSourceBuilder = (new SearchSourceBuilder).query(QueryBuilders.matchAllQuery())
    val searchRequest = new SearchRequest(docIndexName).source(searchSourceBuilder)
    client.search(searchRequest, defaultRequestOptions)
  }


  def readDoc(docId: String): GetResponse = {
    val req: GetRequest = new GetRequest(docIndexName, docId)
    val response = client.get(req, RequestOptions.DEFAULT)
    response
  }

  private def buildDocumentSource(hit: SearchHit): DocumentSource = {
    buildDocumentSource(hit.getSourceAsMap)
  }

  private def buildDocumentSource(sourceMap: util.Map[String,Object]): DocumentSource = {
    val docSourceMap = sourceMap.get(docSourceAttribute).asInstanceOf[util.HashMap[String,Object]]
    val sourceName = docSourceMap.get(docSourceNameAttribute).asInstanceOf[String]
    val sourceType = docSourceMap.get(docSourceTypeAttribute).asInstanceOf[String]
    new DocumentSourceInfo(sourceName, sourceType)
  }

  def convert(sourceMap: util.Map[String,AnyRef], docId: String): Document = {
    // Document source
    val docSource = buildDocumentSource(sourceMap)

    val order = sourceMap.get(elementOrderAttribute).asInstanceOf[util.ArrayList[String]]
    val elementsInOrder = order.toArray.toList.map(_.toString)

    // Build doc
    val doc = new Document(docId, docSource) // TODO derive the correct document source type
    addElements(doc, elementsInOrder)
    doc.documentCompleted()
    doc
  }

  def convert(hit: SearchHit): Document = {
    convert(hit.getSourceAsMap, hit.getId)
  }

  def allElementsForDocId(docId: String): SearchHits = {
    val searchSourceBuilder = (new SearchSourceBuilder).query(QueryBuilders.matchQuery(docIdAttribute, docId))
    val searchRequest = new SearchRequest(elementIndexName).source(searchSourceBuilder)
    val searchResponse = client.search(searchRequest, defaultRequestOptions)
    searchResponse.getHits
  }

  private def addElements(doc: Document, elementsInOrder: List[String]): Unit = {

    val idElementMap = mutable.Map[String,DocElement]()

    // Fill the elements map with id mapped to the element
    for (hit <- allElementsForDocId(doc.getId).getHits) {
      val elemFromSearchHit = mapperFor(hit).buildElement(hit, doc)
      idElementMap += (elemFromSearchHit.getId -> elemFromSearchHit)
    }

    // Add the elements to the document
    for (id <- elementsInOrder) {

      // Find the element
      val elemOpt = idElementMap.get(id)
      elemOpt match {
        case None => throw new IllegalStateException(s"Id '$id' in ordered list for which no element wss found!")
        case Some(elem:DocElement) =>

          // Add it to the parent
          val parentId = elem.parentId
          if (parentId == null) {
            doc.newChild(elem)
          } else {
            val parentOpt = idElementMap.get(parentId)
            parentOpt match {
              case None => throw new IllegalStateException(s"Id '$id' in ordered list for which no element was found!")
              case Some(parent:ElementContainer) => parent.addChild(elem)
              case Some(_) => throw new IllegalStateException(s"Element $elem references a parent with id '$parentId' which is not a container!")
            }
          }
      }
    }

    // Document completed
    doc.documentCompleted()
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
    jsonMap.put(docSourceNameAttribute, doc.getSource.name)
    jsonMap.put(elementOrderAttribute, doc.bodyElements.allElementsAsList().map(elem => elem.getId).toArray)
    jsonMap.put("source", doc.getSource)
    val indexRequest = new IndexRequest(docIndexName, "doc", doc.getId)
    //indexRequest.source(jsonMap)
    indexRequest.source(createJson(doc))
    indexRequest
  }

  private def createJson(doc: Document): XContentBuilder = {
    val contentBuilder =
      XContentFactory.jsonBuilder()
        .startObject()
          .field(elementOrderAttribute, doc.bodyElements.allElementsAsList().map(elem => elem.getId).toArray)
          .startObject(docSourceAttribute)
            .field(docSourceTypeAttribute, doc.getSource.sourceType)
            .field(docSourceNameAttribute, doc.getSource.name)
          .endObject()
        .endObject()
    contentBuilder
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
