import java.awt.*
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import javax.swing.*

fun BufferedImage.color(color: Color): BufferedImage {
    val colored = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
    val g = colored.createGraphics()
    g.drawImage(this, 0, 0, null)
    g.composite = AlphaComposite.SrcAtop
    g.color = color
    g.fillRect(0, 0, width, height)
    g.dispose()
    return colored
}

class Board(val game: Game) : JPanel() {
    private val B_WIDTH = 350
    private val B_HEIGHT = 350
    private val pegImage = ImageIO.read(File("src/main/resources/sphere.png"))
    private val positionMatchPeg = pegImage.color(Color.decode("#263238"))
    private val colorMatchPeg = pegImage.color(Color.decode("#eceff1"))
    private val noMatchPeg = pegImage.color(Color.decode("#90a4ae"))

    private val colors =
        listOf("#ff6e40", "#eeff41", "#b2ff59", "#18ffff", "#536dfe", "#e040fb", "#ff5252").map { Color.decode(it) }
    private var currentAttempt = 0
    private var currentPeg = 0
    private val guessPegs = Array(game.attemptCount) { Array(game.pegCount) { JRadioButton() } }
    private val answerPegs = Array(game.attemptCount) { Array(game.pegCount) { ImageLabel(noMatchPeg) } }
    private val guessButton = JButton("Make a guess").apply { isEnabled = false }

    inner class Guess(pegCount: Int) {
        private val currentPegColors = MutableList<Int?>(pegCount) { null }
        private var selectedColors = 0

        val pegColors
            get() = currentPegColors.requireNoNulls()

        operator fun set(index: Int, colorNumber: Int?) {
            if (currentPegColors[index] != null)
                selectedColors--
            if (colorNumber != null)
                selectedColors++
            currentPegColors[index] = colorNumber
            guessButton.isEnabled = (selectedColors == currentPegColors.size)
        }
    }

    private val guess = Guess(game.pegCount)

    init {
        background = Color.BLACK
        preferredSize = Dimension(B_WIDTH, B_HEIGHT)
        layout = GridBagLayout()
        val constraints = GridBagConstraints().apply {

            fill = GridBagConstraints.BOTH
        }
        val answerPegsPerRow = (game.pegCount + 1) / 2
        val buttonGroup = ButtonGroup()
        for (row in 0 until game.attemptCount) {
            for (column in 0 until game.pegCount) {
                val holeButton = guessPegs[row][column].apply {
                    addActionListener {
                        currentPeg = column
                    }
                    setUI(RoundButtonUI())
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
                buttonGroup.add(holeButton)


                constraints.apply {
                    gridx = game.pegCount + column % answerPegsPerRow
                    gridy = row * 2 + column / answerPegsPerRow
                    gridheight = 1
                    weightx = 1.0
                    weighty = 1.0
                }
                add(answerPegs[row][column], constraints)
            }
        }
        for ((number, color) in colors.take(game.colorCount).withIndex()) {
            val colorButton = JButton().apply {
                foreground = color
                setUI(RoundButtonUI())
                addActionListener {
                    guessPegs[currentAttempt][currentPeg].foreground = foreground
                    guess[currentPeg] = number
                }
            }
            constraints.apply {
                gridx = game.pegCount + answerPegsPerRow
                gridy = number * 2
                gridheight = 2
                weightx = 1.0
                weighty = 1.0
                insets = Insets(5, 5, 5, 5)
            }
            add(colorButton, constraints)
        }
        guessButton.addActionListener {
            val response = game.makeGuess(guess.pegColors)
            for (peg in answerPegs[currentAttempt].take(response.positionMatches))
                peg.image = positionMatchPeg
            for (peg in answerPegs[currentAttempt].drop(response.positionMatches).take(response.colorMatches))
                peg.image = colorMatchPeg
            val result = game.result
            if (result == Game.Result.UNFINISHED) {

            }
            if (game.result != Game.Result.UNFINISHED) {
                val message = "You ${if (game.result == Game.Result.WIN) "win" else "lose"}. Play another game?"
                val playAgain =
                    JOptionPane.showConfirmDialog(this, message, "Game is finished", JOptionPane.YES_NO_OPTION)
                if (playAgain == JOptionPane.YES_OPTION) {

                } else
                    SwingUtilities.getWindowAncestor(this).dispose()
            }
            for (peg in guessPegs[currentAttempt])
                peg.isEnabled = false
            ++currentAttempt
            for (peg in guessPegs[currentAttempt])
                peg.isEnabled = true
            for (pegIndex in 0 until game.pegCount)
                guess[pegIndex] = null
        }
        constraints.apply {
            gridx = 0
            gridy = game.attemptCount * 2
            gridheight = 1
            gridwidth = GridBagConstraints.REMAINDER
        }
        add(guessButton, constraints)
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
