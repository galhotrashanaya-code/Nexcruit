package utils;

import java.io.Serializable;

/**
 * Enum representing the different stages of a job application.
 * Concept used: Java Enum and Serializable.
 */
public enum ApplicationStatus implements Serializable {
    APPLIED("Applied - Under Initial Review"),
    UNDER_REVIEW("Under Review - Being Screened"),
    SHORTLISTED("Shortlisted - Eligible for Interview"),
    INTERVIEW_SCHEDULED("Interview Scheduled"),
    SELECTED("Selected - Offer Given"),
    REJECTED("Rejected - Not Selected");

    // Readable description for user-friendly display
    private final String description;

    ApplicationStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
