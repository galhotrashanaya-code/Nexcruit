package reports;

import applications.Application;
import applications.ApplicationManager;
import auth.AuthManager;
import auth.Candidate;
import auth.Recruiter;
import interviews.Interview;
import interviews.InterviewManager;
import jobs.Job;
import jobs.JobManager;
import utils.ApplicationStatus;
import utils.ConsoleUI;

import java.util.ArrayList;

/**
 * Generates recruitment reports and statistics using simple loops and counters.
 * Concepts used:
 * - Simple for-each loops (No complex streams)
 * - Basic counting variables
 * - String formatting (printf)
 */
public class ReportManager {

    private AuthManager authManager;
    private JobManager jobManager;
    private ApplicationManager applicationManager;
    private InterviewManager interviewManager;

    public ReportManager(AuthManager authManager, JobManager jobManager,
                         ApplicationManager applicationManager, InterviewManager interviewManager) {
        this.authManager = authManager;
        this.jobManager = jobManager;
        this.applicationManager = applicationManager;
        this.interviewManager = interviewManager;
    }

    // Prints high-level system summary
    public void printExecutiveSummary() {
        ArrayList<Job> allJobs = jobManager.getAllJobs();
        ArrayList<Application> allApps = applicationManager.getAllApplications();
        ArrayList<Interview> allInterviews = interviewManager.getAllInterviews();
        ArrayList<Candidate> candidates = authManager.getCandidates();
        ArrayList<Recruiter> recruiters = authManager.getRecruiters();

        // Count active vs closed jobs using simple for loop
        int activeJobsCount = 0;
        for (Job job : allJobs) {
            if (job.isActive()) {
                activeJobsCount++;
            }
        }
        int closedJobsCount = allJobs.size() - activeJobsCount;

        // Count applications by status using simple for loop
        int appliedCount = 0;
        int shortlistedCount = 0;
        int interviewScheduledCount = 0;
        int selectedCount = 0;
        int rejectedCount = 0;

        for (Application app : allApps) {
            if (app.getStatus() == ApplicationStatus.APPLIED || app.getStatus() == ApplicationStatus.UNDER_REVIEW) {
                appliedCount++;
            } else if (app.getStatus() == ApplicationStatus.SHORTLISTED) {
                shortlistedCount++;
            } else if (app.getStatus() == ApplicationStatus.INTERVIEW_SCHEDULED) {
                interviewScheduledCount++;
            } else if (app.getStatus() == ApplicationStatus.SELECTED) {
                selectedCount++;
            } else if (app.getStatus() == ApplicationStatus.REJECTED) {
                rejectedCount++;
            }
        }

        ConsoleUI.printHeader("Nexcruit System Recruitment Executive Summary");
        System.out.printf("  Total Registered Candidates : %d%n", candidates.size());
        System.out.printf("  Total Registered Recruiters : %d%n", recruiters.size());
        System.out.printf("  Total Jobs Posted           : %d (Active: %d | Closed: %d)%n",
                allJobs.size(), activeJobsCount, closedJobsCount);
        System.out.printf("  Total Applications Received : %d%n", allApps.size());
        ConsoleUI.printDivider();
        System.out.println("  APPLICATION PIPELINE BREAKDOWN:");
        System.out.printf("    - Applied / Under Screening: %d%n", appliedCount);
        System.out.printf("    - Shortlisted Candidates   : %d%n", shortlistedCount);
        System.out.printf("    - Interview Scheduled      : %d%n", interviewScheduledCount);
        System.out.printf("    - Selected (Offers Made)   : %d%n", selectedCount);
        System.out.printf("    - Rejected / Ineligible    : %d%n", rejectedCount);
        ConsoleUI.printDivider();
        System.out.printf("  Total Interviews Scheduled   : %d%n", allInterviews.size());
        ConsoleUI.printDivider();
    }

    // Prints job-wise application statistics
    public void printJobWiseReport() {
        ArrayList<Job> jobs = jobManager.getAllJobs();
        ConsoleUI.printHeader("Job-Wise Recruitment Performance Report");

        if (jobs.isEmpty()) {
            System.out.println("No jobs currently posted in the system.");
            return;
        }

        System.out.printf("%-10s | %-26s | %-10s | %-11s | %-8s | %-8s | %-9s%n",
                "Job ID", "Job Title", "Total Apps", "Shortlisted", "Selected", "Rejected", "Avg Match");
        ConsoleUI.printDivider();

        for (Job job : jobs) {
            ArrayList<Application> apps = applicationManager.getApplicationsByJob(job.getJobId());
            int total = apps.size();
            int shortlisted = 0;
            int selected = 0;
            int rejected = 0;
            double sumScores = 0.0;

            for (Application a : apps) {
                sumScores += a.getMatchScore();
                if (a.getStatus() == ApplicationStatus.SHORTLISTED) {
                    shortlisted++;
                } else if (a.getStatus() == ApplicationStatus.SELECTED) {
                    selected++;
                } else if (a.getStatus() == ApplicationStatus.REJECTED) {
                    rejected++;
                }
            }

            double avgScore = (total > 0) ? (sumScores / total) : 0.0;
            String shortTitle = job.getTitle();
            if (shortTitle.length() > 26) {
                shortTitle = shortTitle.substring(0, 23) + "...";
            }

            System.out.printf("%-10s | %-26s | %-10d | %-11d | %-8d | %-8d | %6.1f%%%n",
                    job.getJobId(), shortTitle, total, shortlisted, selected, rejected, avgScore);
        }
        ConsoleUI.printDivider();
    }

    // Prints recruiter-specific report
    public void printRecruiterReport(String recruiterId) {
        ArrayList<Job> recruiterJobs = jobManager.getJobsByRecruiter(recruiterId);
        ArrayList<Interview> recruiterInterviews = interviewManager.getInterviewsByRecruiter(recruiterId);

        ConsoleUI.printHeader("Recruiter Performance Summary [" + recruiterId + "]");
        System.out.println("Total Jobs Managed        : " + recruiterJobs.size());

        int totalApplications = 0;
        int totalShortlisted = 0;
        int totalSelected = 0;

        for (Job job : recruiterJobs) {
            ArrayList<Application> apps = applicationManager.getApplicationsByJob(job.getJobId());
            totalApplications += apps.size();
            for (Application a : apps) {
                if (a.getStatus() == ApplicationStatus.SHORTLISTED) totalShortlisted++;
                if (a.getStatus() == ApplicationStatus.SELECTED) totalSelected++;
            }
        }

        System.out.println("Total Applications Handled: " + totalApplications);
        System.out.println("Candidates Shortlisted    : " + totalShortlisted);
        System.out.println("Offers Accepted/Selected  : " + totalSelected);
        System.out.println("Total Interviews Logged   : " + recruiterInterviews.size());
        ConsoleUI.printDivider();
    }
}
