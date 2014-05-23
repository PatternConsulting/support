package nu.pattern.support.formats

import play.api.libs.json._
import java.util.UUID
import play.api.libs.json.JsString
import play.api.libs.json.JsSuccess
import scala.util.{Failure, Success, Try}

object UUIDFormat extends Format[UUID] {
  override def reads(v: JsValue): JsResult[UUID] = v match {
    case JsString(s) =>
      Try(UUID.fromString(s.trim)) match {
        case Success(u) => JsSuccess(u)
        case Failure(e) => JsError( s"""Invalid UUID \"$s\". (${e.getMessage})""")
      }
    case _ =>
      JsError( s"""Expected string value for UUID (got "$v").""")
  }

  override def writes(v: UUID): JsValue = JsString(v.toString)
}
