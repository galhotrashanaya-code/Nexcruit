package nexcruit;

import exceptions.InvalidInputException;
import utils.InputValidator;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Manages the publishing, search, retrieval, and updating of job postings.
 * Developed by: Himani (Job & Application Management Module)
 *
 * Concepts used:
 * - ArrayList<Job> collection
 * - Simple for-each loops and if-else logic
 * - Exception Handling (InvalidInputException)
 * - Input validation
 * - Serializable
 */
public class JobManager implements Serializable {
    private static final long serialVersionUID = 1L;

    // Himani's requirement: Maintain jobs using ArrayList<Job>
    private ArrayList<Job> jobsList;
    private int jobCounter;

    public JobManager() {
        this.jobsList = new ArrayList<Job>();
        this.jobCounter = 101;
    }

    /**
     * Generates a unique, auto-incrementing Job ID (e.g. JOB-101, JOB-102).
     */
    public String generateJobId() {
        String id = "JOB-" + jobCounter;
        jobCounter++;
        return id;
    }

    /**
     * Creates and publishes a new job opening with thorough input validation.
     */
    public Job createJob(String title, String companyName, String description,
                         ArrayList<String> requiredSkills, double minCgpa,
                         int requiredExperience, int numberOfVacancies)
            throws InvalidInputException {

        // Validate user inputs
        if (!InputValidator.isNonEmpty(title)) {
            throw new InvalidInputException("Job title cannot be empty.");
        }
        if (!InputValidator.isNonEmpty(companyName)) {
            throw new InvalidInputException("Company name cannot be empty.");
        }
        if (!InputValidator.isValidCgpa(minCgpa)) {
            throw new InvalidInputException("Minimum CGPA must be between 0.0 and 10.0.");
        }
        if (!InputValidator.isValidExperience(requiredExperience)) {
            throw new InvalidInputException("Required experience must be between 0 and 50 years.");
        }
        if (numberOfVacancies <= 0) {
            throw new InvalidInputException("Number of vacancies must be at least 1.");
        }
        if (requiredSkills == null || requiredSkills.isEmpty()) {
            throw new InvalidInputException("At least one required skill must be specified.");
        }

        String jobId = generateJobId();
        Job job = new Job(jobId, title.trim(), companyName.trim(),
                description != null ? description.trim() : "",
                requiredSkills, minCgpa, requiredExperience, numberOfVacancies);

        jobsList.add(job);
        return job;
    }

    /**
     * Overloaded method allowing recruiter ID and location for full team integration.
     */
    public Job createJob(String recruiterId, String title, String companyName, String description,
                         ArrayList<String> requiredSkills, double minCgpa,
                         int requiredExperience, int numberOfVacancies, String location)
            throws InvalidInputException {

        Job job = createJob(title, companyName, description, requiredSkills, minCgpa, requiredExperience, numberOfVacancies);
        if (recruiterId != null && !recruiterId.trim().isEmpty()) {
            job.setRecruiterId(recruiterId.trim());
        }
        if (location != null && !location.trim().isEmpty()) {
            job.setLocation(location.trim());
        }
        return job;
    }

    /**
     * Displays all available (OPEN) jobs in a clean, formatted table.
     */
    public void displayAllJobs() {
        ArrayList<Job> openJobs = getActiveJobs();
        if (openJobs.isEmpty()) {
            System.out.println("No open job vacancies available at this moment.");
            return;
        }

        System.out.println("--------------------------------------------------------------------------------");
        System.out.println("                         AVAILABLE JOB VACANCIES                                ");
        System.out.println("--------------------------------------------------------------------------------");
        for (Job job : openJobs) {
            job.displaySummary();
        }
        System.out.println("--------------------------------------------------------------------------------");
    }

    /**
     * Searches jobs by title using case-insensitive substring matching.
     */
    public ArrayList<Job> searchJobsByTitle(String searchTitle) {
        ArrayList<Job> matches = new ArrayList<Job>();
        if (searchTitle == null || searchTitle.trim().isEmpty()) {
            return matches;
        }

        String query = searchTitle.trim().toLowerCase();
        for (Job job : jobsList) {
            if (job.getTitle() != null && job.getTitle().toLowerCase().contains(query)) {
                matches.add(job);
            }
        }
        return matches;
    }

