package shapeville.panels;

import shapeville.*; // Import all from base package

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import java.util.Map;

public class SectorAreaPanel extends TaskPanel {
    private JComboBox<String> sectorSelectorComboBox;
    private JLabel sectorImageLabel;
    private JTextArea solutionFormulaArea;
    private JTextField answerField;
    private JButton submitButton;
    private GameTimer gameTimer;
    private JLabel timerLabel;

    private Map<String, Object> currentSectorData;
    private double correctAnswer;
    private static final DecimalFormat df = new DecimalFormat("#.##"); // For rounding to two decimal places

    public SectorAreaPanel(ShapevilleApp app, ScoreManager scoreManager, QuestionManager questionManager) {
        super(app, scoreManager, questionManager, "Sector of a Circle Area (Bonus)");
        // Layout similar to CompoundShapePanel

        centralContentPanel.setLayout(new BorderLayout(10, 10));

        // Top: Sector Selector (display only) and Timer
        JPanel topControls = new JPanel(new BorderLayout(10, 0));
        topControls.setOpaque(false);
        sectorSelectorComboBox = new JComboBox<>();
        sectorSelectorComboBox.setFont(UIConstants.LABEL_FONT);
        sectorSelectorComboBox.setEnabled(false); // QM drives the sequence

        timerLabel = new JLabel("Time: 00:00");
        timerLabel.setFont(UIConstants.LABEL_FONT.deriveFont(Font.BOLD));
        timerLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        topControls.add(new JLabel("Current Sector:"), BorderLayout.WEST);
        topControls.add(sectorSelectorComboBox, BorderLayout.CENTER);
        topControls.add(timerLabel, BorderLayout.EAST);
        centralContentPanel.add(topControls, BorderLayout.NORTH);

        // Middle: Sector Image and Solution Formula Area (similar to
        // CompoundShapePanel)
        JSplitPane middleSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        middleSplitPane.setOpaque(false);
        middleSplitPane.setResizeWeight(0.65); // Image gets a bit more space
        middleSplitPane.setBorder(null);

        sectorImageLabel = new JLabel();
        sectorImageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        sectorImageLabel.setVerticalAlignment(SwingConstants.CENTER);
        sectorImageLabel.setOpaque(true);
        sectorImageLabel.setBackground(Color.WHITE);
        sectorImageLabel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        sectorImageLabel.setPreferredSize(new Dimension(350, 300));
        middleSplitPane.setTopComponent(new JScrollPane(sectorImageLabel));

        solutionFormulaArea = new JTextArea("Solution formula will appear here.");
        solutionFormulaArea.setFont(UIConstants.LABEL_FONT.deriveFont(13f));
        solutionFormulaArea.setEditable(false);
        solutionFormulaArea.setWrapStyleWord(true);
        solutionFormulaArea.setLineWrap(true);
        solutionFormulaArea.setOpaque(true);
        solutionFormulaArea.setBackground(Color.WHITE);
        solutionFormulaArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        JScrollPane solutionScrollPane = new JScrollPane(solutionFormulaArea);
        solutionScrollPane.setPreferredSize(new Dimension(350, 150));
        middleSplitPane.setBottomComponent(solutionScrollPane);
        centralContentPanel.add(middleSplitPane, BorderLayout.CENTER);

        // Bottom: Answer Input
        JPanel answerInputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        answerInputPanel.setOpaque(false);
        answerInputPanel.add(new JLabel("Enter Sector Area:"));
        answerField = new JTextField(10);
        answerField.setFont(UIConstants.INPUT_FONT);
        answerField.setHorizontalAlignment(JTextField.CENTER);
        answerField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    submitButton.doClick();
                }
            }
        });
        submitButton = createStyledButton("Submit Area");
        submitButton.addActionListener(e -> checkAnswer());
        answerInputPanel.add(answerField);
        answerInputPanel.add(submitButton);
        centralContentPanel.add(answerInputPanel, BorderLayout.SOUTH);

        gameTimer = new GameTimer(UIConstants.TIMER_DURATION_LONG, timerLabel, this::handleTimeUp); // 5 minutes
    }

    @Override
    public void startTask() {
        questionsDoneThisTaskSession = 0;
        scoreManager.resetQuestionAttempts();
        feedbackLabel.setText(" ");
        questionManager.resetTaskProgress("SECTOR_AREA");

        String[] sectorIdentifiers = questionManager.getSectorAreaIdentifiers();
        sectorSelectorComboBox.setModel(new DefaultComboBoxModel<>(sectorIdentifiers));

        enableTaskInputs();
        nextQuestion();
    }

    @Override
    protected void nextQuestion() {
        if (questionManager.allSectorsPracticedForTask(SECTORS_TO_PRACTICE)) {
            showFeedback("Excellent work! You've practiced all sector area calculations. Task complete!", true);
            disableTaskInputs();
            sectorImageLabel.setIcon(null);
            sectorImageLabel.setText("Task Complete!");
            solutionFormulaArea.setText("");
            sectorSelectorComboBox.setSelectedItem(null);
            return;
        }

        currentSectorData = questionManager.getCurrentSectorAreaData();
        if (currentSectorData == null) {
            showFeedback("No more sectors available for practice.", true);
            disableTaskInputs();
            return;
        }

        sectorSelectorComboBox.setSelectedItem(currentSectorData.getOrDefault("name", "Unknown Sector"));

        scoreManager.resetQuestionAttempts();
        ImageIcon icon = ImageLoader.loadImage((String) currentSectorData.get("image"));
        if (icon != null && icon.getIconWidth() > 0) {
            Image img = icon.getImage();
            int prefWidth = sectorImageLabel.getPreferredSize().width > 0
                    ? sectorImageLabel.getPreferredSize().width - 10
                    : 330;
            Image scaledImg = img.getScaledInstance(prefWidth, -1, Image.SCALE_SMOOTH);
            sectorImageLabel.setIcon(new ImageIcon(scaledImg));
        } else {
            sectorImageLabel.setIcon(ImageLoader.getPlaceholderIcon());
            sectorImageLabel.setText("Image not found: " + currentSectorData.get("image"));
        }
        // Area is pre-calculated and stored rounded in QM, using PI=3.14 as per Fig 13
        // example.
        correctAnswer = (double) currentSectorData.getOrDefault("area", 0.0);

        answerField.setText("");
        feedbackLabel.setText("Calculate the area of the shaded sector. Round to two decimal places (use π = 3.14).");
        solutionFormulaArea.setText("Solution formula will appear after your attempt.");
        solutionFormulaArea.setCaretPosition(0);
        enableTaskInputs();
        gameTimer.reset(UIConstants.TIMER_DURATION_LONG); // 5 minutes
        gameTimer.start();
        answerField.requestFocusInWindow();
    }

    private void handleTimeUp() {
        if (!gameTimer.isRunning())
            return;
        gameTimer.stop();
        showFeedback(UIConstants.MSG_TIME_UP, false);
        scoreManager.recordAttempt();
        showSolution();
        disableTaskInputs();
        Timer timer = new Timer(4000, e -> {
            questionManager.recordSectorAreaPracticed((String) currentSectorData.getOrDefault("id", "unknown"));
            questionsDoneThisTaskSession++;
            nextQuestion();
        });
        timer.setRepeats(false);
        timer.start();
    }

    @Override
    protected void checkAnswer() {
        if (currentSectorData == null)
            return;
        gameTimer.stop();

        try {
            String userAnswerText = answerField.getText().trim();
            if (userAnswerText.isEmpty()) {
                showFeedback("Please enter your calculated area.", false);
                if (scoreManager.canAttempt())
                    gameTimer.start();
                return;
            }
            double userAnswerRaw = Double.parseDouble(userAnswerText);
            // Round user's answer to two decimal places for comparison, as per spec for
            // this task.
            double userAnswerRounded = Double.parseDouble(df.format(userAnswerRaw));

            scoreManager.recordAttempt();

            // Compare the rounded user answer with the (already rounded) correct answer
            // from QM.
            if (Math.abs(userAnswerRounded - correctAnswer) < 0.001) { // Epsilon for small diff after rounding
                awardPointsAndShowFeedback(true); // Advanced scoring
                showSolution();
                disableTaskInputs();
                questionManager.recordSectorAreaPracticed((String) currentSectorData.getOrDefault("id", "unknown"));
                questionsDoneThisTaskSession++;

                Timer timer = new Timer(4000, e -> nextQuestion());
                timer.setRepeats(false);
                timer.start();
            } else {
                if (scoreManager.canAttempt()) {
                    showFeedback(
                            UIConstants.MSG_TRY_AGAIN + " ("
                                    + (ScoreManager.MAX_ATTEMPTS_PER_QUESTION
                                            - scoreManager.getCurrentQuestionAttempts())
                                    + " attempts left). Remember to round to 2 decimal places and use π = 3.14.",
                            false);
                    answerField.selectAll();
                    answerField.requestFocusInWindow();
                    gameTimer.start();
                } else {
                    showSolution();
                    disableTaskInputs();
                    questionManager.recordSectorAreaPracticed((String) currentSectorData.getOrDefault("id", "unknown"));
                    questionsDoneThisTaskSession++;

                    Timer timer = new Timer(4000, e -> nextQuestion());
                    timer.setRepeats(false);
                    timer.start();
                }
            }
        } catch (NumberFormatException e) {
            showFeedback(UIConstants.MSG_INVALID_INPUT + " Enter a number for the area.", false);
            if (scoreManager.canAttempt())
                gameTimer.start();
        }
    }

    @Override
    protected void showSolution() {
        if (currentSectorData == null)
            return;
        // Correct answer is already rounded in QM as per spec (PI=3.14, round to 2
        // decimal places)
        feedbackLabel.setText(UIConstants.MSG_CORRECT_ANSWER_IS + df.format(correctAnswer));

        String solutionText = (String) currentSectorData.getOrDefault(
                "solution_formula", "Detailed formula and steps are not available.");
        solutionFormulaArea.setText(solutionText);
        solutionFormulaArea.setCaretPosition(0);
    }

    @Override
    protected void disableTaskInputs() {
        answerField.setEnabled(false);
        submitButton.setEnabled(false);
    }

    @Override
    protected void enableTaskInputs() {
        answerField.setEnabled(true);
        submitButton.setEnabled(true);
    }
}