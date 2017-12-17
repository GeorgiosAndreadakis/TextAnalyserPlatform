package org.tap.framework.parser.helpers

import org.scalatest.FunSuite
import org.xml.sax.Attributes
import org.xml.sax.helpers.AttributesImpl

/** Unit test for EventAttributesAdapter. */
class EventAttributesAdapterTest extends FunSuite {

  val key = "Last-Save-Date"
  val value = "2016-08-31T20:16:00Z"

  test("Attributes may not be null.") {
    assertThrows[IllegalArgumentException] {
      EventAttributesAdapter(null)
    }
  }

  test("Attributes may be empty") {
    val attributes = new AttributesImpl()
    val adapter = EventAttributesAdapter(attributes)
    assert(adapter.getKey == null)
    assert(adapter.getValue == null)
  }

  test("#attributes may contain a single record.") {
    val attributes = new AttributesImpl()
    attributes.addAttribute("uri", "name", "name", "date", "examplekey")
    val adapter = EventAttributesAdapter(attributes)
    assert(adapter.getKey == "examplekey")
    assert(adapter.getValue == null)
  }

  test("#attributes should not have more than 2 records.") {
    assertThrows[IllegalStateException] {
      val attributes = dummyAttributes()
      attributes.addAttribute("uri", "local", "qName", "date", "2016-08-31T20:16:00Z")
      EventAttributesAdapter(attributes)
    }
  }

  test("The first record of an attribute pair is the key whereas the second record is the value.") {
    val adapter = EventAttributesAdapter(dummyAttributes())
    assert(adapter.getKey == key)
    assert(adapter.getValue == value)
  }

  test("Adapter is able to give a basic presentation fpr the attributes.") {
    val adapter = EventAttributesAdapter(dummyAttributes())
    val str = adapter.basicPresentation
    assert(str.contains(key))
    assert(str.contains(value))
  }

  private def dummyAttributes(): AttributesImpl = {
    val attributes = new AttributesImpl()
    attributes.addAttribute("", "name", "name", "CDATA", key)
    attributes.addAttribute("", "content", "content", "CDATA", value)
    attributes
  }
}
