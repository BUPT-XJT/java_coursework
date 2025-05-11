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

public class AreaCalculationPanel extends TaskPanel {
    private JComboBox<String> shapeSelectorComboBox; // User does not select, QM drives this for "practice all 4"
    private JLabel currentShapeDisplayLabel; // To show which shape is currently being practiced
    private JLabel questionDimensionsLabel;
    private JTextField answerField;
    private JButton submitButton;
    private GameTimer gameTimer;
    private JLabel timerLabel;
    private JLabel solutionImageLabel; // To display image with formula and substituted values (Fig 4 style)

    private String currentShapeType;
    private Map<String, Integer> currentDimensions;
    private double correctAnswer;
    private static final DecimalFormat df = new DecimalFormat("#.##");

    public AreaCalculationPanel(ShapevilleApp app, ScoreManager scoreManager, QuestionManager questionManager) {
        super(app, scoreManager, questionManager, "Area Calculation - Basic Shapes");

        centralContentPanel.setLayout(new BorderLayout(10, 10));

        // Top: Shape type display and Timer
        JPanel topInfoPanel = new JPanel(new BorderLayout(10, 0));
        topInfoPanel.setOpaque(false);
        currentShapeDisplayLabel = new JLabel("Current Shape: ", SwingConstants.LEFT);
        currentShapeDisplayLabel.setFont(UIConstants.LABEL_FONT.deriveFont(Font.BOLD));
        timerLabel = new JLabel("Time: 00:00");
        timerLabel.setFont(UIConstants.LABEL_FONT.deriveFont(Font.BOLD));
        timerLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        topInfoPanel.add(currentShapeDisplayLabel, BorderLayout.CENTER);
        topInfoPanel.add(timerLabel, BorderLayout.EAST);
        centralContentPanel.add(topInfoPanel, BorderLayout.NORTH);

        // Middle: Question (dimensions) and Solution Image display area
        JPanel questionAndSolutionArea = new JPanel(new GridLayout(2, 1, 5, 15)); // Dimensions above, solution image
                                                                                  // below
        questionAndSolutionArea.setOpaque(false);

        questionDimensionsLabel = new JLabel("Dimensions will appear here.", SwingConstants.CENTER);
        questionDimensionsLabel.setFont(UIConstants.LABEL_FONT.deriveFont(18f)); // Larger font for dimensions
        questionDimensionsLabel.setOpaque(true);
        questionDimensionsLabel.setBackground(Color.WHITE);
        questionDimensionsLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        questionAndSolutionArea.add(questionDimensionsLabel);

        solutionImageLabel = new JLabel(); // For Fig 4 style solution image
        solutionImageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        solutionImageLabel.setVerticalAlignment(SwingConstants.CENTER);
        solutionImageLabel.setOpaque(true);
        solutionImageLabel.setBackground(Color.WHITE);
        solutionImageLabel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        solutionImageLabel.setPreferredSize(new Dimension(400, 150)); // Adjust as needed
        questionAndSolutionArea.add(solutionImageLabel);

        centralContentPanel.add(questionAndSolutionArea, BorderLayout.CENTER);

        // Bottom: Answer input
        JPanel answerInputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        answerInputPanel.setOpaque(false);
        answerInputPanel.add(new JLabel("Enter Area:"));
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

        gameTimer = new GameTimer(UIConstants.TIMER_DURATION_SHORT, timerLabel, this::handleTimeUp);
    }

    @Override
    public void startTask() {
        questionsDoneThisTaskSession = 0;
        scoreManager.resetQuestionAttempts();
        feedbackLabel.setText(" ");
        questionManager.resetTaskProgress("AREA_CALC"); // Resets QM to cycle through basic area shapes
        enableTaskInputs();
        nextQuestion();
    }

    @Override
    protected void nextQuestion() {
        if (questionManager.allBasicAreaShapesPracticedForTask(BASIC_AREAS_TO_PRACTICE_COUNT)) {
            showFeedback("Well done! You've practiced all basic area shapes. Task complete!", true);
            disableTaskInputs();
            currentShapeDisplayLabel.setText("Task Complete!");
            questionDimensionsLabel.setText("");
            solutionImageLabel.setIcon(null);
            return;
        }

        currentShapeType = questionManager.getCurrentBasicAreaShapeType();
        if (currentShapeType == null) { // Should be caught by above check
            showFeedback("No more shapes to practice in this category.", true);
            disableTaskInputs();
            return;
        }

        currentShapeDisplayLabel.setText("Current Shape: " + currentShapeType);
        scoreManager.resetQuestionAttempts();
        currentDimensions = questionManager.getRandomDimensionsForShape(currentShapeType);
        correctAnswer = questionManager.calculateArea(currentShapeType, currentDimensions);

        StringBuilder qBuilder = new StringBuilder(
                "<html><div style='text-align:center;'>Calculate area of a " + currentShapeType + " with:<br>");
        switch (currentShapeType.toLowerCase()) {
            case "rectangle":
                qBuilder.append("Length = <b>").append(currentDimensions.get("length"))
                        .append("</b> units, Width = <b>").append(currentDimensions.get("width")).append("</b> units");
                break;
            case "parallelogram":
            case "triangle":
                qBuilder.append("Base = <b>").append(currentDimensions.get("base")).append("</b> units, Height = <b>")
                        .append(currentDimensions.get("height")).append("</b> units");
                break;
            case "trapezium":
                qBuilder.append("Top Base (a) = <b>").append(currentDimensions.get("top_base_a"))
                        .append("</b> units<br>");
                qBuilder.append("Bottom Base (b) = <b>").append(currentDimensions.get("bottom_base_b"))
                        .append("</b> units<br>");
                qBuilder.append("Height (h) = <b>").append(currentDimensions.get("height")).append("</b> units");
                break;
        }
        qBuilder.append("</div></html>");
        questionDimensionsLabel.setText(qBuilder.toString());

        answerField.setText("");
        feedbackLabel.setText("Enter the calculated area.");
        solutionImageLabel.setIcon(null); // Clear previous solution image
        enableTaskInputs();
        gameTimer.reset(UIConstants.TIMER_DURATION_SHORT); // Reset to 3 minutes
        gameTimer.start();
        answerField.requestFocusInWindow();
    }

