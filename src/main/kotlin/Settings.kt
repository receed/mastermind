import javax.swing.*

class Settings(private val application: Application) : JDialog(application, "Settings", true) {
    private val attemptSpinnerModel: SpinnerNumberModel
    private val pegSpinnerModel: SpinnerNumberModel
    private val colorSpinnerModel: SpinnerNumberModel

    init {
        val panel = JPanel()
        contentPane = panel
        defaultCloseOperation = DISPOSE_ON_CLOSE
        isResizable = false

        val groupLayout = GroupLayout(panel).apply {
            autoCreateGaps = true
            autoCreateContainerGaps = true
        }
        val attemptLabel = JLabel("Attempt count:")
        val pegLabel = JLabel("Peg count:")
        val colorLabel = JLabel("Color count:")

        val attemptSpinner = JSpinner()
        attemptSpinnerModel = SpinnerNumberModel(1, 1, 10, 1)
        attemptSpinner.model = attemptSpinnerModel

        val pegSpinner = JSpinner()
        pegSpinnerModel = SpinnerNumberModel(1, 1, 6, 1)
        pegSpinner.model = pegSpinnerModel

        val colorSpinner = JSpinner()
        colorSpinnerModel = SpinnerNumberModel(1, 1, 7, 1)
        colorSpinner.model = colorSpinnerModel

        val continueButton = JButton("Continue")
        continueButton.addActionListener {
            updateGameParameters()
            isVisible = false
        }
        val restartButton = JButton("Restart")
        restartButton.addActionListener {
            updateGameParameters()
            isVisible = false
            application.clearBoard()
        }
        val cancelButton = JButton("Cancel")
        cancelButton.addActionListener {
            isVisible = false
        }

        groupLayout.setHorizontalGroup(
            groupLayout.createParallelGroup()
                .addGroup(
                    groupLayout.createSequentialGroup()
                        .addGroup(
                            groupLayout.createParallelGroup().addComponent(attemptLabel).addComponent(pegLabel)
                                .addComponent(colorLabel)
                        )
                        .addGroup(
                            groupLayout.createParallelGroup()
                                .addComponent(
                                    attemptSpinner,
                                    GroupLayout.PREFERRED_SIZE,
                                    GroupLayout.DEFAULT_SIZE,
                                    GroupLayout.PREFERRED_SIZE
                                )
                                .addComponent(
                                    pegSpinner,
                                    GroupLayout.PREFERRED_SIZE,
                                    GroupLayout.DEFAULT_SIZE,
                                    GroupLayout.PREFERRED_SIZE
                                )
                                .addComponent(
                                    colorSpinner,
                                    GroupLayout.PREFERRED_SIZE,
                                    GroupLayout.DEFAULT_SIZE,
                                    GroupLayout.PREFERRED_SIZE
                                )
                        )
                )
                .addGroup(
                    groupLayout.createSequentialGroup()
                        .addComponent(continueButton)
                        .addComponent(restartButton)
                        .addComponent(cancelButton)
                )
        )
        groupLayout.linkSize(attemptSpinner, pegSpinner, colorSpinner)

        groupLayout.setVerticalGroup(
            groupLayout.createSequentialGroup()
                .addGroup(groupLayout.createParallelGroup().addComponent(attemptLabel).addComponent(attemptSpinner))
                .addGroup(groupLayout.createParallelGroup().addComponent(pegLabel).addComponent(pegSpinner))
                .addGroup(groupLayout.createParallelGroup().addComponent(colorLabel).addComponent(colorSpinner))
                .addGroup(
                    groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(continueButton)
                        .addComponent(restartButton)
                        .addComponent(cancelButton)
                )
        )
        panel.layout = groupLayout
        setLocationRelativeTo(application)
    }

    private fun updateGameParameters() {
        application.gameParameters = Game.Parameters(
            attemptSpinnerModel.number as Int,
            pegSpinnerModel.number as Int,
            colorSpinnerModel.number as Int
        )
    }

    fun view() {
        attemptSpinnerModel.value = application.gameParameters.attemptCount
        pegSpinnerModel.value = application.gameParameters.pegCount
        colorSpinnerModel.value = application.gameParameters.colorCount
        pack()
        isVisible = true
    }
}