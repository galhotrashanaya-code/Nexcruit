package test;

import applications.Application;
import applications.ApplicationManager;
import auth.AuthManager;
import auth.Candidate;
import auth.Recruiter;
import auth.User;
import exceptions.DuplicateApplicationException;
import exceptions.InvalidInputException;
import exceptions.UserNotFoundException;
import interviews.Interview;
import interviews.InterviewManager;
import interviews.InterviewRound;
import interviews.InterviewStatus;
import jobs.Job;
import jobs.JobManager;
import matching.CandidateMatchResult;
import matching.MatchEngine;
import storage.DataStore;
import storage.FileManager;
import utils.ApplicationStatus;

import java.util.ArrayList;

/**
 * Simple, beginner-friendly automated test suite.
 * Tests all 4 modules, core calculations, eligibility checks, and persistence.
 * Concepts used: Methods, Objects, Assertions, and Basic Logic.
 */
public class NexcruitTestSuite {

    private static int testsPassed = 0;
    private static int testsTotal = 0;

    public static void main(String[] args) {
        System.out.println("================================================================================");
        System.out.println("             NEXCRUIT BEGINNER-FRIENDLY AUTOMATED TEST SUITE                    ");
        System.out.println("================================================================================");

        testAuthenticationModule();
        testJobModule();
        testApplicationModule();
        testMatchingEngineSkillsFormula();
        testCgpaEligibilityCheck();
        testCandidateRanking();
        testShortlistingWithCgpaExclusion();
        testInterviewManagement();
        testFilePersistence();

        System.out.println("================================================================================");
        System.out.printf("  TOTAL TESTS: %d | PASSED: %d | FAILED: %d%n", testsTotal, testsPassed, (testsTotal - testsPassed));
        System.out.println("================================================================================");

        if (testsPassed == testsTotal) {
            System.out.println("[ALL TESTS PASSED] The Nexcruit project is 100% verified and ready for evaluation!");
        } else {
            System.err.println("[FAILURES DETECTED] Please check the failed tests above.");
        }
    }

    private static void assertTrue(String testName, boolean condition) {
        testsTotal++;
        if (condition) {
            testsPassed++;
            System.out.println("  [PASS] " + testName);
        } else {
            System.err.println("  [FAIL] " + testName);
        }
    }

    private static void testAuthenticationModule() {
        System.out.println("\n--- 1. Testing Authentication and User Management ---");
        AuthManager auth = new AuthManager();

        try {
            // Register candidate
            ArrayList<String> skills = new ArrayList<String>();
            skills.add("Java");
            skills.add("SQL");
            Candidate cand = auth.registerCandidate("Rahul Sharma", "rahul@gmail.com", "pass123",
                    "9876543210", "B.Tech CS", 8.2, 1, skills);
            assertTrue("Candidate ID starts with CAN-", cand.getUserId().startsWith("CAN-"));
            assertTrue("Candidate email is registered", auth.isEmailRegistered("rahul@gmail.com"));

            // Register recruiter
            Recruiter rec = auth.registerRecruiter("Anita Roy", "anita@wipro.com", "pass456",
                    "Wipro", "Talent Acquisition", "9123456780");
            assertTrue("Recruiter ID starts with REC-", rec.getUserId().startsWith("REC-"));

            // Login check
            User loggedIn = auth.login("rahul@gmail.com", "pass123");
            assertTrue("Login successful with correct password", loggedIn != null);

            // Wrong password check
            boolean caughtWrongPassword = false;
            try {
                auth.login("rahul@gmail.com", "wrongpassword");
            } catch (UserNotFoundException e) {
                caughtWrongPassword = true;
            }
            assertTrue("Wrong password throws UserNotFoundException", caughtWrongPassword);

        } catch (Exception e) {
            assertTrue("Exception in auth: " + e.getMessage(), false);
        }
    }

    private static void testJobModule() {
        System.out.println("\n--- 2. Testing Job Management ---");
        JobManager jobMgr = new JobManager();

        try {
            ArrayList<String> skills = new ArrayList<String>();
            skills.add("Java");
            skills.add("SQL");
            Job job = jobMgr.createJob("REC-101", "Java Developer", "Infosys",
                    "Core Java development", skills, 7.5, 0, "Pune");

            assertTrue("Job ID starts with JOB-", job.getJobId().startsWith("JOB-"));
            assertTrue("Job is active by default", job.isActive());
            assertTrue("Job manager has 1 active job", jobMgr.getActiveJobs().size() == 1);

            // Delete job
            jobMgr.deleteJob(job.getJobId());
            assertTrue("Job marked inactive after delete", !job.isActive());

        } catch (Exception e) {
            assertTrue("Exception in job: " + e.getMessage(), false);
        }
    }

