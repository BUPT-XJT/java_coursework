package shapeville;

import java.awt.Color;
import java.awt.Font;

public class UIConstants {
    // Main Theme Colors
    public static final Color BACKGROUND_COLOR = new Color(220, 235, 250); // Lighter, softer blue
    public static final Color PANEL_BACKGROUND_COLOR = new Color(240, 245, 250); // Even lighter for content panels
    public static final Color BUTTON_COLOR = new Color(70, 130, 180); // Steel Blue
    public static final Color BUTTON_HOVER_COLOR = new Color(100, 149, 237); // Cornflower Blue
    public static final Color TEXT_COLOR_DARK = new Color(50, 50, 50);
    public static final Color TEXT_COLOR_LIGHT = Color.WHITE;
    public static final Color ACCENT_COLOR_1 = new Color(255, 165, 0); // Orange for highlights
    public static final Color ACCENT_COLOR_2 = new Color(60, 179, 113); // Medium Sea Green for positive feedback

    // Fonts
    public static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 28);
    public static final Font SUBTITLE_FONT = new Font("Arial", Font.BOLD, 20);
    public static final Font LABEL_FONT = new Font("Arial", Font.PLAIN, 16);
    public static final Font BUTTON_FONT = new Font("Arial", Font.BOLD, 14);
    public static final Font FEEDBACK_FONT = new Font("Arial", Font.ITALIC, 15);
    public static final Font INPUT_FONT = new Font("Arial", Font.PLAIN, 16);

    // Feedback Messages
    public static final String MSG_GREAT_JOB = "Great job!";
    public static final String MSG_TRY_AGAIN = "Not quite, try again!";
    public static final String MSG_CORRECT_ANSWER_IS = "The correct answer is: ";
    public static final String MSG_ALL_ATTEMPTS_USED = "All attempts used. ";
    public static final String MSG_TIME_UP = "Time's up!";
    public static final String MSG_INVALID_INPUT = "Please enter a valid value.";

    // Colors for Accessibility (Shapes/Diagrams)
    public static final Color SHAPE_OUTLINE_COLOR = Color.BLACK;
    public static final Color SHAPE_FILL_COLOR_ACCESSIBLE_1 = new Color(0, 114, 178); // Blue (Okabe-Ito)
    public static final Color SHAPE_FILL_COLOR_ACCESSIBLE_2 = new Color(213, 94, 0); // Vermillion (Okabe-Ito)
    public static final Color SHAPE_FILL_COLOR_ACCESSIBLE_3 = new Color(0, 158, 115); // Bluish Green (Okabe-Ito)

    // Timer Durations (in seconds)
    public static final int TIMER_DURATION_SHORT = 180; // 3 minutes
    public static final int TIMER_DURATION_LONG = 300; // 5 minutes
}