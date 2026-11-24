package nexcruit;

import auth.Candidate;
import auth.Recruiter;
import exceptions.DuplicateApplicationException;
import exceptions.InvalidInputException;

import java.util.ArrayList;

/**
 * Verification test suite for Himani's Job & Application Management Module.
 * Tests all 11 required test cases from the evaluation rubric.
 */
public class JobApplicationTest {

    private static int passed = 0;
    private static int total = 0;

    public static void main(String[] args) {
        System.out.println("================================================================================");
        System.out.println("     HIMANI'S JOB & APPLICATION MANAGEMENT MODULE - VERIFICATION SUITE          ");
        System.out.println("================================================================================");

        JobManager jobManager = new JobManager();
        ApplicationManager appManager = new ApplicationManager();

        // Sample skills
        ArrayList<String> javaSkills = new ArrayList<String>();
        javaSkills.add("Java");
        javaSkills.add("Spring Boot");
        javaSkills.add("SQL");

        ArrayList<String> pythonSkills = new ArrayList<String>();
        pythonSkills.add("Python");
        pythonSkills.add("Django");
        pythonSkills.add("Machine Learning");

        // 1. Create a new job
        System.out.println("\n[Test 1] Create a new job");
        Job job1 = null;
        Job job2 = null;
        try {
            job1 = jobManager.createJob("REC-101", "Backend Software Engineer", "Google",
                    "Develop cloud microservices in Java", javaSkills, 7.5, 1, 5, "Bangalore");
            job2 = jobManager.createJob("REC-101", "Data Scientist", "Google",
                    "Build ML predictive models", pythonSkills, 8.0, 2, 3, "Hyderabad");
            assertCondition("Job 1 created with ID JOB-101", "JOB-101".equals(job1.getJobId()));
            assertCondition("Job 2 created with ID JOB-102", "JOB-102".equals(job2.getJobId()));
            assertCondition("Job 1 has 5 vacancies", job1.getNumberOfVacancies() == 5);
        } catch (Exception e) {
            assertCondition("Job creation failed: " + e.getMessage(), false);
        }

        // 2. Display all jobs
        System.out.println("\n[Test 2] Display all available jobs");
        jobManager.displayAllJobs();
        assertCondition("Total active jobs is 2", jobManager.getActiveJobs().size() == 2);

        // 3. Search jobs by title
        System.out.println("\n[Test 3] Search jobs by title");
        ArrayList<Job> titleResults = jobManager.searchJobsByTitle("backend");
        assertCondition("Found 1 job searching for 'backend'", titleResults.size() == 1);
        assertCondition("Found correct job title", titleResults.get(0).getTitle().contains("Backend"));

        // 4. Search jobs by skill
        System.out.println("\n[Test 4] Search jobs by skill");
        ArrayList<Job> skillResults = jobManager.searchJobsBySkill("Java");
        assertCondition("Found 1 job requiring skill 'Java'", skillResults.size() == 1);
        assertCondition("Found correct job by skill", "JOB-101".equals(skillResults.get(0).getJobId()));

        // 5. Apply for a job using Chesta's actual Candidate class
        System.out.println("\n[Test 5] Apply for a job using Candidate reference");
        ArrayList<String> candSkills = new ArrayList<String>();
        candSkills.add("Java");
        candSkills.add("SQL");
        Candidate candidate1 = new Candidate("CAN-101", "Himani Sharma", "himani@gmail.com", "pass123",
                "9876543210", "B.Tech Computer Science", 8.5, 1, candSkills);

        Application app1 = null;
        try {
            app1 = appManager.applyForJob(candidate1, job1);
            assertCondition("Application created with ID APP-101", "APP-101".equals(app1.getApplicationId()));
            assertCondition("Application stores actual Candidate reference", app1.getCandidate() == candidate1);
            assertCondition("Application stores actual Job reference", app1.getJob() == job1);
            assertCondition("Initial status is APPLIED", "APPLIED".equals(app1.getStatus()));
        } catch (Exception e) {
            assertCondition("Application failed: " + e.getMessage(), false);
        }

        // 6. Prevent duplicate applications
        System.out.println("\n[Test 6] Prevent duplicate applications");
        boolean caughtDuplicate = false;
        try {
            appManager.applyForJob(candidate1, job1);
        } catch (DuplicateApplicationException e) {
            caughtDuplicate = true;
        } catch (Exception e) {
            caughtDuplicate = false;
        }
        assertCondition("Duplicate application prevented with DuplicateApplicationException", caughtDuplicate);

        // 7. Display candidate applications
        System.out.println("\n[Test 7] Display applications submitted by a candidate");
        ArrayList<Application> candApps = appManager.getApplicationsByCandidate(candidate1);
        assertCondition("Candidate has 1 application", candApps.size() == 1);
        appManager.displayApplicationsByCandidate(candidate1.getUserId());

        // 8. Display job-wise applications
        System.out.println("\n[Test 8] Display applications for a particular job");
        ArrayList<Application> jobApps = appManager.getApplicationsByJob(job1);
        assertCondition("Job-101 has 1 application", jobApps.size() == 1);
        appManager.displayApplicationsForJob(job1.getJobId());

        // 9. Update application status
        System.out.println("\n[Test 9] Update application status");
        boolean updated = appManager.updateApplicationStatus(app1.getApplicationId(), "SHORTLISTED", "Eligible candidate shortlisted for interview.");
        assertCondition("Application status update succeeded", updated);
        assertCondition("Application status is now SHORTLISTED", "SHORTLISTED".equals(app1.getStatus()));

        // 10. Handle invalid inputs
        System.out.println("\n[Test 10] Handle invalid inputs");
        boolean caughtInvalidTitle = false;
        try {
            jobManager.createJob("", "Google", "Desc", javaSkills, 7.0, 1, 2);
        } catch (InvalidInputException e) {
            caughtInvalidTitle = true;
        }
        assertCondition("Empty job title rejected with InvalidInputException", caughtInvalidTitle);

        boolean caughtInvalidVacancies = false;
        try {
            jobManager.createJob("Title", "Google", "Desc", javaSkills, 7.0, 1, 0);
        } catch (InvalidInputException e) {
            caughtInvalidVacancies = true;
        }
        assertCondition("Zero vacancies rejected with InvalidInputException", caughtInvalidVacancies);

        // 11. Verify integration with Candidate and Recruiter
        System.out.println("\n[Test 11] Verify integration with Candidate and Recruiter");
        Recruiter recruiter = new Recruiter("REC-101", "Sarah Jenkins", "sarah@google.com", "pass456",
                "Google", "Talent Acquisition", "9123456780");
        assertCondition("Job links to Recruiter ID", recruiter.getUserId().equals(job1.getRecruiterId()));
        assertCondition("Candidate name accessible from Application", "Himani Sharma".equals(app1.getCandidate().getName()));
        assertCondition("Candidate CGPA accessible from Application", app1.getCandidate().getCgpa() == 8.5);

        System.out.println("================================================================================");
        System.out.printf("  RESULTS: %d / %d Tests Passed (%d%%)%n", passed, total, (passed * 100 / total));
        System.out.println("================================================================================");

        if (passed == total) {
            System.out.println("[ALL TESTS PASSED] Himani's module is 100% functional and verified!");
        } else {
            System.err.println("[FAILURES DETECTED] Please check failed assertions above.");
        }
    }

    private static void assertCondition(String message, boolean condition) {
        total++;
        if (condition) {
            passed++;
            System.out.println("  [PASS] " + message);
        } else {
            System.err.println("  [FAIL] " + message);
        }
    }
}
