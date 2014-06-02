package nu.pattern.support.anorm

import java.util.UUID

import anorm.{MetaDataItem, TypeDoesNotMatch}
import anorm.Column._

/**
 * [[anorm.Column]] companion, providing default conversions.
 */
object Column {
  implicit val columnToUUID: anorm.Column[UUID] = nonNull { (value, meta) =>
    val MetaDataItem(qualified, nullable, clazz) = meta
    value match {
      case s: String => Right(UUID.fromString(s))
      case _ => Left(TypeDoesNotMatch(s"Cannot convert $value: ${value.asInstanceOf[AnyRef].getClass} to UUID for column $qualified"))
    }
  }
}
