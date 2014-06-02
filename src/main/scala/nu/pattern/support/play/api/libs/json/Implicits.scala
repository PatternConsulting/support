package nu.pattern.support.play.api.libs.json

import java.util.UUID

import play.api.libs.json.Format

object Implicits {
  implicit val uuidF: Format[UUID] = UUIDFormat
}
