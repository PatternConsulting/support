package nu.pattern.support.maths

case class Box[T](p0: Point[T], p1: Point[T])(implicit ev: Numeric[T]) {
  val width: T = ev.minus(p1.x, p0.x)

  val height: T = ev.minus(p1.y, p0.y)
}
