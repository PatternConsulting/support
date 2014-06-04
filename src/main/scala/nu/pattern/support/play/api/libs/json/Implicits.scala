package nu.pattern.support.play.api.libs.json

import java.util.UUID

import play.api.libs.json._
import anorm.{NotAssigned, Pk}
import anorm.Id
import play.api.libs.json.JsSuccess

object Implicits {

  implicit val uuidF: Format[UUID] = UUIDFormat

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
