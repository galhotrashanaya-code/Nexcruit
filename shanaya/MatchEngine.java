package nexcruit;

import auth.Candidate;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Core Matching Engine implementation for Nexcruit.
 * Developed by: Shanaya (Matching Engine Module - Evaluation 1)
 *
 * Scope for Evaluation 1:
 * - Basic skills-based matching percentage calculation.
 * - Minimum CGPA eligibility verification.
 * - Handles edge cases: empty skills, case-insensitivity, extra spaces, duplicate skills.
 * - Displays clear matching details in the terminal.
 * - Integrates with Chesta's Candidate class and Himani's Job & Application classes.
 *
 * Concepts used:
 * - Interface Implementation (implements Matchable)
 * - Classes and Objects
 * - Encapsulation
 * - Method Overloading and Method Overriding
 * - ArrayList<String>
 * - Simple for-each loops and if-else statements
 * - Basic String methods (trim(), equalsIgnoreCase())
 * - Serializable
 */
public class MatchEngine implements Matchable, Serializable {
    private static final long serialVersionUID = 1L;

    public MatchEngine() {
        // Default constructor
    }

    /**
     * Calculates matching percentage between candidate skills and required skills.
     *
     * Formula:
     * Matching Percentage = (Matched Skills / Total Required Skills) * 100
     *
     * Edge case handling:
     * - No required skills: returns 100.0% (all requirements met)
     * - Empty candidate skills: returns 0.0%
     * - Extra spaces: handled via .trim()
     * - Different capitalization: handled via .equalsIgnoreCase()
     * - Duplicate skills: handled by checking unique entries
     */
    @Override
    public double calculateMatchScore(ArrayList<String> candidateSkills, ArrayList<String> requiredSkills) {
        // Case 1: Job has no required skills -> Candidate satisfies all criteria
        if (requiredSkills == null || requiredSkills.isEmpty()) {
            return 100.0;
        }

        // Case 2: Candidate has no skills listed -> 0% match
        if (candidateSkills == null || candidateSkills.isEmpty()) {
            return 0.0;
        }

        // Get matched skills list
        ArrayList<String> matched = getMatchedSkills(candidateSkills, requiredSkills);
        int matchedCount = matched.size();
        int totalRequired = countUniqueSkills(requiredSkills);

        if (totalRequired == 0) {
            return 100.0;
        }

        // Apply formula: (Matched Skills / Total Required Skills) * 100
        double percentage = ((double) matchedCount / totalRequired) * 100.0;

        // Round to 1 decimal place (e.g., 75.0%)
        return Math.round(percentage * 10.0) / 10.0;
    }

    /**
     * Overloaded method: Calculates match score directly from Candidate and Job objects.
     */
    @Override
    public double calculateMatchScore(Candidate candidate, Job job) {
        if (candidate == null || job == null) {
            return 0.0;
        }
        return calculateMatchScore(candidate.getSkills(), job.getRequiredSkills());
    }

