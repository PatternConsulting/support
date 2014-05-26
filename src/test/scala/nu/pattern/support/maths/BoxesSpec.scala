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

    "exist for boxes sharing adjacent points in a grid" in {
      val rows = 3
      val columns = 3

      val input: Seq[Box] =
        (for (r <- 0 to rows - 1) yield {
          for (c <- 0 to columns - 1) yield {
            val x0 = c * 10
            val y0 = r * 10
            val x1 = x0 + 10
            val y1 = y0 + 10
            Box(x0, y0, x1, y1)
          }
        }).flatten

      /* Vertical intersections. */
      val expected0: Seq[Box] =
        (for (r <- 0 to rows - 1) yield {
          for (c <- 1 to columns - 1) yield {
            val x0 = c * 10
            val y0 = r * 10
            val x1 = x0
            val y1 = y0 + 10
            Box(x0, y0, x1, y1)
          }
        }).flatten

      /* Horizontal intersections. */
      val expected1: Seq[Box] =
        (for (c <- 0 to columns - 1) yield {
          for (r <- 1 to rows - 1) yield {
            val x0 = c * 10
            val y0 = r * 10
            val x1 = x0 + 10
            val y1 = y0
            Box(x0, y0, x1, y1)
          }
        }).flatten

      /* Point intersections. */
      val expected2: Seq[Box] =
        (for (c <- 1 to columns - 1) yield {
          for (r <- 1 to rows - 1) yield {
            val x0 = c * 10
            val y0 = r * 10
            val x1 = x0
            val y1 = y0
            Box(x0, y0, x1, y1)
          }
        }).flatten

      val expected = expected0 ++ expected1 ++ expected2

      assert(rows * (columns - 1) == expected0.size, "Calculation producing vertical intersections was wrong.")
      assert((rows - 1) * columns == expected1.size, "Calculation producing horizontal intersections was wrong.")
      assert((rows - 1) * (columns - 1) == expected2.size, "Calculation producing point intersections was wrong.")

      val actual = input.intersections

      actual must containAllOf(expected)
    }
  }

}
