package nu.pattern.support.maths

case class Box[T](x0: T, y0: T, x1: T, y1: T)(implicit ev: Numeric[T]) {
  val width: T = ev.minus(x1, x0)

  val height: T = ev.minus(y1, y0)
}
