package nu.pattern.support.play.api.libs.json

import anorm.{NotAssigned, Pk}
import org.specs2.mutable._
import play.api.libs.json._
import nu.pattern.support.play.api.libs.json.Implicits._
import anorm.Id
import play.api.libs.json.JsNumber

class PkWritesSpec extends Specification {

  def test[I](input: I, expected: JsValue)(implicit w: Writes[Pk[I]]) {
    s"""An ${input.getClass.getName} with value "$input"""" should {
      s"write to $expected" in {
        val actual = w.writes(Id(input))
        actual must beEqualTo(expected)
      }

      s"write to $JsNull when $NotAssigned" in {
        val na: Pk[I] = NotAssigned
        val expected = JsNull
        val actual: JsValue = w.writes(na)
        actual must beEqualTo(expected)
      }
    }
  }

  test("10", JsString("10"))
  test(10L, JsNumber(10L))
  test(10.01, JsNumber(10.01))

}
