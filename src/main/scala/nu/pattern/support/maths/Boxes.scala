package nu.pattern.support.maths

object Boxes {

  /**
   * Calculates intersections for all given [[Box]] objects. This is _not_ O(_n_^2^) as it arranges the boxes in an R-tree which is then queried with each of the original boxes for all points contained. This is not the most efficient possible algorithm. In the future, this may be replaced with the algorithm described in “An Optimal Rectangle-Intersection Algorithm Using Linear Arrays Only” by Frank Dévai and László Neumann.
   *
   * @param blev Evidence for conversion between type [[T]] and [[Box]].
   * @param nlfev Evidence for conversion between type [[M]] and [[Float]], which is the type native to [[archery.Geom]].
   *
   * @tparam T A type of 2-dimensional rectangle.
   * @tparam M Type used to measure the rectangle.
   */
  def intersections[T, M](boxes: Seq[T])(implicit blev: BoxLike[T, M], nlfev: NumberLike[Float, M]): Set[T] = {
    import archery.{Box => RBox, Entry, RTree}

    val spuriousBoxes =
      boxes
        .map(b => blev.toFloat(blev.from(b)))
        .map(b => RBox(b.p0.x, b.p0.y, b.p1.x, b.p1.y))

    val entries =
      spuriousBoxes
        .zipWithIndex
        .map { case (v, i) => Entry(v, i)}

    val intersectionBoxes =
      spuriousBoxes.map(RTree(entries: _*).searchIntersection).toSet.filter(_.nonEmpty).map {
        entries =>
          entries.map(_.geom.toBox).reduce {
            (b0, b1) =>
              val x0 = nlfev.from(Math.max(b0.x, b1.x))
              val y0 = nlfev.from(Math.max(b0.y, b1.y))
              val x1 = nlfev.from(Math.min(b0.x2, b1.x2))
              val y1 = nlfev.from(Math.min(b0.y2, b1.y2))
              Box(Point(x0, y0), Point(x1, y1))
          }
      }

    intersectionBoxes.map(blev.to)
  }

}
