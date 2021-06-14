import java.awt.BasicStroke
import java.awt.Color
import java.awt.Graphics
import java.awt.Graphics2D
import javax.swing.AbstractButton
import javax.swing.JComponent
import javax.swing.JRadioButton
import javax.swing.plaf.basic.BasicButtonUI

class RoundButtonUI : BasicButtonUI() {
    override fun installUI(c: JComponent?) {
        super.installUI(c)
        (c as AbstractButton).isContentAreaFilled = false
   }

    override fun contains(c: JComponent?, x: Int, y: Int): Boolean =
        RoundUI.contains(c, x, y)

    override fun paint(g: Graphics?, c: JComponent?) {
        RoundUI.paint(g, c)
//        graphics.drawOval()
//        if (button.isSelected)
    }
}