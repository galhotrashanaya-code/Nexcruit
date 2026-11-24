package nexcruit;

import auth.Candidate;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Represents a Job Application linking a Candidate to a specific Job opening.
 * Developed by: Himani (Job & Application Management Module)
 *
 * Concepts used:
 * - Proper Encapsulation (private fields with getters and setters)
 * - Object Relationships (aggregates actual Candidate and Job references)
 * - Serializable for file storage
 */
public class Application implements Serializable {
    private static final long serialVersionUID = 1L;

    // Attributes specified in Himani's requirements
    private String applicationId;
    private Candidate candidate; // Actual Candidate object reference
    private Job job;             // Actual Job object reference
    private String applicationDate;
    private String status;       // Simple status: "APPLIED", "UNDER_REVIEW", "SHORTLISTED", "SELECTED", "REJECTED"
    private double matchScore;   // Matching score from MatchEngine
    private boolean cgpaEligible;
    private String remarks;

    /**
     * Primary Constructor storing actual Candidate and Job references.
     */
    public Application(String applicationId, Candidate candidate, Job job) {
        this.applicationId = applicationId;
        this.candidate = candidate;
        this.job = job;
        this.applicationDate = LocalDate.now().toString();
        this.status = "APPLIED";
        this.matchScore = 0.0;
        this.cgpaEligible = true;
        this.remarks = "Application submitted successfully.";
    }

    /**
     * Overloaded Constructor with custom application date and initial status.
     */
    public Application(String applicationId, Candidate candidate, Job job, String applicationDate, String status) {
        this(applicationId, candidate, job);
        if (applicationDate != null && !applicationDate.trim().isEmpty()) {
            this.applicationDate = applicationDate.trim();
        }
        if (status != null && !status.trim().isEmpty()) {
            this.status = status.trim().toUpperCase();
        }
    }

    /**
     * Displays complete, formatted application details.
     */
    public void displayApplicationDetails() {
        System.out.println("================================================================================");
        System.out.println(" Application ID   : " + applicationId);
        System.out.println(" Job ID           : " + (job != null ? job.getJobId() : "N/A"));
        System.out.println(" Job Title        : " + (job != null ? job.getTitle() : "N/A"));
        System.out.println(" Company          : " + (job != null ? job.getCompanyName() : "N/A"));
        System.out.println(" Candidate ID     : " + (candidate != null ? candidate.getUserId() : "N/A"));
        System.out.println(" Candidate Name   : " + (candidate != null ? candidate.getName() : "N/A"));
        System.out.println(" Candidate CGPA   : " + (candidate != null ? String.format("%.2f", candidate.getCgpa()) : "N/A"));
        System.out.println(" Application Date : " + applicationDate);
        System.out.println(" Status           : " + status);
        System.out.printf(" Match Score      : %.1f%%%n", matchScore);
        System.out.println(" CGPA Eligible    : " + (cgpaEligible ? "YES" : "NO"));
        System.out.println(" Remarks          : " + remarks);
        System.out.println("================================================================================");
    }

    /**
     * Displays a single-line summary for tabular views.
     */
    public void displaySummary() {
        String candName = (candidate != null) ? candidate.getName() : "Unknown";
        String jobTitle = (job != null) ? job.getTitle() : "Unknown";
        if (candName.length() > 18) candName = candName.substring(0, 15) + "...";
        if (jobTitle.length() > 20) jobTitle = jobTitle.substring(0, 17) + "...";

        System.out.printf("[%s] Job: %-8s (%-20s) | Cand: %-8s (%-18s) | Match: %5.1f%% | Status: %s%n",
                applicationId,
                (job != null ? job.getJobId() : "N/A"),
                jobTitle,
                (candidate != null ? candidate.getUserId() : "N/A"),
                candName,
                matchScore,
                status);
    }

    // ==========================================
    // GETTERS AND SETTERS
    // ==========================================

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public Candidate getCandidate() {
        return candidate;
    }

    public void setCandidate(Candidate candidate) {
        this.candidate = candidate;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    // Convenience getters for ID string lookups
    public String getCandidateId() {
        return (candidate != null) ? candidate.getUserId() : "";
    }

    public String getJobId() {
        return (job != null) ? job.getJobId() : "";
    }

    public String getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(String applicationDate) {
        this.applicationDate = applicationDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getMatchScore() {
        return matchScore;
    }

    public void setMatchScore(double matchScore) {
        this.matchScore = matchScore;
    }

    public boolean isCgpaEligible() {
        return cgpaEligible;
    }

    public void setCgpaEligible(boolean cgpaEligible) {
        this.cgpaEligible = cgpaEligible;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Override
    public String toString() {
        return "[" + applicationId + "] " + (candidate != null ? candidate.getName() : "Unknown") +
                " -> " + (job != null ? job.getTitle() : "Unknown") + " (" + status + ")";
    }
}
