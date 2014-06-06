package nu.pattern.support.anorm

import anorm.{Id, Pk, MetaDataItem, TypeDoesNotMatch}
import anorm.Column._
import scala.reflect.ClassTag

/**
 * [[anorm.Column]] companion, providing default conversions.
 */
object Column {
  implicit def columnToPk[T: ClassTag]: anorm.Column[Pk[T]] = nonNull { (value, meta) =>
    val MetaDataItem(qualified, nullable, clazz) = meta
    Id(value) match {
      case v: Pk[T] => Right(v)
      case _ => Left(TypeDoesNotMatch(s"Cannot convert $value: ${value.asInstanceOf[AnyRef].getClass} to Pk for column $qualified"))
    }
  }
}
