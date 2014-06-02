package nu.pattern.support.anorm

import java.util.UUID

import acolyte.QueryResult
import acolyte.Acolyte.{connection, handleQuery}
import acolyte.Implicits._
import acolyte.RowLists.stringList
import anorm.SQL
import org.specs2.mutable._
import anorm.SqlParser._
import nu.pattern.support.anorm.Column._

class ColumnSpec extends Specification {

  "Column mapped as UUID" should {
    val expected = UUID.randomUUID()

    "be parsed from string" in withQueryResult(stringList :+ expected.toString) {
      implicit con =>
        SQL("SELECT c").as(scalar[UUID].single) aka "parsed char" must_== expected
    }
  }

  def withQueryResult[A](r: QueryResult)(f: java.sql.Connection => A): A =
    f(connection(handleQuery { _ => r}))

}
