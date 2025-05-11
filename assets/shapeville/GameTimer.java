package shapeville;

import javax.swing.Timer;
import javax.swing.JLabel;
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
     * Call start() to begin timing again.
     */
    public void reset() {
        stop();
        this.timeLeftSeconds = this.durationSeconds;
        updateLabel();
    }

    /**
     * Resets the timer to a new duration and stops it.
     * 
     * @param newDurationSeconds The new duration in seconds.
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