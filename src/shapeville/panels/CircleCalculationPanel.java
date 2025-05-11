package shapeville.panels;

import shapeville.*; // Import all from base package

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;

public class CircleCalculationPanel extends TaskPanel {
    private JComboBox<String> calculationTypeSelector; // "Area", "Circumference"
    private JComboBox<String> givenValueSelector; // "Radius", "Diameter"
    private JLabel questionLabel;
    private JTextField answerField;
    private JButton submitButton;
    private GameTimer gameTimer;
    private JLabel timerLabel;
    private JLabel solutionImageLabel; // To show image from Fig 5/6 style

    private String currentCalcTypeKey; // e.g., "RadiusArea" from QuestionManager
    private int currentDimensionValue;
    private double correctAnswer;
    private static final DecimalFormat df = new DecimalFormat("#.##");

    public CircleCalculationPanel(ShapevilleApp app, ScoreManager scoreManager, QuestionManager questionManager) {
        super(app, scoreManager, questionManager, "Circle Area & Circumference");

        centralContentPanel.setLayout(new BorderLayout(10, 10));

        // Top: Selectors and Timer
        JPanel topSelectorsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        topSelectorsPanel.setOpaque(false);
        calculationTypeSelector = new JComboBox<>(new String[] { "Area", "Circumference" });
        givenValueSelector = new JComboBox<>(new String[] { "Radius", "Diameter" });
        // These selectors are for display only; QM drives the actual task sequence.
        calculationTypeSelector.setEnabled(false);
        givenValueSelector.setEnabled(false);

        timerLabel = new JLabel("Time: 00:00");
        timerLabel.setFont(UIConstants.LABEL_FONT.deriveFont(Font.BOLD));

        topSelectorsPanel.add(new JLabel("Calculate:"));
        topSelectorsPanel.add(calculationTypeSelector);
        topSelectorsPanel.add(new JLabel("Given:"));
        topSelectorsPanel.add(givenValueSelector);
        topSelectorsPanel.add(timerLabel);
        centralContentPanel.add(topSelectorsPanel, BorderLayout.NORTH);

        // Middle: Question and Solution Image
        JPanel questionSolutionArea = new JPanel(new GridLayout(2, 1, 5, 15));
        questionSolutionArea.setOpaque(false);

        questionLabel = new JLabel("Details will appear here.", SwingConstants.CENTER);
        questionLabel.setFont(UIConstants.LABEL_FONT.deriveFont(18f));
        questionLabel.setOpaque(true);
        questionLabel.setBackground(Color.WHITE);
        questionLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        questionSolutionArea.add(questionLabel);

        solutionImageLabel = new JLabel();
        solutionImageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        solutionImageLabel.setVerticalAlignment(SwingConstants.CENTER);
        solutionImageLabel.setOpaque(true);
        solutionImageLabel.setBackground(Color.WHITE);
        solutionImageLabel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        solutionImageLabel.setPreferredSize(new Dimension(400, 200)); // Adjust as needed for Fig 5/6
        questionSolutionArea.add(solutionImageLabel);
        centralContentPanel.add(questionSolutionArea, BorderLayout.CENTER);

        // Bottom: Answer Input
        JPanel answerInputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        answerInputPanel.setOpaque(false);
        answerInputPanel.add(new JLabel("Enter Answer:"));
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
        submitButton = createStyledButton("Submit Answer");
        submitButton.addActionListener(e -> checkAnswer());
        answerInputPanel.add(answerField);
        answerInputPanel.add(submitButton);
        centralContentPanel.add(answerInputPanel, BorderLayout.SOUTH);

        gameTimer = new GameTimer(UIConstants.TIMER_DURATION_SHORT, timerLabel, this::handleTimeUp);
    }

    @Override
    public void startTask() {
        questionsDoneThisTaskSession = 0;
        scoreManager.resetQuestionAttempts();
        feedbackLabel.setText(" ");
        questionManager.resetTaskProgress("CIRCLE_CALC"); // Resets QM to cycle through circle calc tasks
        enableTaskInputs();
        nextQuestion();
    }

