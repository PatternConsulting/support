package nu.pattern.support.formats

import play.api.libs.json._
import java.util.UUID
import play.api.libs.json.JsString
import play.api.libs.json.JsSuccess

object UUIDFormat extends Format[UUID] {
  val uuidP = "[a-f0-9]{8}-[a-f0-9]{4}-4[a-f0-9]{3}-[89aAbB][a-f0-9]{3}-[a-f0-9]{12}".r

  override def reads(v: JsValue): JsResult[UUID] = v match {
    case JsString(s) =>
      s.trim.toLowerCase match {
        case uuidP() =>
          JsSuccess(UUID.fromString(s))
        case _ =>
          JsError( s"""Incorrect UUID format for \"$s\".""")
      }
    case _ =>
      JsError( s"""Expected string value for UUID (got "$v").""")
  }

  override def writes(v: UUID): JsValue = JsString(v.toString)
}
