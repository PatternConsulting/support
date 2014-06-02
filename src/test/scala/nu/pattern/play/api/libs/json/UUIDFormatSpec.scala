package nu.pattern.play.api.libs.json

import java.util.UUID

import org.specs2.mutable._
import play.api.libs.json.{JsError, JsString, JsSuccess}

class UUIDFormatSpec extends Specification {
  val valid = UUID.randomUUID()

  s""""$valid"""" should {
    {
      val expected = JsString(valid.toString)

      s"""write as "$expected"."""" in {
        UUIDFormat.writes(valid) must beEqualTo(expected)
      }
    }

    {
      val expected = JsSuccess(valid)

      s"""read as "$expected"."""" in {
        UUIDFormat.reads(JsString(valid.toString)) must beEqualTo(expected)
      }

      s"""read as "$expected" (with mangled letter case)."""" in {
        UUIDFormat.reads(JsString(valid.toString.toUpperCase)) must beEqualTo(expected)
      }

      s"""read as "$expected" (with padding whitespace)."""" in {
        UUIDFormat.reads(JsString("  " + valid.toString + "  ")) must beEqualTo(expected)
      }
    }
  }

  s"""Reader""" should {
    s"""return errors for junk values""" in {
      UUIDFormat.reads(JsString("junk value")) must beAnInstanceOf[JsError]
    }

    s"""return errors when a UUID is incorrectly formatted""" in {
      UUIDFormat.reads(JsString(valid.toString.replace('-', '_'))) must beAnInstanceOf[JsError]
    }

  }

}
