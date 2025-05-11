package shapeville.panels;

import shapeville.ShapevilleApp;
import shapeville.ScoreManager;
import shapeville.QuestionManager;
import shapeville.UIConstants;
import shapeville.ImageLoader;
import shapeville.ShapeData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ShapeIdentificationPanel extends TaskPanel {
    private JLabel shapeImageLabel;
    private JTextField answerField;
    private JButton submitButton;
    private ShapeData currentShapeData;
    private boolean is3DMode; // Distinguishes between 2D and 3D sub-tasks

    public ShapeIdentificationPanel(ShapevilleApp app, ScoreManager scoreManager, QuestionManager questionManager,
            boolean is3DMode) {
        super(app, scoreManager, questionManager, is3DMode ? "Identify 3D Shapes" : "Identify 2D Shapes");
        this.is3DMode = is3DMode;

        // Central content: Image and Answer Field
        centralContentPanel.setLayout(new BorderLayout(10, 15)); // Gap between image and input

        shapeImageLabel = new JLabel();
        shapeImageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        shapeImageLabel.setVerticalAlignment(SwingConstants.CENTER);
        shapeImageLabel.setPreferredSize(new Dimension(350, 350)); // Adjust as needed
        shapeImageLabel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        centralContentPanel.add(shapeImageLabel, BorderLayout.CENTER);

        JPanel answerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        answerPanel.setOpaque(false);

        JLabel nameLabel = new JLabel("Shape Name:");
        nameLabel.setFont(UIConstants.LABEL_FONT);
        answerPanel.add(nameLabel);

        answerField = new JTextField(20);
        answerField.setFont(UIConstants.INPUT_FONT);
        answerField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    submitButton.doClick();
                }
            }
        });
        answerPanel.add(answerField);

        submitButton = createStyledButton("Submit");
        submitButton.addActionListener(e -> checkAnswer());
        answerPanel.add(submitButton);

        centralContentPanel.add(answerPanel, BorderLayout.SOUTH);
    }

    @Override
    public void startTask() {
        questionsDoneThisTaskSession = 0;
        scoreManager.resetQuestionAttempts(); // Reset attempts for the first question of this task
        feedbackLabel.setText(" "); // Clear previous feedback
        questionManager.resetTaskProgress(is3DMode ? "SHAPE_ID_3D" : "SHAPE_ID_2D"); // Reset QM progress for this task
                                                                                     // type
        enableTaskInputs();
        nextQuestion();
    }

    @Override
    protected void nextQuestion() {
        // Check if the required number of unique shapes for this session has been
        // identified
        if (questionManager.allShapesIdentifiedForTask(is3DMode, SHAPES_TO_IDENTIFY)) {
            showFeedback("Great! You've identified " + SHAPES_TO_IDENTIFY + " shapes. Task complete!", true);
            disableTaskInputs();
            shapeImageLabel.setIcon(null); // Clear image
            shapeImageLabel.setText("Task Complete!");
            return;
        }

        currentShapeData = questionManager.getCurrentShape(is3DMode);

        if (currentShapeData != null) {
            ImageIcon icon = ImageLoader.loadImage(currentShapeData.getImageName());
            if (icon != null && icon.getIconWidth() > 0) { // Check if image loaded successfully
                // Scale image to fit if it's too large for the label, maintaining aspect ratio
                int maxWidth = shapeImageLabel.getPreferredSize().width - 20; // Account for border/padding
                int maxHeight = shapeImageLabel.getPreferredSize().height - 20;
                if (icon.getIconWidth() > maxWidth || icon.getIconHeight() > maxHeight) {
                    Image img = icon.getImage();
                    double widthRatio = (double) maxWidth / icon.getIconWidth();
                    double heightRatio = (double) maxHeight / icon.getIconHeight();
                    double ratio = Math.min(widthRatio, heightRatio);
                    int newWidth = (int) (icon.getIconWidth() * ratio);
                    int newHeight = (int) (icon.getIconHeight() * ratio);
                    shapeImageLabel
                            .setIcon(new ImageIcon(img.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH)));
                } else {
                    shapeImageLabel.setIcon(icon);
                }
            } else {
                shapeImageLabel.setIcon(ImageLoader.getPlaceholderIcon()); // Show placeholder if image fails
                shapeImageLabel.setText("Image not found: " + currentShapeData.getImageName());
            }
            shapeImageLabel.setText(null); // Clear any previous text like "Task Complete!"
            answerField.setText("");
            feedbackLabel.setText("What shape is this?");
            scoreManager.resetQuestionAttempts(); // Reset attempts for this new shape
            enableTaskInputs();
            answerField.requestFocusInWindow();
        } else {
            // This case should ideally be handled by allShapesIdentifiedForTask
            showFeedback("No more shapes available for this task.", true);
            disableTaskInputs();
        }
    }

    @Override
    protected void checkAnswer() {
        if (currentShapeData == null)
            return; // Should not happen

        String userAnswer = answerField.getText().trim().toLowerCase();
        if (userAnswer.isEmpty()) {
            showFeedback("Please enter a name for the shape.", false);
            return;
        }

        scoreManager.recordAttempt();
        // Allow for common synonyms, e.g., "cuboid" vs "rectangular prism"
        // This logic can be expanded in QuestionManager or by adding a synonyms list to
        // ShapeData
        boolean isCorrect = userAnswer.equals(currentShapeData.getName());
        if (!isCorrect && currentShapeData.getName().equals("cuboid") && userAnswer.equals("rectangular prism"))
            isCorrect = true;
        if (!isCorrect && currentShapeData.getName().equals("square-based pyramid")
                && userAnswer.equals("square pyramid"))
            isCorrect = true;
        // Add more synonyms as needed

        if (isCorrect) {
            awardPointsAndShowFeedback(is3DMode); // 3D shapes might have advanced scoring
            questionManager.recordShapeIdentified(currentShapeData.getId(), is3DMode); // Record this shape as
                                                                                       // identified
            questionsDoneThisTaskSession++; // Increment session counter
            disableTaskInputs(); // Disable input while showing feedback/ transitioning

            Timer timer = new Timer(1500, e -> nextQuestion()); // Delay before next question
            timer.setRepeats(false);
            timer.start();
        } else {
            if (scoreManager.canAttempt()) {
                showFeedback(UIConstants.MSG_TRY_AGAIN + " ("
                        + (ScoreManager.MAX_ATTEMPTS_PER_QUESTION - scoreManager.getCurrentQuestionAttempts())
                        + " attempts left)", false);
                answerField.selectAll();
                answerField.requestFocusInWindow();
            } else {
                showSolution();
                questionManager.recordShapeIdentified(currentShapeData.getId(), is3DMode); // Still "counts" as seen
                                                                                           // even if not guessed
                questionsDoneThisTaskSession++;
                disableTaskInputs();

                Timer timer = new Timer(3000, e -> nextQuestion()); // Longer delay to see solution
                timer.setRepeats(false);
                timer.start();
            }
        }
    }

    @Override
    protected void showSolution() {
        if (currentShapeData == null)
            return;
        showFeedback(UIConstants.MSG_ALL_ATTEMPTS_USED + UIConstants.MSG_CORRECT_ANSWER_IS +
                currentShapeData.getName().substring(0, 1).toUpperCase() + currentShapeData.getName().substring(1)
                + ".", false);
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
