import java.awt.Graphics
import javax.swing.AbstractButton
import javax.swing.JComponent
import javax.swing.plaf.basic.BasicButtonUI

class RoundButtonUI : BasicButtonUI() {
    override fun installUI(c: JComponent?) {
        super.installUI(c)
        (c as AbstractButton).isContentAreaFilled = false
    }

    override fun contains(c: JComponent?, x: Int, y: Int): Boolean =
        RoundUI.contains(c, x, y)

    override fun paint(g: Graphics?, c: JComponent?) {
        val borderWidth = if ((c as AbstractButton).model.isPressed) 6 else 0
        RoundUI.paint(g, c, borderWidth)
    }
}