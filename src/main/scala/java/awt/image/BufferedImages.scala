package java.awt.image

import java.awt.AlphaComposite

object BufferedImages {
  def convertTo(original: BufferedImage, `type`: Int) = {
    if (original.getType == `type`) {

      original

    } else {

      val converted = new BufferedImage(original.getWidth, original.getHeight, `type`)
      val g = converted.createGraphics

      try {
        g.setComposite(AlphaComposite.Src)
        g.drawImage(original, 0, 0, null)
      } finally {
        g.dispose()
      }

      converted

    }
  }
}
