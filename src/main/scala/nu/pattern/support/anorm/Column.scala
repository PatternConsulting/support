package nu.pattern.support.anorm

import anorm.{Id, Pk, MetaDataItem, TypeDoesNotMatch}
import anorm.Column._

/**
 * [[anorm.Column]] companion, providing default conversions.
 */
object Column {
  implicit def columnToPk[T]: anorm.Column[Pk[T]] = nonNull { (value, meta) =>
    val MetaDataItem(qualified, nullable, clazz) = meta
    value match {
      case v: T => Right(Id(v))
      case _ => Left(TypeDoesNotMatch(s"Cannot convert $value: ${value.asInstanceOf[AnyRef].getClass} to Pk for column $qualified"))
    }
  }
}
