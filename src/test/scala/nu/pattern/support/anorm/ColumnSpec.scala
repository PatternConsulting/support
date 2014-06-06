package nu.pattern.support.anorm

import acolyte.QueryResult
import acolyte.Acolyte.{connection, handleQuery}
import acolyte.Implicits._
import acolyte.RowLists._
import anorm.{Pk, Id, SQL}
import org.specs2.mutable._
import anorm.SqlParser._
import nu.pattern.support.anorm.Column._

class ColumnSpec extends Specification {

  "Integer column mapped as Pk" should {
    val expected = Id(10)

    "be parsed from integer" in withQueryResult(intList :+ expected.get) {
      implicit con =>
        SQL("SELECT c").as(scalar[Pk[Int]].single) aka "parsed Pk" must_== expected
    }

    "not be parsed from long" in withQueryResult(longList :+ 10L) {
      implicit con =>
        SQL("SELECT c").as(scalar[Pk[Int]].single) aka "parsed Pk" must_== expected
    }
  }

  def withQueryResult[A](r: QueryResult)(f: java.sql.Connection => A): A =
    f(connection(handleQuery { _ => r}))

}
