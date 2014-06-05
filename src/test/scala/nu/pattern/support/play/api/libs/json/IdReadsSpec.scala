package nu.pattern.support.play.api.libs.json

import anorm.Id
import org.specs2.mutable._
import play.api.libs.json._
import nu.pattern.support.play.api.libs.json.Implicits._

class IdReadsSpec extends Specification {

  def test[E](input: JsValue, wrapped: E)(implicit r: Reads[Id[E]]) {
    s"""A "$input"""" should {
      s"read as ${classOf[Id[_]]} with $wrapped (${wrapped.getClass.getName})" in {
        val expected: JsResult[Id[E]] = JsSuccess(Id(wrapped))
        val actual: JsResult[Id[E]] = r.reads(input)
        actual must beEqualTo(expected)
      }
    }
  }

  test(JsString("10"), "10")
  test(JsNumber(10L), 10L)
  test(JsNumber(10.01), 10.01)

}