    @Override
    protected void nextQuestion() {
        if (questionManager.allCircleCalcTasksPracticedForTask()) {
            showFeedback("Fantastic! You've practiced all circle calculation types. Task complete!", true);
            disableTaskInputs();
            questionLabel.setText("Task Complete!");
            solutionImageLabel.setIcon(null);
            return;
        }

        currentCalcTypeKey = questionManager.getCurrentCircleCalcTaskKey();
        if (currentCalcTypeKey == null) {
            showFeedback("No more circle calculation tasks.", true);
            disableTaskInputs();
            return;
        }

        // Update JComboBox displays based on the currentCalcTypeKey from QM
        String calcTypeDisplay = currentCalcTypeKey.endsWith("Area") ? "Area" : "Circumference";
        String givenTypeDisplay = currentCalcTypeKey.startsWith("Radius") ? "Radius" : "Diameter";
        calculationTypeSelector.setSelectedItem(calcTypeDisplay);
        givenValueSelector.setSelectedItem(givenTypeDisplay);

        scoreManager.resetQuestionAttempts();
        currentDimensionValue = questionManager.getRandomCircleDimension(); // 1-20
        correctAnswer = questionManager.calculateCircleProperty(currentCalcTypeKey, currentDimensionValue);

        questionLabel
                .setText("<html><div style='text-align:center;'>Calculate the <b>" + calcTypeDisplay.toLowerCase() +
                        "</b> of a circle given its <b>" + givenTypeDisplay.toLowerCase() +
                        "</b> = <b>" + currentDimensionValue + "</b> units.</div></html>");

        answerField.setText("");
        feedbackLabel.setText("Enter your calculated value.");
        solutionImageLabel.setIcon(null); // Clear previous solution
        enableTaskInputs();
        gameTimer.reset(UIConstants.TIMER_DURATION_SHORT);
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
        Timer timer = new Timer(3500, e -> {
            questionManager.recordCircleCalcTaskPracticed(currentCalcTypeKey);
            questionsDoneThisTaskSession++;
            nextQuestion();
        });
        timer.setRepeats(false);
        timer.start();
    }

    @Override
    protected void checkAnswer() {
        if (currentCalcTypeKey == null)
            return;
        gameTimer.stop();

        try {
            String userAnswerText = answerField.getText().trim();
            if (userAnswerText.isEmpty()) {
                showFeedback("Please enter your calculated value.", false);
                if (scoreManager.canAttempt())
                    gameTimer.start();
                return;
            }
            double userAnswer = Double.parseDouble(userAnswerText);
            scoreManager.recordAttempt();

            // Use a small epsilon for comparing doubles, especially with PI
            if (Math.abs(userAnswer - correctAnswer) < 0.015) {
                awardPointsAndShowFeedback(false); // Basic scoring
                showSolution();
                disableTaskInputs();
                questionManager.recordCircleCalcTaskPracticed(currentCalcTypeKey);
                questionsDoneThisTaskSession++;

                Timer timer = new Timer(3500, e -> nextQuestion());
                timer.setRepeats(false);
                timer.start();
            } else {
                if (scoreManager.canAttempt()) {
                    showFeedback(UIConstants.MSG_TRY_AGAIN + " ("
                            + (ScoreManager.MAX_ATTEMPTS_PER_QUESTION - scoreManager.getCurrentQuestionAttempts())
                            + " attempts left)", false);
                    answerField.selectAll();
                    answerField.requestFocusInWindow();
                    gameTimer.start();
                } else {
                    showSolution();
                    disableTaskInputs();
                    questionManager.recordCircleCalcTaskPracticed(currentCalcTypeKey);
                    questionsDoneThisTaskSession++;

                    Timer timer = new Timer(3500, e -> nextQuestion());
                    timer.setRepeats(false);
                    timer.start();
                }
            }
        } catch (NumberFormatException e) {
            showFeedback(UIConstants.MSG_INVALID_INPUT + " Enter a number.", false);
            if (scoreManager.canAttempt())
                gameTimer.start();
        }
    }

    @Override
    protected void showSolution() {
        if (currentCalcTypeKey == null)
            return;
        feedbackLabel.setText(UIConstants.MSG_CORRECT_ANSWER_IS + df.format(correctAnswer));

        String imageName = "";
        if (currentCalcTypeKey.startsWith("Radius")) {
            imageName = "circle_radius_solution.png"; // Represents Fig 5 style
        } else { // Diameter
            imageName = "circle_diameter_solution.png"; // Represents Fig 6 style
        }
        // These images should show the circle, given dim, formula, and substituted
        // values.
        // For truly dynamic display: you'd draw this or use JLabels to show specific
        // values on a base image.
        // For skeletal simplicity, we assume pre-made solution images.
        // You'll need to create these images or implement dynamic rendering.
        if (!imageName.isEmpty()) {
            ImageIcon icon = ImageLoader.loadImage(imageName);
            if (icon != null && icon.getIconWidth() > 0) {
                Image img = icon.getImage();
                int newWidth = solutionImageLabel.getWidth() > 0 ? solutionImageLabel.getWidth() - 10 : 380;
                int newHeight = solutionImageLabel.getHeight() > 0 ? solutionImageLabel.getHeight() - 10 : 180;
                if (newWidth <= 0 || newHeight <= 0) {
                    solutionImageLabel.setIcon(icon);
                } else {
                    Image scaledImg = img.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
                    solutionImageLabel.setIcon(new ImageIcon(scaledImg));
                }
            } else {
                solutionImageLabel.setText("Solution image not found: " + imageName);
            }
        } else {
            solutionImageLabel.setText("Solution: " + currentCalcTypeKey + " with value " + currentDimensionValue);
        }
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