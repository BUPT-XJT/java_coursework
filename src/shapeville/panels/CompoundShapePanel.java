package shapeville.panels;

import shapeville.*; // Import all from base package

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import java.util.Map;

public class CompoundShapePanel extends TaskPanel {
    private JComboBox<String> shapeSelectorComboBox; // To select one of the 9 shapes (driven by QM)
    private JLabel shapeImageLabel; // Displays the compound shape (Fig 10)
    private JTextArea solutionBreakdownArea; // To show formula/breakdown after
    private JTextField answerField;
    private JButton submitButton;
    private GameTimer gameTimer;
    private JLabel timerLabel;

    private Map<String, Object> currentCompoundShapeData;
    private double correctAnswer;
    private static final DecimalFormat df = new DecimalFormat("#.##");

    public CompoundShapePanel(ShapevilleApp app, ScoreManager scoreManager, QuestionManager questionManager) {
        super(app, scoreManager, questionManager, "Compound Shapes Area (Bonus)");

        centralContentPanel.setLayout(new BorderLayout(10, 10));

        // Top: Shape Selector (display only) and Timer
        JPanel topControls = new JPanel(new BorderLayout(10, 0));
        topControls.setOpaque(false);
        shapeSelectorComboBox = new JComboBox<>();
        shapeSelectorComboBox.setFont(UIConstants.LABEL_FONT);
        shapeSelectorComboBox.setEnabled(false); // QM drives the sequence

        timerLabel = new JLabel("Time: 00:00");
        timerLabel.setFont(UIConstants.LABEL_FONT.deriveFont(Font.BOLD));
        timerLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        topControls.add(new JLabel("Current Shape:"), BorderLayout.WEST);
        topControls.add(shapeSelectorComboBox, BorderLayout.CENTER);
        topControls.add(timerLabel, BorderLayout.EAST);
        centralContentPanel.add(topControls, BorderLayout.NORTH);

        // Middle: Shape Image and Solution Breakdown Area
        JSplitPane middleSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        middleSplitPane.setOpaque(false);
        middleSplitPane.setResizeWeight(0.6); // Image gets more space initially
        middleSplitPane.setBorder(null);

        shapeImageLabel = new JLabel();
        shapeImageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        shapeImageLabel.setVerticalAlignment(SwingConstants.CENTER);
        shapeImageLabel.setOpaque(true);
        shapeImageLabel.setBackground(Color.WHITE);
        shapeImageLabel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        shapeImageLabel.setPreferredSize(new Dimension(400, 300)); // Good starting size
        middleSplitPane.setTopComponent(new JScrollPane(shapeImageLabel)); // Scroll if image is huge

        solutionBreakdownArea = new JTextArea("Solution breakdown will appear here.");
        solutionBreakdownArea.setFont(UIConstants.LABEL_FONT.deriveFont(13f));
        solutionBreakdownArea.setEditable(false);
        solutionBreakdownArea.setWrapStyleWord(true);
        solutionBreakdownArea.setLineWrap(true);
        solutionBreakdownArea.setOpaque(true);
        solutionBreakdownArea.setBackground(Color.WHITE);
        solutionBreakdownArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        JScrollPane solutionScrollPane = new JScrollPane(solutionBreakdownArea);
        solutionScrollPane.setPreferredSize(new Dimension(400, 150));
        middleSplitPane.setBottomComponent(solutionScrollPane);
        centralContentPanel.add(middleSplitPane, BorderLayout.CENTER);

        // Bottom: Answer Input
        JPanel answerInputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        answerInputPanel.setOpaque(false);
        answerInputPanel.add(new JLabel("Enter Total Area:"));
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
        questionManager.resetTaskProgress("COMPOUND_SHAPE");

        // Populate shapeSelectorComboBox with names/IDs from QuestionManager
        String[] compoundShapeIdentifiers = questionManager.getCompoundShapeIdentifiers();
        shapeSelectorComboBox.setModel(new DefaultComboBoxModel<>(compoundShapeIdentifiers));

        enableTaskInputs();
        nextQuestion();
    }

