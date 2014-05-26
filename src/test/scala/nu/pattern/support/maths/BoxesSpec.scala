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

    "exist for 1 x 1 grid of adjacent boxes" in {
      val fixtures = Fixtures.grid(1, 1)
      val actual = fixtures.input.intersections
      actual must containAllOf(fixtures.expected)
    }

    "exist for 4 x 4 grid of adjacent boxes" in {
      val fixtures = Fixtures.grid(4, 4)
      val actual = fixtures.input.intersections
      actual must containAllOf(fixtures.expected)
    }

    "exist for 3 disjoint 2 x 2 grids of adjacent boxes" in {
      val boxWidth = 10
      val boxHeight = 10

      val gridWidth = 2
      val gridHeight = 2

      val fixtures: Seq[Fixtures] =
        for (i <- 0 to 3) yield {
          val horizontalOffset = (boxWidth * gridWidth) * i + 5

          Fixtures
            .grid(gridWidth, gridHeight, boxWidth, boxHeight, horizontalOffset = horizontalOffset)
        }

      val input: Seq[Box] = fixtures.map(_.input).flatten
      val expected: Seq[Box] = fixtures.map(_.expected).flatten

      val actual = input.intersections
      actual must containAllOf(expected)
    }

    "exist for 4 stair-cased boxes sharing 3 points" in {
      val input =
        Box(10, 10, 20, 20) ::
          Box(20, 20, 30, 30) ::
          Box(30, 30, 40, 40) ::
          Box(40, 40, 50, 50) ::
          Nil

      val expected =
        Box(20, 20, 20, 20) ::
          Box(30, 30, 30, 30) ::
          Box(40, 40, 40, 40) ::
          Nil

      val actual = input.intersections
      actual must containAllOf(expected)
    }

    "exist for 4 stair-cased boxes with 3 overlaps and 0 shared points" in {
      val input =
        Box(10, 10, 25, 25) ::
          Box(20, 20, 35, 35) ::
          Box(30, 30, 45, 45) ::
          Box(40, 40, 55, 55) ::
          Nil

      val expected =
        Box(20, 20, 25, 25) ::
          Box(30, 30, 35, 35) ::
          Box(40, 40, 45, 45) ::
          Nil

      val actual = input.intersections
      actual must containAllOf(expected)
    }

    "exist for 4 stair-cased boxes with 3 overlaps and 2 shared points" in {
      val input =
        Box(10, 10, 30, 30) ::
          Box(20, 20, 40, 40) ::
          Box(30, 30, 50, 50) ::
          Box(40, 40, 60, 60) ::
          Nil

      val expected =
        Box(20, 20, 30, 30) ::
          Box(30, 30, 30, 30) ::
          Box(30, 30, 40, 40) ::
          Box(40, 40, 40, 40) ::
          Box(40, 40, 50, 50) ::
          Nil

      val actual = input.intersections
      actual must containAllOf(expected)
    }
  }

  case class Fixtures(input: Seq[Box], expected: Seq[Box])

  object Fixtures {

    def grid(columns: Int, rows: Int, width: Int = 10, height: Int = 10, horizontalOffset: Int = 0, verticalOffset: Int = 0): Fixtures = {
      val input: Seq[Box] =
        (for (r <- 0 to rows - 1) yield {
          for (c <- 0 to columns - 1) yield {
            val x0 = c * width
            val y0 = r * height
            val x1 = x0 + width
            val y1 = y0 + height

            Box(x0 + horizontalOffset, y0 + verticalOffset, x1 + horizontalOffset, y1 + verticalOffset)
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

            Box(x0 + horizontalOffset, y0 + verticalOffset, x1 + horizontalOffset, y1 + verticalOffset)
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

            Box(x0 + horizontalOffset, y0 + verticalOffset, x1 + horizontalOffset, y1 + verticalOffset)
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

            Box(x0 + horizontalOffset, y0 + verticalOffset, x1 + horizontalOffset, y1 + verticalOffset)
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
