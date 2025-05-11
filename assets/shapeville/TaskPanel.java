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
    protected JPanel centralContentPanel; // Main area for task-specific content
    protected JLabel taskTitleLabel;

    // Constants for task completion
    protected static final int SHAPES_TO_IDENTIFY = 4;
    protected static final int ANGLE_TYPES_TO_IDENTIFY = 4;
    protected static final int BASIC_AREAS_TO_PRACTICE = 4; // All 4 types
    protected static final int CIRCLE_CALCS_TO_PRACTICE = 4; // All 4 combinations
    protected static final int COMPOUND_SHAPES_TO_PRACTICE = 9; // All 9 from Fig 10
    protected static final int SECTORS_TO_PRACTICE = 8; // All 8 from Fig 13

    public TaskPanel(ShapevilleApp app, ScoreManager scoreManager, QuestionManager questionManager, String taskTitle) {
        this.app = app;
        this.scoreManager = scoreManager;
        this.questionManager = questionManager;

        setLayout(new BorderLayout(15, 15)); // Gaps between components
        setBackground(UIConstants.PANEL_BACKGROUND_COLOR); // Slightly different from main app background
        setBorder
        feedbackLabel.setOpaque(true); // To make background color visible
        feedbackLabel.setBackground(this.getBackground()); // Match panel background initially
        feedbackLabel.setBorder(BorderFactory.createEmptyBorder(5,0,5,0));

        // Home Button
        homeButton = new JButton("Home");
        homeButton.setFont(UIConstants.BUTTON_FONT);
        homeButton.setBackground(UIConstants.BUTTON_COLOR);
        homeButton.setForeground(UIConstants.TEXT_COLOR_LIGHT);
        homeButton.setFocusPainted(false);
        homeButton.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        homeButton.addActionListener(e -> {
            questionsDoneThisTaskSession = 0; // Reset session counter when going home
            app.showPanel("HOME");
        });
        homeButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                homeButton.setBackground(UIConstants.BUTTON_HOVER_COLOR);
            }
            public void mouseExited(MouseEvent evt) {
                homeButton.setBackground(UIConstants.BUTTON_COLOR);
            }
        });


        // Bottom Control Panel (to hold feedback and home button)
        JPanel bottomControlsPanel = new JPanel(new BorderLayout(10, 0));
        bottomControlsPanel.setOpaque(false); // Transparent
        bottomControlsPanel.add(feedbackLabel, BorderLayout.CENTER);
        
        JPanel homeButtonContainer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0,0)); // Align home button right
        homeButtonContainer.setOpaque(false);
        homeButtonContainer.add(homeButton);
        bottomControlsPanel.add(homePanel.java`)
    (包名应为 `shapeville.panels`)

```java
package shapeville.panels;

import shapeville.ShapevilleApp;
import shapeville.ScoreManager;
import shapeville.QuestionManager;
import shapeville.UIConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public abstract class TaskPanel extends JPanel {
    protected ShapevilleApp app;
    protected ScoreManager scoreManager;
    protected QuestionManager questionManager;
    protected JLabel feedbackLabel;
    protected JButton homeButton;
    protected int questionsDoneThisTaskSession; // Tracks questions successfully completed in this task's current
                                                // showing

    // This constant might vary per task, so consider making it configurable or
    // overridden
    protected static final int DEFAULT_QUESTIONS_PER_TASK_SESSION = 4;

    public TaskPanel(ShapevilleApp app, ScoreManager scoreManager, QuestionManager questionManager) {
        this.app = app;
        this.scoreManager = scoreManager;
        this.questionManager = questionManager;
        setLayout(new BorderLayout(15, 15)); // Gaps between components
        setBackground(UIConstants.PANEL_BACKGROUND_COLOR); // Use a slightly different background for task panels
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Feedback Label
        feedbackLabel = new JLabel(" ", SwingConstants.CENTER); // Start with a space to maintain height
        feedbackLabel.setFont(UIConstants.FEEDBACK_FONT);
        feedbackLabel.setOpaque(true); // To show background color for feedback area
        feedbackLabel.setBackground(UIConstants.PANEL_BACKGROUND_COLOR.brighter());
        feedbackLabel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        // Home Button
        homeButton = createStyledButton("Home", null);
        homeButton.addActionListener(e -> {
            questionsDoneThisTaskSession = 0; // Reset for next time this panel is shown
            app.showPanel("HOME");
        });

        JPanel bottomControls = new JPanel(new BorderLayout(10,0));
        bottomControls.setOpaque(false); // Transparent
        bottomControls.add(feedbackLabel, BorderLayout.CENTER);
        
        JPanel homeButtonContainer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        homeButtonContainer.setOpaque(false);
        homeButtonContainer.add(homeButton);
        bottomControls.add(homeButtonContainer, BorderLayout.EAST);
        
        add(bottomControls, BorderLayout.SOUTH);
    }

    protected JButton createStyledButton(String text, String actionCommand) {
        JButton button = new JButton(text);
        button.setFont(UIConstants.BUTTON_FONT);
        button.setBackground(UIConstants.BUTTON_COLOR);
        button.setForeground(UIConstants.TEXT_COLOR_LIGHT);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIConstants.BUTTON_COLOR.darker(), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        if (actionCommand != null) {
            button.setActionCommand(actionCommand);
            // Action listener will be added by subclass or specific use case
        }
        
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                button.setBackground(UIConstants.BUTTON_HOVER_COLOR);
            (new EmptyBorder(20, 30, 20, 30)); // Padding

        // Title Panel
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setOpaque(false);
        taskTitleLabel = new JLabel(taskTitle, SwingConstants.CENTER);
        taskTitleLabel.setFont(UIConstants.SUBTITLE_FONT);
        taskTitleLabel.setForeground(UIConstants.TEXT_COLOR_DARK);
        titlePanel.add(taskTitleLabel);
        add(titlePanel, BorderLayout.NORTH);

        // Central Content Panel (to be filled by subclasses)
        centralContentPanel = new JPanel();
        centralContentPanel.setOpaque(false); // Subclasses can set their own layout
        add(centralContentPanel, BorderLayout.CENTER);

        // Bottom Controls Panel (Feedback and Home button)
        JPanel bottomControls = new JPanel(new BorderLayout(10, 0));
        bottomControls.setOpaque(false);
        bottomControls.setBorder(new EmptyBorder(10,0,0,0)); // Top padding for separation

        feedbackLabel = new JLabel(" ", SwingConstants.CENTER); // Start with a space for alignment
        feedbackLabel.setFont(UIConstants.FEEDBACK_FONT);
        feedbackLabel.setPreferredSize(new Dimension(400, 30)); // Give it some default size
        bottomControls.add(feedbackLabel, BorderLayout.CENTER);

        homeButton = createStyledButton("Return Home");
        homeButton.addActionListener(e -> {
            // Optionally, confirm if user wants to leave task if in progress
            app.showPanel("HOME");
        });
        
        JPanel homeButtonContainer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        homeButtonContainer.setOpaque(false);
        homeButtonContainer.add(homeButton);
        bottomControls.add(homeButtonContainer, BorderLayout.EAST);
        
        add(bottomControls, BorderLayout.SOUTH);
    }
    
    protected JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(UIConstants.ButtonContainer, BorderLayout.EAST);

        add(bottomControlsPanel, BorderLayout.SOUTH);
    }

    /**
     * Displays feedback to the user.
     * @param message The message to display.
     * @param isPositive true if the feedback is positive (e.g., correct answer), false for negative/neutral.
     */
    protected void showFeedback(String message, boolean isPositive) {
        feedbackLabel.setText("<html><div style='text-align: center;'>" + message + "</div></html>"); // Center text in JLabel
        if (isPositive) {
            feedbackLabel.setForeground(UIConstants.TEXT_COLOR_DARK); // Dark text for positive feedback
            feedbackLabel.setBackground(UIConstants.ACCENT_COLOR_2.brighter()); // Light green background
        } else {
            feedbackLabel.setForeground(UIConstants.TEXT_COLOR_DARK); // Dark text
            feedbackLabel.setBackground(UIConstants.ACCENT_COLOR_1.brighter()); // Light orange/yellow background
        }
        feedbackLabel.setOpaque(true); // Ensure background is painted

        // Optional: Use a Swing Timer to clear feedback after a few seconds
        Timer clearFeedbackTimer = new Timer(3000, e -> {
            feedbackLabel.setText(" ");
            feedbackLabel.setOpaque(false); // Make transparent again
            feedbackLabel.setBackground(this.getBackground()); // Reset background
            ((Timer)e.getSource()).stop();
        });
        clearFeedbackTimer.setRepeats(false);
        // clearFeedbackTimer.start(); // Uncomment to enable auto-clear
    }

    /**
     * Calculates points for the current question based on attempts, adds them to the score,
     * and displays a positive feedback message.
     * @param isAdvancedScoring true if advanced scoring rules apply.
     */
    protected void awardPointsAndShowFeedback(boolean isAdvancedScoring) {
        // Assumes scoreManager.recordAttempt() has been called before this.
        int pointsAwarded = scoreManager.awardCalculatedPoints(isAdvancedScoring);
        if (pointsAwarded > 0) {
            showFeedback(UIConstants.MSG_GREAT_JOB + " You earned " + pointsAwarded + " points!", true);
        } else {
            // This case might happen if an attempt was recorded but it was beyond the max attempts
            // or if a correct answer was given after max attempts (no points).
            showFeedback(UIConstants.MSG_GREAT_JOB + " Correct!", true); // Generic correct if no points
        }
        app.homeScreenPanel.updateProgress(); // Update progress bar on home screen
    }
    
    /**
     * Called when this task panel is shown.
     * Should reset the panel for a new task session (e.g., load first question).
     */
    public abstract void startTask();

    /**
     * Loads or sets up the next question/item in the task.
     */
    protected abstract void nextQuestion();

    /**
     * Checks the user's answer for the current question.
     */
    protected abstract void checkAnswer();

    /**
     * Displays the solution for the current question, typically after all attempts are used or time is up.
     */
    protected abstract void showSolution();

    /**
     * Utility method to disable common input components.
     * Subclasses should override to disable their specific input fields if necessary.
     */
    protected void disableTaskInputs() {
        // Example: if there's a common submit button, disable it here.
        // Often, specific panels will handle their own input disabling.
    }

    /**
     * Utility method to enable common input components.
     */
    protected void enableTaskInputs() {
        // Example: enable common submit button.
    }
}