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
import org.tap.domain._

/**
  * Defines the mapping of a document element to an index request for accessing ElasticSearch.
  *
  * @author Georgios Andreadakis (georgios@andreadakis-consulting.de)
  */
sealed trait DocElementIndexRequestMapper {

  val docIdAttributeName = "docId"
  val parentIdAttributeName = "parentId"
  val elemTypeAttributeName = "type"
  val elementIndexName = "elements"

  def buildElement(hit: SearchHit, doc: Document): DocElement
  def mapTo(elem:DocElement, doc:Document): IndexRequest
}

case class ParagraphMapper() extends DocElementIndexRequestMapper {

  private val textAttributeName = "text"

  override def mapTo(elem: DocElement, doc: Document): IndexRequest = {
    val pMap = new java.util.HashMap[String, AnyRef]
    pMap.put(elemTypeAttributeName, "p")
    pMap.put(parentIdAttributeName, elem.asInstanceOf[Paragraph].parentId)
    pMap.put(textAttributeName, elem.asInstanceOf[Paragraph].text)
    pMap.put(docIdAttributeName, doc.getId)
    val indexRequest = new IndexRequest(elementIndexName, "doc", elem.getId)
    indexRequest.source(pMap)
  }

  override def buildElement(hit: SearchHit, doc: Document): DocElement = {
    val id = hit.getId
    val text = hit.getSourceAsMap.get(textAttributeName).asInstanceOf[String]
    val parentId = hit.getSourceAsMap.get(parentIdAttributeName).asInstanceOf[String]
    val paragraph = Paragraph(id, text)
    paragraph.parentId = parentId
    paragraph
  }
}

case class SectionMapper() extends DocElementIndexRequestMapper {

  private val levelAttributeName = "level"
  private val titleAttributeName = "title"

  override def mapTo(elem: DocElement, doc: Document): IndexRequest = {
    val pMap = new java.util.HashMap[String, AnyRef]
    val section = elem.asInstanceOf[Section]
    pMap.put(elemTypeAttributeName, "h")
    pMap.put(docIdAttributeName, doc.getId)
    pMap.put(parentIdAttributeName, section.parentId)
    pMap.put(levelAttributeName, section.level.toString)
    pMap.put(titleAttributeName, section.title)

    val indexRequest = new IndexRequest(elementIndexName, "doc", elem.getId)
    indexRequest.source(pMap)
  }

  override def buildElement(hit: SearchHit, doc: Document): DocElement = {
    val id = hit.getId
    val level = hit.getSourceAsMap.get(levelAttributeName).asInstanceOf[String]
    val title = hit.getSourceAsMap.get(titleAttributeName).asInstanceOf[String]
    val parentId = hit.getSourceAsMap.get(parentIdAttributeName).asInstanceOf[String]
    val section = Section(id, SectionLevel(level), title)
    section.parentId = parentId
    section
  }

}

