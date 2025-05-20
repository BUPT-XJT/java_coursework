package shapeville.panels;

import shapeville.UIConstants;

import javax.swing.*;
import java.awt.*;

public class AngleDrawingPanel extends JPanel {
    private int angleInDegrees = 0; // Angle to draw

    public AngleDrawingPanel() {
        setPreferredSize(new Dimension(250, 250)); // Increased size
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
    }

    public void setAngle(int degrees) {
        // Normalize angle to be 0-359 for drawing consistency if needed,
        // but spec says 0-360 input.
        this.angleInDegrees = degrees % 360;
        if (this.angleInDegrees < 0)
            this.angleInDegrees += 360;
        if (degrees == 360)
            this.angleInDegrees = 360; // Special case for full circle display if needed

        repaint(); // Request a redraw
    }

    public int getAngle() {
        return angleInDegrees;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setStroke(new BasicStroke(2.5f)); // Thicker lines

        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int radius = Math.min(getWidth(), getHeight()) / 2 - 30; // Line length, more padding

        // Draw the first line (horizontal, along positive x-axis from center)
        g2d.setColor(UIConstants.SHAPE_OUTLINE_COLOR);
        g2d.drawLine(centerX, centerY, centerX + radius, centerY);

        // Calculate endpoint of the second line based on the angle
        // Swing angles: 0 is East, positive is CCW.
        // For standard math angles: 0 is East, positive is CCW.
        // To draw like a protractor (0 East, positive CCW on screen):
        double angleRad = Math.toRadians(angleInDegrees); // Math.toRadians expects angle in degrees

        // If you want 0 degrees to be along the positive Y-axis (North) and angles to
        // go clockwise (like a clock):
        // double angleRad = Math.toRadians(angleInDegrees - 90);
        // Or for 0-East, positive CCW in standard Cartesian, but screen Y is inverted:
        // double angleRadForScreen = Math.toRadians(-angleInDegrees);

        int x2 = centerX + (int) (radius * Math.cos(angleRad));
        int y2 = centerY - (int) (radius * Math.sin(angleRad)); // Subtract sin because Y is inverted on screen
        g2d.drawLine(centerX, centerY, x2, y2);

        // Draw the arc
        g2d.setColor(UIConstants.SHAPE_FILL_COLOR_ACCESSIBLE_1);
        g2d.setStroke(new BasicStroke(2f)); // Slightly thinner for arc
        // Arc angles: 0 is 3 o'clock, positive is CCW.
        if (angleInDegrees > 0 && angleInDegrees <= 360) {
            if (angleInDegrees == 360) { // Draw full circle for 360
                g2d.drawArc(centerX - radius / 3, centerY - radius / 3, 2 * (radius / 3), 2 * (radius / 3), 0, 360);
            } else {
                g2d.drawArc(centerX - radius / 3, centerY - radius / 3, 2 * (radius / 3), 2 * (radius / 3), 0,
                        angleInDegrees);
            }
        }

        // Draw angle value text
        g2d.setColor(Color.BLACK);
        g2d.setFont(UIConstants.LABEL_FONT.deriveFont(14f));
        String angleText = angleInDegrees + "°";

        // Position text near the arc, slightly offset.
        // Midpoint angle for text positioning:
        double midAngleRad = Math.toRadians(-angleInDegrees / 2.0);
        int textRadius = radius / 2 + 10; // Distance from center for text

        int textX = centerX + (int) (textRadius * Math.cos(midAngleRad));
        int textY = centerY + (int) (textRadius * Math.sin(midAngleRad));

        // Adjust text position to not overlap lines, crude adjustments
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(angleText);

        if (angleInDegrees > 10 && angleInDegrees < 170)
            textY -= fm.getAscent() / 2; // Above arc for smaller angles
        else if (angleInDegrees > 190 && angleInDegrees < 350)
            textY += fm.getAscent(); // Below arc for reflex

        // Basic centering of text
        textX -= textWidth / 2;
        textY += fm.getAscent() / 2; // Roughly center vertically

        g2d.drawString(angleText, textX, textY);
        
    }
}