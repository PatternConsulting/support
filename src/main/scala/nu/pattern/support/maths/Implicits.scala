package nu.pattern.support.maths

object Implicits {

  implicit class BoxesOps[T, M](b: Seq[T])(implicit ev: BoxLike[T, M]) {
    def intersections(implicit nev: Numeric[M], nlev: NumberLike[Float, M]) = Boxes.intersections(b)
  }

}
