package archery

import org.specs2.mutable._
import nu.pattern.support.Resources

class RTreeOpsSpec extends Specification {

  private implicit object StringPointParser extends Resources.Parser[Seq[String], Point] {
    override def apply(v: Seq[String]): Point = Point(v(0).toFloat, v(1).toFloat)
  }

  private def grid(w: Int, h: Int, spaceX: Int = 1, spaceY: Int = 1): Seq[Point] =
    (for (y <- 0 to w - 1) yield {
      for (x <- 0 to h - 1) yield {
        Point((spaceX * x).toFloat, (spaceY * y).toFloat)
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

      actual.size must beEqualTo(expected.size)
      actual must containTheSameElementsAs(expected)
    }

    "return only reachable points, with an unbounded K, from a corner." in {
      val expected = points.filter(withinCircle(0, 0, 10))
      val actual = space.localK(Point(0, 0), 10, Int.MaxValue).map(_.value)

      actual.size must beEqualTo(expected.size)
      actual must containTheSameElementsAs(expected)
    }
  }

  "Local k-Neighbor searches of non-uniform points" should {
    val points = Resources.read[Point]("/variable-density-cluster-1.csv").toSeq
    val entries = points.map(p => Entry(p, p))
    val space = RTree(entries: _*)

    "return only reachable points, with an unbounded K, from the center." in {
      val expected = points.filter(withinCircle(0.2, 0.2, 0.1))
      val actual = space.localK(Point(0.2F, 0.2F), 0.1, Int.MaxValue).map(_.value)

      actual.size must beEqualTo(expected.size)
      actual must containTheSameElementsAs(expected)
    }

    "return only reachable points, with an unbounded K, from a corner." in {
      val expected = points.filter(withinCircle(0, 0, 0.2))
      val actual = space.localK(Point(0, 0), 0.2, Int.MaxValue).map(_.value)

      actual.size must beEqualTo(expected.size)
      actual must containTheSameElementsAs(expected)
    }
  }

  "Traversal across uniform points" should {
    val width = 10
    val height = 10
    val points = grid(width, height)
    val entries = points.map(p => Entry(p, p))
    val space = RTree(entries: _*)

    "from a known point, return all points when distance is enough for spacing" in {
      val expected = points
      val actual = space.traverse(points(0), 1.1).map(_.value)

      actual.size must beEqualTo(expected.size)
      actual must containTheSameElementsAs(expected)
    }

    "from a foreign point within the cluster, return all points when distance is enough for spacing" in {
      val expected = points
      val actual = space.traverse(Point(0.5F, 0.5F), 1.1).map(_.value)

      actual.size must beEqualTo(expected.size)
      actual must containTheSameElementsAs(expected)
    }

    "from a foreign point outside but within threshold, return all points when distance is enough for spacing" in {
      val expected = points
      val actual = space.traverse(Point(-0.5F, -0.5F), 1.1).map(_.value)

      actual.size must beEqualTo(expected.size)
      actual must containTheSameElementsAs(expected)
    }

    "from a foreign point outside and without threshold, return no points" in {
      val expected = Nil
      val actual = space.traverse(Point(width + 10, height + 10), 1.1).map(_.value)

      actual.size must beEqualTo(expected.size)
      actual must containTheSameElementsAs(expected)
    }
  }

  "Traversal across non-uniform points" in {
    val cluster1 = Resources.read[Point]("/variable-density-cluster-1.csv").toSeq
    //val cluster2 = Resources.read[Point]("/variable-density-cluster-2.csv").toSeq
    val cluster3 = Resources.read[Point]("/variable-density-cluster-3.csv").toSeq

    val points = cluster1 ++ /*cluster2 ++*/ cluster3
    val entries = points.map(p => Entry(p, p))
    val space = RTree(entries: _*)

    "return only points reachable from a search point (from cluster 1)" in {
      val expected = cluster1
      val actual = space.traverse(cluster1(0), 0.05).map(_.value)

      actual.size must beEqualTo(expected.size)
      actual must containTheSameElementsAs(expected)
    }

    "return only points reachable from a search point (from cluster 2)" in {
      val expected = cluster3
      val actual = space.traverse(cluster3(0), 0.2).map(_.value)

      actual.size must beEqualTo(expected.size)
      actual must containTheSameElementsAs(expected)
    }
  }
}
