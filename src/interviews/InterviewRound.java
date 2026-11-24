package interviews;

import java.io.Serializable;

/**
 * Enum representing the rounds of an interview.
 * Concept used: Java Enum and Serializable.
 */
public enum InterviewRound implements Serializable {
    TECHNICAL_ROUND_1("Technical Round 1 (Coding & Core Java)"),
    TECHNICAL_ROUND_2("Technical Round 2 (System Design & Frameworks)"),
    HR_ROUND("HR & Behavioral Round"),
    MANAGERIAL_ROUND("Managerial & Fitment Round"),
    FINAL_ROUND("Final Discussion & Offer Round");

    private final String description;

    InterviewRound(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
