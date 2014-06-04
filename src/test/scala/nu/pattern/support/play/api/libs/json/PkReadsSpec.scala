package nu.pattern.support.play.api.libs.json

import anorm.{Pk, NotAssigned, Id}
import org.specs2.mutable._
import play.api.libs.json._
import play.api.libs.json.JsString
import play.api.libs.json.JsNumber
import nu.pattern.support.play.api.libs.json.Implicits._

class PkReadsSpec extends Specification {

  def test[E](input: JsValue, wrapped: E)(implicit r: Reads[Pk[E]]) {
    s"""A "$input"""" should {
      s"read as ${classOf[Id[_]]} with $wrapped (${wrapped.getClass.getName})" in {
        val expected: JsResult[Pk[E]] = JsSuccess(Id(wrapped))
        val actual: JsResult[Pk[E]] = r.reads(input)
        actual must beEqualTo(expected)
      }

      s"read as $NotAssigned when $JsNull" in {
        val expected: JsResult[Pk[E]] = JsSuccess(NotAssigned)
        val actual: JsResult[Pk[E]] = r.reads(JsNull)
        actual must beEqualTo(expected)
      }
    }
  }

  test(JsString("10"), "10")
  test(JsNumber(10L), 10L)
  test(JsNumber(10.01), 10.01)

}
