import java.awt.Graphics
import javax.swing.*
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

    override fun installListeners(button: AbstractButton?) {
        super.installListeners(button)
        if (button is JRadioButton) {
            button.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("UP"), "Previous")
            button.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("DOWN"), "Next")
            button.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("LEFT"), "Previous")
            button.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("RIGHT"), "Next")
        }
    }

    override fun uninstallListeners(button: AbstractButton?) {
        super.uninstallListeners(button)
        if (button is JRadioButton) {
            button.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).remove(KeyStroke.getKeyStroke("UP"))
            button.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).remove(KeyStroke.getKeyStroke("DOWN"))
            button.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).remove(KeyStroke.getKeyStroke("LEFT"))
            button.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).remove(KeyStroke.getKeyStroke("RIGHT"))
        }
    }
}