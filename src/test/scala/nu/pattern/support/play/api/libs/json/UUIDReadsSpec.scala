package nu.pattern.support.play.api.libs.json

import java.util.UUID

import org.specs2.mutable._
import play.api.libs.json.{JsSuccess, JsError, JsString}
import nu.pattern.support.play.api.libs.json.Implicits.UUIDReads

class UUIDReadsSpec extends Specification {

  s"""Reader""" should {
    val valid = UUID.randomUUID()

    s"""return errors for junk values""" in {
      UUIDReads.reads(JsString("junk value")) must beAnInstanceOf[JsError]
    }

    s"""return errors when a UUID is incorrectly formatted""" in {
      UUIDReads.reads(JsString(valid.toString.replace('-', '_'))) must beAnInstanceOf[JsError]
    }

    s"""read as "$valid"."""" in {
      val expected = JsSuccess(valid)
      val actual = UUIDReads.reads(JsString(valid.toString))
      actual must beEqualTo(expected)
    }

    s"""read as "$valid" (with mangled letter case)."""" in {
      val expected = JsSuccess(valid)
      val actual = UUIDReads.reads(JsString(valid.toString.toUpperCase))
      actual must beEqualTo(expected)
    }

    s"""read as "$valid" (with padding whitespace)."""" in {
      val expected = JsSuccess(valid)
      val actual = UUIDReads.reads(JsString("  " + valid.toString + "  "))
      actual must beEqualTo(expected)
    }

  }

}
