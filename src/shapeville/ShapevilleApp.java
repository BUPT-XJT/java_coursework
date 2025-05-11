package shapeville;

import shapeville.panels.*; // 导入所有面板类
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ShapevilleApp extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private HomeScreenPanel homeScreenPanel;
    private ScoreManager scoreManager;
    private QuestionManager questionManager;

    // Task Panels
    private ShapeIdentificationPanel shapeIdPanel2D;
    private ShapeIdentificationPanel shapeIdPanel3D;
    private AngleIdentificationPanel angleIdPanel;
    private AreaCalculationPanel areaCalcPanel;
    private CircleCalculationPanel circleCalcPanel;
    private CompoundShapePanel compoundShapePanel;
    private SectorAreaPanel sectorAreaPanel;

    public ShapevilleApp() {
        setTitle("Shapeville - Geometry Fun!");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(850, 700); // 稍微增大尺寸以容纳更多内容
        setLocationRelativeTo(null); // 居中显示

        scoreManager = new ScoreManager();
        questionManager = new QuestionManager(); // 初始化问题数据管理器

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(UIConstants.BACKGROUND_COLOR); // 设置主面板背景

        homeScreenPanel = new HomeScreenPanel(this, scoreManager);
        mainPanel.add(homeScreenPanel, "HOME");

        // Initialize other panels and add to cardLayout
        shapeIdPanel2D = new ShapeIdentificationPanel(this, scoreManager, questionManager, false); // false for 2D
        mainPanel.add(shapeIdPanel2D, "SHAPE_ID_2D");

        shapeIdPanel3D = new ShapeIdentificationPanel(this, scoreManager, questionManager, true); // true for 3D
        mainPanel.add(shapeIdPanel3D, "SHAPE_ID_3D");

        angleIdPanel = new AngleIdentificationPanel(this, scoreManager, questionManager);
        mainPanel.add(angleIdPanel, "ANGLE_ID");

        areaCalcPanel = new AreaCalculationPanel(this, scoreManager, questionManager);
        mainPanel.add(areaCalcPanel, "AREA_CALC");

        circleCalcPanel = new CircleCalculationPanel(this, scoreManager, questionManager);
        mainPanel.add(circleCalcPanel, "CIRCLE_CALC");

        compoundShapePanel = new CompoundShapePanel(this, scoreManager, questionManager);
        mainPanel.add(compoundShapePanel, "COMPOUND_SHAPE");

        sectorAreaPanel = new SectorAreaPanel(this, scoreManager, questionManager);
        mainPanel.add(sectorAreaPanel, "SECTOR_AREA");

        add(mainPanel);
        showPanel("HOME"); // Start with home screen
    }

    public HomeScreenPanel getHomeScreenPanel() { // 添加 getter 方法
        return this.homeScreenPanel;
    }

    public void showPanel(String panelName) {
        cardLayout.show(mainPanel, panelName);
        // If the panel is a TaskPanel, call its startTask() or reset method
        Component currentComponent = null;
        for (Component comp : mainPanel.getComponents()) {
            if (comp.isVisible() && comp instanceof JPanel) { // 确保是JPanel且可见
                // CardLayout的直接子组件通常就是我们要找的
                // 如果有更深的嵌套，这个逻辑可能需要调整
                if (((JPanel) comp).getLayout() instanceof CardLayout) {
                    // This is the mainPanel itself, iterate its children
                    for (Component innerComp : ((JPanel) comp).getComponents()) {
                        if (innerComp.isVisible()) {
                            currentComponent = innerComp;
                            break;
                        }
                    }
                } else {
                    currentComponent = comp; // This should be the active panel
                }
                break;
            }
        }

        if (currentComponent instanceof TaskPanel) {
            ((TaskPanel) currentComponent).startTask();
        } else if (panelName.equals("HOME") && homeScreenPanel != null) {
            homeScreenPanel.updateProgress(); // Update progress bar on returning home
        }
    }

    public void endSession() {
        JOptionPane.showMessageDialog(this,
                "You have achieved " + scoreManager.getTotalScore() + " points in this session. Goodbye!",
                "Session Ended", JOptionPane.INFORMATION_MESSAGE);
        System.exit(0);
    }

    public static void main(String[] args) {
        // Set Look and Feel for better appearance (optional)
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Couldn't set system look and feel.");
        }

        SwingUtilities.invokeLater(() -> {
            ShapevilleApp app = new ShapevilleApp();
            app.setVisible(true);
        });
    }
}
