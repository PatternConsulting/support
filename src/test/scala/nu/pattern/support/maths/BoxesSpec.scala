package nu.pattern.support.maths

import java.awt.Rectangle

import org.specs2.mutable._

import Boxes._

class BoxesSpec extends Specification {

  val r0 = new Rectangle(10, 10, 100, 100)
  val r1 = new Rectangle(20, 20, 200, 200)
  val s0 = Seq(r0, r1)

  Boxes.intersections(s0)

  val b0 = Box(10, 10, 20, 20)
  val b1 = Box(20, 20, 30, 30)
  val s1 = Seq(b0, b1)

  Boxes.intersections(s1)

}
