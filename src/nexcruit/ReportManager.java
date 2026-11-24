package nexcruit;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Generates recruitment analytics and statistical reports for Nexcruit.
 * Developed by: Sanchi (File Handling & Reports Module - Evaluation 1)
 *
 * Scope for Evaluation 1:
 * - Total job vacancies.
 * - Total applications submitted.
 * - Application status counts (Shortlisted, Selected, Rejected, Under Review).
 * - Basic selection ratio calculation.
 * - Job-wise application statistics breakdown.
 * - Beginner-friendly: simple loops, counters, if-else logic, no complex streams.
 *
 * Concepts used:
 * - Encapsulation
 * - Classes and Object Relationships (uses JobManager and ApplicationManager)
 * - ArrayList collections
 * - Control structures: for-each loops, if-else conditions
 * - Mathematical ratio calculation with division-by-zero protection
 * - Serializable
 */
public class ReportManager implements Serializable {
    private static final long serialVersionUID = 1L;

    private JobManager jobManager;
    private ApplicationManager applicationManager;

    /**
     * Default Constructor.
     */
    public ReportManager() {
        this.jobManager = null;
        this.applicationManager = null;
    }

    /**
     * Parameterized Constructor accepting live JobManager and ApplicationManager references.
     *
     * @param jobManager Live JobManager containing job postings
     * @param applicationManager Live ApplicationManager containing applications
     */
    public ReportManager(JobManager jobManager, ApplicationManager applicationManager) {
        this.jobManager = jobManager;
        this.applicationManager = applicationManager;
    }

    // Setters allowing dynamic assignment
    public void setJobManager(JobManager jobManager) {
        this.jobManager = jobManager;
    }

    public void setApplicationManager(ApplicationManager applicationManager) {
        this.applicationManager = applicationManager;
    }

    // =========================================================================
    // STATISTICAL METRICS (Calculated dynamically using simple loops)
    // =========================================================================

    /**
     * Returns total number of jobs posted in the system.
     */
    public int getTotalJobs() {
        if (jobManager == null) {
            return 0;
        }
        ArrayList<Job> jobs = jobManager.getAllJobs();
        return jobs != null ? jobs.size() : 0;
    }

    /**
     * Returns total number of vacancies across all job postings.
     * Calculated using a simple for-each loop.
     */
    public int getTotalVacancies() {
        if (jobManager == null) {
            return 0;
        }
        ArrayList<Job> jobs = jobManager.getAllJobs();
        if (jobs == null) {
            return 0;
        }

        int totalVacancies = 0;
        for (Job job : jobs) {
            if (job != null) {
                totalVacancies += job.getNumberOfVacancies();
            }
        }
        return totalVacancies;
    }

    /**
     * Returns total number of applications submitted across all jobs.
     */
    public int getTotalApplications() {
        if (applicationManager == null) {
            return 0;
        }
        ArrayList<Application> apps = applicationManager.getAllApplications();
        return apps != null ? apps.size() : 0;
    }

