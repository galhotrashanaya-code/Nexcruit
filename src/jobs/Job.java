package jobs;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Represents a Job Vacancy posted by a Recruiter.
 * Encapsulates required skills, minimum CGPA eligibility, and job specifications.
 * Concepts used:
 * - Encapsulation (private fields with getters and setters)
 * - ArrayList (list of required skills)
 * - Serializable
 */
public class Job implements Serializable {
    private static final long serialVersionUID = 1L;

    private String jobId;
    private String recruiterId;
    private String title;
    private String company;
    private String description;
    private ArrayList<String> requiredSkills;
    private double minCgpa;
    private int minExperience;
    private String location;
    private boolean active;
    private String postedDate;

    public Job(String jobId, String recruiterId, String title, String company,
               String description, ArrayList<String> requiredSkills, double minCgpa,
               int minExperience, String location) {
        this.jobId = jobId;
        this.recruiterId = recruiterId;
        this.title = title;
        this.company = company;
        this.description = description;
        if (requiredSkills != null) {
            this.requiredSkills = new ArrayList<String>(requiredSkills);
        } else {
            this.requiredSkills = new ArrayList<String>();
        }
        this.minCgpa = minCgpa;
        this.minExperience = minExperience;
        this.location = location;
        this.active = true;
        this.postedDate = LocalDate.now().toString();
    }

    // Prints a single line summary of the job opening
    public void displaySummary() {
        System.out.printf("[%s] %-25s | Company: %-18s | Min CGPA: %-4.2f | Exp: %d yr(s) | Status: %s%n",
                jobId, title, company, minCgpa, minExperience, (active ? "ACTIVE" : "CLOSED"));
    }

    // Prints complete details of the job opening
    public void displayDetails() {
        System.out.println("================================================================================");
        System.out.println(" Job ID           : " + jobId);
        System.out.println(" Job Title        : " + title);
        System.out.println(" Company          : " + company);
        System.out.println(" Location         : " + location);
        System.out.println(" Recruiter ID     : " + recruiterId);
        System.out.println(" Posted Date      : " + postedDate);
        System.out.println(" Job Status       : " + (active ? "ACTIVE (Accepting Applications)" : "CLOSED"));
        System.out.printf(" Mandatory Min CGPA: %.2f / 10.0%n", minCgpa);
        System.out.println(" Min Experience   : " + minExperience + " year(s)");
        System.out.println(" Required Skills  : " + String.join(", ", requiredSkills));
        System.out.println(" Job Description  : " + description);
        System.out.println("================================================================================");
    }

    // Getters and Setters
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<String> getRequiredSkills() {
        return new ArrayList<String>(requiredSkills);
    }

    public void setRequiredSkills(ArrayList<String> requiredSkills) {
        if (requiredSkills != null) {
            this.requiredSkills = new ArrayList<String>(requiredSkills);
        } else {
            this.requiredSkills = new ArrayList<String>();
        }
    }

    public double getMinCgpa() {
        return minCgpa;
    }

    public void setMinCgpa(double minCgpa) {
        this.minCgpa = minCgpa;
    }

    public int getMinExperience() {
        return minExperience;
    }

    public void setMinExperience(int minExperience) {
        this.minExperience = minExperience;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(String postedDate) {
        this.postedDate = postedDate;
    }

    @Override
    public String toString() {
        return jobId + " - " + title + " at " + company + " (" + (active ? "Open" : "Closed") + ")";
    }
}
