package nu.pattern.support.maths

import Implicits.BoxesOps
import archery.Box
import org.specs2.mutable._

class BoxesSpec extends Specification {

  "Intersections" should {
    //    "not exist for disjoint boxes" in {
    //      val boxes: Seq[Box] = for (i <- 0 to 9) yield {
    //        val x0 = i * 10
    //        val y0 = i * 10
    //        val x1 = x0 + 5
    //        val y1 = y0 + 5
    //        Box(x0, y0, x1, y1)
    //      }
    //
    //      boxes.intersections must beEmpty
    //    }

    "exist for boxes sharing points (even for boxes with no area)" in {
      val boxes: Seq[Box] = for (i <- 0 to 3) yield {
        val x0 = i * 10
        val y0 = 0
        val x1 = x0 + 10
        val y1 = y0 + 10
        Box(x0, y0, x1, y1)
      }

      val expected: Seq[Box] = for (i <- 1 to 3) yield {
        val x0 = i * 10
        val y0 = 0
        val x1 = x0
        val y1 = y0 + 10
        Box(x0, y0, x1, y1)
      }

      val actual = boxes.intersections

      actual.size must beEqualTo(expected.size)
      actual must containAllOf(expected)
    }
  }

}