    private static void testApplicationModule() {
        System.out.println("\n--- 3. Testing Application & Duplicate Prevention ---");
        ApplicationManager appMgr = new ApplicationManager();

        try {
            Application app1 = appMgr.applyForJob("CAN-101", "JOB-101");
            assertTrue("Application ID assigned", app1.getApplicationId().startsWith("APP-"));
            assertTrue("Initial status is APPLIED", app1.getStatus() == ApplicationStatus.APPLIED);

            // Try to apply again for the same job -> DuplicateApplicationException
            boolean caughtDuplicate = false;
            try {
                appMgr.applyForJob("CAN-101", "JOB-101");
            } catch (DuplicateApplicationException e) {
                caughtDuplicate = true;
            }
            assertTrue("Duplicate application prevented with DuplicateApplicationException", caughtDuplicate);

        } catch (Exception e) {
            assertTrue("Exception in application: " + e.getMessage(), false);
        }
    }

    private static void testMatchingEngineSkillsFormula() {
        System.out.println("\n--- 4. Testing Matching Engine Skills Formula ---");
        MatchEngine engine = new MatchEngine();

        ArrayList<String> requiredSkills = new ArrayList<String>();
        requiredSkills.add("Java");
        requiredSkills.add("Spring Boot");
        requiredSkills.add("SQL");
        requiredSkills.add("Git");
        Job job = new Job("JOB-101", "REC-101", "Java Developer", "TCS", "Desc", requiredSkills, 7.0, 0, "Delhi");

        // Candidate with 4/4 skills -> 100%
        ArrayList<String> c1Skills = new ArrayList<String>();
        c1Skills.add("Java");
        c1Skills.add("Spring Boot");
        c1Skills.add("SQL");
        c1Skills.add("Git");
        Candidate c1 = new Candidate("CAN-101", "Alice", "alice@test.com", "p", "1", "B.Tech", 8.0, 1, c1Skills);
        double score1 = engine.calculateMatchScore(c1, job);
        assertTrue("Candidate with 4/4 skills gets 100.0%", score1 == 100.0);

        // Candidate with 2/4 skills -> 50%
        ArrayList<String> c2Skills = new ArrayList<String>();
        c2Skills.add("Java");
        c2Skills.add("SQL");
        Candidate c2 = new Candidate("CAN-102", "Bob", "bob@test.com", "p", "1", "B.Tech", 8.0, 1, c2Skills);
        double score2 = engine.calculateMatchScore(c2, job);
        assertTrue("Candidate with 2/4 skills gets 50.0%", score2 == 50.0);

        // Candidate with 0/4 skills -> 0%
        ArrayList<String> c3Skills = new ArrayList<String>();
        c3Skills.add("Python");
        c3Skills.add("Django");
        Candidate c3 = new Candidate("CAN-103", "Charlie", "charlie@test.com", "p", "1", "B.Tech", 8.0, 1, c3Skills);
        double score3 = engine.calculateMatchScore(c3, job);
        assertTrue("Candidate with 0/4 skills gets 0.0%", score3 == 0.0);
    }

    private static void testCgpaEligibilityCheck() {
        System.out.println("\n--- 5. Testing CGPA Eligibility Check ---");
        MatchEngine engine = new MatchEngine();

        ArrayList<String> reqSkills = new ArrayList<String>();
        reqSkills.add("Java");
        Job job = new Job("JOB-101", "REC-101", "Developer", "TCS", "Desc", reqSkills, 7.5, 0, "Delhi");

        Candidate passingCand = new Candidate("CAN-101", "Pass Cand", "p@t.com", "pass", "1", "B.Tech", 7.5, 0);
        Candidate failingCand = new Candidate("CAN-102", "Fail Cand", "f@t.com", "pass", "1", "B.Tech", 7.2, 0);

        assertTrue("Candidate with CGPA 7.5 >= 7.5 passes eligibility", engine.checkCgpaEligibility(passingCand, job));
        assertTrue("Candidate with CGPA 7.2 < 7.5 fails eligibility", !engine.checkCgpaEligibility(failingCand, job));
    }

    private static void testCandidateRanking() {
        System.out.println("\n--- 6. Testing Candidate Ranking ---");
        MatchEngine engine = new MatchEngine();

        ArrayList<String> reqSkills = new ArrayList<String>();
        reqSkills.add("Java");
        reqSkills.add("SQL");
        Job job = new Job("JOB-101", "REC-101", "Dev", "TCS", "Desc", reqSkills, 7.0, 0, "Delhi");

        // High match (100%)
        ArrayList<String> s1 = new ArrayList<String>();
        s1.add("Java");
        s1.add("SQL");
        Candidate highCand = new Candidate("CAN-1", "High", "h@t.com", "p", "1", "B.Tech", 8.0, 0, s1);

        // Low match (50%)
        ArrayList<String> s2 = new ArrayList<String>();
        s2.add("Java");
        Candidate lowCand = new Candidate("CAN-2", "Low", "l@t.com", "p", "1", "B.Tech", 8.5, 0, s2);

        ArrayList<CandidateMatchResult> list = new ArrayList<CandidateMatchResult>();
        list.add(engine.evaluateCandidate(lowCand, job, new Application("APP-2", "JOB-101", "CAN-2")));
        list.add(engine.evaluateCandidate(highCand, job, new Application("APP-1", "JOB-101", "CAN-1")));

        ArrayList<CandidateMatchResult> ranked = engine.rankCandidates(list);
        assertTrue("Rank 1 is candidate with highest score", "CAN-1".equals(ranked.get(0).getCandidate().getUserId()));
        assertTrue("Rank 2 is candidate with lower score", "CAN-2".equals(ranked.get(1).getCandidate().getUserId()));
    }