    /**
     * Checks whether candidate CGPA meets or exceeds the minimum required CGPA.
     * Simple, transparent rule: candidateCgpa >= minCgpa
     */
    @Override
    public boolean checkCgpaEligibility(double candidateCgpa, double minCgpa) {
        if (candidateCgpa >= minCgpa) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Overloaded method: Checks CGPA eligibility directly from Candidate and Job objects.
     */
    @Override
    public boolean checkCgpaEligibility(Candidate candidate, Job job) {
        if (candidate == null || job == null) {
            return false;
        }
        return checkCgpaEligibility(candidate.getCgpa(), job.getMinCgpa());
    }

    /**
     * Finds which required skills the candidate possesses.
     * Handles:
     * - Case-insensitivity (Java == java == JAVA)
     * - Extra whitespace ("  Java  " == "Java")
     * - Avoids duplicate matches
     */
    @Override
    public ArrayList<String> getMatchedSkills(ArrayList<String> candidateSkills, ArrayList<String> requiredSkills) {
        ArrayList<String> matchedList = new ArrayList<String>();
        if (candidateSkills == null || requiredSkills == null) {
            return matchedList;
        }

        // Clean extra spaces and remove duplicates
        ArrayList<String> cleanRequired = cleanAndRemoveDuplicates(requiredSkills);
        ArrayList<String> cleanCandidate = cleanAndRemoveDuplicates(candidateSkills);

        // Simple nested loop to compare skills
        for (String req : cleanRequired) {
            for (String candSkill : cleanCandidate) {
                if (req.equalsIgnoreCase(candSkill)) {
                    matchedList.add(req);
                    break; // Move to next required skill
                }
            }
        }

        return matchedList;
    }

    /**
     * Finds which required skills the candidate is missing.
     */
    public ArrayList<String> getMissingSkills(ArrayList<String> candidateSkills, ArrayList<String> requiredSkills) {
        ArrayList<String> missingList = new ArrayList<String>();
        if (requiredSkills == null) {
            return missingList;
        }

        ArrayList<String> cleanRequired = cleanAndRemoveDuplicates(requiredSkills);
        ArrayList<String> matched = getMatchedSkills(candidateSkills, requiredSkills);

        for (String req : cleanRequired) {
            boolean isMatched = false;
            for (String m : matched) {
                if (req.equalsIgnoreCase(m)) {
                    isMatched = true;
                    break;
                }
            }
            if (!isMatched) {
                missingList.add(req);
            }
        }

        return missingList;
    }

    /**
     * Displays a clean, beginner-friendly matching summary in the terminal.
     */
    public void displayMatchResult(Candidate candidate, Job job) {
        if (candidate == null || job == null) {
            System.out.println("Cannot display match: Candidate or Job is null.");
            return;
        }

        double score = calculateMatchScore(candidate, job);
        boolean isEligible = checkCgpaEligibility(candidate, job);
        ArrayList<String> matched = getMatchedSkills(candidate.getSkills(), job.getRequiredSkills());
        ArrayList<String> missing = getMissingSkills(candidate.getSkills(), job.getRequiredSkills());
        int totalRequired = countUniqueSkills(job.getRequiredSkills());

        System.out.println("--------------------------------------------------------------------------------");
        System.out.println("                 NEXCRUIT CANDIDATE MATCHING EVALUATION                         ");
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println(" Candidate Name   : " + candidate.getName() + " (" + candidate.getUserId() + ")");
        System.out.println(" Candidate CGPA   : " + String.format("%.2f", candidate.getCgpa()));
        System.out.println(" Job Title        : " + job.getTitle() + " (" + job.getJobId() + ")");
        System.out.println(" Company          : " + job.getCompanyName());
        System.out.println(" Minimum CGPA Req : " + String.format("%.2f", job.getMinCgpa()));
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println(" Required Skills  : " + (job.getRequiredSkills().isEmpty() ? "None" : String.join(", ", job.getRequiredSkills())));
        System.out.println(" Candidate Skills : " + (candidate.getSkills().isEmpty() ? "None" : String.join(", ", candidate.getSkills())));
        System.out.println(" Matched Skills   : " + matched.size() + "/" + totalRequired + " " + (matched.isEmpty() ? "[None]" : matched.toString()));
        System.out.println(" Missing Skills   : " + missing.size() + "/" + totalRequired + " " + (missing.isEmpty() ? "[None]" : missing.toString()));
        System.out.println(" Matching Score   : " + String.format("%.1f", score) + "%");
        System.out.println(" CGPA Eligibility : " + (isEligible ? "ELIGIBLE (Meets minimum CGPA)" : "INELIGIBLE (Below minimum CGPA)"));
        System.out.println(" Overall Status   : " + (isEligible ? (score >= 50.0 ? "RECOMMENDED" : "UNDER REVIEW") : "DISQUALIFIED (Failed CGPA)"));
        System.out.println("--------------------------------------------------------------------------------");
    }

    /**
     * Associates the evaluation result directly with an Application object.
     * Accessible by Sanchi's ReportManager and Himani's ApplicationManager.
     */
    public void evaluateApplication(Application application) {
        if (application == null || application.getCandidate() == null || application.getJob() == null) {
            return;
        }

        Candidate candidate = application.getCandidate();
        Job job = application.getJob();

        double score = calculateMatchScore(candidate, job);
        boolean isEligible = checkCgpaEligibility(candidate, job);
        ArrayList<String> matched = getMatchedSkills(candidate.getSkills(), job.getRequiredSkills());

        // Update application fields
        application.setMatchScore(score);
        application.setCgpaEligible(isEligible);

        if (!isEligible) {
            application.setRemarks("Ineligible: Candidate CGPA (" + String.format("%.2f", candidate.getCgpa()) +
                    ") below job minimum (" + String.format("%.2f", job.getMinCgpa()) + ").");
        } else {
            application.setRemarks("Evaluated: " + String.format("%.1f", score) + "% skill match (" +
                    matched.size() + "/" + countUniqueSkills(job.getRequiredSkills()) + " skills matched).");
        }
    }

    // Helper method: Cleans whitespace and removes duplicate skills
    private ArrayList<String> cleanAndRemoveDuplicates(ArrayList<String> list) {
        ArrayList<String> unique = new ArrayList<String>();
        if (list == null) return unique;

        for (String item : list) {
            if (item != null && !item.trim().isEmpty()) {
                String clean = item.trim();
                boolean exists = false;
                for (String u : unique) {
                    if (u.equalsIgnoreCase(clean)) {
                        exists = true;
                        break;
                    }
                }
                if (!exists) {
                    unique.add(clean);
                }
            }
        }
        return unique;
    }

    // Helper method: Counts unique skills in a list
    private int countUniqueSkills(ArrayList<String> list) {
        return cleanAndRemoveDuplicates(list).size();
    }
}
