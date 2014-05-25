package nu.pattern.support.maths


object Boxes {

  import archery.{Box => ArcheryBox, Geom}

  /**
   * Archery hard-codes [[Float]] to store points in [[Geom]]. It's safe to go to and from [[Int]] without losing precision. Besides this and [[Float]], there are no other options that can be safely implied. Callers must exercise caution and deliberate handling for other types (like [[Double]] or [[BigDecimal]]).
   */
  implicit object IntBoxLikeArcheryBox extends BoxLike[ArcheryBox, Int] {
    override def from(v: ArcheryBox): Box[Int] = {
      val x0 = v.x.toInt
      val y0 = v.y.toInt
      val x1 = v.x2.toInt
      val y1 = v.y2.toInt
      Box(Point(x0, y0), Point(x1, y1))
    }

    override def to(v: Box[Int]): ArcheryBox = {
      val x0 = v.p0.x.toFloat
      val y0 = v.p0.y.toFloat
      val x1 = v.p1.x.toFloat
      val y1 = v.p1.y.toFloat
      ArcheryBox(x0, y0, x1, y1)
    }
  }

  /**
   * Archery hard-codes [[Float]] to store points in [[Geom]]. It's safe to go to and from [[Float]] without losing precision. Besides this and [[Int]], there are no other options that can be safely implied. Callers must exercise caution and deliberate handling for other types (like [[Double]] or [[BigDecimal]]).
   */
  implicit object FloatBoxLikeArcheryBox extends BoxLike[ArcheryBox, Float] {
    override def from(v: ArcheryBox): Box[Float] = {
      val x0 = v.x
      val y0 = v.y
      val x1 = v.x2
      val y1 = v.y2
      Box(Point(x0, y0), Point(x1, y1))
    }

    override def to(v: Box[Float]): ArcheryBox = {
      val x0 = v.p0.x
      val y0 = v.p0.y
      val x1 = v.p1.x
      val y1 = v.p1.y
      ArcheryBox(x0, y0, x1, y1)
    }
  }

  /**
   * Calculates intersections for all given [[Box]] objects. This is _not_ O(_n_^2^) as it arranges the boxes in an R-tree which is then queried with each of the original boxes for all points contained. This is not the most efficient possible algorithm. In the future, this may be replaced with the algorithm described in “An Optimal Rectangle-Intersection Algorithm Using Linear Arrays Only” by Frank Dévai and László Neumann.
   *
   * @param bev Evidence for conversion between type [[T]] and [[Box]].
   * @param abev Evidence for conversion from type [[Box]] to and from [[archery.Box]], which has specific rules given the use of [[Float]] by [[Geom]].
   * @param  nev Operations for numbers of type [[M]].
   *
   * @tparam T A type of 2-dimensional rectangle.
   * @tparam M Type used to measure the rectangle.
   */
  def intersections[T, M](boxes: Seq[T])(implicit bev: BoxLike[T, M], abev: BoxLike[ArcheryBox, M], nev: Numeric[M]): Set[T] = {
    import archery.{Entry, RTree}

    val spuriousBoxes =
      boxes
        .map(bev.from)
        .map(abev.to)

    val entries =
      spuriousBoxes
        .zipWithIndex
        .map { case (v, i) => Entry(v, i)}

    val intersectionBoxes =
      spuriousBoxes.map(RTree(entries: _*).searchIntersection).toSet.filter(_.nonEmpty).map {
        entries =>
          entries.map(_.geom.toBox).map(abev.from).reduce { (b0, b1) =>
            /* Find the right- and down-most top-left point. */
            val x0 = nev.max(b0.p0.x, b1.p0.x)
            val y0 = nev.max(b0.p0.y, b1.p0.y)

            /* Find the left- and up-most bottom-right point. */
            val x1 = nev.min(b0.p1.x, b1.p1.x)
            val y1 = nev.min(b0.p1.y, b1.p1.y)

            Box(Point(x0, y0), Point(x1, y1))
          }
      }

    intersectionBoxes.map(bev.to)
  }

}
