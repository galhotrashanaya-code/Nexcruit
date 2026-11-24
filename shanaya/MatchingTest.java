package nexcruit;

import auth.Candidate;
import java.util.ArrayList;

/**
 * Automated Test Suite for Shanaya's Matching Engine Module (Evaluation 1).
 * Verifies all 10 required test cases from the evaluation rubric.
 */
public class MatchingTest {

    private static int passed = 0;
    private static int total = 0;

    public static void main(String[] args) {
        System.out.println("================================================================================");
        System.out.println("        SHANAYA'S MATCHING ENGINE MODULE - EVALUATION 1 TEST SUITE              ");
        System.out.println("================================================================================");

        MatchEngine engine = new MatchEngine();

        // Base required skills for standard tests: 4 skills
        ArrayList<String> requiredSkills = new ArrayList<String>();
        requiredSkills.add("Java");
        requiredSkills.add("OOP");
        requiredSkills.add("SQL");
        requiredSkills.add("Data Structures");

        Job sampleJob = new Job("JOB-101", "REC-101", "Java Developer", "Google",
                "Core Java development", requiredSkills, 7.5, 0, 5, "Bangalore");

        // 1. Candidate with 100% skill match (4/4 skills)
        System.out.println("\n[Test 1] Candidate with 100% skill match");
        ArrayList<String> skills100 = new ArrayList<String>();
        skills100.add("Java");
        skills100.add("OOP");
        skills100.add("SQL");
        skills100.add("Data Structures");
        Candidate cand100 = new Candidate("CAN-101", "Alex Rivera", "alex@gmail.com", "pass",
                "9876543210", "B.Tech CS", 8.5, 1, skills100);

        double score1 = engine.calculateMatchScore(cand100, sampleJob);
        assertTest("100% skill match produces exactly 100.0%", score1 == 100.0);

        // 2. Candidate with 75% skill match (3/4 skills)
        System.out.println("\n[Test 2] Candidate with 75% skill match");
        ArrayList<String> skills75 = new ArrayList<String>();
        skills75.add("Java");
        skills75.add("OOP");
        skills75.add("SQL");
        Candidate cand75 = new Candidate("CAN-102", "Priya Sharma", "priya@gmail.com", "pass",
                "9876543211", "B.Tech IT", 8.0, 0, skills75);

        double score2 = engine.calculateMatchScore(cand75, sampleJob);
        assertTest("3 out of 4 skills produces 75.0%", score2 == 75.0);

        // 3. Candidate with 50% skill match (2/4 skills)
        System.out.println("\n[Test 3] Candidate with 50% skill match");
        ArrayList<String> skills50 = new ArrayList<String>();
        skills50.add("Java");
        skills50.add("SQL");
        Candidate cand50 = new Candidate("CAN-103", "Rohan Mehta", "rohan@gmail.com", "pass",
                "9876543212", "BCA", 7.8, 0, skills50);

        double score3 = engine.calculateMatchScore(cand50, sampleJob);
        assertTest("2 out of 4 skills produces 50.0%", score3 == 50.0);

        // 4. Candidate with zero matching skills (0/4 skills)
        System.out.println("\n[Test 4] Candidate with zero matching skills");
        ArrayList<String> skills0 = new ArrayList<String>();
        skills0.add("Python");
        skills0.add("Ruby");
        skills0.add("HTML");
        Candidate cand0 = new Candidate("CAN-104", "Simran Kaur", "simran@gmail.com", "pass",
                "9876543213", "B.Sc", 8.2, 0, skills0);

        double score4 = engine.calculateMatchScore(cand0, sampleJob);
        assertTest("0 matching skills produces 0.0%", score4 == 0.0);

        // 5. Candidate with insufficient CGPA (6.8 < 7.5 minimum cutoff)
        System.out.println("\n[Test 5] Candidate with insufficient CGPA");
        Candidate candLowCgpa = new Candidate("CAN-105", "Dev Patel", "dev@gmail.com", "pass",
                "9876543214", "B.Tech", 6.8, 0, skills100);

        boolean eligible5 = engine.checkCgpaEligibility(candLowCgpa, sampleJob);
        assertTest("CGPA 6.80 < 7.50 is INELIGIBLE (false)", !eligible5);

        // 6. Candidate with sufficient CGPA (7.5 >= 7.5 minimum cutoff)
        System.out.println("\n[Test 6] Candidate with sufficient CGPA");
        Candidate candPassCgpa = new Candidate("CAN-106", "Kavya Nair", "kavya@gmail.com", "pass",
                "9876543215", "B.Tech", 7.5, 0, skills100);

        boolean eligible6 = engine.checkCgpaEligibility(candPassCgpa, sampleJob);
        assertTest("CGPA 7.50 >= 7.50 is ELIGIBLE (true)", eligible6);

        // 7. Empty required skills on job
        System.out.println("\n[Test 7] Empty required skills");
        ArrayList<String> emptyRequired = new ArrayList<String>();
        Job jobNoReqs = new Job("JOB-102", "REC-101", "Intern", "Google", "Open intern role",
                emptyRequired, 6.0, 0, 10, "Remote");

        double score7 = engine.calculateMatchScore(cand50, jobNoReqs);
        assertTest("Job with no required skills produces 100.0%", score7 == 100.0);

        // 8. Empty candidate skills
        System.out.println("\n[Test 8] Empty candidate skills");
        ArrayList<String> emptyCandidate = new ArrayList<String>();
        Candidate candNoSkills = new Candidate("CAN-107", "Newbie", "new@gmail.com", "pass",
                "9876543216", "B.Tech", 8.0, 0, emptyCandidate);

        double score8 = engine.calculateMatchScore(candNoSkills, sampleJob);
        assertTest("Candidate with no skills produces 0.0%", score8 == 0.0);

        // 9. Skills with different capitalization ("java", "oOp", "Sql", "DATA STRUCTURES")
        System.out.println("\n[Test 9] Skills with different capitalization");
        ArrayList<String> mixedCaseSkills = new ArrayList<String>();
        mixedCaseSkills.add("java");
        mixedCaseSkills.add("oOp");
        mixedCaseSkills.add("Sql");
        mixedCaseSkills.add("DATA STRUCTURES");
        Candidate candMixedCase = new Candidate("CAN-108", "Case Tester", "case@gmail.com", "pass",
                "9876543217", "B.Tech", 8.5, 0, mixedCaseSkills);

        double score9 = engine.calculateMatchScore(candMixedCase, sampleJob);
        assertTest("Case-insensitive matching produces 100.0%", score9 == 100.0);

        // 10. Duplicate skill entries ("Java", "Java", "SQL", "SQL")
        System.out.println("\n[Test 10] Duplicate skill entries and extra spaces");
        ArrayList<String> duplicateSkills = new ArrayList<String>();
        duplicateSkills.add("  Java  ");
        duplicateSkills.add("Java");
        duplicateSkills.add("SQL");
        duplicateSkills.add("SQL");
        Candidate candDuplicates = new Candidate("CAN-109", "Dup Tester", "dup@gmail.com", "pass",
                "9876543218", "B.Tech", 8.0, 0, duplicateSkills);

        double score10 = engine.calculateMatchScore(candDuplicates, sampleJob);
        // Only 2 unique skills matched ("Java", "SQL") out of 4 required -> 50.0%
        assertTest("Duplicates not double-counted (2 unique skills matched = 50.0%)", score10 == 50.0);

        // Display test demonstration of terminal output
        System.out.println("\n--- DEMONSTRATION OF TERMINAL OUTPUT ---");
        engine.displayMatchResult(cand75, sampleJob);

        // Application integration test
        System.out.println("\n[Integration] Evaluating Application object");
        Application app = new Application("APP-101", cand75, sampleJob);
        engine.evaluateApplication(app);
        assertTest("Application match score set to 75.0%", app.getMatchScore() == 75.0);
        assertTest("Application CGPA eligible set to true", app.isCgpaEligible());
        assertTest("Application remarks updated with match summary", app.getRemarks().contains("75.0%"));

        System.out.println("\n================================================================================");
        System.out.printf("  EVALUATION 1 TEST RESULTS: %d / %d Tests Passed (%d%%)%n",
                passed, total, (passed * 100 / total));
        System.out.println("================================================================================");

        if (passed == total) {
            System.out.println("[ALL TESTS PASSED] Shanaya's Matching Engine is 100% verified!");
        } else {
            System.err.println("[FAILURES DETECTED] Please check failed assertions above.");
        }
    }

    private static void assertTest(String testName, boolean condition) {
        total++;
        if (condition) {
            passed++;
            System.out.println("  [PASS] " + testName);
        } else {
            System.err.println("  [FAIL] " + testName);
        }
    }
}
