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

import org.elasticsearch.action.index.IndexRequest
import org.elasticsearch.search.SearchHit
import org.tap.domain.{DocElement, Document, Paragraph, Section}

/**
  * Defines the mapping of a document element to an index request for accessing ElasticSearch.
  *
  * @author Georgios Andreadakis (georgios@andreadakis-consulting.de)
  */
sealed trait DocElementIndexRequestMapper {

  val docIdAttributName = "docId"
  val elemTypeAttributName = "type"
  val elementIndexName = "elements"

  def buildElement(elemHit: SearchHit): DocElement
  def mapTo(elem:DocElement, doc:Document): IndexRequest

}

case class ParagraphMapper() extends DocElementIndexRequestMapper {

  private val textAttributeName = "text"

  override def mapTo(elem: DocElement, doc: Document): IndexRequest = {
    val pMap = new java.util.HashMap[String, AnyRef]
    pMap.put(elemTypeAttributName, "p")
    pMap.put(textAttributeName, elem.asInstanceOf[Paragraph].text)
    pMap.put(docIdAttributName, doc.getId)
    val indexRequest = new IndexRequest(elementIndexName, "doc", elem.id)
    indexRequest.source(pMap)
  }

  def buildElement(elemHit: SearchHit): DocElement = {
    val text = elemHit.getSourceAsMap.get(textAttributeName).asInstanceOf[String]
    Paragraph(text)
  }
}

case class SectionMapper() extends DocElementIndexRequestMapper {

  private val levelAttributName = "level"
  private val titleAttributName = "title"

  override def buildElement(elemHit: SearchHit): DocElement = {
    val level = elemHit.getSourceAsMap.get(levelAttributName).asInstanceOf[String]
    val title = elemHit.getSourceAsMap.get(titleAttributName).asInstanceOf[String]
    Section(level.toInt, title)
  }

  override def mapTo(elem: DocElement, doc: Document): IndexRequest = {
    val pMap = new java.util.HashMap[String, AnyRef]
    val section = elem.asInstanceOf[Section]
    pMap.put(elemTypeAttributName, "h")
    pMap.put(docIdAttributName, doc.getId)
    pMap.put(levelAttributName, section.level.toString)
    pMap.put(titleAttributName, section.title)

    val indexRequest = new IndexRequest(elementIndexName, "doc", elem.id)
    indexRequest.source(pMap)
  }
}
