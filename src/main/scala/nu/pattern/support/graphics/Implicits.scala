package nu.pattern.support.graphics

import java.awt.image.BufferedImage

object Implicits {

  implicit class BufferedImageOps(i: BufferedImage) {
    def convertTo(`type`: Int) = BufferedImages.convertTo(i, `type`)
  }

}
