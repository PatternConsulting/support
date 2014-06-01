import scala.annotation.tailrec
import scala.collection.mutable

package object archery {

  /**
   * Complimentary operations for [[RTree]].
   */
  implicit class RTreeOps[A](tree: RTree[A]) {

    /**
     * A variant on the k-nearest neighbor function (see [[RTree.nearestK]]) that parameterizes the distance variable.
     *
     * @param d Distance value bounding the locality of a neighboring point.
     */
    def localK(pt: Point, d: Double, k: Int): IndexedSeq[Entry[A]] =
      if (k < 1) {
        Vector.empty
      } else {
        implicit val ord = Ordering.by[(Double, Entry[A]), Double](_._1)
        val pq = mutable.PriorityQueue.empty[(Double, Entry[A])]
        tree.root.nearestK(pt, k, d, pq)
        val arr = new Array[Entry[A]](pq.size)
        var i = arr.length - 1
        while (i >= 0) {
          val (_, e) = pq.dequeue()
          arr(i) = e
          i -= 1
        }
        arr
      }

    def traverse(point: Point, d: Double): IndexedSeq[Entry[A]] = {
      val size = tree.size

      @tailrec def withAccumulator(queue: List[Entry[A]], previouslyVisited: IndexedSeq[Entry[A]] = IndexedSeq.empty): IndexedSeq[Entry[A]] =
        queue match {
          case e :: tail =>
            /* If the entry's geometry is a Point, use it instead of creating a new object. */
            val p = e.geom match {
              case p: Point => p
              case g => Point(g.x, g.y)
            }

            val nearest = tree.localK(p, d, size)

            val unvisited = nearest.diff(previouslyVisited)
            val visited = unvisited ++ previouslyVisited

            withAccumulator(unvisited.toList ::: tail, visited)
          case Nil =>
            previouslyVisited
        }

      tree.localK(point, d, size).foldLeft(IndexedSeq.empty[Entry[A]]) { (a, e) =>
        withAccumulator(e :: Nil)
      }
    }

  }

}
