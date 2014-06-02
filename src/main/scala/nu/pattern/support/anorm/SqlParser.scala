package nu.pattern.support.anorm

import java.util.UUID

import anorm.RowParser
import anorm.SqlParser._

object SqlParser {

  /**
   * Parses specified column as float.
   *
   * {{{
   * import java.util.UUID
   * import nu.pattern.support.anorm.{ SQL, SqlParser }
   *
   * val t: (UUID, String) = SQL("SELECT a, b FROM test")
   *   .as(SqlParser.uuid("a") ~ SqlParser.str("b") map (* SqlParser.flatten) single)
   * }}}
   */
  def uuid(columnName: String)(implicit c: anorm.Column[UUID]): RowParser[UUID] =
    get[UUID](columnName)(c)

}