    @Override
    protected void nextQuestion() {
        if (questionManager.allCompoundShapesPracticedForTask(COMPOUND_SHAPES_TO_PRACTICE_COUNT)) {
            showFeedback("Amazing! You've practiced all compound shapes. Task complete!", true);
            disableTaskInputs();
            shapeImageLabel.setIcon(null);
            shapeImageLabel.setText("Task Complete!");
            solutionBreakdownArea.setText("");
            shapeSelectorComboBox.setSelectedItem(null);
            return;
        }

        currentCompoundShapeData = questionManager.getCurrentCompoundShapeData();
        if (currentCompoundShapeData == null) {
            showFeedback("No more compound shapes available.", true);
            disableTaskInputs();
            return;
        }

        // Update JComboBox to reflect the current shape from QuestionManager
        shapeSelectorComboBox.setSelectedItem(currentCompoundShapeData.getOrDefault("name", "Unknown Shape"));

        scoreManager.resetQuestionAttempts();
        ImageIcon icon = ImageLoader.loadImage((String) currentCompoundShapeData.get("image"));
        if (icon != null && icon.getIconWidth() > 0) {
            // Scale image if needed, similar to ShapeIdentificationPanel
            Image img = icon.getImage();
            int prefWidth = shapeImageLabel.getPreferredSize().width > 0 ? shapeImageLabel.getPreferredSize().width - 10
                    : 380;
            Image scaledImg = img.getScaledInstance(prefWidth, -1, Image.SCALE_SMOOTH); // Scale to width, maintain
                                                                                        // aspect ratio
            shapeImageLabel.setIcon(new ImageIcon(scaledImg));
        } else {
            shapeImageLabel.setIcon(ImageLoader.getPlaceholderIcon());
            shapeImageLabel.setText("Image not found: " + currentCompoundShapeData.get("image"));
        }
        correctAnswer = (double) currentCompoundShapeData.getOrDefault("area", 0.0);

        answerField.setText("");
        feedbackLabel.setText("Calculate the total area of the displayed shape.");
        solutionBreakdownArea.setText("Solution breakdown will appear after your attempt.");
        solutionBreakdownArea.setCaretPosition(0); // Scroll to top
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
        Timer timer = new Timer(4000, e -> { // Longer delay
            questionManager
                    .recordCompoundShapePracticed((String) currentCompoundShapeData.getOrDefault("id", "unknown"));
            questionsDoneThisTaskSession++;
            nextQuestion();
        });
        timer.setRepeats(false);
        timer.start();
    }

    @Override
    protected void checkAnswer() {
        if (currentCompoundShapeData == null)
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
            double userAnswer = Double.parseDouble(userAnswerText);
            scoreManager.recordAttempt();

            if (Math.abs(userAnswer - correctAnswer) < 0.015) { // Epsilon for float comparison
                awardPointsAndShowFeedback(true); // Advanced scoring for bonus
                showSolution();
                disableTaskInputs();
                questionManager
                        .recordCompoundShapePracticed((String) currentCompoundShapeData.getOrDefault("id", "unknown"));
                questionsDoneThisTaskSession++;

                Timer timer = new Timer(4000, e -> nextQuestion());
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
                    questionManager.recordCompoundShapePracticed(
                            (String) currentCompoundShapeData.getOrDefault("id", "unknown"));
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
        if (currentCompoundShapeData == null)
            return;
        feedbackLabel.setText(UIConstants.MSG_CORRECT_ANSWER_IS + df.format(correctAnswer));

        String solutionText = (String) currentCompoundShapeData.getOrDefault(
                "solution_breakdown", "Detailed solution steps are not available for this shape.");
        solutionBreakdownArea.setText(solutionText);
        solutionBreakdownArea.setCaretPosition(0); // Scroll to top
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