    private static void testShortlistingWithCgpaExclusion() {
        System.out.println("\n--- 7. Testing Shortlisting with Strict CGPA Exclusion ---");
        MatchEngine engine = new MatchEngine();
        ApplicationManager appMgr = new ApplicationManager();

        ArrayList<String> reqSkills = new ArrayList<String>();
        reqSkills.add("Java");
        reqSkills.add("Spring Boot");
        Job job = new Job("JOB-101", "REC-101", "Dev", "TCS", "Desc", reqSkills, 7.5, 0, "Delhi");

        // Candidate A: 100% skills match, but CGPA 6.5 (< 7.5 cutoff)
        ArrayList<String> sA = new ArrayList<String>();
        sA.add("Java");
        sA.add("Spring Boot");
        Candidate candA = new Candidate("CAN-A", "Cand A", "a@t.com", "p", "1", "B.Tech", 6.5, 0, sA);

        // Candidate B: 50% skills match, and CGPA 8.0 (>= 7.5 cutoff)
        ArrayList<String> sB = new ArrayList<String>();
        sB.add("Java");
        Candidate candB = new Candidate("CAN-B", "Cand B", "b@t.com", "p", "1", "B.Tech", 8.0, 0, sB);

        Application appA = new Application("APP-A", "JOB-101", "CAN-A");
        Application appB = new Application("APP-B", "JOB-101", "CAN-B");
        ArrayList<Application> apps = new ArrayList<Application>();
        apps.add(appA);
        apps.add(appB);
        appMgr.restoreApplications(apps);

        CandidateMatchResult resA = engine.evaluateCandidate(candA, job, appA);
        CandidateMatchResult resB = engine.evaluateCandidate(candB, job, appB);

        ArrayList<CandidateMatchResult> results = new ArrayList<CandidateMatchResult>();
        results.add(resA);
        results.add(resB);

        ArrayList<CandidateMatchResult> ranked = engine.rankCandidates(results);
        ArrayList<CandidateMatchResult> shortlisted = engine.shortlistCandidates(job, ranked, 50.0, appMgr);

        assertTrue("Only 1 candidate is shortlisted", shortlisted.size() == 1);
        assertTrue("Shortlisted candidate is Candidate B", "CAN-B".equals(shortlisted.get(0).getCandidate().getUserId()));
        assertTrue("Candidate A is REJECTED due to CGPA cutoff despite 100% skills", appA.getStatus() == ApplicationStatus.REJECTED);
        assertTrue("Candidate B is SHORTLISTED", appB.getStatus() == ApplicationStatus.SHORTLISTED);
    }

    private static void testInterviewManagement() {
        System.out.println("\n--- 8. Testing Interview Management ---");
        InterviewManager intMgr = new InterviewManager();

        try {
            Interview interview = intMgr.scheduleInterview("APP-101", "CAN-101", "JOB-101", "REC-101",
                    InterviewRound.TECHNICAL_ROUND_1, "2026-12-01 11:00 AM", "Google Meet");

            assertTrue("Interview scheduled with INT- ID", interview.getInterviewId().startsWith("INT-"));
            assertTrue("Initial status is SCHEDULED", interview.getStatus() == InterviewStatus.SCHEDULED);

            intMgr.updateInterviewStatus(interview.getInterviewId(), InterviewStatus.PASSED, "Cleared coding round.");
            assertTrue("Interview status updated to PASSED", interview.getStatus() == InterviewStatus.PASSED);

        } catch (Exception e) {
            assertTrue("Exception in interview: " + e.getMessage(), false);
        }
    }

    private static void testFilePersistence() {
        System.out.println("\n--- 9. Testing File Persistence (Serialization) ---");
        FileManager fileMgr = new FileManager();
        AuthManager auth = new AuthManager();
        JobManager jobMgr = new JobManager();
        ApplicationManager appMgr = new ApplicationManager();
        InterviewManager intMgr = new InterviewManager();

        try {
            Candidate cand = auth.registerCandidate("Persist Tester", "persist@demo.com", "pass", 8.0);
            boolean saved = fileMgr.saveData(auth, jobMgr, appMgr, intMgr);
            assertTrue("Data saved successfully to disk", saved);

            DataStore loaded = fileMgr.loadData();
            assertTrue("Loaded store has users", loaded.getUsers().size() > 0);

        } catch (Exception e) {
            assertTrue("Exception in persistence: " + e.getMessage(), false);
        }
    }
}
