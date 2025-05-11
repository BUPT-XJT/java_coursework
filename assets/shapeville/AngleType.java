package shapeville;

/**
 * Enum representing different types of angles as per Figure 3.
 */
public enum AngleType {
    ACUTE("Acute Angle", "less than 90° and greater than 0°"),
    RIGHT("Right Angle", "equal to 90°"),
    OBTUSE("Obtuse Angle", "less than 180° and greater than 90°"),
    STRAIGHT("Straight Angle", "equal to 180°"),
    REFLEX("Reflex Angle", "greater than 180° and less than 360°");
    // Note: 0° and 360° are boundary cases, often not classified into these primary
    // types
    // without specific context. The spec implies generating angles *between* 0 and
    // 360.

    private final String displayName;
    private final String description; // As per Figure 3

    AngleType(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Determines the AngleType based on a given angle value in degrees.
     * Assumes angle is > 0 and < 360 for these classifications.
     * 
     * @param angleDegrees The angle in degrees.
     * @return The corresponding AngleType, or null if it doesn't fit these primary
     *         types (e.g., 0 or 360).
     */
    public static AngleType fromAngleValue(int angleDegrees) {
        if (angleDegrees > 0 && angleDegrees < 90) {
            return ACUTE;
        } else if (angleDegrees == 90) {
            return RIGHT;
        } else if (angleDegrees > 90 && angleDegrees < 180) {
            return OBTUSE;
        } else if (angleDegrees == 180) {
            return STRAIGHT;
        } else if (angleDegrees > 180 && angleDegrees < 360) {
            return REFLEX;
        }
        return null; // For 0, 360, or angles outside the 0-360 range if not pre-validated.
    }

    @Override
    public String toString() {
        return displayName; // Makes JComboBox display nicely
    }
}