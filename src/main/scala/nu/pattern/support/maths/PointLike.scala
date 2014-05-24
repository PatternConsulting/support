package nu.pattern.support.maths

import scala.annotation.implicitNotFound

@implicitNotFound("No member of type class PointLike in scope for ${T}.")
trait PointLike[T] {
  def toFloat(p: Point[T])(implicit ev: Numeric[T]) = Point(ev.toFloat(p.x), ev.toFloat(p.y))
}
