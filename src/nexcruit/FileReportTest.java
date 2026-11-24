package nexcruit;

import auth.Admin;
import auth.AuthManager;
import auth.Candidate;
import auth.Recruiter;
import auth.User;

import java.io.File;
import java.util.ArrayList;

/**
 * Comprehensive Test Suite for Sanchi's File Handling & Reports Module.
 * Tests all 12 evaluation rubric requirements:
 * 1. Automatic data directory creation.
 * 2. User serialization and persistence.
 * 3. Job serialization and persistence.
 * 4. Application serialization (with object references).
 * 5. Missing file graceful handling.
 * 6. Empty collections handling.
 * 7. Division-by-zero protection in selection ratio.
 * 8. Accurate recruitment statistics (shortlisted, selected, rejected, under review).
 * 9. Job-wise breakdown calculations.
 * 10. Precise selection ratio calculation.
 * 11. Full application restart simulation (state persistence + counter sync).
 * 12. Formatted report terminal output verification.
 */
public class FileReportTest {

    private static int totalTests = 0;
    private static int passedTests = 0;

    public static void main(String[] args) {
        System.out.println("================================================================================");
        System.out.println("         NEXCRUIT - SANCHI FILE HANDLING & REPORTS TEST SUITE (EVAL 1)          ");
        System.out.println("================================================================================");

        FileManager fileManager = new FileManager();

        testDataDirectoryCreation(fileManager);
        testSaveAndLoadUsers(fileManager);
        testSaveAndLoadJobs(fileManager);
        testSaveAndLoadApplications(fileManager);
        testMissingFileHandlingGraceful(fileManager);
        testEmptyCollectionsHandling(fileManager);
        testNullSafety(fileManager);
        testRecruitmentStatistics();
        testSelectionRatioDivisionByZero();
        testJobWiseStatistics();
        testApplicationCloseAndRestartSimulation(fileManager);
        testReportTerminalOutputs();

        System.out.println("================================================================================");
        System.out.println("Total Tests Run: " + totalTests + " | Passed: " + passedTests + " | Failed: " + (totalTests - passedTests));
        if (passedTests == totalTests) {
            System.out.println("Status: ALL TESTS PASSED SUCCESSFULLY (100%)");
        } else {
            System.out.println("Status: SOME TESTS FAILED");
        }
        System.out.println("================================================================================");
    }

    private static void assertTrue(String testName, boolean condition) {
        totalTests++;
        if (condition) {
            passedTests++;
            System.out.printf("Test %-2d: %-55s -> PASSED\n", totalTests, testName);
        } else {
            System.out.printf("Test %-2d: %-55s -> FAILED\n", totalTests, testName);
        }
    }

    // 1. Data directory creation
    private static void testDataDirectoryCreation(FileManager fileManager) {
        File dir = new File("data");
        assertTrue("Automatic data directory creation", dir.exists() && dir.isDirectory());
    }

    // 2. User serialization & deserialization
    private static void testSaveAndLoadUsers(FileManager fileManager) {
        AuthManager auth = new AuthManager();
        try {
            ArrayList<String> candSkills = new ArrayList<String>();
            candSkills.add("Java");
            candSkills.add("SQL");
            auth.registerCandidate("Aarav Sharma", "aarav@gmail.com", "pass123", "9876543210", "B.Tech CSE", 8.8, 2, candSkills);
            auth.registerRecruiter("Neha Mehta", "neha@google.com", "recpass", "Google Inc.", "Engineering", "9876500000");
            auth.registerAdmin("System Admin", "admin@nexcruit.com", "adminpass");
        } catch (Exception e) {
            assertTrue("Register test users without error", false);
            return;
        }

        ArrayList<User> originalUsers = auth.getAllUsers();
        boolean saved = fileManager.saveUsers(originalUsers);
        ArrayList<User> loadedUsers = fileManager.loadUsers();

        boolean condition = saved && loadedUsers.size() == 3;
        assertTrue("Save and load registered users (users.dat)", condition);
    }

    // 3. Job serialization & deserialization
    private static void testSaveAndLoadJobs(FileManager fileManager) {
        JobManager jm = new JobManager();
        ArrayList<String> skills1 = new ArrayList<String>();
        skills1.add("Java");
        skills1.add("OOP");
        skills1.add("SQL");

        ArrayList<String> skills2 = new ArrayList<String>();
        skills2.add("Python");
        skills2.add("Django");

        try {
            jm.createJob("Java Developer", "Infosys", "Core Java Role", skills1, 7.5, 1, 5);
            jm.createJob("Python Backend", "Wipro", "Django backend developer", skills2, 6.5, 2, 3);
        } catch (Exception e) {
            assertTrue("Create test jobs without error", false);
            return;
        }

        ArrayList<Job> originalJobs = jm.getAllJobs();
        boolean saved = fileManager.saveJobs(originalJobs);
        ArrayList<Job> loadedJobs = fileManager.loadJobs();

        boolean condition = saved && loadedJobs.size() == 2 &&
                loadedJobs.get(0).getTitle().equals("Java Developer") &&
                loadedJobs.get(0).getRequiredSkills().contains("Java");
        assertTrue("Save and load job records (jobs.dat)", condition);
    }

