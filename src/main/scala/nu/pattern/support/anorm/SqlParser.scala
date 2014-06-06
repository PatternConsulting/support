package nu.pattern.support.anorm

import anorm.{Pk, Id, RowParser}
import anorm.SqlParser._
import java.util.UUID

object SqlParser {

  /**
   * Parses specified column as [[java.util.UUID]].
   *
   * {{{
   * import java.util.UUID
   * import nu.pattern.support.anorm.SqlParser._
   *
   * val t: (UUID, String) = SQL("SELECT a, b FROM test")
   *   .as(uuid("a") ~ SqlParser.str("b") map (* SqlParser.flatten) single)
   * }}}
   */
  def uuid(columnName: String): RowParser[UUID] =
    get[UUID](columnName)(Column.columnToUUID)

  /**
   * Parses specified column as [[Id]].
   *
   * {{{
   * import anorm.Id
   * import nu.pattern.support.anorm.SqlParser._
   *
   * val t: (Pk[Int], String) = SQL("SELECT a, b FROM test")
   *   .as(pk[Int]("a") ~ SqlParser.str("b") map (* SqlParser.flatten) single)
   * }}}
   */
  def pk[T](columnName: String)(implicit c: anorm.Column[Id[T]]): RowParser[Id[T]] =
    get[Id[T]](columnName)(c)

}
