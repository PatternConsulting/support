package nu.pattern.support.maths

import java.awt.Rectangle

import annotation.implicitNotFound

@implicitNotFound("No member of type class BoxLike in scope for ${T}.")
trait BoxLike[T, M] {
  def from(v: T): Box[M]

  def to(v: Box[M]): T
}

object BoxLike {

  implicit object BoxLikeRectangle extends BoxLike[Rectangle, Int] {
    override def from(r: Rectangle) = Box(Point(r.x, r.y), Point(r.x + r.width, r.y + r.height))

    override def to(b: Box[Int]) = {
      val x = b.p0.x
      val y = b.p0.y
      val width = b.p1.x - x
      val height = b.p1.y - y
      new Rectangle(x, y, width, height)
    }
  }

}
