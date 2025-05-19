package shapeville.panels;

import shapeville.*; // Import all from base package for AngleType, etc.

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.List;

public class AngleIdentificationPanel extends TaskPanel {
    private JTextField angleInputField;
    private AngleDrawingPanel angleDrawingPanel;
    private JComboBox<AngleType> angleTypeSelector;
    private JButton displayAngleButton;
    private JButton submitTypeButton;

    private int currentDisplayedAngleValue;
    private AngleType correctTypeForDisplayedAngle;
    private AngleType targetTypeToIdentifyThisQuestion; // The type QM wants user to show/identify

    public AngleIdentificationPanel(ShapevilleApp app, ScoreManager scoreManager, QuestionManager questionManager) {
        super(app, scoreManager, questionManager, "Identify Angle Types");

        // Central Content Area
        centralContentPanel.setLayout(new BorderLayout(10, 10));

        // Top: Angle Input and Display Button
        JPanel inputArea = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        inputArea.setOpaque(false);
        inputArea.add(new JLabel("Enter Angle (0-360, multiples of 10):"));
        angleInputField = new JTextField(6);
        angleInputField.setFont(UIConstants.INPUT_FONT);
        angleInputField.setHorizontalAlignment(JTextField.CENTER);
        angleInputField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    displayAngleButton.doClick();
                }
            }
        });
        displayAngleButton = createStyledButton("Display Angle");
        displayAngleButton.addActionListener(e -> processAngleInputAndDisplay());

        inputArea.add(angleInputField);
        inputArea.add(displayAngleButton);
        centralContentPanel.add(inputArea, BorderLayout.NORTH);

        // Middle: Angle Drawing Panel
        angleDrawingPanel = new AngleDrawingPanel();
        // Wrap drawing panel in another panel to control its size and centering better
        JPanel drawingContainer = new JPanel(new GridBagLayout()); // Use GridBag to center
        drawingContainer.setOpaque(false);
        drawingContainer.add(angleDrawingPanel);
        centralContentPanel.add(drawingContainer, BorderLayout.CENTER);

        // Bottom: Type Selection and Submit Button
        JPanel selectionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        selectionPanel.setOpaque(false);
        selectionPanel.add(new JLabel("Select Angle Type:"));

        // Populate JComboBox with AngleType enum values
        // Filter out null if AngleType.fromAngleValue can return null for 0/360
        List<AngleType> displayableTypes = Arrays.asList(AngleType.values());
        angleTypeSelector = new JComboBox<>(displayableTypes.toArray(new AngleType[0]));
        angleTypeSelector.setFont(UIConstants.LABEL_FONT);
        angleTypeSelector.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                    boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof AngleType) {
                    setText(((AngleType) value).getDisplayName());
                }
                return this;
            }
        });

        submitTypeButton = createStyledButton("Submit Type");
        submitTypeButton.addActionListener(e -> checkAnswer());
        submitTypeButton.setEnabled(false); // Enabled after angle is displayed

        selectionPanel.add(angleTypeSelector);
        selectionPanel.add(submitTypeButton);
        centralContentPanel.add(selectionPanel, BorderLayout.SOUTH);
    }

    private void processAngleInputAndDisplay() {
        try {
            String inputText = angleInputField.getText().trim();
            if (inputText.isEmpty()) {
                showFeedback("Please enter an angle value.", false);
                submitTypeButton.setEnabled(false);
                return;
            }
            int angle = Integer.parseInt(inputText);
            if (angle < 0 || angle > 360 || angle % 10 != 0) {
                showFeedback("Invalid angle. Must be 0-360, and a multiple of 10.", false);
                angleDrawingPanel.setAngle(0); // Clear drawing
                submitTypeButton.setEnabled(false);
                return;
            }
            currentDisplayedAngleValue = angle;
            angleDrawingPanel.setAngle(currentDisplayedAngleValue);
            correctTypeForDisplayedAngle = AngleType.fromAngleValue(currentDisplayedAngleValue);

            if (correctTypeForDisplayedAngle == null && currentDisplayedAngleValue != 0
                    && currentDisplayedAngleValue != 360) {
                // This should not happen if AngleType.fromAngleValue is comprehensive for 1-359
                showFeedback("The entered angle (" + currentDisplayedAngleValue
                        + "°) is a boundary case not typically classified. Try another.", false);
                submitTypeButton.setEnabled(false);
            } else if (currentDisplayedAngleValue == 0 || currentDisplayedAngleValue == 360) {
                // Specific handling for 0 or 360 degrees as per problem spec (if they should be
                // classified)
                // For now, let's assume they are not part of the "4 types" to identify.
                // The spec focuses on acute, right, obtuse, reflex. Straight is also on fig 3.
                showFeedback("Displayed " + currentDisplayedAngleValue + "°. Now select its type if applicable.", true);
                feedbackLabel.setText("Select the type for the displayed angle (" + currentDisplayedAngleValue + "°).");
                submitTypeButton.setEnabled(true); // Allow submission even for 0/360 to test understanding
            } else {
                feedbackLabel.setText("Select the type for the displayed angle (" + currentDisplayedAngleValue + "°).");
                submitTypeButton.setEnabled(true);
            }
            scoreManager.resetQuestionAttempts(); // Reset attempts for this new angle identification
            angleTypeSelector.setSelectedIndex(-1); // Clear previous selection
            angleTypeSelector.requestFocusInWindow();

        } catch (NumberFormatException ex) {
            showFeedback(UIConstants.MSG_INVALID_INPUT + " Please enter a number.", false);
            angleDrawingPanel.setAngle(0);
            submitTypeButton.setEnabled(false);
        }
    }

    @Override
    public void startTask() {
        questionsDoneThisTaskSession = 0;
        scoreManager.resetQuestionAttempts();
        feedbackLabel.setText(" ");
        questionManager.resetTaskProgress("ANGLE_ID"); // Resets which types are targeted
        enableTaskInputs();
        nextQuestion(); // Sets up the initial state/prompt for the task.
    }

    @Override
    protected void nextQuestion() {
        // The task: "Process continues until the user has identified all 4 types of
        // angles OR clicks on the 'Home' button."
        // This means we need to track which of the target types have been correctly
        // identified.
        targetTypeToIdentifyThisQuestion = questionManager.getTargetAngleTypeToIdentify();

        if (targetTypeToIdentifyThisQuestion == null) { // All 4 target types identified
            showFeedback("Excellent! You've identified all " + ANGLE_TYPES_TO_IDENTIFY_COUNT
                    + " target angle types. Task complete!", true);
            disableTaskInputs();
            angleDrawingPanel.setAngle(0);
            angleDrawingPanel.repaint(); // Clear drawing
            return;
        }

        // For this task, user drives by inputting an angle.
        // The feedback could guide them towards identifying the
        // `targetTypeToIdentifyThisQuestion`
        // Or, the problem could simply be "identify the type of the angle you enter".
        // Let's stick to "user enters angle, then identifies its type".
        // The "identified all 4 types" means 4 *successful identifications*, and QM
        // ensures these are distinct types.

        angleInputField.setText("");
        angleDrawingPanel.setAngle(0); // Clear previous drawing
        angleDrawingPanel.repaint();
        feedbackLabel.setText("Enter an angle value (0-360, multiples of 10) and click 'Display Angle'.");
        submitTypeButton.setEnabled(false); // Disabled until an angle is displayed
        angleInputField.requestFocusInWindow();
        scoreManager.resetQuestionAttempts(); // For this new identification attempt
        angleTypeSelector.setSelectedIndex(-1);
    }

    @Override
    protected void checkAnswer() {
        AngleType selectedType = (AngleType) angleTypeSelector.getSelectedItem();
        if (selectedType == null) {
            showFeedback("Please select an angle type.", false);
            return;
        }

        // correctTypeForDisplayedAngle was set when the angle was displayed.
        // Handle 0/360 if they are special: if 0 is entered, is it "Acute"? Spec
        // doesn't explicitly say.
        // Figure 3: Acute >0 and <90.
        // Let's assume for 0 degrees or 360, it's not one of the core types unless
        // clarified.
        // However, if AngleType.fromAngleValue returns null for these, then
        // correctTypeForDisplayedAngle would be null.

        boolean isCorrect;
        if (correctTypeForDisplayedAngle == null) { // e.g. for 0 or 360 degrees, if not mapped to a type
            // How to score this? Maybe user should select nothing or a "None" option if
            // available.
            // For now, if correctType is null, any selection is "wrong" unless we define
            // 0/360.
            // If input was 0 or 360, and they are NOT classified, then no type should be
            // "correct".
            // This part needs specification clarity. Let's assume 0/360 are not the target
            // "types" to identify.
            isCorrect = false;
            if (currentDisplayedAngleValue == 0 || currentDisplayedAngleValue == 360) {
                showFeedback(
                        "0° or 360° are special angles, not typically classified as Acute, Obtuse etc. in this context.",
                        false);
                // Don't count attempt or points, let them try another angle.
                // Or, if spec implies they *should* know this, then it's an incorrect attempt.
                // For now, let's make it an incorrect attempt if they select something.
                scoreManager.recordAttempt();
            } else { // Should not happen if input validation is good
                scoreManager.recordAttempt();
            }
        } else {
            isCorrect = (selectedType == correctTypeForDisplayedAngle);
            scoreManager.recordAttempt();
        }

        if (isCorrect) {
        awardPointsAndShowFeedback(false); 
        questionManager.recordAngleTypeIdentified(correctTypeForDisplayedAngle); 
        questionsDoneThisTaskSession++; 
        // 启用输入组件
        enableTaskInputs();
        Timer timer = new Timer(1500, e -> nextQuestion());
        timer.setRepeats(false);
        timer.start();
    } else {
        if (scoreManager.canAttempt()) {
            showFeedback(UIConstants.MSG_TRY_AGAIN + " ("
                    + (ScoreManager.MAX_ATTEMPTS_PER_QUESTION - scoreManager.getCurrentQuestionAttempts())
                    + " attempts left)", false);
            // 启用输入组件
            enableTaskInputs();
            angleTypeSelector.requestFocusInWindow();
        } else {
            showSolution(); 
            disableTaskInputs();
            Timer timer = new Timer(3000, e -> nextQuestion());
            timer.setRepeats(false);
            timer.start();
        }
    }
}

    @Override
    protected void showSolution() {
        String solutionText;
        if (correctTypeForDisplayedAngle != null) {
            solutionText = UIConstants.MSG_ALL_ATTEMPTS_USED + UIConstants.MSG_CORRECT_ANSWER_IS +
                    correctTypeForDisplayedAngle.getDisplayName() + " for " + currentDisplayedAngleValue + "°.";
        } else {
            if (currentDisplayedAngleValue == 0 || currentDisplayedAngleValue == 360) {
                solutionText = "Angles like 0° or 360° are boundary cases and not typically classified as one of the listed types (Acute, Obtuse, etc.).";
            } else {
                solutionText = UIConstants.MSG_ALL_ATTEMPTS_USED + "The displayed angle was problematic to classify.";
            }
        }
        showFeedback(solutionText, false);
    }

    @Override
    protected void disableTaskInputs() {
        angleInputField.setEnabled(false);
        displayAngleButton.setEnabled(false);
        angleTypeSelector.setEnabled(false);
        submitTypeButton.setEnabled(false);
    }

    @Override
    protected void enableTaskInputs() {
        angleInputField.setEnabled(true);
        displayAngleButton.setEnabled(true);
        angleTypeSelector.setEnabled(true);
        // submitTypeButton is enabled only after an angle is displayed.
    }
}