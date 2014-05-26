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

    val _boxes = boxes.map(ev.from)

    val entries =
      _boxes
        .zipWithIndex
        .map { case (v, i) => Entry(v, i)}

    /**
     * Use each box as a search area, collecting any points—which may be from other boxes. If the resulting search turns up exactly one result, that box was the search parameter and no overlap exists (filter it out). For multiple matches, find the smallest possible overlap area using points composing the matched boxes.
     */
    val intersectionBoxes =
      _boxes.map(RTree(entries: _*).searchIntersection).toSet.filter(1 < _.size).map {
        entries =>
          entries.map(_.geom.toBox).reduce { (b0, b1) =>
            /* Find the right- and down-most top-left point. */
            val x0 = Math.max(b0.x, b1.x)
            val y0 = Math.max(b0.y, b1.y)

            /* Find the left- and up-most bottom-right point. */
            val x1 = Math.min(b0.x2, b1.x2)
            val y1 = Math.min(b0.y2, b1.y2)

            Box(x0, y0, x1, y1)
          }
      }

    intersectionBoxes.map(ev.to)
  }

}
