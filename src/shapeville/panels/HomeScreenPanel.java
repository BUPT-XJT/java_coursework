package shapeville.panels;

import shapeville.ShapevilleApp;
import shapeville.ScoreManager;
import shapeville.UIConstants;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class HomeScreenPanel extends JPanel {
    private ShapevilleApp app;
    private ScoreManager scoreManager;
    private JProgressBar progressBar;
    private JLabel scoreDisplayLabel; // For text like "Score: X"

    // Estimate a max possible score for 100% progress. This needs to be calculated
    // based on the number of questions and points per task.
    // Example rough calculation:
    // KS1: ShapeID2D(4q*3pts=12) + ShapeID3D(4q*6pts=24) + AngleID(4q*3pts=12) = 48
    // KS2: AreaCalc(4q*3pts=12) + CircleCalc(4q*3pts=12) = 24
    // Bonus: Compound(9q*6pts=54) + Sector(8q*6pts=48) = 102
    // Total ~ 48 + 24 + 102 = 174. Let's use 180 or 200.
    private static final int MAX_ESTIMATED_SCORE_FOR_PROGRESS = 180;

    public HomeScreenPanel(ShapevilleApp app, ScoreManager scoreManager) {
        this.app = app;
        this.scoreManager = scoreManager;
        setLayout(new BorderLayout(20, 20)); // Gaps between regions
        setBackground(UIConstants.BACKGROUND_COLOR);
        setBorder(new EmptyBorder(30, 40, 30, 40)); // Padding around the panel

        // --- Title Panel ---
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setOpaque(false);
        JLabel titleLabel = new JLabel("Welcome to Shapeville!", SwingConstants.CENTER);
        titleLabel.setFont(UIConstants.TITLE_FONT);
        titleLabel.setForeground(UIConstants.TEXT_COLOR_DARK);
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);

        // --- Main Button Panel (using GridBagLayout for flexible arrangement) ---
        JPanel mainButtonPanel = new JPanel(new GridBagLayout());
        mainButtonPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL; // Make buttons expand horizontally
        gbc.insets = new Insets(8, 15, 8, 15);   // Padding around buttons
        gbc.ipadx = 20; // Internal padding for button width
        gbc.ipady = 12; // Internal padding for button height
        gbc.weightx = 1.0; // Distribute extra horizontal space

        // Helper method to add section titles and buttons
        int gridY = 0;

        // Key Stage 1 Tasks
        addSectionTitle(mainButtonPanel, gbc, "Key Stage 1 Tasks", gridY++);
        gbc.gridwidth = 1; // Reset for single buttons
        gbc.gridy = gridY;
        gbc.gridx = 0;
        mainButtonPanel.add(createStyledButton("Identify 2D Shapes", "SHAPE_ID_2D"), gbc);
        gbc.gridx = 1;
        mainButtonPanel.add(createStyledButton("Identify 3D Shapes", "SHAPE_ID_3D"), gbc);
        gridY++;
        gbc.gridy = gridY;
        gbc.gridx = 0;
        gbc.gridwidth = 2; // Span two columns
        mainButtonPanel.add(createStyledButton("Identify Angle Types", "ANGLE_ID"), gbc);
        gridY++;

        // Key Stage 2 Tasks
        addSectionTitle(mainButtonPanel, gbc, "Key Stage 2 Tasks", gridY++);
        gbc.gridwidth = 1; // Reset
        gbc.gridy = gridY;
        gbc.gridx = 0;
        mainButtonPanel.add(createStyledButton("Area - Basic Shapes", "AREA_CALC"), gbc);
        gbc.gridx = 1;
        mainButtonPanel.add(createStyledButton("Circle Calculations", "CIRCLE_CALC"), gbc);
        gridY++;

        // Bonus Challenges
        addSectionTitle(mainButtonPanel, gbc, "Bonus Challenges", gridY++);
        gbc.gridwidth = 1; // Reset
        gbc.gridy = gridY;
        gbc.gridx = 0;
        mainButtonPanel.add(createStyledButton("Compound Shapes Area", "COMPOUND_SHAPE"), gbc);
        gbc.gridx = 1;
        mainButtonPanel.add(createStyledButton("Sector Area", "SECTOR_AREA"), gbc);
        
        // Add a panel to center the mainButtonPanel if it doesn't fill the space
        JPanel centerAlignPanel = new JPanel(new GridBagLayout());
        centerAlignPanel.setOpaque(false);
        centerAlignPanel.add(mainButtonPanel);
        add(centerAlignPanel, BorderLayout.CENTER);


        // --- Bottom Panel (Progress Bar and End Session Button) ---
        JPanel bottomPanel = new JPanel(new BorderLayout(20, 0)); // Gap between progress and button
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(new EmptyBorder(20, 0, 0, 0)); // Top padding

        // Progress Area
        JPanel progressArea = new JPanel(new BorderLayout(10,0));
        progressArea.setOpaque(false);
        scoreDisplayLabel = new JLabel("Score: 0");
        scoreDisplayLabel.setFont(UIConstants.LABEL_FONT);
        scoreDisplayLabel.setForeground(UIConstants.TEXT_COLOR_DARK);
        progressBar = new JProgressBar(0, MAX_ESTIMATED_SCORE_FOR_PROGRESS);
        progressBar.setValue(0);
        progressBar.setPreferredSize(new Dimension(100, 25)); // Width is flexible, height fixed
        progressBar.setForeground(UIConstants.ACCENT_COLOR_2);
        progressBar.setStringPainted(true); // Show percentage or custom string
        progressBar.setFont(UIConstants.LABEL_FONT.deriveFont(Font.BOLD, 12f));


        progressArea.add(scoreDisplayLabel, BorderLayout.WEST);
        progressArea.add(progressBar, BorderLayout.CENTER);
        bottomPanel.add(progressArea, BorderLayout.CENTER);

        // End Session Button
        JButton endButton = createStyledButton("End Session", null); // No action command needed if handled directly
        endButton.addActionListener(e -> app.endSession());
        JPanel endButtonContainer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        endButtonContainer.setOpaque(false);
        endButtonContainer.add(endButton);
        bottomPanel.add(endButtonContainer, BorderLayout.EAST);

        add(bottomPanel, BorderLayout.SOUTH);
        updateProgress(); // Initial call
    }

    private void addSectionTitle(JPanel panel, GridBagConstraints gbc, String title, int gridY) {
        JLabel label = new JLabel(title);
        label.setFont(UIConstants.SUBTITLE_FONT);
        label.setForeground(UIConstants.TEXT_COLOR_DARK);
        gbc.gridx = 0;
        gbc.gridy = gridY;
        gbc.gridwidth = 2; // Span two columns
        gbc.fill = GridBagConstraints.NONE; // Don't make title expand
        gbc.anchor = GridBagConstraints.CENTER; // Center title
        gbc.insets = new Insets(15, 5, 5, 5); // Different insets for titles
        panel.add(label, gbc);
        gbc.gridwidth = 1; // Reset for next buttons
        gbc.fill = GridBagConstraints.HORIZONTAL; // Buttons should expand
        gbc.anchor = GridBagConstraints.CENTER; // Reset anchor
        gbc.insets = new Insets(8, 15, 8, 15); // Reset insets for buttons
    }

    private JButton createStyledButton(String text, String actionCommand) {
        JButton button = new JButton(text);
        button.setFont(UIConstants.BUTTON_FONT);
        button.setBackground(UIConstants.BUTTON_COLOR);
        button.setForeground(UIConstants.TEXT_COLOR_LIGHT);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIConstants.BUTTON_COLOR.darker(), 1), // Subtle border
                BorderFactory.createEmptyBorder(10, 20, 10, 20)    // Padding inside button
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        if (actionCommand != null) {
            button.setActionCommand(actionCommand);
            button.addActionListener(e -> app.showPanel(e.getActionCommand()));
        }

        // Hover effect
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

    public void updateProgress() {
        if (scoreManager == null || progressBar == null || scoreDisplayLabel == null) {
            return; // Avoid NullPointerException during setup
        }
        int currentScore = scoreManager.getTotalScore();
        scoreDisplayLabel.setText("Score: " + currentScore);
        progressBar.setValue(Math.min(currentScore, MAX_ESTIMATED_SCORE_FOR_PROGRESS));

        // Update progress bar string to show score / max_score
        if (MAX_ESTIMATED_SCORE_FOR_PROGRESS > 0) {
            int percentage = (currentScore * 100) / MAX_ESTIMATED_SCORE_FOR_PROGRESS;
            progressBar.setString(Math.min(100,percentage) + "%");
        } else {
            progressBar.setString("0%");
        }
        if (currentScore >= MAX_ESTIMATED_SCORE_FOR_PROGRESS) {
            progressBar.setString("Completed!"); // Or "100%"
        }
    }
}