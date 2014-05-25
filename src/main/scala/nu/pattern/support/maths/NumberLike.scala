package nu.pattern.support.maths

import scala.annotation.implicitNotFound

/**
 * [[Numeric]] doesn't support translating to parameterized types. This class offers implicit mixins for performing numeric type conversions.
 */
@implicitNotFound("No member of type class NumberLike in scope for ${T}.")
trait NumberLike[S, T] {
  /**
   * Get a [[T]] for an [[S]]. There is no conversion back, however, as some numerical conversions result in a loss of precision or accuracy. Those cases require callers to explicitly act according to their needs.
   */
  def from(v: S): T
}

object NumberLike {

  class FloatIntNumberLike extends NumberLike[Int, Float] {
    override def from(v: Int): Float = v.toFloat
  }

}
