import java.awt.EventQueue
import javax.swing.JFrame
import javax.swing.JMenu
import javax.swing.JMenuBar
import javax.swing.JMenuItem

class Application : JFrame() {
    var gameParameters = Game.Parameters()
    private var board: Board = Board(Game(gameParameters), this)
    private val settingsDialog = Settings(this)

    fun clearBoard() {
        remove(board)
        board = Board(Game(gameParameters), this)
        add(board)
        validate()
    }

    private fun initMenu() {
        val menu = JMenu("Menu")
        val restart = JMenuItem("Restart")
        restart.addActionListener {
            clearBoard()
        }
        menu.add(restart)
        val settings = JMenuItem("Settings")
        settings.addActionListener {
            settingsDialog.view()
        }
        menu.add(settings)
        jMenuBar = JMenuBar()
        jMenuBar.add(menu)
    }

    private fun initUI() {
        initMenu()
        add(board)
        setSize(250, 200)
        title = "Application"
        defaultCloseOperation = EXIT_ON_CLOSE
        setLocationRelativeTo(null)
        pack()
    }

    init {
        initUI()
    }
}

fun main() {
    EventQueue.invokeLater {
        val ex = Application()
        ex.isVisible = true
    }
}
