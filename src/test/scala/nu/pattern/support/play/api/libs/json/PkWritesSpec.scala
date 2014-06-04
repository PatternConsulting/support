package nu.pattern.support.play.api.libs.json

import anorm.{Id, NotAssigned, Pk}
import org.specs2.mutable._
import play.api.libs.json._
import play.api.libs.json.Json._
import nu.pattern.support.play.api.libs.json.Implicits._
import anorm.Id
import play.api.libs.json.JsNumber

class PkWritesSpec extends Specification {

  def test[I](input: I, expected: JsValue)(implicit w: Writes[I]) {
    s"""An ${input.getClass.getName} with value "$input"""" should {
      s"write to $expected" in {
        val actual = toJson(input)
        actual must beEqualTo(expected)
      }
    }
  }

  test(Id("10"), JsString("10"))
  test(Id(10L), JsNumber(10L))
  test(NotAssigned, JsNull)

}
