package nexcruit;

import auth.Candidate;
import exceptions.DuplicateApplicationException;
import exceptions.InvalidInputException;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Manages the submission, tracking, duplicate prevention, and status updates of applications.
 * Developed by: Himani (Job & Application Management Module)
 *
 * Concepts used:
 * - ArrayList<Application>
 * - Custom Exceptions (DuplicateApplicationException, InvalidInputException)
 * - Object relationships (Candidate and Job references)
 * - Simple loops and if-else logic
 * - Serializable
 */
public class ApplicationManager implements Serializable {
    private static final long serialVersionUID = 1L;

    // Himani's requirement: Maintain application records using ArrayList<Application>
    private ArrayList<Application> applicationsList;
    private int applicationCounter;

    public ApplicationManager() {
        this.applicationsList = new ArrayList<Application>();
        this.applicationCounter = 101;
    }

    /**
     * Generates a unique, auto-incrementing Application ID (e.g. APP-101, APP-102).
     */
    public String generateApplicationId() {
        String id = "APP-" + applicationCounter;
        applicationCounter++;
        return id;
    }

    /**
     * Checks if a candidate has already applied for a specific job.
     * Prevents duplicate applications.
     */
    public boolean hasCandidateApplied(Candidate candidate, Job job) {
        if (candidate == null || job == null) {
            return false;
        }
        return hasCandidateApplied(candidate.getUserId(), job.getJobId());
    }

    /**
     * Overloaded helper checking duplicates using ID strings.
     */
    public boolean hasCandidateApplied(String candidateId, String jobId) {
        if (candidateId == null || jobId == null) {
            return false;
        }
        for (Application app : applicationsList) {
            if (app.getCandidateId().equalsIgnoreCase(candidateId.trim()) &&
                app.getJobId().equalsIgnoreCase(jobId.trim())) {
                return true; // Duplicate found!
            }
        }
        return false;
    }

    /**
     * Submits an application for a candidate for a specific job.
     * Strictly prevents duplicate applications by throwing DuplicateApplicationException.
     */
    public Application applyForJob(Candidate candidate, Job job)
            throws DuplicateApplicationException, InvalidInputException {

        if (candidate == null) {
            throw new InvalidInputException("Candidate reference cannot be null.");
        }
        if (job == null) {
            throw new InvalidInputException("Job reference cannot be null.");
        }
        if (!job.isActive()) {
            throw new InvalidInputException("Cannot apply: Job " + job.getJobId() + " is currently CLOSED.");
        }

        // Check for duplicate application
        if (hasCandidateApplied(candidate, job)) {
            throw new DuplicateApplicationException("Candidate " + candidate.getName() + " (" + candidate.getUserId() +
                    ") has already submitted an application for Job " + job.getTitle() + " (" + job.getJobId() + ").");
        }

        String appId = generateApplicationId();
        Application application = new Application(appId, candidate, job);
        applicationsList.add(application);
        return application;
    }

    /**
     * Displays all applications in the system.
     */
    public void displayAllApplications() {
        if (applicationsList.isEmpty()) {
            System.out.println("No applications submitted in the system yet.");
            return;
        }

        System.out.println("--------------------------------------------------------------------------------");
        System.out.println("                           ALL SUBMITTED APPLICATIONS                           ");
        System.out.println("--------------------------------------------------------------------------------");
        for (Application app : applicationsList) {
            app.displaySummary();
        }
        System.out.println("--------------------------------------------------------------------------------");
    }

    /**
     * Displays applications submitted for a particular job.
     */
    public void displayApplicationsForJob(String jobId) {
        ArrayList<Application> jobApps = getApplicationsByJob(jobId);
        if (jobApps.isEmpty()) {
            System.out.println("No applications found for Job ID: " + jobId);
            return;
        }

        System.out.println("--------------------------------------------------------------------------------");
        System.out.println("                    APPLICATIONS FOR JOB: " + jobId);
        System.out.println("--------------------------------------------------------------------------------");
        for (Application app : jobApps) {
            app.displaySummary();
        }
        System.out.println("--------------------------------------------------------------------------------");
    }

