package nu.pattern.support.maths

import scala.annotation.implicitNotFound

@implicitNotFound("No member of type class PointLike in scope for ${T}.")
trait PointLike[T] {
}
