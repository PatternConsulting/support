package nu.pattern.java.awt.image

import java.awt.image.BufferedImage

object Implicits {

  implicit class BufferedImageOps(i: BufferedImage) {
    def convertTo(`type`: Int) = BufferedImages.convertTo(i, `type`)
  }

}
