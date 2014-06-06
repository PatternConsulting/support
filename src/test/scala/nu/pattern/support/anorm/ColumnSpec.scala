package nu.pattern.support.anorm

import acolyte.QueryResult
import acolyte.Acolyte.{connection, handleQuery}
import acolyte.Implicits._
import acolyte.RowLists._
import anorm.{Id, SQL}
import org.specs2.mutable._
import anorm.SqlParser._
import nu.pattern.support.anorm.Column._
import java.util.UUID

class ColumnSpec extends Specification {

  s"String column mapped as UUID" should {
    val expected = UUID.randomUUID

    "be parsed from a properly-formatted string" in withQueryResult(stringList :+ expected.toString) {
      implicit con =>
        SQL("SELECT c").as(scalar[UUID].single) aka s"parsed UUID" must_== expected
    }

    "not be parsed from an incorrectly-formatted string" in withQueryResult(stringList :+ "bogus") {
      implicit con =>
        SQL("SELECT c").as(scalar[UUID].single) must throwA[Exception]
    }

    "not be parsed from a long" in withQueryResult(longList :+ 10L) {
      implicit con =>
        SQL("SELECT c").as(scalar[UUID].single) must throwA[Exception]
    }
  }

  s"Integer column mapped as Id" should {
    val expected = Id(10)

    "be parsed from an integer" in withQueryResult(intList :+ expected.get) {
      implicit con =>
        SQL("SELECT c").as(scalar[Id[Int]].single) aka s"parsed Id" must_== expected
    }

    "not be parsed from a long" in withQueryResult(longList :+ 10L) {
      implicit con =>
        SQL("SELECT c").as(scalar[Id[Int]].single) must throwA[Exception]
    }
  }

  def withQueryResult[A](r: QueryResult)(f: java.sql.Connection => A): A =
    f(connection(handleQuery { _ => r}))

}