    // 4. Application serialization & deserialization (with Candidate & Job objects)
    private static void testSaveAndLoadApplications(FileManager fileManager) {
        Candidate cand = new Candidate("CAN-101", "Rohan Verma", "rohan@gmail.com", "pass123", "9999999999", "B.Tech", 8.2, 1);
        ArrayList<String> skills = new ArrayList<String>();
        skills.add("Java");
        skills.add("SQL");
        Job job = new Job("JOB-101", "Junior Java Dev", "TCS", "Entry Level", skills, 7.0, 0, 4);

        Application app1 = new Application("APP-101", cand, job);
        app1.setStatus("SHORTLISTED");
        app1.setMatchScore(85.0);

        ArrayList<Application> apps = new ArrayList<Application>();
        apps.add(app1);

        boolean saved = fileManager.saveApplications(apps);
        ArrayList<Application> loaded = fileManager.loadApplications();

        boolean condition = saved && loaded.size() == 1 &&
                loaded.get(0).getCandidate().getName().equals("Rohan Verma") &&
                loaded.get(0).getJob().getTitle().equals("Junior Java Dev") &&
                loaded.get(0).getStatus().equals("SHORTLISTED") &&
                loaded.get(0).getMatchScore() == 85.0;
        assertTrue("Save and load applications with references (applications.dat)", condition);
    }

    // 5. Handling missing files gracefully
    private static void testMissingFileHandlingGraceful(FileManager fileManager) {
        // Temporarily delete a file if exists
        File dummy = new File("data/nonexistent.dat");
        dummy.delete();

        // Testing that loadUsers returns an empty list if file is missing (not throwing crash)
        File uFile = new File(fileManager.getUsersFilePath());
        boolean wasDeleted = false;
        if (uFile.exists()) {
            uFile.delete();
            wasDeleted = true;
        }

        ArrayList<User> result = fileManager.loadUsers();
        boolean condition = result != null && result.isEmpty();
        assertTrue("Graceful handling of missing files (returns empty list)", condition);
    }

    // 6. Empty collections handling
    private static void testEmptyCollectionsHandling(FileManager fileManager) {
        boolean savedUsers = fileManager.saveUsers(new ArrayList<User>());
        boolean savedJobs = fileManager.saveJobs(new ArrayList<Job>());
        boolean savedApps = fileManager.saveApplications(new ArrayList<Application>());

        boolean condition = savedUsers && savedJobs && savedApps;
        assertTrue("Handle saving empty collections successfully", condition);
    }

    // 7. Null safety
    private static void testNullSafety(FileManager fileManager) {
        boolean uResult = fileManager.saveUsers(null);
        boolean jResult = fileManager.saveJobs(null);
        boolean aResult = fileManager.saveApplications(null);

        boolean condition = !uResult && !jResult && !aResult;
        assertTrue("Null safety protection for save operations", condition);
    }

    // 8. Recruitment statistics calculation
    private static void testRecruitmentStatistics() {
        JobManager jm = new JobManager();
        ApplicationManager am = new ApplicationManager();

        try {
            ArrayList<String> sk = new ArrayList<String>();
            sk.add("Java");
            Job j1 = jm.createJob("Dev 1", "Comp A", "Desc", sk, 7.0, 1, 10);
            Job j2 = jm.createJob("Dev 2", "Comp B", "Desc", sk, 6.0, 0, 5);

            Candidate c1 = new Candidate("CAN-1", "User 1", "u1@test.com", "p", "1", "B", 8.0, 1);
            Candidate c2 = new Candidate("CAN-2", "User 2", "u2@test.com", "p", "2", "B", 7.0, 0);
            Candidate c3 = new Candidate("CAN-3", "User 3", "u3@test.com", "p", "3", "B", 6.5, 0);
            Candidate c4 = new Candidate("CAN-4", "User 4", "u4@test.com", "p", "4", "B", 7.5, 2);

            Application a1 = am.applyForJob(c1, j1);
            Application a2 = am.applyForJob(c2, j1);
            Application a3 = am.applyForJob(c3, j2);
            Application a4 = am.applyForJob(c4, j2);

            am.updateApplicationStatus(a1.getApplicationId(), "SELECTED", "Passed interview");
            am.updateApplicationStatus(a2.getApplicationId(), "SHORTLISTED", "Good score");
            am.updateApplicationStatus(a3.getApplicationId(), "REJECTED", "Low CGPA");
            // a4 remains "APPLIED"

            ReportManager rm = new ReportManager(jm, am);

            boolean condition = rm.getTotalJobs() == 2 &&
                    rm.getTotalVacancies() == 15 &&
                    rm.getTotalApplications() == 4 &&
                    rm.getSelectedCount() == 1 &&
                    rm.getShortlistedCount() == 1 &&
                    rm.getRejectedCount() == 1 &&
                    rm.getUnderReviewCount() == 1;

            assertTrue("Accurate recruitment counts (Jobs, Vacancies, Shortlisted, Selected, Rejected)", condition);
        } catch (Exception e) {
            assertTrue("Recruitment statistics calculation", false);
        }
    }

