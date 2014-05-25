package nu.pattern.support.maths

import archery.{Box => ArcheryBox}

object Implicits {

  implicit class BoxesOps[T, M](b: Seq[T]) {
    def intersections()(implicit blev: BoxLike[T, M], ablev: BoxLike[ArcheryBox, M], nev: Numeric[M]) = Boxes.intersections(b)
  }

}