    /**
     * Returns the count of applications currently in 'SHORTLISTED' status.
     * Uses a simple loop with case-insensitive check.
     */
    public int getShortlistedCount() {
        if (applicationManager == null) {
            return 0;
        }
        ArrayList<Application> apps = applicationManager.getAllApplications();
        if (apps == null) {
            return 0;
        }

        int count = 0;
        for (Application app : apps) {
            if (app != null && app.getStatus() != null) {
                if (app.getStatus().equalsIgnoreCase("SHORTLISTED")) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * Returns the count of applications currently in 'SELECTED' status.
     */
    public int getSelectedCount() {
        if (applicationManager == null) {
            return 0;
        }
        ArrayList<Application> apps = applicationManager.getAllApplications();
        if (apps == null) {
            return 0;
        }

        int count = 0;
        for (Application app : apps) {
            if (app != null && app.getStatus() != null) {
                if (app.getStatus().equalsIgnoreCase("SELECTED")) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * Returns the count of applications currently in 'REJECTED' status.
     */
    public int getRejectedCount() {
        if (applicationManager == null) {
            return 0;
        }
        ArrayList<Application> apps = applicationManager.getAllApplications();
        if (apps == null) {
            return 0;
        }

        int count = 0;
        for (Application app : apps) {
            if (app != null && app.getStatus() != null) {
                if (app.getStatus().equalsIgnoreCase("REJECTED")) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * Returns the count of applications currently under review or freshly applied.
     */
    public int getUnderReviewCount() {
        if (applicationManager == null) {
            return 0;
        }
        ArrayList<Application> apps = applicationManager.getAllApplications();
        if (apps == null) {
            return 0;
        }

        int count = 0;
        for (Application app : apps) {
            if (app != null && app.getStatus() != null) {
                String s = app.getStatus();
                if (s.equalsIgnoreCase("APPLIED") || s.equalsIgnoreCase("UNDER_REVIEW")) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * Calculates the Selection Ratio as a percentage.
     *
     * Formula:
     * Selection Ratio = (Selected Candidates / Total Applications) * 100
     *
     * Handles division-by-zero safely: returns 0.0% if total applications is zero.
     *
     * @return Selection ratio percentage rounded to 1 decimal place (e.g., 10.0%)
     */
    public double getSelectionRatio() {
        int totalApps = getTotalApplications();
        if (totalApps == 0) {
            return 0.0;
        }

        int selected = getSelectedCount();
        double ratio = ((double) selected / totalApps) * 100.0;

        // Round to 1 decimal place
        return Math.round(ratio * 10.0) / 10.0;
    }

    // =========================================================================
    // JOB-WISE APPLICATION METRICS
    // =========================================================================

    /**
     * Counts applications received for a specific job ID.
     */
    public int getApplicationsCountForJob(String jobId) {
        if (applicationManager == null || jobId == null) {
            return 0;
        }
        ArrayList<Application> apps = applicationManager.getApplicationsByJob(jobId);
        return apps != null ? apps.size() : 0;
    }

    /**
     * Counts selected candidates for a specific job ID.
     */
    public int getSelectedCountForJob(String jobId) {
        if (applicationManager == null || jobId == null) {
            return 0;
        }
        ArrayList<Application> apps = applicationManager.getApplicationsByJob(jobId);
        if (apps == null) return 0;

        int count = 0;
        for (Application app : apps) {
            if (app != null && "SELECTED".equalsIgnoreCase(app.getStatus())) {
                count++;
            }
        }
        return count;
    }

    /**
     * Counts shortlisted candidates for a specific job ID.
     */
    public int getShortlistedCountForJob(String jobId) {
        if (applicationManager == null || jobId == null) {
            return 0;
        }
        ArrayList<Application> apps = applicationManager.getApplicationsByJob(jobId);
        if (apps == null) return 0;

        int count = 0;
        for (Application app : apps) {
            if (app != null && "SHORTLISTED".equalsIgnoreCase(app.getStatus())) {
                count++;
            }
        }
        return count;
    }

    /**
     * Counts rejected candidates for a specific job ID.
     */
    public int getRejectedCountForJob(String jobId) {
        if (applicationManager == null || jobId == null) {
            return 0;
        }
        ArrayList<Application> apps = applicationManager.getApplicationsByJob(jobId);
        if (apps == null) return 0;

        int count = 0;
        for (Application app : apps) {
            if (app != null && "REJECTED".equalsIgnoreCase(app.getStatus())) {
                count++;
            }
        }
        return count;
    }

    // =========================================================================
    // FORMATTED REPORT GENERATION
    // =========================================================================

    /**
     * Prints the primary Recruitment Summary Report to the terminal.
     * Matches the required specification layout.
     */
    public void generateRecruitmentReport() {
        int totalJobs = getTotalJobs();
        int totalVacancies = getTotalVacancies();
        int totalApplications = getTotalApplications();
        int underReview = getUnderReviewCount();
        int shortlisted = getShortlistedCount();
        int selected = getSelectedCount();
        int rejected = getRejectedCount();
        double ratio = getSelectionRatio();

        System.out.println("========== NEXCRUIT RECRUITMENT REPORT ==========");
        System.out.println();
        System.out.println("Total Jobs: " + totalJobs);
        System.out.println("Total Vacancies: " + totalVacancies);
        System.out.println("Total Applications: " + totalApplications);
        System.out.println("Under Review: " + underReview);
        System.out.println("Shortlisted: " + shortlisted);
        System.out.println("Selected: " + selected);
        System.out.println("Rejected: " + rejected);
        System.out.println();
        System.out.println("Selection Ratio: " + String.format("%.1f", ratio) + "%");
        System.out.println();
        System.out.println("================================================");
    }

    /**
     * Overloaded method allowing direct generation by passing managers.
     */
    public void generateRecruitmentReport(JobManager jm, ApplicationManager am) {
        this.jobManager = jm;
        this.applicationManager = am;
        generateRecruitmentReport();
    }

    /**
     * Prints detailed Job-wise application breakdown to the console.
     */
    public void generateJobWiseReport() {
        if (jobManager == null) {
            System.out.println("[ReportManager] No JobManager assigned.");
            return;
        }

        ArrayList<Job> allJobs = jobManager.getAllJobs();
        if (allJobs == null || allJobs.isEmpty()) {
            System.out.println("No jobs currently posted in the system.");
            return;
        }

        System.out.println("========================================================================================");
        System.out.println("                       NEXCRUIT - JOB-WISE APPLICATION REPORT                           ");
        System.out.println("========================================================================================");
        System.out.printf("%-10s | %-24s | %-8s | %-6s | %-8s | %-8s | %-8s\n",
                "JOB ID", "JOB TITLE", "VACANCY", "APPS", "SHORTL.", "SELECT.", "REJECT.");
        System.out.println("----------------------------------------------------------------------------------------");

        for (Job job : allJobs) {
            if (job != null) {
                String jobId = job.getJobId();
                String title = job.getTitle();
                if (title != null && title.length() > 24) {
                    title = title.substring(0, 21) + "...";
                }
                int vacancies = job.getNumberOfVacancies();
                int apps = getApplicationsCountForJob(jobId);
                int shortl = getShortlistedCountForJob(jobId);
                int sel = getSelectedCountForJob(jobId);
                int rej = getRejectedCountForJob(jobId);

                System.out.printf("%-10s | %-24s | %-8d | %-6d | %-8d | %-8d | %-8d\n",
                        jobId, title, vacancies, apps, shortl, sel, rej);
            }
        }
        System.out.println("========================================================================================");
    }

    /**
     * Overloaded method allowing job-wise report generation by passing managers.
     */
    public void generateJobWiseReport(JobManager jm, ApplicationManager am) {
        this.jobManager = jm;
        this.applicationManager = am;
        generateJobWiseReport();
    }

    /**
     * Prints an application summary for a specific candidate.
     */
    public void generateCandidateStatusReport(String candidateId) {
        if (applicationManager == null || candidateId == null) {
            return;
        }

        ArrayList<Application> candApps = applicationManager.getApplicationsByCandidate(candidateId);
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println(" Application Summary for Candidate ID: " + candidateId);
        System.out.println(" Total Applications Submitted: " + candApps.size());
        System.out.println("--------------------------------------------------------------------------------");

        if (candApps.isEmpty()) {
            System.out.println(" No applications submitted yet.");
        } else {
            for (Application app : candApps) {
                System.out.println(" - " + app.getApplicationId() + " | Job: " +
                        (app.getJob() != null ? app.getJob().getTitle() : "N/A") +
                        " | Status: " + app.getStatus() + " | Score: " + app.getMatchScore() + "%");
            }
        }
        System.out.println("--------------------------------------------------------------------------------");
    }
}
