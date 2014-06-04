package nu.pattern.support.play.api.libs.json

import java.util.UUID

import org.specs2.mutable._
import play.api.libs.json.{JsSuccess, JsString}
import play.api.libs.json.Json._
import nu.pattern.support.play.api.libs.json.Implicits.UUIDWrites

class UUIDWritesSpec extends Specification {

  s"Writes" should {
    val valid = UUID.randomUUID()
    val expected = JsString(valid.toString)

    s"""write "$valid" as "$expected"""" in {
      val actual = toJson(valid)
      actual must beEqualTo(expected)
    }
  }

}
