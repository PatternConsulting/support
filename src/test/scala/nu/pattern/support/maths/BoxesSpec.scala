package nu.pattern.support.maths

import Implicits.BoxesOps
import archery.Box
import org.specs2.mutable._

class BoxesSpec extends Specification {

  "Intersections" should {
    "not exist for disjoint boxes" in {
      val count = 10

      val input: Seq[Box] =
        for (i <- 0 to count - 1) yield {
          val x0 = i * 10
          val y0 = i * 10
          val x1 = x0 + 5
          val y1 = y0 + 5
          Box(x0, y0, x1, y1)
        }

      input.intersections must beEmpty
    }

    "exist for identical boxes occupying the same location" in {
      val count = 10

      val input: Seq[Box] =
        for (i <- 0 to count - 1) yield {
          val x0 = 0
          val y0 = 0
          val x1 = x0 + 10
          val y1 = y0 + 10
          Box(x0, y0, x1, y1)
        }

      val expected = Box(0, 0, 10, 10) :: Nil

      val actual = input.intersections

      actual.size must beEqualTo(expected.size)
      actual must containAllOf(expected)
    }

    "exist for boxes sharing adjacent points in a 2 x 2 grid" in {
      val fixtures = Fixtures.grid(3, 3)
      val actual = fixtures.input.intersections
      actual must containAllOf(fixtures.expected)
    }

    "exist for boxes sharing adjacent points in a 3 x 3 grid" in {
      val fixtures = Fixtures.grid(3, 3)
      val actual = fixtures.input.intersections
      actual must containAllOf(fixtures.expected)
    }

    "exist for boxes sharing adjacent points in a 2 x 1 grid" in {
      val fixtures = Fixtures.grid(2, 1)
      val actual = fixtures.input.intersections
      actual must containAllOf(fixtures.expected)
    }

    "exist for boxes sharing adjacent points in a 1 x 2 grid" in {
      val fixtures = Fixtures.grid(1, 2)
      val actual = fixtures.input.intersections
      actual must containAllOf(fixtures.expected)
    }

    "exist for boxes sharing adjacent points in a 3 x 1 grid" in {
      val fixtures = Fixtures.grid(3, 1)
      val actual = fixtures.input.intersections
      actual must containAllOf(fixtures.expected)
    }

    "exist for boxes sharing adjacent points in a 1 x 3 grid" in {
      val fixtures = Fixtures.grid(1, 3)
      val actual = fixtures.input.intersections
      actual must containAllOf(fixtures.expected)
    }

    "exist for boxes sharing adjacent points in a 3 x 6 grid" in {
      val fixtures = Fixtures.grid(3, 6)
      val actual = fixtures.input.intersections
      actual must containAllOf(fixtures.expected)
    }

    "exist for boxes sharing adjacent points in a 6 x 3 grid" in {
      val fixtures = Fixtures.grid(6, 3)
      val actual = fixtures.input.intersections
      actual must containAllOf(fixtures.expected)
    }
  }

  case class Fixtures(input: Seq[Box], expected: Seq[Box])

  object Fixtures {

    def grid(columns: Int, rows: Int, width: Int = 10, height: Int = 10): Fixtures = {
      val input: Seq[Box] =
        (for (r <- 0 to rows - 1) yield {
          for (c <- 0 to columns - 1) yield {
            val x0 = c * width
            val y0 = r * height
            val x1 = x0 + width
            val y1 = y0 + height
            Box(x0, y0, x1, y1)
          }
        }).flatten

      /* Vertical intersections. */
      val expected0: Seq[Box] =
        (for (r <- 0 to rows - 1) yield {
          for (c <- 1 to columns - 1) yield {
            val x0 = c * width
            val y0 = r * height
            val x1 = x0
            val y1 = y0 + height
            Box(x0, y0, x1, y1)
          }
        }).flatten

      /* Horizontal intersections. */
      val expected1: Seq[Box] =
        (for (c <- 0 to columns - 1) yield {
          for (r <- 1 to rows - 1) yield {
            val x0 = c * width
            val y0 = r * height
            val x1 = x0 + width
            val y1 = y0
            Box(x0, y0, x1, y1)
          }
        }).flatten

      /* Point intersections. */
      val expected2: Seq[Box] =
        (for (c <- 1 to columns - 1) yield {
          for (r <- 1 to rows - 1) yield {
            val x0 = c * width
            val y0 = r * height
            val x1 = x0
            val y1 = y0
            Box(x0, y0, x1, y1)
          }
        }).flatten

      assert(rows * (columns - 1) == expected0.size, "Calculation producing vertical intersections was wrong.")
      assert((rows - 1) * columns == expected1.size, "Calculation producing horizontal intersections was wrong.")
      assert((rows - 1) * (columns - 1) == expected2.size, "Calculation producing point intersections was wrong.")

      val expected = expected0 ++ expected1 ++ expected2

      Fixtures(input, expected)
    }

  }

}
