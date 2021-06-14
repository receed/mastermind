import java.awt.*
import java.awt.event.ActionEvent
import java.awt.event.KeyEvent
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import javax.swing.*
import javax.swing.plaf.basic.BasicRadioButtonUI
import kotlin.math.max
import kotlin.properties.Delegates
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

fun BufferedImage.color(color: Color): BufferedImage {
    val colored = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
    val g = colored.createGraphics()
    g.drawImage(this, 0, 0, null)
    g.composite = AlphaComposite.SrcAtop.derive(0.5F)
    g.color = color
    g.fillRect(0, 0, width, height)
    g.dispose()
    return colored
}

inline fun makeAction(crossinline action: (ActionEvent) -> Unit): AbstractAction {
    return object : AbstractAction() {
        override fun actionPerformed(event: ActionEvent) =
            action(event)
    }
}

class Board(private val game: Game, private val application: Application) : JPanel() {
    companion object {
        private const val B_WIDTH = 200
        private const val B_HEIGHT = 400
        private val pegImage = ImageIO.read(File("src/main/resources/sphere.png"))
        private val positionMatchPeg = pegImage.color(Color.decode("#d32f2f"))
        private val colorMatchPeg = pegImage.color(Color.decode("#eceff1"))
        private val noMatchPeg = pegImage.color(Color.decode("#90a4ae"))
        private val colors =
            listOf("#ff6e40", "#eeff41", "#b2ff59", "#18ffff", "#536dfe", "#e040fb", "#ff5252").map { Color.decode(it) }
    }

    val answerPegsPerRow = (game.pegCount + 1) / 2
    private var currentAttempt = 0
    private val guessPegs = Array(game.attemptCount) { Array(game.pegCount) { JRadioButton() } }
    private var currentPeg = 0
        set(value) {
            field = if (value == game.pegCount) 0 else value
            guessPegs[currentAttempt][field].isSelected = true
        }
    private val answerPegs = Array(game.attemptCount) { Array(game.pegCount) { ImageLabel(noMatchPeg) } }
    private val colorButtons = Array(game.colorCount) { JButton() }
    private val codePegs = Array(game.pegCount) { JButton() }
    private val guessButton = JButton("Make a guess").apply { isEnabled = false }
    private val constraints = GridBagConstraints().apply {
        fill = GridBagConstraints.BOTH
    }

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
        background = Color.decode("#ffe0b2")
        preferredSize = Dimension(B_WIDTH, B_HEIGHT)
        layout = GridBagLayout()
        val buttonGroup = ButtonGroup()
        for (row in 0 until game.attemptCount) {
            for (column in 0 until game.pegCount) {
                val holeButton = guessPegs[row][column].apply {
                    isEnabled = (row == 0)
                    addActionListener {
                        currentPeg = column
                    }
                    setUI(RoundRadioButtonUI())
                }
                constraints.apply {
                    gridx = column
                    gridy = row * 2
                    gridheight = 2
                    weightx = 2.0
                    weighty = 2.0
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
        guessPegs[0][0].isSelected = true
        for ((colorIndex, color) in colors.take(game.colorCount).withIndex()) {
            val colorButton = colorButtons[colorIndex].apply {
                foreground = color
                setUI(RoundButtonUI())
                addActionListener {
                    guessPegs[currentAttempt][currentPeg].foreground = foreground
                    guess[currentPeg] = colorIndex
                    currentPeg++
                }
                val command = "click"
                getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke((colorIndex + 1).toString()), command)
                actionMap.put(command, makeAction {
                    this@apply.doClick()
                })
            }
            constraints.apply {
                gridx = game.pegCount + answerPegsPerRow
                gridy = colorIndex * 2
                gridheight = 2
                weightx = 2.0
                weighty = 2.0
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
            if (game.result == Game.Result.UNFINISHED) {
                for (peg in guessPegs[currentAttempt])
                    peg.isEnabled = false
                ++currentAttempt
                for (peg in guessPegs[currentAttempt])
                    peg.isEnabled = true
                for (pegIndex in 0 until game.pegCount)
                    guess[pegIndex] = null
                currentPeg = 0
            } else {
                showCode()
                val message = "You ${if (game.result == Game.Result.WIN) "win" else "lose"}. Play another game?"
                val playAgain =
                    JOptionPane.showConfirmDialog(this, message, "Game is finished", JOptionPane.YES_NO_OPTION)
                if (playAgain == JOptionPane.YES_OPTION)
                    application.clearBoard()
                else
                    SwingUtilities.getWindowAncestor(this).dispose()
            }
        }
        guessButton.apply {
            val command = "guess"
            getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), command)
            actionMap.put(command, makeAction {
                this@apply.doClick()
            })
        }
        constraints.apply {
            gridx = 0
            gridy = max(game.attemptCount, game.colorCount) * 2
            gridheight = 1
            gridwidth = GridBagConstraints.REMAINDER
            weighty = 2.0
        }
        add(guessButton, constraints)
    }

    private fun showCode() {
        remove(guessButton)
        val code = requireNotNull(game.code)
        for ((pegIndex, peg) in codePegs.withIndex()) {
            peg.apply {
                setUI(RoundButtonUI())
                isEnabled = false
                foreground = colors[code[pegIndex]]
            }
            constraints.apply {
                gridx = pegIndex
                gridy = max(game.attemptCount, game.colorCount) * 2
                gridheight = 1
                gridwidth = 1
                weightx = 2.0
                weighty = 2.0
                insets = Insets(5, 5, 5, 5)
            }
            add(peg, constraints)
        }
        validate()
    }
}
