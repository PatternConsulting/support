package nu.pattern.support.formats

import play.api.libs.json._
import java.util.UUID

object Implicits {
  implicit val uuidF: Format[UUID] = UUIDFormat
}
