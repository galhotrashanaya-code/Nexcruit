package interviews;

import java.io.Serializable;

/**
 * Enum representing the status of an interview.
 * Concept used: Java Enum and Serializable.
 */
public enum InterviewStatus implements Serializable {
    SCHEDULED("Interview Scheduled"),
    COMPLETED("Interview Completed"),
    PASSED("Passed - Qualified to Next Step"),
    FAILED("Failed - Not Selected"),
    CANCELLED("Cancelled");

    private final String description;

    InterviewStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
