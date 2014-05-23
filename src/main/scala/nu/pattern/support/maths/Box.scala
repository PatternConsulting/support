package nu.pattern.support.maths

import nu.pattern.support.maths.Box.Point

case class Box[T](p0: Point[T], p1: Point[T])(implicit ev: Numeric[T]) {
  val width: T = ev.minus(p1.x, p0.x)

  val height: T = ev.minus(p1.y, p0.y)
}

object Box {

  case class Point[T](x: T, y: T)(implicit ev: Numeric[T])

  def apply[T](x: T, y: T, width: T, height: T)(implicit ev: Numeric[T]): Box[T] =
    Box(Point(x, y), Point(ev.plus(x, width), ev.plus(y, height)))

}
