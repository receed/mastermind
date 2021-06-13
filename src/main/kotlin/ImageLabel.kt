import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.Image
import java.awt.RenderingHints
import java.awt.geom.AffineTransform
import java.awt.image.BufferedImage
import javax.swing.JLabel
import kotlin.math.max
import kotlin.math.min

class ImageLabel(private val image: BufferedImage) : JLabel() {
    private fun getScaledInstance(image: BufferedImage, targetWidth: Int, targetHeight: Int): BufferedImage {
        var result = image
        while (result.width != targetWidth || result.height != targetHeight) {
            val newWidth = max(result.width / 2, targetWidth)
            val newHeight = max(result.height / 2, targetHeight)
            val newImage = BufferedImage(newWidth, newHeight, image.type)
            val g = newImage.createGraphics()
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR)
            g.drawImage(result, 0, 0, newWidth, newHeight, null)
            g.dispose()
            result = newImage
        }
        return result
    }

    override fun paint(g: Graphics) {
        val size = min(width, height)
        g.drawImage(getScaledInstance(image, size, size), (width - size) / 2, (height - size) / 2, null)
    }
}