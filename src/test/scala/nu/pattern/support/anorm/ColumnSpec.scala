package nu.pattern.support.anorm

import acolyte.QueryResult
import acolyte.Acolyte.{connection, handleQuery}
import acolyte.Implicits._
import acolyte.RowLists._
import anorm.{Id, SQL}
import org.specs2.mutable._
import anorm.SqlParser._
import nu.pattern.support.anorm.Column._

class ColumnSpec extends Specification {

  "Integer column mapped as Pk" should {
    val expected = Id(10)

    "be parsed from an integer" in withQueryResult(intList :+ expected.get) {
      implicit con =>
        SQL("SELECT c").as(scalar[Id[Int]].single) aka "parsed Pk" must_== expected
    }

    "not be parsed from long" in withQueryResult(longList :+ 10L) {
      implicit con =>
        SQL("SELECT c").as(scalar[Id[Int]].single) must throwA[RuntimeException]
    }
  }

  def withQueryResult[A](r: QueryResult)(f: java.sql.Connection => A): A =
    f(connection(handleQuery { _ => r}))

}
