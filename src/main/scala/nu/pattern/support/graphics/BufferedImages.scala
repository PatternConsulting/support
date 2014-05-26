package nu.pattern.support.graphics

import java.awt.AlphaComposite
import java.awt.image.BufferedImage

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
