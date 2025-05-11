package shapeville.panels;

import shapeville.ShapevilleApp;
import shapeville.ScoreManager;
import shapeville.QuestionManager;
import shapeville.UIConstants;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public abstract class TaskPanel extends JPanel {
    protected ShapevilleApp app;
    protected ScoreManager scoreManager;
    protected QuestionManager questionManager;

    protected JLabel feedbackLabel;
    protected JButton homeButton;
    protected JPanel centralContentPanel; // Main area for task-specific GUI elements
    protected JLabel taskTitleLabel;      // Label to display the title of the current task
   
    protected int questionsDoneThisTaskSession = 0; // Track questions done in this session
   
    // These constants define how many unique items need to be completed for each task type.
    // They guide the logic in QuestionManager's "all...PracticedForTask" methods.
    protected static final int SHAPES_TO_IDENTIFY_COUNT = 4;
    protected static final int ANGLE_TYPES_TO_IDENTIFY_COUNT = 4;
    protected static final int BASIC_AREAS_TO_PRACTICE_COUNT = 4; // All 4 distinct shapes
    protected static final int CIRCLE_CALCS_TO_PRACTICE_COUNT = 4; // All 4 Radius/Diameter & Area/Circumference combinations
    protected static final int COMPOUND_SHAPES_TO_PRACTICE_COUNT = 9; // All 9 from Fig 10
    protected static final int SECTORS_TO_PRACTICE_COUNT = 8; // All 8 from Fig 13

    public TaskPanel(ShapevilleApp app, ScoreManager scoreManager, QuestionManager questionManager, String taskTitle) {
        this.app = app;
        this.scoreManager = scoreManager;
        this.questionManager = questionManager;

        setLayout(new BorderLayout(15, 15)); // Gaps between main regions
        setBackground(UIConstants.PANEL_BACKGROUND_COLOR);
        setBorder(new EmptyBorder(20, 30, 20, 30)); // Padding around the entire task panel

        // --- Title Panel ---
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setOpaque(false);
        taskTitleLabel = new JLabel(taskTitle, SwingConstants.CENTER);
        taskTitleLabel.setFont(UIConstants.SUBTITLE_FONT); // Using SUBTITLE_FONT for task titles
        taskTitleLabel.setForeground(UIConstants.TEXT_COLOR_DARK);
        titlePanel.add(taskTitleLabel);
        add(titlePanel, BorderLayout.NORTH);

        // --- Central Content Panel (to be populated by subclasses) ---
        centralContentPanel = new JPanel(); // Subclasses will set layout and add components
        centralContentPanel.setOpaque(false); // Keep it transparent by default
        add(centralContentPanel, BorderLayout.CENTER);

        // --- Bottom Controls Panel (Feedback and Home Button) ---
        JPanel bottomControlsPanel = new JPanel(new BorderLayout(10, 0)); // Gap between feedback and home button area
        bottomControlsPanel.setOpaque(false);
        bottomControlsPanel.setBorder(new EmptyBorder(15, 0, 0, 0)); // Top padding for separation

        feedbackLabel = new JLabel(" ", SwingConstants.CENTER); // Start with a space for layout consistency
        feedbackLabel.setFont(UIConstants.FEEDBACK_FONT);
        feedbackLabel.setOpaque(true); // To make background visible for feedback
        feedbackLabel.setBackground(this.getBackground()); // Match panel background initially
        feedbackLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
                new EmptyBorder(8, 10, 8, 10) // Padding inside feedback label
        ));
        feedbackLabel.setPreferredSize(new Dimension(400, 40)); // Give it some default height
        bottomControlsPanel.add(feedbackLabel, BorderLayout.CENTER);

        homeButton = createStyledButton("Return Home"); // Using helper for styling
        homeButton.addActionListener(e -> {
            // Optional: Add a confirmation dialog if the task is in progress and not completed.
            // For now, directly goes home.
            app.showPanel("HOME");
        });
        
        JPanel homeButtonContainer = new JPanel(new FlowLayout(FlowLayout.RIGHT)); // Align home button to the right
        homeButtonContainer.setOpaque(false);
        homeButtonContainer.add(homeButton);
        bottomControlsPanel.add(homeButtonContainer, BorderLayout.EAST);
        
        add(bottomControlsPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Helper method to create styled JButtons consistently.
     * @param text The text for the button.
     * @return A styled JButton.
     */
    protected JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(UIConstants.BUTTON_FONT);
        button.setBackground(UIConstants.BUTTON_COLOR);
        button.setForeground(UIConstants.TEXT_COLOR_LIGHT);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIConstants.BUTTON_COLOR.darker(), 1),
                BorderFactory.createEmptyBorder(8, 18, 8, 18) // Padding inside button
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                button.setBackground(UIConstants.BUTTON_HOVER_COLOR);
            }
            public void mouseExited(MouseEvent evt) {
                button.setBackground(UIConstants.BUTTON_COLOR);
            }
        });
        return button;
    }

    /**
     * Displays feedback to the user with appropriate styling.
     * @param message The message to display.
     * @param isPositive true if positive feedback, false otherwise.
     */
    protected void showFeedback(String message, boolean isPositive) {
        feedbackLabel.setText("<html><div style='text-align: center;'>" + message + "</div></html>");
        if (isPositive) {
            feedbackLabel.setForeground(UIConstants.TEXT_COLOR_DARK);
            feedbackLabel.setBackground(UIConstants.ACCENT_COLOR_2.brighter().brighter()); // Very light green
        } else {
            feedbackLabel.setForeground(Color.BLACK); // Ensure contrast on lighter error background
            feedbackLabel.setBackground(UIConstants.ACCENT_COLOR_1.brighter().brighter()); // Very light orange/yellow
        }
        feedbackLabel.setOpaque(true); // Make background visible

        // Auto-clear feedback after a delay (optional)
        Timer clearFeedbackTimer = new Timer(3500, e -> {
            feedbackLabel.setText(" ");
            feedbackLabel.setOpaque(false);
            feedbackLabel.setBackground(this.getBackground()); // Reset to panel background
            ((Timer)e.getSource()).stop();
        });
        clearFeedbackTimer.setRepeats(false);
        // clearFeedbackTimer.start(); // Uncomment this line to enable auto-clearing feedback
    }

    /**
     * Handles awarding points and displaying feedback upon a correct answer.
     * Assumes scoreManager.recordAttempt() has been called prior.
     * @param isAdvancedScoring true if advanced scoring rules apply.
     */
    protected void awardPointsAndShowFeedback(boolean isAdvancedScoring) {
        int pointsAwarded = scoreManager.awardCalculatedPoints(isAdvancedScoring);
        if (pointsAwarded > 0) {
            showFeedback(UIConstants.MSG_GREAT_JOB + " You earned " + pointsAwarded + " points!", true);
        } else {
            // If correct but no points (e.g., after max attempts or specific game rule)
            showFeedback(UIConstants.MSG_GREAT_JOB + " Correct!", true);
        }
        if (app.getHomeScreenPanel() != null) { // Ensure homeScreenPanel is initialized
            app.getHomeScreenPanel().updateProgress(); // Update progress bar on the home screen
        }
    }
    
    /**
     * Abstract method to be called when this task panel becomes visible.
     * Subclasses must implement this to reset their state and load the first question/item.
     */
    public abstract void startTask();

    /**
     * Abstract method for subclasses to implement logic for loading/setting up the next question or item.
     */
    protected abstract void nextQuestion();

    /**
     * Abstract method for subclasses to implement answer checking logic.
     */
    protected abstract void checkAnswer();

    /**
     * Abstract method for subclasses to implement logic for displaying the solution.
     */
    protected abstract void showSolution();

    /**
     * Utility method to disable common input components. Subclasses should call this
     * and then disable their specific components.
     */
    protected void disableTaskInputs() {
        // Example: homeButton.setEnabled(false); // If needed during question
        // Subclasses will disable their specific input fields (JTextFields, JButtons, etc.)
    }

    /**
     * Utility method to enable common input components. Subclasses should call this
     * and then enable their specific components.
     */
    protected void enableTaskInputs() {
        // Example: homeButton.setEnabled(true);
    }
}