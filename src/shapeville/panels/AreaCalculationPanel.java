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
    private JComboBox<String> shapeSelectorComboBox; // 用户选择形状的下拉框
    private JLabel currentShapeDisplayLabel; // 显示当前练习的形状
    private JLabel questionDimensionsLabel;
    private JTextField answerField;
    private JButton submitButton;
    private GameTimer gameTimer;
    private JLabel timerLabel;
    private JLabel solutionImageLabel; // 显示带有公式和代入值的图片（图4风格）

    private String currentShapeType;
    private Map<String, Integer> currentDimensions;
    private double correctAnswer;
    private static final DecimalFormat df = new DecimalFormat("#.##");

    public AreaCalculationPanel(ShapevilleApp app, ScoreManager scoreManager, QuestionManager questionManager) {
        super(app, scoreManager, questionManager, "Area Calculation - Basic Shapes");

        centralContentPanel.setLayout(new BorderLayout(10, 10));

        // 顶部：形状类型显示和计时器
        JPanel topInfoPanel = new JPanel(new BorderLayout(10, 0));
        topInfoPanel.setOpaque(false);

        currentShapeDisplayLabel = new JLabel("Current Shape: ", SwingConstants.LEFT);
        currentShapeDisplayLabel.setFont(UIConstants.LABEL_FONT.deriveFont(Font.BOLD));

        timerLabel = new JLabel("Time: 00:00");
        timerLabel.setFont(UIConstants.LABEL_FONT.deriveFont(Font.BOLD));
        timerLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        // 添加形状选择下拉框
        String[] shapeOptions = {"Rectangle", "Parallelogram", "Triangle", "Trapezium"};
        shapeSelectorComboBox = new JComboBox<>(shapeOptions);
        shapeSelectorComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 用户选择形状后，更新题目
                currentShapeType = (String) shapeSelectorComboBox.getSelectedItem();
                nextQuestion();
            }
        });

        topInfoPanel.add(currentShapeDisplayLabel, BorderLayout.CENTER);
        topInfoPanel.add(timerLabel, BorderLayout.EAST);
        topInfoPanel.add(shapeSelectorComboBox, BorderLayout.WEST);

        centralContentPanel.add(topInfoPanel, BorderLayout.NORTH);

        // 中间：问题（尺寸）和解决方案图片显示区域
        JPanel questionAndSolutionArea = new JPanel(new GridLayout(2, 1, 5, 15));
        questionAndSolutionArea.setOpaque(false);

        questionDimensionsLabel = new JLabel("Dimensions will appear here.", SwingConstants.CENTER);
        questionDimensionsLabel.setFont(UIConstants.LABEL_FONT.deriveFont(18f));
        questionDimensionsLabel.setOpaque(true);
        questionDimensionsLabel.setBackground(Color.WHITE);
        questionDimensionsLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        questionAndSolutionArea.add(questionDimensionsLabel);

        solutionImageLabel = new JLabel();
        solutionImageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        solutionImageLabel.setVerticalAlignment(SwingConstants.CENTER);
        solutionImageLabel.setOpaque(true);
        solutionImageLabel.setBackground(Color.WHITE);
        solutionImageLabel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        solutionImageLabel.setPreferredSize(new Dimension(400, 150));
        questionAndSolutionArea.add(solutionImageLabel);

        centralContentPanel.add(questionAndSolutionArea, BorderLayout.CENTER);

        // 底部：答案输入
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
        questionManager.resetTaskProgress("AREA_CALC"); // 重置QM以循环遍历基本面积形状
        enableTaskInputs();
        // 初始化时选择第一个形状并显示题目
        shapeSelectorComboBox.setSelectedIndex(0);
        currentShapeType = (String) shapeSelectorComboBox.getSelectedItem();
        nextQuestion();
    }

    @Override
    protected void nextQuestion() {
        if (currentShapeType == null) {
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
        solutionImageLabel.setIcon(null); // 清除之前的解决方案图片
        enableTaskInputs();
        gameTimer.reset(UIConstants.TIMER_DURATION_SHORT); // 重置为3分钟
        gameTimer.start();
        answerField.requestFocusInWindow();
    }

    private void handleTimeUp() {
        if (!gameTimer.isRunning())
            return; // 如果已经处理过，避免多次调用
        gameTimer.stop();
        showFeedback(UIConstants.MSG_TIME_UP, false);
        scoreManager.recordAttempt(); // 计为一次尝试

        // 即使时间到了，也显示解决方案并进入下一题
        showSolution(); // 显示解决方案
        disableTaskInputs();
        Timer timer = new Timer(3500, e -> {
            questionManager.recordBasicAreaShapePracticed(currentShapeType); // 标记为已练习
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
        gameTimer.stop(); // 提交答案时停止计时器

        try {
            String userAnswerText = answerField.getText().trim();
            if (userAnswerText.isEmpty()) {
                showFeedback("Please enter your calculated area.", false);
                if (scoreManager.canAttempt())
                    gameTimer.start(); // 如果还有尝试次数，恢复计时
                return;
            }
            double userAnswer = Double.parseDouble(userAnswerText);
            scoreManager.recordAttempt();

            if (Math.abs(userAnswer - correctAnswer) < 0.01) { // 浮点数比较的容差
                awardPointsAndShowFeedback(false); // 基本评分
                showSolution(); // 显示带有公式和代入值的图片
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
                    gameTimer.start(); // 恢复计时
                } else {
                    showSolution();
                    disableTaskInputs();
                    questionManager.recordBasicAreaShapePracticed(currentShapeType); // 仍然计为“已练习”
                    questionsDoneThisTaskSession++;

                    Timer timer = new Timer(3500, e -> nextQuestion());
                    timer.setRepeats(false);
                    timer.start();
                }
            }
        } catch (NumberFormatException e) {
            showFeedback(UIConstants.MSG_INVALID_INPUT + " Enter a number for the area.", false);
            if (scoreManager.canAttempt())
                gameTimer.start(); // 如果还有尝试次数，恢复计时
        }
    }

    @Override
    protected void showSolution() {
        if (currentShapeType == null)
            return;
        feedbackLabel.setText(UIConstants.MSG_CORRECT_ANSWER_IS + df.format(correctAnswer));

        // 加载并显示对应形状的图片
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
            // 必要时缩放图片
            if (icon != null && icon.getIconWidth() > 0) {
                Image img = icon.getImage();
                int newWidth = solutionImageLabel.getWidth() > 0? solutionImageLabel.getWidth() - 10 : 380;
                int newHeight = solutionImageLabel.getHeight() > 0? solutionImageLabel.getHeight() - 10 : 140;
                // 如果标签尺寸尚未确定，保持原始图片显示
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