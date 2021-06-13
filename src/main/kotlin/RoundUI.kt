import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.Point
import java.awt.RenderingHints
import javax.swing.*
import javax.swing.plaf.ButtonUI
import javax.swing.plaf.basic.BasicButtonUI
import javax.swing.plaf.basic.BasicRadioButtonMenuItemUI
import javax.swing.plaf.basic.BasicRadioButtonUI
import kotlin.math.min

object RoundUI {
    private val JComponent.diameter
        get() = min(width, height)

    fun contains(c: JComponent?, x: Int, y: Int): Boolean {
        return Point(c!!.width, c.height).distance(Point(x * 2, y * 2)) < c.diameter
    }

    fun paint(g: Graphics?, c: JComponent?) {
        g!!.color = c!!.foreground
        c.border = BorderFactory.createEmptyBorder()
        (g as Graphics2D).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        g.fillOval((c.width - c.diameter) / 2, (c.height - c.diameter) / 2, c.diameter - 1, c.diameter - 1)
    }
}