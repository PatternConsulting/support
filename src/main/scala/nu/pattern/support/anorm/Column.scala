package nu.pattern.support.anorm

import anorm._
import anorm.Column._
import anorm.Id

/**
 * [[anorm.Column]] companion, providing default conversions.
 */
object Column {
  implicit def columnToId[T](implicit c: Column[T]): Column[Id[T]] =
    nonNull { (value, meta) => c(value, meta).map(Id(_)) }
}
