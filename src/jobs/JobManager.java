package jobs;

import exceptions.InvalidInputException;
import utils.InputValidator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;

/**
 * Manages the lifecycle of job vacancies (creation, viewing, updating, deactivating).
 * Concepts used:
 * - LinkedHashMap (preserves insertion order of jobs)
 * - ArrayList (to return lists of jobs)
 * - Custom Exceptions (InvalidInputException)
 * - Simple loops and if-else logic
 */
public class JobManager implements Serializable {
    private static final long serialVersionUID = 1L;

    private LinkedHashMap<String, Job> jobsMap;
    private int jobCounter;

    public JobManager() {
        this.jobsMap = new LinkedHashMap<String, Job>();
        this.jobCounter = 101;
    }

    // Creates a new job posting with validation
    public Job createJob(String recruiterId, String title, String company,
                         String description, ArrayList<String> requiredSkills,
                         double minCgpa, int minExperience, String location)
            throws InvalidInputException {

        if (!InputValidator.isNonEmpty(title)) {
            throw new InvalidInputException("Job title cannot be empty.");
        }
        if (!InputValidator.isNonEmpty(company)) {
            throw new InvalidInputException("Company name cannot be empty.");
        }
        if (!InputValidator.isValidCgpa(minCgpa)) {
            throw new InvalidInputException("Minimum CGPA must be between 0.0 and 10.0.");
        }
        if (!InputValidator.isValidExperience(minExperience)) {
            throw new InvalidInputException("Minimum experience must be between 0 and 50 years.");
        }
        if (requiredSkills == null || requiredSkills.isEmpty()) {
            throw new InvalidInputException("At least one required skill must be specified.");
        }

        String jobId = "JOB-" + jobCounter;
        jobCounter++;

        Job job = new Job(jobId, recruiterId, title.trim(), company.trim(),
                description != null ? description.trim() : "",
                requiredSkills, minCgpa, minExperience,
                location != null ? location.trim() : "Remote / Hybrid");

        jobsMap.put(jobId, job);
        return job;
    }

    // Updates existing job specifications
    public boolean updateJob(String jobId, String title, String description,
                             ArrayList<String> requiredSkills, double minCgpa,
                             int minExperience, String location, boolean active)
            throws InvalidInputException {

        Job job = jobsMap.get(jobId);
        if (job == null) {
            return false;
        }

        if (!InputValidator.isNonEmpty(title)) {
            throw new InvalidInputException("Job title cannot be empty.");
        }
        if (!InputValidator.isValidCgpa(minCgpa)) {
            throw new InvalidInputException("Minimum CGPA must be between 0.0 and 10.0.");
        }
        if (!InputValidator.isValidExperience(minExperience)) {
            throw new InvalidInputException("Minimum experience must be between 0 and 50 years.");
        }

        job.setTitle(title.trim());
        job.setDescription(description != null ? description.trim() : "");
        if (requiredSkills != null && !requiredSkills.isEmpty()) {
            job.setRequiredSkills(requiredSkills);
        }
        job.setMinCgpa(minCgpa);
        job.setMinExperience(minExperience);
        if (location != null && !location.trim().isEmpty()) {
            job.setLocation(location.trim());
        }
        job.setActive(active);
        return true;
    }

    // Soft delete: deactivates job so candidate application history remains intact
    public boolean deleteJob(String jobId) {
        Job job = jobsMap.get(jobId);
        if (job != null) {
            job.setActive(false);
            return true;
        }
        return false;
    }

    // Lookup job by ID
    public Job getJobById(String jobId) {
        if (jobId == null) return null;
        return jobsMap.get(jobId.trim().toUpperCase());
    }

    // Returns all jobs
    public ArrayList<Job> getAllJobs() {
        return new ArrayList<Job>(jobsMap.values());
    }

    // Returns only active jobs that are open for applications
    public ArrayList<Job> getActiveJobs() {
        ArrayList<Job> activeList = new ArrayList<Job>();
        for (Job j : jobsMap.values()) {
            if (j.isActive()) {
                activeList.add(j);
            }
        }
        return activeList;
    }

    // Returns jobs created by a specific recruiter
    public ArrayList<Job> getJobsByRecruiter(String recruiterId) {
        ArrayList<Job> list = new ArrayList<Job>();
        if (recruiterId == null) return list;

        for (Job j : jobsMap.values()) {
            if (recruiterId.equalsIgnoreCase(j.getRecruiterId())) {
                list.add(j);
            }
        }
        return list;
    }

    // Restores jobs from saved file state
    public void restoreJobs(Collection<Job> loadedJobs) {
        jobsMap.clear();
        if (loadedJobs == null) return;

        for (Job j : loadedJobs) {
            jobsMap.put(j.getJobId(), j);
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
