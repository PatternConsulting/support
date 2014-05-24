package nu.pattern.support.maths

import java.awt.Rectangle

object Boxes {

  /**
   * Calculates intersections for all given [[Box]] objects. This is _not_ O(_n_^2^) as it arranges the boxes in an R-tree which is then queried with each of the original boxes for all points contained. This is not the most efficient possible algorithm. In the future, this may be replaced with the algorithm described in “An Optimal Rectangle-Intersection Algorithm Using Linear Arrays Only” by Frank Dévai and László Neumann.
   *
   * @param blev Evidence for conversion between type [[T]] and [[Box]].
   * @param nlev Evidence for conversion from type [[Float]] (which is the type native to [[archery.Geom]]) to [[M]].
   *
   * @tparam T A type of 2-dimensional rectangle.
   * @tparam M Type used to measure the rectangle.
   */
  def intersections[T, M](boxes: Seq[T])(implicit blev: BoxLike[T, M], nev: Numeric[M], nlev: NumberLike[Float, M]): Set[T] = {
    import archery.{Box => ArcheryBox, Entry, RTree}

    /**
     * Internal logic for dealing with our native boxes versus Archery's own type.
     */
    object BoxLikeArcheryBox extends BoxLike[ArcheryBox, M] {
      override def from(v: ArcheryBox): Box[M] = {
        val x0 = nlev.from(Math.max(v.x, v.x))
        val y0 = nlev.from(Math.max(v.y, v.y))
        val x1 = nlev.from(Math.min(v.x2, v.x2))
        val y1 = nlev.from(Math.min(v.y2, v.y2))
        Box(Point(x0, y0), Point(x1, y1))
      }

      override def to(v: Box[M]): ArcheryBox = {
        val x0 = nev.toFloat(v.p0.x)
        val y0 = nev.toFloat(v.p0.y)
        val x1 = nev.toFloat(v.p1.x)
        val y1 = nev.toFloat(v.p1.y)
        ArcheryBox(x0, y0, x1, y1)
      }
    }

    val spuriousBoxes =
      boxes
        .map(blev.from)
        .map(BoxLikeArcheryBox.to)

    val entries =
      spuriousBoxes
        .zipWithIndex
        .map { case (v, i) => Entry(v, i)}

    val intersectionBoxes =
      spuriousBoxes.map(RTree(entries: _*).searchIntersection).toSet.filter(_.nonEmpty).map {
        entries =>
          entries.map(_.geom.toBox).map(BoxLikeArcheryBox.from).reduce { (b0, b1) =>
            /* Find the right- and down-most top-left point. */
            val x0 = nev.max(b0.p0.x, b1.p0.x)
            val y0 = nev.max(b0.p0.y, b1.p0.y)

            /* Find the left- and up-most bottom-right point. */
            val x1 = nev.min(b0.p1.x, b1.p1.x)
            val y1 = nev.min(b0.p1.y, b1.p1.y)

            Box(Point(x0, y0), Point(x1, y1))
          }
      }

    intersectionBoxes.map(blev.to)
  }

}
