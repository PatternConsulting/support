package nu.pattern.support.play.api.libs.json

import anorm.Id
import org.specs2.mutable._
import play.api.libs.json._
import nu.pattern.support.play.api.libs.json.Implicits._

class IdWritesSpec extends Specification {

  def test[I](input: I, expected: JsValue)(implicit w: Writes[I]) {
    s"""An ${input.getClass.getName} with value "$input"""" should {
      s"write to $expected" in {
        val actual = w.writes(Id(input))
        actual must beEqualTo(expected)
      }
    }
  }

  test("10", JsString("10"))
  test(10L, JsNumber(10L))
  test(10.01, JsNumber(10.01))

}
