package nu.pattern.support.play.api.libs.json

import anorm.{Pk, NotAssigned}
import org.specs2.mutable._
import play.api.libs.json._
import play.api.libs.json.Json._
import nu.pattern.support.play.api.libs.json.Implicits._

class NotAssignedReadsSpec extends Specification {

  def test(input: JsValue) {
    s"""A "$input"""" should {
      s"read as $NotAssigned" in {
        val expected = JsSuccess(NotAssigned)
        val actual = fromJson[Pk[Nothing]](input)
        actual must beEqualTo(expected)
      }
    }
  }

  test(obj("null" -> JsNull) \ "null")
  test(obj() \ "undefined")

}