    private void handleTimeUp() {
        if (!gameTimer.isRunning())
            return; // Avoid multiple calls if already handled
        gameTimer.stop();
        showFeedback(UIConstants.MSG_TIME_UP, false);
        scoreManager.recordAttempt(); // Count as an attempt

        // Even if time up, show solution and move to next, as per spec "3 unsuccessful
        // attempts OR time up" (implicit)
        // For simplicity, time up after any attempt counts as one of the 3, or just
        // forces solution.
        // Let's assume time-up always shows solution and moves on for this task.
        showSolution(); // Display solution
        disableTaskInputs();
        Timer timer = new Timer(3500, e -> { // Longer delay to see solution
            questionManager.recordBasicAreaShapePracticed(currentShapeType); // Mark as practiced
            questionsDoneThisTaskSession++;
            nextQuestion();
        });
        timer.setRepeats(false);
        timer.start();
    }

    @Override
    protected void checkAnswer() {
        if (currentShapeType == null)
            return;
        gameTimer.stop(); // Stop timer on submission

        try {
            String userAnswerText = answerField.getText().trim();
            if (userAnswerText.isEmpty()) {
                showFeedback("Please enter your calculated area.", false);
                if (scoreManager.canAttempt())
                    gameTimer.start(); // Resume if attempts left
                return;
            }
            double userAnswer = Double.parseDouble(userAnswerText);
            scoreManager.recordAttempt();

            if (Math.abs(userAnswer - correctAnswer) < 0.01) { // Epsilon for float comparison
                awardPointsAndShowFeedback(false); // Basic scoring
                showSolution(); // Show image with formula and substituted values
                disableTaskInputs();
                questionManager.recordBasicAreaShapePracticed(currentShapeType);
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
                    gameTimer.start(); // Resume timer
                } else {
                    showSolution();
                    disableTaskInputs();
                    questionManager.recordBasicAreaShapePracticed(currentShapeType); // Still counts as "practiced"
                    questionsDoneThisTaskSession++;

                    Timer timer = new Timer(3500, e -> nextQuestion());
                    timer.setRepeats(false);
                    timer.start();
                }
            }
        } catch (NumberFormatException e) {
            showFeedback(UIConstants.MSG_INVALID_INPUT + " Enter a number for the area.", false);
            if (scoreManager.canAttempt())
                gameTimer.start(); // Resume timer if attempts left
        }
    }

    @Override
    protected void showSolution() {
        if (currentShapeType == null)
            return;
        feedbackLabel.setText(UIConstants.MSG_CORRECT_ANSWER_IS + df.format(correctAnswer));

        // Load and display the image from Figure 4 corresponding to currentShapeType
        // This image should show the shape, dimensions, formula, and substituted
        // values.
        String imageName = "";
        switch (currentShapeType.toLowerCase()) {
            case "rectangle":
                imageName = "rectangle_formula.png";
                break;
            case "parallelogram":
                imageName = "parallelogram_formula.png";
                break;
            case "triangle":
                imageName = "triangle_formula.png";
                break;
            case "trapezium":
                imageName = "trapezium_formula.png";
                break;
        }
        if (!imageName.isEmpty()) {
            ImageIcon icon = ImageLoader.loadImage(imageName);
            // Scale if necessary
            if (icon != null && icon.getIconWidth() > 0) {
                Image img = icon.getImage();
                int newWidth = solutionImageLabel.getWidth() > 0 ? solutionImageLabel.getWidth() - 10 : 380;
                int newHeight = solutionImageLabel.getHeight() > 0 ? solutionImageLabel.getHeight() - 10 : 140;
                // Maintain aspect ratio if one dimension is 0 (auto)
                if (newWidth <= 0 || newHeight <= 0) { // if label size not determined yet
                    solutionImageLabel.setIcon(icon); // show original
                } else {
                    Image scaledImg = img.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
                    solutionImageLabel.setIcon(new ImageIcon(scaledImg));
                }
            } else {
                solutionImageLabel.setText("Solution image not found: " + imageName);
            }
        } else {
            solutionImageLabel.setText("Solution display for " + currentShapeType);
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
