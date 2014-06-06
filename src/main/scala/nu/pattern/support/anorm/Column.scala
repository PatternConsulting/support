package nu.pattern.support.anorm

import anorm._
import anorm.Column._
import anorm.Id
import java.util.UUID

/**
 * [[anorm.Column]] companion, providing default conversions.
 */
object Column {
  /**
   * An alternative to [[anorm.Column.columnToUUID]] that allows parsing [[String]] values.
   */
  implicit val columnToUUID: Column[UUID] = nonNull { (value, meta) =>
    val MetaDataItem(qualified, nullable, clazz) = meta
    value match {
      case s: String => Right(UUID.fromString(s))
      case d: UUID => Right(d)
      case _ => Left(TypeDoesNotMatch(s"Cannot convert $value: ${value.asInstanceOf[AnyRef].getClass} to UUID for column $qualified"))
    }
  }

  implicit def columnToId[T](implicit c: Column[T]): Column[Id[T]] =
    nonNull { (value, meta) => c(value, meta).map(Id(_))}
}
