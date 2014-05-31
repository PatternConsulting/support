package nu.pattern.support.maths

trait PointLike[P, M] {
  def x(p: P): M

  def y(p: P): M
}
