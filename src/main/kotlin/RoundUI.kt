import java.awt.*
import javax.swing.BorderFactory
import javax.swing.JComponent
import kotlin.math.min

object RoundUI {
    private val JComponent.diameter
        get() = min(width, height)

    fun getBounds(c: JComponent): Rectangle {
        val diameter = c.diameter
        return Rectangle((c.width - diameter) / 2, (c.height - diameter) / 2, diameter, diameter)
    }

    fun contains(c: JComponent?, x: Int, y: Int): Boolean {
        return Point(c!!.width, c.height).distance(Point(x * 2, y * 2)) < c.diameter
    }

    fun paint(g: Graphics?, c: JComponent?, borderWidth: Int = 0) {
        require(g is Graphics2D)
        requireNotNull(c)
        g.color = c.foreground
        c.border = BorderFactory.createEmptyBorder()
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        val diameter = c.diameter
        val x = (c.width - diameter) / 2
        val y = (c.height - diameter) / 2
        if (borderWidth == 0) {
            g.color = c.foreground
            g.fillOval(x, y, diameter - 1, diameter - 1)
        } else {
            g.color = Color.WHITE
            g.fillOval(x, y, diameter - 1, diameter - 1)
            g.color = c.foreground
            val borderSize = borderWidth * 2
            g.fillOval(x + borderWidth, y + borderWidth, diameter - borderSize - 1, diameter - borderSize - 1)
        }
    }
}