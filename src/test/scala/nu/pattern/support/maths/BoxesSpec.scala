package nu.pattern.support.maths

import Implicits.BoxesOps
import archery.Box
import org.specs2.mutable._

class BoxesSpec extends Specification {

  "Intersections" should {
    "not exist for disjoint boxes" in {
      val boxes: Seq[Box] = for (i <- 0 to 9) yield {
        Box(i * 10, i * 10, i * 10 + 5, i * 10 + 5)
      }

      boxes.intersections must beEmpty
    }
  }

}
