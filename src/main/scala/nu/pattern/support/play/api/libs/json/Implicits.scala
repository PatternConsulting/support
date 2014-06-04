package nu.pattern.support.play.api.libs.json

import java.util.UUID

import play.api.libs.json._
import anorm.{NotAssigned, Pk}
import anorm.Id
import play.api.libs.json.JsSuccess
import play.api.data.validation.ValidationError
import scala.util.{Failure, Success, Try}

object Implicits {

  implicit object UUIDWrites extends Writes[UUID] {
    def writes(o: UUID) = JsString(o.toString)
  }

  implicit object UUIDReads extends Reads[UUID] {
    def reads(js: JsValue) = js match {
      case JsString(s) =>
        Try(UUID.fromString(s.trim)) match {
          case Success(u) => JsSuccess(u)
          case Failure(e) => JsError( s"""Invalid UUID \"$s\". (${e.getMessage})""")
        }
      case _ => JsError(Seq(JsPath() -> Seq(ValidationError("error.expected.jsstring"))))
    }
  }

  implicit def PkWrites[T](implicit w: Writes[T]): Writes[Pk[T]] = new Writes[Pk[T]] {
    def writes(o: Pk[T]) = o match {
      case Id(value) => w.writes(value)
      case NotAssigned => JsNull
    }
  }

  implicit def PkReads[T](implicit r: Reads[T]): Reads[Pk[T]] = new Reads[Pk[T]] {
    def reads(js: JsValue): JsResult[Pk[T]] = r.reads(js).fold(e => JsSuccess(NotAssigned), v => JsSuccess(Id(v)))
  }

}
