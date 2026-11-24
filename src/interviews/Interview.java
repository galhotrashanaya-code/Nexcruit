package interviews;

import java.io.Serializable;

/**
 * Represents a scheduled interview between a Recruiter and Candidate.
 * Concepts used:
 * - Encapsulation (private fields with getters and setters)
 * - Enums (InterviewRound, InterviewStatus)
 * - Serializable
 */
public class Interview implements Serializable {
    private static final long serialVersionUID = 1L;

    private String interviewId;
    private String applicationId;
    private String candidateId;
    private String jobId;
    private String recruiterId;
    private InterviewRound round;
    private String scheduledDateTime;
    private String mode; // e.g. "Google Meet" or "Office Room 101"
    private InterviewStatus status;
    private String feedback;

    public Interview(String interviewId, String applicationId, String candidateId,
                     String jobId, String recruiterId, InterviewRound round,
                     String scheduledDateTime, String mode) {
        this.interviewId = interviewId;
        this.applicationId = applicationId;
        this.candidateId = candidateId;
        this.jobId = jobId;
        this.recruiterId = recruiterId;
        this.round = round;
        this.scheduledDateTime = scheduledDateTime;
        this.mode = (mode != null && !mode.isEmpty()) ? mode : "Online Video Call";
        this.status = InterviewStatus.SCHEDULED;
        this.feedback = "Interview scheduled. Awaiting completion.";
    }

    // Displays summary row for console tables
    public void displaySummary() {
        System.out.printf("[%s] Job: %-8s | Candidate: %-8s | Round: %-18s | Date: %-16s | Status: %s%n",
                interviewId, jobId, candidateId, round.name(), scheduledDateTime, status.name());
    }

    // Displays detailed card for interview review
    public void displayDetails() {
        System.out.println("================================================================================");
        System.out.println(" Interview ID     : " + interviewId);
        System.out.println(" Application ID   : " + applicationId);
        System.out.println(" Candidate ID     : " + candidateId);
        System.out.println(" Job ID           : " + jobId);
        System.out.println(" Recruiter ID     : " + recruiterId);
        System.out.println(" Round            : " + round.getDescription());
        System.out.println(" Scheduled Date   : " + scheduledDateTime);
        System.out.println(" Mode / Location  : " + mode);
        System.out.println(" Current Status   : " + status.getDescription());
        System.out.println(" Feedback / Notes : " + feedback);
        System.out.println("================================================================================");
    }

    // Getters and Setters
    public String getInterviewId() {
        return interviewId;
    }

    public void setInterviewId(String interviewId) {
        this.interviewId = interviewId;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(String candidateId) {
        this.candidateId = candidateId;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getRecruiterId() {
        return recruiterId;
    }

    public void setRecruiterId(String recruiterId) {
        this.recruiterId = recruiterId;
    }

    public InterviewRound getRound() {
        return round;
    }

    public void setRound(InterviewRound round) {
        this.round = round;
    }

    public String getScheduledDateTime() {
        return scheduledDateTime;
    }

    public void setScheduledDateTime(String scheduledDateTime) {
        this.scheduledDateTime = scheduledDateTime;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public InterviewStatus getStatus() {
        return status;
    }

    public void setStatus(InterviewStatus status) {
        this.status = status;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    @Override
    public String toString() {
        return interviewId + " [" + round + " on " + scheduledDateTime + " - " + status + "]";
    }
}
