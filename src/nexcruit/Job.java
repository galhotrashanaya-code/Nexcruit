package nexcruit;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Represents a Job Vacancy in the Nexcruit system.
 * Developed by: Himani (Job & Application Management Module)
 *
 * Concepts used:
 * - Encapsulation (private variables with getters and setters)
 * - Constructors and Method Overloading
 * - ArrayList<String> for technical skills
 * - Serializable for file storage
 */
public class Job implements Serializable {
    private static final long serialVersionUID = 1L;

    // Core attributes assigned to Himani
    private String jobId;
    private String title;
    private String companyName;
    private String description;
    private ArrayList<String> requiredSkills;
    private double minCgpa;
    private int requiredExperience;
    private int numberOfVacancies;
    private String status; // "OPEN" or "CLOSED"

    // Integration attributes for Recruiter & Location compatibility
    private String recruiterId;
    private String location;
    private String postedDate;

    /**
     * Primary Constructor for Job.java (Himani's assigned specification).
     */
    public Job(String jobId, String title, String companyName, String description,
               ArrayList<String> requiredSkills, double minCgpa,
               int requiredExperience, int numberOfVacancies) {
        this.jobId = jobId;
        this.title = title;
        this.companyName = companyName;
        this.description = description;
        if (requiredSkills != null) {
            this.requiredSkills = new ArrayList<String>(requiredSkills);
        } else {
            this.requiredSkills = new ArrayList<String>();
        }
        this.minCgpa = minCgpa;
        this.requiredExperience = requiredExperience;
        this.numberOfVacancies = numberOfVacancies;
        this.status = "OPEN";
        this.recruiterId = "REC-DEFAULT";
        this.location = "Hybrid / Remote";
        this.postedDate = LocalDate.now().toString();
    }

    /**
     * Overloaded Constructor with Recruiter ID and Location for complete team integration.
     */
    public Job(String jobId, String recruiterId, String title, String companyName,
               String description, ArrayList<String> requiredSkills, double minCgpa,
               int requiredExperience, int numberOfVacancies, String location) {
        this(jobId, title, companyName, description, requiredSkills, minCgpa, requiredExperience, numberOfVacancies);
        if (recruiterId != null && !recruiterId.trim().isEmpty()) {
            this.recruiterId = recruiterId.trim();
        }
        if (location != null && !location.trim().isEmpty()) {
            this.location = location.trim();
        }
    }

    /**
     * Displays complete, formatted job details to the console.
     */
    public void displayJobDetails() {
        System.out.println("================================================================================");
        System.out.println(" Job ID           : " + jobId);
        System.out.println(" Job Title        : " + title);
        System.out.println(" Company Name     : " + companyName);
        System.out.println(" Location         : " + location);
        System.out.println(" Recruiter ID     : " + recruiterId);
        System.out.println(" Job Status       : " + status);
        System.out.println(" Vacancies Open   : " + numberOfVacancies);
        System.out.printf(" Mandatory Min CGPA: %.2f / 10.0%n", minCgpa);
        System.out.println(" Min Experience   : " + requiredExperience + " year(s)");
        System.out.println(" Required Skills  : " + (requiredSkills.isEmpty() ? "None specified" : String.join(", ", requiredSkills)));
        System.out.println(" Description      : " + description);
        System.out.println(" Posted Date      : " + postedDate);
        System.out.println("================================================================================");
    }

    /**
     * Displays a compact single-line summary for table listings.
     */
    public void displaySummary() {
        System.out.printf("[%s] %-24s | %-18s | Vacancies: %-2d | Min CGPA: %-4.2f | Exp: %d yr(s) | Status: %s%n",
                jobId, title, companyName, numberOfVacancies, minCgpa, requiredExperience, status);
    }

    // ==========================================
    // GETTERS AND SETTERS (Proper Encapsulation)
    // ==========================================

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    // Alias for backward compatibility
    public String getCompany() {
        return companyName;
    }

    public void setCompany(String company) {
        this.companyName = company;
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

    public int getRequiredExperience() {
        return requiredExperience;
    }

    public void setRequiredExperience(int requiredExperience) {
        this.requiredExperience = requiredExperience;
    }

    // Compatibility alias for MatchEngine
    public int getMinExperience() {
        return requiredExperience;
    }

    public void setMinExperience(int minExperience) {
        this.requiredExperience = minExperience;
    }

    public int getNumberOfVacancies() {
        return numberOfVacancies;
    }

    public void setNumberOfVacancies(int numberOfVacancies) {
        this.numberOfVacancies = numberOfVacancies;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isActive() {
        return "OPEN".equalsIgnoreCase(status);
    }

    public void setActive(boolean active) {
        this.status = active ? "OPEN" : "CLOSED";
    }

    public String getRecruiterId() {
        return recruiterId;
    }

    public void setRecruiterId(String recruiterId) {
        this.recruiterId = recruiterId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(String postedDate) {
        this.postedDate = postedDate;
    }

    @Override
    public String toString() {
        return "[" + jobId + "] " + title + " at " + companyName + " (" + status + ", " + numberOfVacancies + " openings)";
    }
}
