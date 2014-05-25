package nu.pattern.support.maths

object Implicits {

  implicit class BoxesOps[T, M](b: Seq[T])(implicit ev: archery.BoxLike[T]) {
    def intersections = Boxes.intersections(b)
  }

}
