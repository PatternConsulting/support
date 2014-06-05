package nu.pattern.support.play.api.libs.json

import org.specs2.mutable._
import play.api.libs.json._
import play.api.libs.json.Json._
import nu.pattern.support.play.api.libs.json.Implicits._
import anorm.NotAssigned

class NotAssignedWritesSpec extends Specification {

  s"""$NotAssigned"""" should {
    s"write to $JsNull" in {
      val expected = JsNull
      val actual = toJson(NotAssigned)
      actual must beEqualTo(expected)
    }
  }

}
