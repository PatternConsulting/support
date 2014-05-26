package nu.pattern.support.graphics

import java.awt.image.BufferedImage

class Implicits {

  implicit class BufferedImageOps(i: BufferedImage) {
    def convertTo(`type`: Int) = BufferedImages.convertTo(i, `type`)
  }

}
