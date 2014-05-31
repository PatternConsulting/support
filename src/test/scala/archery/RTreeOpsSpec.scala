package archery

import org.specs2.mutable._
import nu.pattern.support.Resources

class RTreeOpsSpec extends Specification {
  def grid(w: Int, h: Int, spaceX: Int = 0, spaceY: Int = 0): Seq[Point] =
    (for (y <- 0 to w) yield {
      for (x <- 0 to h) yield {
        Point((spaceX + x).toFloat, (spaceY + y).toFloat)
      }
    }).flatten

  def withinCircle[T](x: T, y: T, r: T)(p: Point)(implicit ev: Numeric[T]) =
    p.distance(Point(ev.toFloat(x), ev.toFloat(y))) < ev.toFloat(r)

  "Local k-Neighbor searches of uniform points" should {

    val points = grid(100, 100)
    val entries = points.map(p => Entry(p, p))
    val space = RTree(entries: _*)

    "return only reachable points, with an unbounded K, from the center." in {
      val expected = points.filter(withinCircle(50, 50, 10))
      val actual = space.localK(Point(50, 50), 10, Int.MaxValue).map(_.value)
      actual must containTheSameElementsAs(expected)
    }

    "return only reachable points, with an unbounded K, from a corner." in {
      val expected = points.filter(withinCircle(0, 0, 10))
      val actual = space.localK(Point(0, 0), 10, Int.MaxValue).map(_.value)
      actual must containTheSameElementsAs(expected)
    }

  }

  "Local k-Neighbor searches of variable points" should {
    implicit object StringPointParser extends Resources.Parser[Seq[String], Point] {
      override def apply(v: Seq[String]): Point = Point(v(0).toFloat, v(1).toFloat)
    }

    val points = Resources.read[Point]("/variable-density-cluster-1.csv").toSeq
    val entries = points.map(p => Entry(p, p))
    val space = RTree(entries: _*)

    "return only reachable points, with an unbounded K, from the center." in {
      val expected = points.filter(withinCircle(0.2, 0.2, 0.1))
      val actual = space.localK(Point(0.2F, 0.2F), 0.1, Int.MaxValue).map(_.value)
      actual must containTheSameElementsAs(expected)
    }

    "return only reachable points, with an unbounded K, from a corner." in {
      val expected = points.filter(withinCircle(0, 0, 0.2))
      val actual = space.localK(Point(0, 0), 0.2, Int.MaxValue).map(_.value)
      actual must containTheSameElementsAs(expected)
    }

  }
}
