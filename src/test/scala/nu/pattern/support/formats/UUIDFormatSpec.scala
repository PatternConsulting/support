package nu.pattern.support.formats

import org.specs2.mutable._
import java.util.UUID
import play.api.libs.json.{JsSuccess, JsString}

class UUIDFormatSpec extends Specification {
  val sample = UUID.randomUUID()

  s"""UUID "$sample"""" should {
    {
      val expected = JsString(sample.toString)

      s"""write as "$expected"."""" in {
        UUIDFormat.writes(sample) must equalTo(expected)
      }
    }

    {
      val expected = JsSuccess(sample)

      s"""read as "$expected"."""" in {
        UUIDFormat.reads(JsString(sample.toString)) must equalTo(expected)
      }

      s"""read as "$expected" (with mangled letter case)."""" in {
        UUIDFormat.reads(JsString(sample.toString.toUpperCase)) must equalTo(expected)
      }
    }
  }

}
