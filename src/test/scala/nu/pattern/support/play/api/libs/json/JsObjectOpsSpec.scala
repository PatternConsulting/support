package nu.pattern.support.play.api.libs.json

import org.specs2.mutable._
import play.api.libs.json._
import Json._
import Implicits.JsObjectOps

class JsObjectOpsSpec extends Specification {

  case class Address(street: String, city: String, country: Option[String])

  implicit val addressR = reads[Address]

  implicit val addressW = writes[Address]

  case class User(name: String, nickname: Option[String], age: Int, home: Address)

  implicit val userR = reads[User]

  implicit val userW = writes[User]

  val sample =
    User(
      "Jane Smith",
      None,
      31,
      Address(
        "1234 East Street",
        "Townsville",
        Some("United States")
      )
    )

  "Deep merging as" should {
    "preserve the exemplar for empty input" in {
      obj().deepMergeAs(sample) must beEqualTo(sample)
    }

    "not fail for spurious fields" in {
      obj("foo" -> "bear").deepMergeAs(sample) must beEqualTo(sample)
    }

    "set surface values" in {
      val expected = sample.copy(name = "John Smith")
      obj("name" -> "John Smith").deepMergeAs(sample) must beEqualTo(expected)
    }

    "set optional surface values" in {
      val expected = sample.copy(nickname = Some("Jannet"))
      obj("nickname" -> "Jannet").deepMergeAs(sample) must beEqualTo(expected)
    }

    "unset optional surface values" in {
      val expected = sample.copy(nickname = None)
      obj("nickname" -> JsNull).deepMergeAs(sample) must beEqualTo(expected)
    }

    "set nested values" in {
      val expected = sample.copy(home = sample.home.copy(street = "5678 West Street"))
      obj("home" -> obj("street" -> "5678 West Street")).deepMergeAs(sample) must beEqualTo(expected)
    }

    "unset optional nested values" in {
      val expected = sample.copy(home = sample.home.copy(country = None))
      obj("home" -> obj("country" -> JsNull)).deepMergeAs(sample) must beEqualTo(expected)
    }
  }
}
