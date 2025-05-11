package shapeville.panels;

import shapeville.ShapevilleApp;
import shapeville.ScoreManager;
import shapeville.UIConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class HomeScreenPanel extends JPanel {
    private ShapevilleApp app;
    private ScoreManager scoreManager;
    private JProgressBar progressBar;
    private JLabel scoreLabel; // To display score next to progress bar

    // Define a max possible score for progress calculation. This is an estimate.
    // You might need to adjust this based on how many questions and their point
    // values.
    好的// Example: 2D(4*3) + 3D(4*6) + Angles(4*3，这是第三组 Java 文件的完整代码。这一组包含了主页屏幕) +
      // Area(4*3) + Circle(4*3) = 12+24+12和所有任务面板的抽象基类。

    ---

    ###
    第三组 Java

    文件代码 (完整无遗漏)

**+12+12 = 72 (core)
    // Bonus: Compound(9*6) + Sector9. `HomeScreenPanel.java`**
   (路径: `src/shapeville/panels/HomeScreenPanel.java`)
   (包名应为 `shapeville.panels`)

```java
package shapeville.(8*6) = 54+48 = 102
    // Total ~ 174panels;

    import shapeville.ShapevilleApp;
    import shapeville.ScoreManager;
    import shapeville.UIConstants;

    private static final int MAX_ESTIMATED_SCORE

    import javax.swing.*;
    import java.awt.*;
    import java.awt.event.MouseAdapter;import=180;

    public HomeScreenPanel(ShapevilleApp app, ScoreManager scoreManager) {
        this. java.awt.event.MouseEvent;

public class HomeScreenPanel extends JPanel {
    private ShapevilleApp app;app = app;
        this.scoreManager = scoreManager;
        setLayout(new BorderLayout(20, 2
    private ScoreManager scoreManager;
    private JProgressBar progressBar;
    // Estimate a max possible score for0)); // Add gaps
        setBackground(UIConstants.BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmpty好的，这是第三Border(30, 30, 30, 30)); // Add padding

        // Title Panel progress bar display. This needs refinement.
    private static final int ESTIMATED_MAX_SCORE = 15组 Java 文件的完整代码。这组包含了主页屏幕和所有任务面板
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setO的抽象基类。

---

### 第三组 Java 文件代码 (完整无遗漏)

**9. `HomeScreen0;


    public HomeScreenPanel(ShapevilleApp app, ScoreManager scoreManager) {
        this.app = app;Panel.java`**
   (路径: `src/shapeville/panels/HomeScreenPanel.java`)

paque(false); // Transparent background
        JLabel titleLabel = new JLabel("Welcome to Shapeville!", SwingConstants.CENTER
        this.scoreManager = scoreManager;
        setLayout(new BorderLayout(20, 20)); //```java
package shapeville.panels;

    import shapeville.ShapevilleApp;
import shapeville.Score);titleLabel.setFont(UIConstants.TITLE_FONT);titleLabel.setForeground(UIConstants Add
    some gaps

    setBackground(UIConstants.BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(3Manager;
    import shapeville.UIConstants;

    import javax.swing.*;
import javax.swing.border.EmptyBorder;.TEXT_COLOR_DARK);titlePanel.add(titleLabel);add(titlePanel,0,30,30,30)); // More
                                                                                                                // padding

    // Title Panel
    JPanel title
    import java.awt.*;
    import java.awt.event.MouseAdapter;
    import java.awt.event.MouseEvent BorderLayout.NORTH);

    // Button Grid Panel
    JPanel buttonGridPanel = new JPanel(new GridLayout(0, 2Panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setOpaque(false); // Transparent background
    JLabel titleLabel = new JLabel("Welcome to Shapeville!", SwingConstants.CENTER);title;

    public class HomeScreenPanel extends JPanel {
        private ShapevilleApp app;
        private ScoreManager scoreManager;
        private JProgressBar progressBar;
        private JLabel scoreLabel; // To display score text next to progress bar

        // , 20, 20)); // 2 columns, variable rows, 20px gaps
        buttonGridPanel.setOpaque(false);

        // Add buttons for each task
        // Key Stage 1
        buttonGridPanel.add(createStyledButton("Identify 2D Shapes (KS1)", "SHAPE_Label.setFont(UIConstants.TITLE_FONT);
        titleLabel.setForeground(UIConstants.TEXT_COLOR_DARK);
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);

        // Button Grid Panel
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel Estimated total points for 100%

        progress (adjust as per your task points)
    // KS1: 2 tasks * 4 questions * 3 points/q (basic) = 24 (Shape ID 2D, AngleID_2D"));
        buttonGridPanel.add(createStyledButton("Identify 3D Shapes (KS1)", "SHAPE_ID_3D"));
        buttonGridPanel.add(createStyledButton("Identify Angle Types (KS1)", "ANGLE_ID"));
        // Key Stage 2
        buttonGridPanel.add(createStyledButton("Area - Basic Shapes (KS2)", "AREA_CALC"));
        buttonGrid.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10); // Spacing between buttons
        gbc.ipadx = 20; // Internal padding to make buttons wider
        gbc.ipady = 10; // ID)
    //      + 1 task * 4 questions * 6 points/q (advanced for 3D shapes) = 24
    // KS2: 2 tasks * 4 questions * 3 points/q (basic) = 24 (Area, Circle)
    // Bonus: ~9 questions * 6 points/qPanel.add(createStyledButton("Circle Calculations (KS2)", "CIRCLE_CALC"));
        // Bonus Tasks (can be visually separated or styled differently if desired)
        buttonGridPanel.add(createStyledButton("Compound Internal padding to make buttons taller

        // Key Stage 1 Tasks
        JLabel ks1Label = new JLabel("Key Stage 1 Tasks");
        ks1Label.setFont(UIConstants.SUBTITLE_FONT);
        ks1Label.setForeground(UIConstants.TEXT_COLOR_DARK);
        gbc.gridx = 0; gbc. + ~8 questions * 6 points/q = 54 + 48 = 102
    // Total ~ 24+24+24+102 = 174. Let's set a rough max for progress.
        private static final int MAX_ESTIMATED_SCORE_FOR_PROGRESS =

        Shapes Area (Bonus)", "COMPOUND_SHAPE"));
        buttonGridPanel.add(createStyledButton("Sector Area (Bonus)", "SECTOR_AREA"));
        
        // Center the button grid panel using another panel with GridBagLayout for better centering
        JPanel buttonContainerPanel = new JPanel(new GridBagLayout());
        buttonContainerPanel.setOque(false);
        buttonContainerPanel.add(buttonGridPanel);
        add(buttonContainerPanel, BorderLayout.CENTER);


        // Bottom Panel for Progress and End Button
        JPanel bottomPanel = new JPanelgridy = 0; gbc.gridwidth = 2;
        buttonPanel.add(ks1Label, gbc);
        gbc.gridwidth = 1; // Reset gridwidth

        gbc.gridx = 0; gbc.gridy = 1;
        buttonPanel.add(createStyledButton("Identify 2D Shapes", "SHAPE_ID_2D"), gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        buttonPanel.add(createStyledButton("Identify 3D Shapes", "SHAPE_ID_3D"), gbc);
        gbc. 150;

        public HomeScreenPanel(ShapevilleApp app, ScoreManager scoreManager) {
            this.app=app;this.scoreManager=scoreManager;setLayout(new BorderLayout(20,20)); // Add gaps between regions
            setBackground(UIConstants.BACKGROUND_COLOR);setBorder(new EmptyBorder(30,40,30,40)); // Add padding around
                                                                                                 // the panel

            // Title Panel
            JPanel titlePanel=new JPanel(new FlowLayout(FlowLayout.CENTER));titlePanel.setOpaque(false); // Transparent
                                                                                                         // background
            JLabel titleLabel=new JLabel("Welcome to Shapeville!",SwingConstants.CENTER(new BorderLayout(10,5));bottomPanel.setOpaque(false);bottomPanel.setBorder(BorderFactory.createEmptyBorder(10,0,0,0)); // Top
                                                                                                                                                                                                               // padding

            scoreLabel=new JLabel("Score: 0");scoreLabel.setFont(UIConstants.LABEL_FONT);scoreLabel.setForeground(UIConstants.TEXT_COLOR_DARK);

            progressBar=new JProgressBar(0,MAX_ESTIMATED_SCORE);progressBar.setStringPainted(false); // We'll use a
                                                                                                     // separate label
                                                                                                     // for score text
            progressBar.setValue(0);progressBar.setPreferredSize(new Dimension(progressBar.getgridx=0;gbc.gridy=2;gbc.gridwidth=2; // Span
                                                                                                                                   // two
                                                                                                                                   // columns
            buttonPanel.add(createStyledButton("Identify Angle Types","ANGLE_ID"),gbc);gbc.gridwidth=1; // Reset
                                                                                                        // gridwidth

            // Key Stage 2 Tasks
            JLabel ks2Label=new JLabel("Key Stage 2 Tasks");ks2Label.setFont(UIConstants.SUBTITLE_FONT);ks2Label.setForeground(UIConstants.TEXT_COLOR_DARK);titleLabel.setFont(UIConstants.TITLE_FONT);titleLabel.setForeground(UIConstants.TEXT_COLOR_DARK);titlePanel.add(titleLabel);add(titlePanel,BorderLayout.NORTH);

            // Button Grid Panel for Tasks
            JPanel buttonGridPanel=new JPanel(new GridLayout(0,2,20,20)); // 0 rows = dynamic, 2 columns, gaps
            buttonGridPanel.setOpaque(false);buttonGridPanel.setBorder(new EmptyBorder(20,0,20,0)); // Padding
                                                                                                    // top/bottom

            // Add buttons for each task using a helper method for consistent styling
            addButton(buttonGridPanel,"Identify 2D Shapes (KS1)","SHAPE_ID_2D");addButton(buttonGridPanel,"Identify 3D Shapes (KS1)","SHAPE_ID_PreferredSize().width, 25)); // Make progress bar thickerprogressBar.setForeground(UIConstants.ACCENT_COLOR_2); // Progress
                                                                                                                                                                                                                                                               // color

            JPanel progressContainer=new JPanel(new BorderLayout(5,0));progressContainer.setOpaque(false);progressContainer.add(scoreLabel,BorderLayout.WEST);progressContainer.add(progressBar,BorderLayout.CENTER);

            bottomPanel.add(progressContainer,BorderLayout.CENTER);

            JButton endButton=createStyledButton("End Session","END_SESSION_ACTION"); // Use a unique action command);
            gbc.gridx=0;gbc.gridy=3;gbc.gridwidth=2;buttonPanel.add(ks2Label,gbc);gbc.gridwidth=1;

            gbc.gridx=0;gbc.gridy=4;buttonPanel.add(createStyledButton("Area - Basic Shapes","AREA_CALC"),gbc);gbc.gridx=1;gbc.gridy=4;buttonPanel.add(createStyledButton("Circle Calculations","CIRCLE_CALC"),gbc);

            // Bonus Tasks
            JLabel bonusLabel=new JLabel("Bonus Challenges");bonusLabel.setFont(UIConstants.SUBTITLE_FONT);bonusLabel.setForeground(UIConstants.TEXT_COLOR_DARK);gbc.gridx=0;gbc.gridy=5;gbc.gridwidth=23D");addButton(buttonGridPanel,"Identify Angle Types (KS1)","ANGLE_ID");addButton(buttonGridPanel,"Area Calculation - Basic (KS2)","AREA_CALC");addButton(buttonGridPanel,"Circle Calculations (KS2)","CIRCLE_CALC");addButton(buttonGridPanel,"Compound Shapes Area (Bonus)","COMPOUND_SHAPE");addButton(buttonGridPanel endButton.setPreferredSize(new Dimension(150,40)); // Fixed
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     // size
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     // for
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     // end
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     // button
            endButton.addActionListener(e->app.endSession()); // Direct call

            JPanel endButtonPanel=new JPanel(new FlowLayout(FlowLayout.RIGHT)); // Align end button to the right
            endButtonPanel.setOpaque(false);endButtonPanel.add(endButton);bottomPanel.add(endButtonPanel,BorderLayout.EAST);

            add(bottomPanel,BorderLayout.SOUTH);updateProgress(); // Initial update
        }

        private JButton createStyledButton(String text, String actionCommand) {
            JButton button=new JButton(text);button.setFont(UIConstants.BUTTON_FONT);button.setBackground(UIConstants.BUTTON_COLOR);button.setForeground(UIConstants.TEXT_COLOR_LIGHT);button.setFocusPainted(false); // Remove
                                                                                                                                                                                                                      // focus
                                                                                                                                                                                                                      // border
            button.setBorder(BorderFactory.createEmptyBorder(10,15,10,15)); // Padding inside button
            button.setActionCommand(actionCommand);button.addActionListener(e->app.showPanel(e.getActionCommand()));

            // Add hover;
            buttonPanel.add(bonusLabel,gbc);gbc.gridwidth=1;

            gbc.gridx=0;gbc.gridy=6;buttonPanel.add(createStyledButton("Compound Shapes Area","COMPOUND_SHAPE"),gbc);gbc.gridx=1;gbc.gridy=6;buttonPanel.add(createStyledButton("Sector Area","SECTOR_AREA"),gbc);

            add(buttonPanel,BorderLayout.CENTER);

            // Bottom Panel for Progress and End Button
            JPanel bottomPanel=new JPanel(new BorderLayout(10,5));bottomPanel.setOpaque(false);bottomPanel.setBorder(BorderFactory.createEmptyBorder(10,0,0,0));

            progressBar=new,"Sector Area (Bonus)","SECTOR_AREA");

            // Center the button grid panel using another panel with FlowLayout
            JPanel centerButtonFlowPanel=new JPanel(new FlowLayout(FlowLayout.CENTER));centerButtonFlowPanel.setOpaque(false);centerButtonFlowPanel.add(buttonGridPanel);add(centerButtonFlowPanel,BorderLayout.CENTER);

            // Bottom Panel for Progress and End Session
            JPanel bottomPanel=new JPanel(new BorderLayout(10,5)); // Gap between progress and button
            bottomPanel.setOpaque(false);bottomPanel.setBorder(new EmptyBorder(10,0,10,0));

            scoreLabel=new JLabel("Score: 0",SwingConstants.LEFT);scoreLabel.setFont(UIConstants.LABEL_FONT);scoreLabel.setForeground(UIConstants.TEXT_COLOR_DARK);

            progressBar=new JProgressBar(0,MAX_ESTIMATED_SCORE_FOR_PROGRESS);progressBar.setStringPainted(false); // We'll
                                                                                                                  // use
                                                                                                                  // the
                                                                                                                  // separate
                                                                                                                  // scoreLabel
            progressBar.setValue(0);progressBar.setPreferredSize(new Dimension(200,25)); // Give progress bar a decent
                                                                                         // height
            progressBar.setForeground(UIConstants.ACCENT_COLOR_2); // Progress bar color

            JPanel progressContainer=new JPanel(new FlowLayout(FlowLayout.LEFT,10,0));progressContainer.setOpaque(false);progressContainer.add(new JLabel("Progress: "));progressContainer.add(progressBar);progressContainer.add(effect button.addMouseListener(new MouseAdapter(){public void mouseEntered(MouseEvent evt){button.setBackground(UIConstants.BUTTON_HOVER_COLOR);}public void mouseExited(MouseEvent evt){button.setBackground(UIConstants.BUTTON_COLOR);}});return button;
        }

        public void updateProgress() {
            if(scoreManager==null||progressBar==null||scoreLabel==null){return; // Not yet initialized
            }int currentScore=scoreManager.getTotalScore();scoreLabel.setText("Score: "+currentScore);

            // Calculate progress percentage
            int progressValue=(currentScore*100)/(MAX_ESTIMATED_SCORE>0?MAX_ESTIMATED_SCORE:1);progressBar.setValue(JProgressBar(0,ESTIMATED_MAX_SCORE);progressBar.setStringPainted(true);progressBar.setFont(UIConstants.LABEL_FONT.deriveFont(Font.BOLD));progressBar.setForeground(UIConstants.ACCENT_COLOR_2); // Progress
                                                                                                                                                                                                                                                                                                                    // bar
                                                                                                                                                                                                                                                                                                                    // color
            progressBar.setBackground(Color.LIGHT_GRAY);updateProgress(); // Initial progress
            bottomPanel.add(progressBar,BorderLayout.CENTER);

            JButton endButton=createStyledButton("End Session","END_SESSION_ACTION");endButton.setActionCommand(null); // Clear
                                                                                                                       // action
                                                                                                                       // command
                                                                                                                       // if
                                                                                                                       // handled
                                                                                                                       // directly
            endButton.addActionListener(e->app.endSession());

            JPanel endButtonContainer=new JPanel(new FlowLayout(FlowLayout.RIGHT)); // Align button to right
            endButtonContainer.setOpaque(false);endButtonContainer.add(endButton);bottomPanel.add(endButtonContainer,BorderLayout.EAST);

            add(bottomPanel,BorderLayout.SOUTH);
        }

        private JButton createStyledButton(String text, String actionCommand) {
            JButton button=new JButton(text);button.setFont(UIConstants.BUTTON_FONT);button.setBackground(UIConstants.BUTTON_COLOR);button.setForeground(UIConstants.TEXT_COLOR_LIGHT);button.setFocusPainted(false); // Remove
                                                                                                                                                                                                                      // focus
                                                                                                                                                                                                                      // border
            button.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(UIConstants.BUTTON_COLOR.darker(),1), // Subtle
                                                                                                                                     // border
            BorderFactory.createEmptyBorder(8,15,8,15) // Padding
            ));button.setCursor(new Cursor(Cursor.HAND_CURSOR));

            if(actionCommand!=null){button.setActionscoreLabel);

            bottomPanel.add(progressContainer,BorderLayout.CENTER);

            JButton endButton=createStyledButton("End Session");endButton.addActionListener(e->app.endSession());JPanel endButtonPanel=new JPanel(new FlowLayout(FlowLayout.RIGHT)); // Align
                                                                                                                                                                                     // button
                                                                                                                                                                                     // to
                                                                                                                                                                                     // the
                                                                                                                                                                                     // right
            endButtonPanel.setOpaque(false);endButtonPanel.add(endButton);bottomPanel.add(endButtonPanel,BorderLayout.EAST);

            add(bottomPanel,BorderLayout.SOUTH);
        }

        private JButton createStyledButton(String text) {
            JButton button=new JButton(text);button.setFont(UIConstants.BUTTON_FONT);button.setBackground(UIConstants.BUTTON_COLOR);button.setForeground(UIConstants.TEXT_COLOR_LIGHT);button.setFocusPainted(false);button.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(UIConstants.BUTTON_COLOR.darkMath.min(MAX_ESTIMATED_SCORE,currentScore)); // ProgressBar
                                                                                                                                                                                                                                                                                                                                                                                  // value
                                                                                                                                                                                                                                                                                                                                                                                  // should
                                                                                                                                                                                                                                                                                                                                                                                  // be
                                                                                                                                                                                                                                                                                                                                                                                  // actual
                                                                                                                                                                                                                                                                                                                                                                                  // score
                                                                                                                                                                                                                                                                                                                                                                                  // up
                                                                                                                                                                                                                                                                                                                                                                                  // to
                                                                                                                                                                                                                                                                                                                                                                                  // max
            // If you want the progress bar to show percentage 0-100:
            // progressBar.setMaximum(100);
            // progressBar.setValue(Math.min(100, progressValue));
            // progressBar.setString(progressValue + "%"); // If string painted
        }
}