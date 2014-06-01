package archery

import nu.pattern.support.Resources
import org.specs2.mutable._
import spire.math._

class RTreeOpsSpec extends Specification {

  private implicit object StringPointParser extends Resources.Parser[Seq[String], Point] {
    override def apply(v: Seq[String]): Point = Point(v(0).toFloat, v(1).toFloat)
  }

  private def grid[T](w: Int, h: Int)(implicit ev: Numeric[T]): Seq[(T, T)] =
    (for (y <- 0 to w - 1) yield {
      for (x <- 0 to h - 1) yield {
        (ev.fromType(x), ev.fromType(y))
      }
    }).flatten

  def withinCircle[T](x: T, y: T, r: T)(p: Point)(implicit ev: Numeric[T]) =
    p.distance(Point(ev.toFloat(x), ev.toFloat(y))) < ev.toFloat(r)

  "Traversal across uniform points" should {
    val width = 10
    val height = 10
    val points = grid[Float](width, height).map((Point.apply _).tupled)
    val entries = points.map(p => Entry(p, p))
    val space = RTree(entries: _*)

    "return all points when distance is enough for spacing, from a known point" in {
      val expected = points
      val actual = space.traverse(points(0), 1.1F).map(_.value)

      actual.size must beEqualTo(expected.size)
      actual must containTheSameElementsAs(expected)
    }

    "return all points when distance is enough for spacing, from a foreign point within the cluster" in {
      val expected = points
      val actual = space.traverse(Point(0.5F, 0.5F), 1.1F).map(_.value)

      actual.size must beEqualTo(expected.size)
      actual must containTheSameElementsAs(expected)
    }

    "return all points when distance is enough for spacing, from a foreign point outside but within threshold" in {
      val expected = points
      val actual = space.traverse(Point(-0.5F, -0.5F), 1.1F).map(_.value)

      actual.size must beEqualTo(expected.size)
      actual must containTheSameElementsAs(expected)
    }

    "from a foreign point outside and without threshold, return no points" in {
      val expected = Nil
      val actual = space.traverse(Point(width + 10, height + 10), 1.1F).map(_.value)

      actual.size must beEqualTo(expected.size)
      actual must containTheSameElementsAs(expected)
    }
  }

  "Traversal across non-uniform points" should {
    val cluster1 = Resources.read[Point]("/variable-density-cluster-1.csv").toSeq
    val cluster2 = Resources.read[Point]("/variable-density-cluster-2.csv").toSeq
    val cluster3 = Resources.read[Point]("/variable-density-cluster-3.csv").toSeq

    /* Fill with a large number of spurious outliers. */
    val outliers = Seq
      .fill(1000)(cluster1 ++ cluster2 ++ cluster3)
      .zipWithIndex
      .map { case (c, i) => c.map(p => Point(p.x + i + 1, p.y + i + 1))}
      .flatten

    val points = cluster1 ++ cluster3 ++ outliers
    val entries = points.map(p => Entry(p, p))
    val space = RTree(entries: _*)

    "return only points reachable from a search point (from cluster 1)" in {
      val expected = cluster1
      val actual = space.traverse(cluster1(0), 0.05F).map(_.value)

      actual.size must beEqualTo(expected.size)
      actual must containTheSameElementsAs(expected)
    }

    "return only points reachable from a search point (from cluster 2)" in {
      val expected = cluster3
      val actual = space.traverse(cluster3(0), 0.2F).map(_.value)

      actual.size must beEqualTo(expected.size)
      actual must containTheSameElementsAs(expected)
    }
  }

  "Traversal across overlapping points" should {
    val points = Seq.fill(100)(1).map(c => Point(c, c))
    val entries = points.map(p => Entry(p, p))
    val space = RTree(entries: _*)

    "return only points reachable from a local search point" in {
      val expected = points
      val actual = space.traverse(expected(0), 0.5F).map(_.value)

      actual.size must beEqualTo(expected.size)
      actual must containTheSameElementsAs(expected)
    }

    "return no points from a remote point" in {
      val actual = space.traverse(Point(2, 2), 0.5F).map(_.value)

      actual must beEmpty
    }
  }

  "Density-reachable uniform points" should {
    val width = 2
    val height = 2

    val cluster = grid[Float](width, height).map((Point.apply _).tupled)
    val clusters = Seq
      .fill(3)(cluster)
      .zipWithIndex
      .map { case (c, i) => c.map(p => Point(p.x + i * 2, p.y + i * 2))}

    val points: Seq[Point] = clusters.flatten
    val entries = points.map(p => Entry(p, p))
    val space = RTree(entries: _*)

    "form clusters" in {
      val expected: Set[Set[Point]] = clusters.map(_.toSet).toSet
      val actual: Set[Set[Point]] = space.clusters(1.1F).map(_.map(_.value).toSet).toSet

      actual.size must beEqualTo(expected.size)
      actual must beEqualTo(expected)
    }
  }

  "Density-reachable non-uniform points" should {
    val cluster1 = Resources.read[Point]("/variable-density-cluster-1.csv").toSeq
    val cluster3 = Resources.read[Point]("/variable-density-cluster-3.csv").toSeq

    val points = cluster1 ++ cluster3
    val entries = points.map(p => Entry(p, p))
    val space = RTree(entries: _*)

    "form clusters" in {
      val expected: Set[Set[Point]] = (cluster1 :: cluster3 :: Nil).map(_.toSet).toSet
      val actual: Set[Set[Point]] = space.clusters(0.2F).map(_.map(_.value).toSet).toSet

      actual.size must beEqualTo(expected.size)
      actual must beEqualTo(expected)
    }
  }
}