    /**
     * Displays applications submitted by a particular candidate.
     */
    public void displayApplicationsByCandidate(String candidateId) {
        ArrayList<Application> candApps = getApplicationsByCandidate(candidateId);
        if (candApps.isEmpty()) {
            System.out.println("No applications found for Candidate ID: " + candidateId);
            return;
        }

        System.out.println("--------------------------------------------------------------------------------");
        System.out.println("                 APPLICATIONS SUBMITTED BY: " + candidateId);
        System.out.println("--------------------------------------------------------------------------------");
        for (Application app : candApps) {
            app.displaySummary();
        }
        System.out.println("--------------------------------------------------------------------------------");
    }

    /**
     * Returns a list of applications for a particular job.
     */
    public ArrayList<Application> getApplicationsByJob(String jobId) {
        ArrayList<Application> list = new ArrayList<Application>();
        if (jobId == null || jobId.trim().isEmpty()) {
            return list;
        }

        String cleanJobId = jobId.trim().toUpperCase();
        for (Application app : applicationsList) {
            if (app.getJobId().equalsIgnoreCase(cleanJobId)) {
                list.add(app);
            }
        }
        return list;
    }

    /**
     * Overloaded helper taking a Job object.
     */
    public ArrayList<Application> getApplicationsByJob(Job job) {
        if (job == null) return new ArrayList<Application>();
        return getApplicationsByJob(job.getJobId());
    }

    /**
     * Returns a list of applications submitted by a particular candidate.
     */
    public ArrayList<Application> getApplicationsByCandidate(String candidateId) {
        ArrayList<Application> list = new ArrayList<Application>();
        if (candidateId == null || candidateId.trim().isEmpty()) {
            return list;
        }

        String cleanCandId = candidateId.trim().toUpperCase();
        for (Application app : applicationsList) {
            if (app.getCandidateId().equalsIgnoreCase(cleanCandId)) {
                list.add(app);
            }
        }
        return list;
    }

    /**
     * Overloaded helper taking a Candidate object.
     */
    public ArrayList<Application> getApplicationsByCandidate(Candidate candidate) {
        if (candidate == null) return new ArrayList<Application>();
        return getApplicationsByCandidate(candidate.getUserId());
    }

    /**
     * Finds an application by its unique ID.
     */
    public Application getApplicationById(String applicationId) {
        if (applicationId == null || applicationId.trim().isEmpty()) {
            return null;
        }

        String cleanId = applicationId.trim().toUpperCase();
        for (Application app : applicationsList) {
            if (app.getApplicationId().equalsIgnoreCase(cleanId)) {
                return app;
            }
        }
        return null;
    }

    /**
     * Updates an application's lifecycle status and remarks.
     */
    public boolean updateApplicationStatus(String applicationId, String newStatus, String remarks) {
        Application app = getApplicationById(applicationId);
        if (app != null && newStatus != null && !newStatus.trim().isEmpty()) {
            app.setStatus(newStatus.trim().toUpperCase());
            if (remarks != null && !remarks.trim().isEmpty()) {
                app.setRemarks(remarks.trim());
            }
            return true;
        }
        return false;
    }

    /**
     * Returns all applications in the system.
     */
    public ArrayList<Application> getAllApplications() {
        return new ArrayList<Application>(applicationsList);
    }

    /**
     * Restores applications from saved file state.
     */
    public void restoreApplications(ArrayList<Application> loadedApplications) {
        applicationsList.clear();
        if (loadedApplications == null) return;

        for (Application app : loadedApplications) {
            applicationsList.add(app);
            try {
                String[] parts = app.getApplicationId().split("-");
                if (parts.length == 2) {
                    int num = Integer.parseInt(parts[1]);
                    if (num >= applicationCounter) {
                        applicationCounter = num + 1;
                    }
                }
            } catch (NumberFormatException ignored) {
            }
        }
    }
}
