import java.awt.*
import javax.swing.JButton
import kotlin.math.min

class RoundButton : JButton() {
    private val diameter
        get() = min(size.width, size.height)

    override fun contains(x: Int, y: Int): Boolean {
        return Point(size.width, size.height).distance(Point(x * 2, y * 2)) < diameter
    }

    override fun paintComponent(g: Graphics?) {
        g?.let {
            it.color = background
            (it as Graphics2D).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
            it.fillOval((size.width - diameter) / 2, (size.height - diameter) / 2, diameter - 1, diameter - 1)
        }
    }

    override fun paintBorder(g: Graphics?) {
        g?.let {
            it.color = Color.BLACK
            (it as Graphics2D).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
            it.drawOval((size.width - diameter) / 2, (size.height - diameter) / 2, diameter - 1, diameter - 1)
        }
    }
}