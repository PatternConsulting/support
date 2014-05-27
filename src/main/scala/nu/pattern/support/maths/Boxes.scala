package nu.pattern.support.maths

object Boxes {

  /**
   * Calculates intersections for all given boxes. This is _not_ O(_n_^2^) as it arranges the boxes in an R-tree which is then queried with each of the original boxes for all points contained. This is not the most efficient possible algorithm. In the future, this may be replaced with the algorithm described in “An Optimal Rectangle-Intersection Algorithm Using Linear Arrays Only” by Frank Dévai and László Neumann.
   *
   * @param ev Evidence for conversion from type `T` to and from [[archery.Box]] (this functional uses Archery to search the space represented by the arguments), which has specific rules given the use of [[Float]] by [[archery.Geom]].

   * @tparam T A type of 2-dimensional box, evidenced by [[archery.BoxLike]].
   */
  def intersections[T](boxes: Seq[T])(implicit ev: archery.BoxLike[T]): Set[T] = {
    import archery.{Entry, Box, RTree}

    val entries =
      boxes
        .map(ev.from)
        .zipWithIndex
        .map { case (v, i) => Entry(v, i)}

    val space = RTree(entries: _*)

    val intersectionBoxes = entries.map {
      entry =>
        val b0 = entry.geom.toBox

        /* Find all boxes that intersect the entry, but exclude the entry itself (as it'll participate in the intersection calculation for each match). If there are no matches besides, then zero intersections will be produced. */
        val matchingBoxes =
          space
            .searchIntersection(entry.geom.toBox)
            .filterNot(_.value == entry.value)
            .map(_.geom.toBox)

        /* Create the intersection between each matching box and the search box. Note this causes redundant results, but those will be eliminated when converting to a set and this is an optimization that can be made later. */
        matchingBoxes.map { b1 =>
          /* Find the right- and down-most top-left point. */
          val x0 = Math.max(b0.x, b1.x)
          val y0 = Math.max(b0.y, b1.y)

          /* Find the left- and up-most bottom-right point. */
          val x1 = Math.min(b0.x2, b1.x2)
          val y1 = Math.min(b0.y2, b1.y2)

          Box(x0, y0, x1, y1)
        }
    }.flatten

    intersectionBoxes.toSet.map(ev.to)
  }

}
