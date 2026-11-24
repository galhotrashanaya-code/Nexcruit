package applications;

import exceptions.DuplicateApplicationException;
import exceptions.InvalidInputException;
import utils.ApplicationStatus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;

/**
 * Manages the candidate application workflow and enforces business rules
 * such as strictly preventing duplicate applications for the same job vacancy.
 * Concepts used:
 * - LinkedHashMap and ArrayList
 * - Custom Exception (DuplicateApplicationException)
 * - Simple loop and if-else logic
 */
public class ApplicationManager implements Serializable {
    private static final long serialVersionUID = 1L;

    private LinkedHashMap<String, Application> applicationsMap;
    private int applicationCounter;

    public ApplicationManager() {
        this.applicationsMap = new LinkedHashMap<String, Application>();
        this.applicationCounter = 101;
    }

    // Checks if candidate has already applied for this job using a simple for-each loop
    public boolean hasCandidateApplied(String candidateId, String jobId) {
        if (candidateId == null || jobId == null) {
            return false;
        }
        for (Application app : applicationsMap.values()) {
            if (app.getCandidateId().equalsIgnoreCase(candidateId.trim()) &&
                app.getJobId().equalsIgnoreCase(jobId.trim())) {
                return true;
            }
        }
        return false;
    }

    // Submits an application for a candidate, throwing DuplicateApplicationException if already applied
    public Application applyForJob(String candidateId, String jobId)
            throws DuplicateApplicationException, InvalidInputException {

        if (candidateId == null || candidateId.trim().isEmpty()) {
            throw new InvalidInputException("Candidate ID is required.");
        }
        if (jobId == null || jobId.trim().isEmpty()) {
            throw new InvalidInputException("Job ID is required.");
        }

        String cleanCandidateId = candidateId.trim().toUpperCase();
        String cleanJobId = jobId.trim().toUpperCase();

        // Check for duplicate application
        if (hasCandidateApplied(cleanCandidateId, cleanJobId)) {
            throw new DuplicateApplicationException("Candidate " + cleanCandidateId +
                    " has already applied for Job " + cleanJobId + ".");
        }

        String appId = "APP-" + applicationCounter;
        applicationCounter++;

        Application application = new Application(appId, cleanJobId, cleanCandidateId);
        applicationsMap.put(appId, application);
        return application;
    }

    // Retrieve an application by its unique ID
    public Application getApplicationById(String applicationId) {
        if (applicationId == null) return null;
        return applicationsMap.get(applicationId.trim().toUpperCase());
    }

    // Get all applications submitted by a specific candidate
    public ArrayList<Application> getApplicationsByCandidate(String candidateId) {
        ArrayList<Application> list = new ArrayList<Application>();
        if (candidateId == null) return list;

        for (Application app : applicationsMap.values()) {
            if (app.getCandidateId().equalsIgnoreCase(candidateId.trim())) {
                list.add(app);
            }
        }
        return list;
    }

    // Get all applications submitted for a specific job
    public ArrayList<Application> getApplicationsByJob(String jobId) {
        ArrayList<Application> list = new ArrayList<Application>();
        if (jobId == null) return list;

        for (Application app : applicationsMap.values()) {
            if (app.getJobId().equalsIgnoreCase(jobId.trim())) {
                list.add(app);
            }
        }
        return list;
    }

    // Updates application status and remarks
    public boolean updateStatus(String applicationId, ApplicationStatus newStatus, String remarks) {
        Application app = getApplicationById(applicationId);
        if (app != null && newStatus != null) {
            app.setStatus(newStatus);
            if (remarks != null && !remarks.trim().isEmpty()) {
                app.setRemarks(remarks.trim());
            }
            return true;
        }
        return false;
    }

    // Returns all applications
    public ArrayList<Application> getAllApplications() {
        return new ArrayList<Application>(applicationsMap.values());
    }

    // Restores applications from file state
    public void restoreApplications(Collection<Application> loadedApplications) {
        applicationsMap.clear();
        if (loadedApplications == null) return;

        for (Application app : loadedApplications) {
            applicationsMap.put(app.getApplicationId(), app);
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
