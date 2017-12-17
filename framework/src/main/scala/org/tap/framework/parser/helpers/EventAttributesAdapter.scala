/*
 * Copyright (c) 2017 Georgios Andreadakis
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
package org.tap.framework.parser.helpers

import org.xml.sax.Attributes

/**
  * Models an adapter for a list of xml attributes.
  * <p>
  *   This adapter assumes that there will be not more than 2 attributes, one for a key and the second for a value.<br>
  *   In some cases only 1 record may be given. For example, when parsing a Word document there was the case of a link
  *   which had a single attribute record with the value '_GoBack'.<br>
  *   There may be also 0 attributes.
  * </p>
  */
case class EventAttributesAdapter(attributes: Attributes) {

  if (attributes == null) throw new IllegalArgumentException("#attributes may not be null!")
  if (attributes.getLength > 2) throw new IllegalStateException("#attributes should not have more than 2 records.")

  def getValue: String = attributes.getValue(1)
  def getKey: String = attributes.getValue(0)

  def basicPresentation: String = {
    val builder = StringBuilder.newBuilder
    for (i: Int <- 0 until 2) {
      builder.append("(")
      builder.append(attributes.getLocalName(i))
      builder.append(",")
      builder.append(attributes.getValue(i))
      builder.append(",")
      builder.append(attributes.getQName(i))
      builder.append(",")
      builder.append(attributes.getType(i))
      builder.append(",")
      val uri = attributes.getURI(i)
      val uriStr = if (uri == null || uri.isEmpty) "<empty>" else uri
      builder.append(uriStr)
      builder.append(")")
    }
    builder.toString()
  }
}
