package applications;

import utils.ApplicationStatus;

import java.io.Serializable;
import java.time.LocalDate;
public class Application implements Serializable {
    private static final long serialVersionUID = 1L;

    private String applicationId;
    private String jobId;
    private String candidateId;
    private String applicationDate;
    private ApplicationStatus status;
    private double matchScore;
    private boolean cgpaEligible;
    private String remarks;

    public Application(String applicationId, String jobId, String candidateId) {
        this.applicationId = applicationId;
        this.jobId = jobId;
        this.candidateId = candidateId;
        this.applicationDate = LocalDate.now().toString();
        this.status = ApplicationStatus.APPLIED;
        this.matchScore = 0.0;
        this.cgpaEligible = true;
        this.remarks = "Application received and queued for screening.";
    }

    public void displaySummary() {
        System.out.printf("[%s] Job: %-8s | Candidate: %-8s | Match: %5.1f%% | CGPA OK: %-5s | Status: %s%n",
                applicationId, jobId, candidateId, matchScore, (cgpaEligible ? "YES" : "NO"), status.name());
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(String candidateId) {
        this.candidateId = candidateId;
    }

    public String getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(String applicationDate) {
        this.applicationDate = applicationDate;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(ApplicationStatus status) {
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
        return applicationId + " [Job: " + jobId + ", Candidate: " + candidateId + ", Status: " + status + ", Score: " + matchScore + "%]";
    }
}
