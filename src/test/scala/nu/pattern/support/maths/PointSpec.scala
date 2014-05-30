package nu.pattern.support.maths

import org.specs2.mutable._

class PointSpec extends Specification {
  "Clusters" should {

    "are found for points staggered step-wise" in {
      val ε = 2

      val expected =
        for (c <- 0 to 30 by 10) yield {
          for (p <- 0 to 6 by ε) yield {
            Point(p + c, p + c)
          }
        }

      val input = expected.flatten.toSeq

      val actual = Point.clusters(input, ε + 1, 1)
      actual must beEqualTo(expected.map(_.toSet).toSet)
    }

    "are found for points arranged grid-wise" in {
      val ε = 2

      val expected: Seq[Seq[Point[Int]]] =
        for (c <- 0 to 30 by 10) yield {
          (for (y <- 0 to 6 by ε) yield {
            for (x <- 0 to 6 by ε) yield {
              Point(x + c, y + c)
            }
          }).flatten
        }

      val input = expected.flatten.toSeq

      val actual = Point.clusters(input, ε + 1, 1)
      actual must beEqualTo(expected.map(_.toSet).toSet)
    }
  }
}
