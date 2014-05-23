package nu.pattern.support.maths

import java.awt.Rectangle

import annotation.implicitNotFound
import nu.pattern.support.maths.Box.Point

@implicitNotFound("No member of type class BoxLike in scope for ${T}.")
trait BoxLike[T, M] {
  def box(v: T): Box[M]
}

object BoxLike {

  implicit object BoxLikeRectangle extends BoxLike[Rectangle, Int] {
    override def box(r: Rectangle) = Box(Point(r.x, r.y), Point(r.x + r.width, r.y + r.height))
  }

}