    // 9. Selection ratio division by zero protection
    private static void testSelectionRatioDivisionByZero() {
        ReportManager rm = new ReportManager();
        double ratio = rm.getSelectionRatio();
        assertTrue("Selection ratio division-by-zero protection (returns 0.0%)", ratio == 0.0);
    }

    // 10. Job-wise statistics calculation
    private static void testJobWiseStatistics() {
        JobManager jm = new JobManager();
        ApplicationManager am = new ApplicationManager();

        try {
            ArrayList<String> sk = new ArrayList<String>();
            sk.add("Java");
            Job j1 = jm.createJob("Role 1", "Co1", "Desc", sk, 7.0, 1, 2);
            Candidate c1 = new Candidate("CAN-1", "A", "a@a.com", "p", "1", "B", 8.0, 1);
            Candidate c2 = new Candidate("CAN-2", "B", "b@b.com", "p", "2", "B", 8.0, 1);

            Application a1 = am.applyForJob(c1, j1);
            Application a2 = am.applyForJob(c2, j1);

            am.updateApplicationStatus(a1.getApplicationId(), "SELECTED", "Hired");
            am.updateApplicationStatus(a2.getApplicationId(), "SHORTLISTED", "Shortlisted");

            ReportManager rm = new ReportManager(jm, am);
            int appCount = rm.getApplicationsCountForJob(j1.getJobId());
            int selCount = rm.getSelectedCountForJob(j1.getJobId());
            int shortCount = rm.getShortlistedCountForJob(j1.getJobId());

            boolean condition = appCount == 2 && selCount == 1 && shortCount == 1;
            assertTrue("Job-wise application statistics calculation", condition);
        } catch (Exception e) {
            assertTrue("Job-wise statistics calculation", false);
        }
    }

    // 11. Full application state restart simulation (saveAll -> loadAll -> counter synchronization)
    private static void testApplicationCloseAndRestartSimulation(FileManager fileManager) {
        AuthManager auth1 = new AuthManager();
        JobManager jm1 = new JobManager();
        ApplicationManager am1 = new ApplicationManager();

        try {
            ArrayList<String> skCand = new ArrayList<String>();
            skCand.add("Java");
            Candidate c1 = auth1.registerCandidate("Sim Candidate", "sim@test.com", "pass123", "1234567890", "B.E.", 8.5, 1, skCand);
            ArrayList<String> sk = new ArrayList<String>();
            sk.add("Java");
            sk.add("SQL");
            Job j1 = jm1.createJob("Sim Job", "Sim Corp", "Description", sk, 7.0, 0, 3);
            Application a1 = am1.applyForJob(c1, j1);

            // Save entire state
            boolean saved = fileManager.saveAll(auth1, jm1, am1);

            // Fresh restart with blank managers
            AuthManager auth2 = new AuthManager();
            JobManager jm2 = new JobManager();
            ApplicationManager am2 = new ApplicationManager();

            fileManager.loadAll(auth2, jm2, am2);

            // Verify data restoration
            boolean usersRestored = auth2.getAllUsers().size() == 1;
            boolean jobsRestored = jm2.getAllJobs().size() == 1;
            boolean appsRestored = am2.getAllApplications().size() == 1;

            // Verify ID counter synchronization (new IDs should not conflict with CAN-101, JOB-101, APP-101)
            String nextJobId = jm2.generateJobId();
            String nextAppId = am2.generateApplicationId();
            boolean countersSynced = nextJobId.equals("JOB-102") && nextAppId.equals("APP-102");

            boolean condition = saved && usersRestored && jobsRestored && appsRestored && countersSynced;
            assertTrue("Application close & restart simulation (State persistence + ID sync)", condition);
        } catch (Exception e) {
            assertTrue("Application close & restart simulation", false);
        }
    }

    // 12. Report terminal output verification
    private static void testReportTerminalOutputs() {
        JobManager jm = new JobManager();
        ApplicationManager am = new ApplicationManager();

        try {
            ArrayList<String> sk = new ArrayList<String>();
            sk.add("Java");
            Job j = jm.createJob("Software Engineer", "Amazon", "SDE 1", sk, 8.0, 0, 10);
            Candidate c = new Candidate("CAN-99", "Pooja", "p@p.com", "p", "99", "BTech", 9.0, 2);
            Application app = am.applyForJob(c, j);
            am.updateApplicationStatus(app.getApplicationId(), "SELECTED", "Hired");

            ReportManager rm = new ReportManager(jm, am);

            System.out.println("\n[Testing Formatted Terminal Output Sample]");
            rm.generateRecruitmentReport();
            rm.generateJobWiseReport();
            rm.generateCandidateStatusReport("CAN-99");

            assertTrue("Terminal report outputs formatted without exceptions", true);
        } catch (Exception e) {
            assertTrue("Terminal report outputs formatted without exceptions", false);
        }
    }
}
