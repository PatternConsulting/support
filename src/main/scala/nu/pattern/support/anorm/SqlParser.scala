package nu.pattern.support.anorm

import anorm.{Pk, RowParser}
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
  def uuid(columnName: String)(implicit c: anorm.Column[UUID]): RowParser[UUID] =
    get[UUID](columnName)(c)

  /**
   * Parses specified column as [[Pk]].
   *
   * {{{
   * import anorm.Pk
   * import nu.pattern.support.anorm.SqlParser._
   *
   * val t: (Pk[Int], String) = SQL("SELECT a, b FROM test")
   *   .as(pk[Int]("a") ~ SqlParser.str("b") map (* SqlParser.flatten) single)
   * }}}
   */
  def pk[T](columnName: String)(implicit c: anorm.Column[Pk[T]]): RowParser[Pk[T]] =
    get[Pk[T]](columnName)(c)

}
