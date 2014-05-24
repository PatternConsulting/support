package nu.pattern.support.maths

case class Box[T](p0: Point[T], p1: Point[T])(implicit ev: Numeric[T]) {
  val width: T = ev.minus(p1.x, p0.x)

  val height: T = ev.minus(p1.y, p0.y)
}

object Box {

  def apply[T](x0: T, y0: T, x1: T, y1: T)(implicit ev: Numeric[T]): Box[T] =
    Box(Point(x0, y0), Point(x1, y1))

  implicit def NativeBoxLike[T] = {
    new BoxLike[Box[T], T] {
      override def from(v: Box[T]): Box[T] = v

      override def to(v: Box[T]): Box[T] = v
    }
  }

}
