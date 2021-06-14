import java.awt.Graphics
import javax.swing.AbstractButton
import javax.swing.JComponent
import javax.swing.JRadioButton
import javax.swing.plaf.basic.BasicRadioButtonUI

class RoundRadioButtonUI : BasicRadioButtonUI() {
    override fun installUI(c: JComponent?) {
        super.installUI(c)
        (c as AbstractButton).isContentAreaFilled = false
    }

    override fun contains(c: JComponent?, x: Int, y: Int): Boolean =
        RoundUI.contains(c, x, y)

    override fun paint(g: Graphics?, c: JComponent?) {
        val button = c as JRadioButton
        RoundUI.paint(g, c)
        val borderWidth = if (button.isEnabled) {
            if (button.isSelected) 6
            else 3
        } else 0
        RoundUI.paint(g, c, borderWidth)
    }
}