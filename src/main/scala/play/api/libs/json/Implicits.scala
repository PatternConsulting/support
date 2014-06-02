package play.api.libs.json

import java.util.UUID

object Implicits {
  implicit val uuidF: Format[UUID] = UUIDFormat
}
