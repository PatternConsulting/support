package nu.pattern.support.maths

import java.awt.geom.Rectangle2D

import annotation.implicitNotFound

@implicitNotFound("No member of type class BoxLike in scope for ${T}.")
trait BoxLike[T, M] {
  def box(v: T): Box[M]
}

object BoxLike {

  implicit object BoxLikeRectangle2D extends BoxLike[Rectangle2D, Double] {
    override def box(r: Rectangle2D) = Box(r.getX, r.getY, r.getX + r.getWidth, r.getY + r.getHeight)
  }

}
