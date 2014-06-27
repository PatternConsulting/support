package nu.pattern.support.play.api.libs.json

import java.util.UUID

import play.api.libs.json._
import anorm.{NotAssigned, Pk}
import scala.util.Try
import anorm.Id
import play.api.libs.json.JsSuccess
import scala.util.Success
import scala.util.Failure
import play.api.libs.json.JsString
import play.api.data.validation.ValidationError

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

  /** Almost identical in convention to [[Writes.OptionWrites]]. [[Pk]] is simply another value-holder type that speaks to specific needs. */
  implicit def PkWrites[T](implicit w: Writes[T]): Writes[Pk[T]] = new Writes[Pk[T]] {
    def writes(o: Pk[T]) = o match {
      case Id(value) => w.writes(value)
      case NotAssigned => JsNull
    }
  }

  /** Almost identical in convention to [[Reads.OptionReads]]. [[Pk]] is simply another value-holder type that speaks to specific needs. */
  implicit def PkReads[T](implicit r: Reads[T]): Reads[Pk[T]] = new Reads[Pk[T]] {
    def reads(js: JsValue): JsResult[Pk[T]] = r.reads(js).fold(e => JsSuccess(NotAssigned), v => JsSuccess(Id(v)))
  }

  implicit def IdReads[T](implicit r: Reads[T]): Reads[Id[T]] = new Reads[Id[T]] {
    override def reads(js: JsValue): JsResult[Id[T]] = r.reads(js).map(Id(_))
  }

  implicit object NotAssignedReads extends Reads[Pk[Nothing]] {
    override def reads(js: JsValue): JsResult[Pk[Nothing]] = JsSuccess(NotAssigned)
  }

  implicit def IdWrites[T](implicit w: Writes[T]): Writes[Id[T]] = new Writes[Id[T]] {
    override def writes(o: Id[T]): JsValue = w.writes(o.get)
  }

  implicit object NotAssignedWrites extends Writes[Pk[Nothing]] {
    override def writes(o: Pk[Nothing]): JsValue = JsNull
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
