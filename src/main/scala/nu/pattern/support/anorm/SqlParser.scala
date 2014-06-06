package nu.pattern.support.anorm

import anorm.{Pk, RowParser}
import anorm.SqlParser._

object SqlParser {

  /**
   * Parses specified column as float.
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
