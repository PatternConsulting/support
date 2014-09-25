package nu.pattern.support.play.api.libs.json

import java.util.UUID

import play.api.data.validation.ValidationError
import play.api.libs.json._

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

  implicit class JsObjectOps(value: JsObject) {
    /**
     * Performs a deep merge between the local `value` and an exemplar object. The source `value` may not be a valid `T`, but it's values will be applied over a [[JsObject]] representation of `other`, and the result _must_ be readable back to a `T`.
     *
     * @param other Original model used to construct
     * @param r [[Reads]] that brings the merged [[JsObject]] back to a `T`.
     * @param w [[Writes]] that converts `other` to [[JsObject]] for merging with `value`.
     * @tparam T Ultimate concrete type.
     *
     * @return The local `value` deeply-merged onto `other`.
     */
    def deepMergeAs[T](other: T)(implicit r: Reads[T], w: Writes[T]): T = {
      val o = w.writes(other).as[JsObject]
      val m = o.deepMerge(value)
      m.as[T]
    }
  }

}
