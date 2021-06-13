import java.awt.*
import java.io.File
import javax.imageio.ImageIO
import javax.swing.*

class Board(val game: Game) : JPanel() {
    private val B_WIDTH = 350
    private val B_HEIGHT = 350
    private val DELAY = 25
    private val pegImage = ImageIO.read(File("src/main/resources/sphere.png"))

    private fun initBoard() {
        background = Color.BLACK
        preferredSize = Dimension(B_WIDTH, B_HEIGHT)
        layout = GridBagLayout()
        val constraints = GridBagConstraints().apply {

            fill = GridBagConstraints.BOTH
        }
        val answerPegsPerRow = game.pegCount / 2
        for (row in 0 until game.attemptCount) {
            for (column in 0 until game.pegCount) {
                val holeButton = RoundButton().apply {
                    addActionListener { this@Board.background = Color.WHITE }
                }
                constraints.apply {
                    gridx = column
                    gridy = row * 2
                    gridheight = 2
                    weightx = 1.0
                    weighty = 1.0
                    insets = Insets(5, 5, 5, 5)
                }
                add(holeButton, constraints)

                val answerLabel = ImageLabel(pegImage)

                constraints.apply {
                    gridx = game.pegCount + column % answerPegsPerRow
                    gridy = row * 2 + column / answerPegsPerRow
                    gridheight = 1
                    weightx = 1.0
                    weighty = 1.0
                }
                add(answerLabel, constraints)
            }
        }
    }

    public override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
    }

    init {
        initBoard()
    }
}


class Application : JFrame() {
    private fun initUI() {
        add(Board(Game()))
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

fun main(args: Array<String>) {
    EventQueue.invokeLater {
        val ex = Application()
        ex.isVisible = true
    }
}
