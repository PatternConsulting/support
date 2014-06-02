package java.awt.image

object Implicits {

  implicit class BufferedImageOps(i: BufferedImage) {
    def convertTo(`type`: Int) = BufferedImages.convertTo(i, `type`)
  }

}
