import java.awt.Graphics
import javax.swing.JComponent
import javax.swing.plaf.basic.BasicRadioButtonUI

class RoundRadioButtonUI : BasicRadioButtonUI() {
    override fun contains(c: JComponent?, x: Int, y: Int): Boolean =
        RoundUI.contains(c, x, y)

    override fun paint(g: Graphics?, c: JComponent?) =
        RoundUI.paint(g, c)
}