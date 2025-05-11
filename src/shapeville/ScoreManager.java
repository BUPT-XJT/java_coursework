
package shapeville;

public class ScoreManager {
    private int totalScore;
    private int currentQuestionAttempts; // Attempts for the current question
    public static final int MAX_ATTEMPTS_PER_QUESTION = 3;

    public ScoreManager() {
        this.totalScore = 0;
        this.currentQuestionAttempts = 0;
    }

    /**
     * Resets the attempt counter for a new question.
     */
    public void resetQuestionAttempts() {
        this.currentQuestionAttempts = 0;
    }

    /**
     * Records an attempt for the current question.
     */
    public void recordAttempt() {
        if (this.currentQuestionAttempts < MAX_ATTEMPTS_PER_QUESTION) {
            this.currentQuestionAttempts++;
        }
    }

    /**
     * Gets the number of attempts made for the current question.
     * 
     * @return The number of attempts.
     */
    public int getCurrentQuestionAttempts() {
        return currentQuestionAttempts;
    }

    /**
     * Checks if more attempts are allowed for the current question.
     * 
     * @return true if attempts are left, false otherwise.
     */
    public boolean canAttempt() {
        return currentQuestionAttempts < MAX_ATTEMPTS_PER_QUESTION;
    }

    /**
     * Calculates points based on the scoring level and number of attempts made for
     * the current question.
     * Does not add to total score; use {@link #awardCalculatedPoints(boolean)} for
     * that.
     * 
     * @param isAdvancedScoring true if advanced scoring applies, false for basic.
     * @return The points that would be awarded.
     */
    public int calculatePointsForCurrentAttempt(boolean isAdvancedScoring) {
        if (currentQuestionAttempts == 0)
            return 0; // Should not happen if recordAttempt is called first

        if (isAdvancedScoring) {
            if (currentQuestionAttempts == 1)
                return 6;
            if (currentQuestionAttempts == 2)
                return 4;
            if (currentQuestionAttempts == 3)
                return 2;
        } else { // Basic scoring
            if (currentQuestionAttempts == 1)
                return 3;
            if (currentQuestionAttempts == 2)
                return 2;
            if (currentQuestionAttempts == 3)
                return 1;
        }
        return 0; // No points if more than max attempts (should be caught by canAttempt)
    }

    /**
     * Awards points calculated based on current attempts and adds to total score.
     * 
     * @param isAdvancedScoring true if advanced scoring applies.
     * @return The points awarded.
     */
    public int awardCalculatedPoints(boolean isAdvancedScoring) {
        int points = calculatePointsForCurrentAttempt(isAdvancedScoring);
        this.totalScore += points;
        return points;
    }

    public int getTotalScore() {
        return totalScore;
    }

    /**
     * Resets the total score and current question attempts.
     * Typically used when starting a new session or game.
     */
    public void resetSessionScore() {
        this.totalScore = 0;
        this.currentQuestionAttempts = 0;
    }
}