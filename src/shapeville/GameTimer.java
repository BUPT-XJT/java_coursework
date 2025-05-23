package shapeville;

import javax.swing.Timer;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class GameTimer {
    private Timer swingTimer;
    private int durationSeconds; // Original duration for reset
    private int timeLeftSeconds;
    private JLabel timerLabel;
    private Runnable onTimeUpCallback; // Action to perform when time is up

    public GameTimer(int durationSeconds, JLabel timerLabel, Runnable onTimeUpCallback) {
        this.durationSeconds = durationSeconds;
        this.timeLeftSeconds = durationSeconds;
        this.timerLabel = timerLabel;
        this.onTimeUpCallback = onTimeUpCallback;

        this.swingTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (timeLeftSeconds > 0) {
                    timeLeftSeconds--;
                    updateLabel();
                    // 添加每超过90秒弹窗的逻辑
                    if ((durationSeconds - timeLeftSeconds) % 90 == 0) {
                        // 使用showOptionDialog显示带有yes和no按钮的对话框
                        int option = JOptionPane.showOptionDialog(null,
                                "Hi, are you still working on this problem?",
                                "Help Needed",
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.QUESTION_MESSAGE,
                                null,
                                new Object[]{"Yes", "No"},
                                "Yes");
                        // 根据用户选择的按钮进行相应处理
                        if (option == JOptionPane.YES_OPTION) {
                            // 用户点击了Yes按钮
                            JOptionPane.showMessageDialog(null, "Good job,I'm sure you'll be able to work it out soon!");
                        } else if (option == JOptionPane.NO_OPTION) {
                            // 用户点击了No按钮
                            JOptionPane.showMessageDialog(null, "It doesn't matter. Let's keep working hard to solve this problem!");
                        }
                    }
                } else {
                    stop(); // Stop the timer itself
                    if (onTimeUpCallback != null) {
                        onTimeUpCallback.run(); // Execute the callback
                    }
                }
            }
        });
        updateLabel(); // Initialize label text
    }

    private void updateLabel() {
        if (timerLabel != null) {
            int minutes = timeLeftSeconds / 60;
            int seconds = timeLeftSeconds % 60;
            timerLabel.setText(String.format("Time: %02d:%02d", minutes, seconds));
        }
    }

    public void start() {
        if (!swingTimer.isRunning()) {
            // If timeLeftSeconds is 0 or less from a previous run, reset it.
            if (timeLeftSeconds <= 0) {
                this.timeLeftSeconds = this.durationSeconds;
            }
            updateLabel();
            swingTimer.start();
        }
    }

    public void stop() {
        if (swingTimer.isRunning()) {
            swingTimer.stop();
        }
    }

    /**
     * Resets the timer to its initial duration and stops it.
     */
    public void reset(int newDurationSeconds) {
        stop();
        this.durationSeconds = newDurationSeconds;
        this.timeLeftSeconds = newDurationSeconds;
        updateLabel();
    }

    public boolean isRunning() {
        return swingTimer.isRunning();
    }

    public int getTimeLeftSeconds() {
        return timeLeftSeconds;
    }
}