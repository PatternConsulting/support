package archery

import java.awt.Rectangle

import scala.annotation.implicitNotFound

/**
 * Archery uses an internal type called [[Geom]] to represent its shapes. This trait is meant to provide bridges between that and types like [[Rectangle]].
 *
 * @tparam T Original box or rectangle type.
 */
@implicitNotFound("No member of type archery.BoxLike in scope for ${T}.")
trait BoxLike[T] {
  def from(v: T): Box

  def to(v: Box): T
}

object BoxLike {

  /**
   * Archery uses [[Float]] to store points in [[Geom]], while [[Rectangle]] uses [[Int]]. Although Archery's not expected to produce output requiring higher numeric precision, this conversion will rely on [[Rectangle]] double-precision methods to perform conversion, thereby ensuring predictable behavior (principle of least surprise).
   */
  implicit object RectangleBoxLike extends BoxLike[Rectangle] {
    override def from(v: Rectangle): Box = {
      val x0 = v.x
      val y0 = v.y
      val x1 = x0 + v.width
      val y1 = x1 + v.height
      Box(x0, y0, x1, y1)
    }

    override def to(v: Box): Rectangle = {
      val x = v.x
      val y = v.y
      val width = v.x2 - x
      val height = v.y2 - y
      val r = new Rectangle()
      r.setRect(x, y, width, height)
      r
    }
  }

}
