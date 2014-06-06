package nu.pattern.support.play.api.libs.json

import org.specs2.mutable._
import nu.pattern.support.play.api.libs.json.Implicits.{IdReads, IdWrites, NotAssignedReads, NotAssignedWrites}
import anorm.{Pk, NotAssigned}
import play.api.libs.json.Json._
import anorm.Id
import play.api.libs.json._
import play.api.libs.functional.syntax._

class ImplicitsSpec extends Specification {

  case class IntIdModel(id: Id[Int], value: String)

  object IntIdModel {
    implicit val format = Json.format[IntIdModel]
    val o = IntIdModel(Id(10), classOf[IntIdModel].getSimpleName)
    val js = obj("id" -> 10, "value" -> classOf[IntIdModel].getSimpleName)
  }

  s"Object ${IntIdModel.o}" should {
    s"write to ${IntIdModel.js}" in {
      val expected = IntIdModel.js
      val actual = toJson(IntIdModel.o)
      actual must beEqualTo(expected)
    }

    s"read from ${IntIdModel.js}" in {
      val expected = JsSuccess(IntIdModel.o)
      val actual = fromJson[IntIdModel](IntIdModel.js)
      actual must beEqualTo(expected)
    }
  }

  case class NotAssignedModel(id: Pk[Nothing], otherId: Pk[Nothing], value: String)

  object NotAssignedModel {
    implicit val writes = Json.writes[NotAssignedModel]

    /* There is no way around this: assuming NotAssigned for values that don't exist in the JsObject. */
    implicit val reads = (
      (__ \ "id").read[Pk[Nothing]] and
        (__ \ "otherId").read[Pk[Nothing]].orElse(Reads.pure(NotAssigned)) and
        (__ \ "value").read[String]
      )(NotAssignedModel.apply _)

    val o = NotAssignedModel(NotAssigned, NotAssigned, classOf[NotAssignedModel].getSimpleName)
    val jsIn = obj("id" -> JsNull, "value" -> classOf[NotAssignedModel].getSimpleName)
    val jsOut = obj("id" -> JsNull, "otherId" -> JsNull, "value" -> classOf[NotAssignedModel].getSimpleName)
  }

  s"Object ${NotAssignedModel.o}" should {
    s"write to ${NotAssignedModel.jsOut}" in {
      val expected = NotAssignedModel.jsOut
      val actual = toJson(NotAssignedModel.o)
      actual must beEqualTo(expected)
    }

    s"read from ${NotAssignedModel.jsIn}" in {
      val expected = JsSuccess(NotAssignedModel.o)
      val actual = fromJson[NotAssignedModel](NotAssignedModel.jsIn)
      actual must beEqualTo(expected)
    }
  }

}
