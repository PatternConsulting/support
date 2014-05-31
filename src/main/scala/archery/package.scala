import scala.collection.mutable

package object archery {

  /**
   * Complimentary operations for [[RTree]].
   */
  implicit class RTreeOps[A](tree: RTree[A]) {

    /**
     * A variant on the k-nearest neighbor function (see [[RTree.nearestK]]) that parameterizes the distance variable.
     */
    def nearestK(pt: Point, d: Double, k: Int): IndexedSeq[Entry[A]] =
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

  }

}