    /**
     * Searches jobs by required technical skill (case-insensitive).
     */
    public ArrayList<Job> searchJobsBySkill(String searchSkill) {
        ArrayList<Job> matches = new ArrayList<Job>();
        if (searchSkill == null || searchSkill.trim().isEmpty()) {
            return matches;
        }

        String query = searchSkill.trim().toLowerCase();
        for (Job job : jobsList) {
            ArrayList<String> skills = job.getRequiredSkills();
            for (String skill : skills) {
                if (skill != null && skill.toLowerCase().contains(query)) {
                    matches.add(job);
                    break; // Matched this job, avoid duplicate addition
                }
            }
        }
        return matches;
    }

    /**
     * Updates an existing job's details.
     */
    public boolean updateJob(String jobId, String title, String description,
                             ArrayList<String> requiredSkills, double minCgpa,
                             int requiredExperience, int numberOfVacancies, String status)
            throws InvalidInputException {

        Job job = getJobById(jobId);
        if (job == null) {
            return false;
        }

        if (!InputValidator.isNonEmpty(title)) {
            throw new InvalidInputException("Job title cannot be empty.");
        }
        if (!InputValidator.isValidCgpa(minCgpa)) {
            throw new InvalidInputException("Minimum CGPA must be between 0.0 and 10.0.");
        }
        if (!InputValidator.isValidExperience(requiredExperience)) {
            throw new InvalidInputException("Required experience must be between 0 and 50 years.");
        }
        if (numberOfVacancies <= 0) {
            throw new InvalidInputException("Number of vacancies must be at least 1.");
        }

        job.setTitle(title.trim());
        job.setDescription(description != null ? description.trim() : "");
        if (requiredSkills != null && !requiredSkills.isEmpty()) {
            job.setRequiredSkills(requiredSkills);
        }
        job.setMinCgpa(minCgpa);
        job.setRequiredExperience(requiredExperience);
        job.setNumberOfVacancies(numberOfVacancies);
        if (status != null && !status.trim().isEmpty()) {
            job.setStatus(status.trim().toUpperCase());
        }
        return true;
    }

    /**
     * Finds a job by its unique Job ID.
     */
    public Job getJobById(String jobId) {
        if (jobId == null || jobId.trim().isEmpty()) {
            return null;
        }
        String cleanId = jobId.trim().toUpperCase();
        for (Job job : jobsList) {
            if (job.getJobId().equalsIgnoreCase(cleanId)) {
                return job;
            }
        }
        return null;
    }

    /**
     * Returns all jobs in the system.
     */
    public ArrayList<Job> getAllJobs() {
        return new ArrayList<Job>(jobsList);
    }

    /**
     * Returns only open / active jobs.
     */
    public ArrayList<Job> getActiveJobs() {
        ArrayList<Job> activeList = new ArrayList<Job>();
        for (Job job : jobsList) {
            if (job.isActive()) {
                activeList.add(job);
            }
        }
        return activeList;
    }

    /**
     * Returns jobs posted by a specific recruiter.
     */
    public ArrayList<Job> getJobsByRecruiter(String recruiterId) {
        ArrayList<Job> recruiterJobs = new ArrayList<Job>();
        if (recruiterId == null) return recruiterJobs;

        for (Job job : jobsList) {
            if (recruiterId.equalsIgnoreCase(job.getRecruiterId())) {
                recruiterJobs.add(job);
            }
        }
        return recruiterJobs;
    }

    /**
     * Restores jobs from saved state while keeping ID counters synchronized.
     */
    public void restoreJobs(ArrayList<Job> loadedJobs) {
        jobsList.clear();
        if (loadedJobs == null) return;

        for (Job j : loadedJobs) {
            jobsList.add(j);
            try {
                String[] parts = j.getJobId().split("-");
                if (parts.length == 2) {
                    int num = Integer.parseInt(parts[1]);
                    if (num >= jobCounter) {
                        jobCounter = num + 1;
                    }
                }
            } catch (NumberFormatException ignored) {
            }
        }
    }
}
