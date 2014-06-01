import scala.annotation.tailrec
import scala.collection.mutable

package object archery {

  implicit class BoxOps(b: Box) {
    /**
     * Expand the dimensions of this box by `d` in all directions.
     */
    def grow(d: Float) = Box(b.x - d, b.y - d, b.x2 + d, b.y2 + d)
  }

  /**
   * Complimentary operations for [[RTree]].
   */
  implicit class RTreeOps[A](tree: RTree[A]) {

    /**
     * Starting from `point`, find all neighboring points within `distance`, then visit them and perform the same operation. Effectively a clustering algorithm that returns all points in a local cluster.
     */
    def traverse(point: Point, distance: Float): Seq[Entry[A]] = {
      def withinRadius(center: Point, radius: Float)(e: Entry[A]): Boolean =
        e.geom.distance(center) < radius

      @tailrec def withAccumulator(queue: List[Entry[A]], previouslyVisited: Seq[Entry[A]] = Seq.empty): Seq[Entry[A]] =
        queue match {
          case e :: tail =>
            /* If the entry's geometry is a Point, use it instead of creating a new object. */
            val p = e.geom match {
              case p: Point => p
              case g => Point(g.x, g.y)
            }

            val nearest = tree.search(p.toBox.grow(distance), withinRadius(p, distance))

            val unvisited = nearest.diff(previouslyVisited)
            val visited = unvisited ++ previouslyVisited

            withAccumulator(unvisited.toList ::: tail, visited)
          case Nil =>
            previouslyVisited
        }

      tree.search(point.toBox.grow(distance), withinRadius(point, distance)).foldLeft(Seq.empty[Entry[A]]) { (a, e) =>
        withAccumulator(e :: Nil)
      }
    }

    def clusters(distance: Float, entries: Seq[Entry[A]] = tree.entries.toSeq): Seq[Seq[Entry[A]]] = {

      /* Folding isn't sufficient here. An explicit stack is needed to track which points have yet to be searched, as well as those which have already been visited (and clustered). */
      @tailrec def withAccumulator(queue: List[Entry[A]], cs: Seq[Seq[Entry[A]]] = Seq.empty): Seq[Seq[Entry[A]]] =
        queue match {
          case e :: tail =>
            /* If the entry's geometry is a Point, use it instead of creating a new object. */
            val p = e.geom match {
              case p: Point => p
              case g => Point(g.x, g.y)
            }

            val c = tree.traverse(p, distance)

            withAccumulator(tail.diff(c), cs :+ c)
          case Nil =>
            cs
        }

      withAccumulator(entries.toList)
    }

  }

}
