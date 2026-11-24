package matching;

import applications.Application;
import applications.ApplicationManager;
import auth.AuthManager;
import auth.Candidate;
import jobs.Job;
import utils.ApplicationStatus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Core Matching Engine implementation for Nexcruit.
 * Concepts used:
 * - Interface implementation (implements Matchable)
 * - Simple nested loops for skill matching
 * - Simple if-else statements for eligibility checks
 * - Collections.sort() with a beginner-friendly Comparator
 */
public class MatchEngine implements Matchable, Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Skill matching formula:
     * Matching Percentage = (Number of Matched Skills / Total Required Skills) * 100
     */
    @Override
    public double calculateMatchScore(Candidate candidate, Job job) {
        if (candidate == null || job == null) {
            return 0.0;
        }

        ArrayList<String> requiredSkills = job.getRequiredSkills();
        if (requiredSkills == null || requiredSkills.isEmpty()) {
            return 100.0; // If job has no skill requirements, candidate gets 100%
        }

        ArrayList<String> matched = getMatchedSkills(candidate, job);
        int matchedCount = matched.size();
        int totalRequired = requiredSkills.size();

        // Calculate percentage using standard formula
        double percentage = ((double) matchedCount / totalRequired) * 100.0;
        // Round to 1 decimal place (e.g. 75.0)
        return Math.round(percentage * 10.0) / 10.0;
    }

    /**
     * Separate CGPA eligibility check:
     * Candidate CGPA must be greater than or equal to the job's minimum CGPA.
     */
    @Override
    public boolean checkCgpaEligibility(Candidate candidate, Job job) {
        if (candidate == null || job == null) {
            return false;
        }
        // Simple condition: candidate CGPA >= job min CGPA
        if (candidate.getCgpa() >= job.getMinCgpa()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Finds which required skills the candidate possesses.
     * Uses simple nested loops with equalsIgnoreCase.
     */
    @Override
    public ArrayList<String> getMatchedSkills(Candidate candidate, Job job) {
        ArrayList<String> matched = new ArrayList<String>();
        if (candidate == null || job == null) {
            return matched;
        }

        ArrayList<String> candidateSkills = candidate.getSkills();
        ArrayList<String> requiredSkills = job.getRequiredSkills();

        if (candidateSkills == null || requiredSkills == null) {
            return matched;
        }

        // Simple nested loop to check each required skill
        for (String req : requiredSkills) {
            for (String candSkill : candidateSkills) {
                if (req.equalsIgnoreCase(candSkill.trim())) {
                    matched.add(req);
                    break;
                }
            }
        }
        return matched;
    }

    /**
     * Finds which required skills the candidate is missing.
     */
    @Override
    public ArrayList<String> getMissingSkills(Candidate candidate, Job job) {
        ArrayList<String> missing = new ArrayList<String>();
        if (job == null) {
            return missing;
        }

        ArrayList<String> requiredSkills = job.getRequiredSkills();
        if (requiredSkills == null) {
            return missing;
        }

        ArrayList<String> matched = getMatchedSkills(candidate, job);

        for (String req : requiredSkills) {
            boolean isFound = false;
            for (String m : matched) {
                if (req.equalsIgnoreCase(m)) {
                    isFound = true;
                    break;
                }
            }
            if (!isFound) {
                missing.add(req);
            }
        }
        return missing;
    }

    /**
     * Extensible Weighted Scoring:
     * - Skills: 50%
     * - CGPA: 25%
     * - Education: 15%
     * - Experience: 10%
     */
    @Override
    public double calculateWeightedScore(Candidate candidate, Job job) {
        if (candidate == null || job == null) {
            return 0.0;
        }

        // 1. Skills portion (up to 50 points)
        double matchPercent = calculateMatchScore(candidate, job);
        double skillsPortion = (matchPercent / 100.0) * 50.0;

        // 2. CGPA portion (up to 25 points, normalized out of 10.0)
        double cgpa = candidate.getCgpa();
        if (cgpa > 10.0) cgpa = 10.0;
        if (cgpa < 0.0) cgpa = 0.0;
        double cgpaPortion = (cgpa / 10.0) * 25.0;

        // 3. Education portion (up to 15 points)
        double educationPortion = 10.0; // default for diploma/others
        String edu = candidate.getEducation();
        if (edu != null) {
            String lowerEdu = edu.toLowerCase();
            if (lowerEdu.contains("ph.d") || lowerEdu.contains("doctorate")) {
                educationPortion = 15.0;
            } else if (lowerEdu.contains("m.tech") || lowerEdu.contains("ms") || lowerEdu.contains("mca") || lowerEdu.contains("master")) {
                educationPortion = 14.0;
            } else if (lowerEdu.contains("b.tech") || lowerEdu.contains("be") || lowerEdu.contains("bca") || lowerEdu.contains("bachelor") || lowerEdu.contains("bs")) {
                educationPortion = 12.5;
            }
        }

        // 4. Experience portion (up to 10 points)
        double expPortion = 10.0;
        if (job.getMinExperience() > 0) {
            double ratio = (double) candidate.getExperienceYears() / job.getMinExperience();
            if (ratio > 1.0) ratio = 1.0;
            expPortion = ratio * 10.0;
        }

        double totalScore = skillsPortion + cgpaPortion + educationPortion + expPortion;
        return Math.round(totalScore * 10.0) / 10.0;
    }

    /**
     * Evaluates a single candidate application.
     */
    public CandidateMatchResult evaluateCandidate(Candidate candidate, Job job, Application application) {
        double matchScore = calculateMatchScore(candidate, job);
        boolean cgpaEligible = checkCgpaEligibility(candidate, job);
        ArrayList<String> matched = getMatchedSkills(candidate, job);
        ArrayList<String> missing = getMissingSkills(candidate, job);
        double weightedScore = calculateWeightedScore(candidate, job);

        // Update application data
        if (application != null) {
            application.setMatchScore(matchScore);
            application.setCgpaEligible(cgpaEligible);
            if (!cgpaEligible) {
                application.setRemarks("Ineligible: Candidate CGPA (" + candidate.getCgpa() + ") below min requirement (" + job.getMinCgpa() + ")");
            } else {
                application.setRemarks("Evaluated: " + matchScore + "% skill match (" + matched.size() + "/" + job.getRequiredSkills().size() + " matched)");
            }
        }

        return new CandidateMatchResult(candidate, application, job, matchScore, cgpaEligible, matched, missing, weightedScore);
    }

    /**
     * Evaluates all applications submitted for a job.
     */
    public ArrayList<CandidateMatchResult> evaluateJobApplications(Job job, ArrayList<Application> applications, AuthManager authManager) {
        ArrayList<CandidateMatchResult> results = new ArrayList<CandidateMatchResult>();
        if (job == null || applications == null || authManager == null) {
            return results;
        }

        for (Application app : applications) {
            Candidate candidate = (Candidate) authManager.getUserById(app.getCandidateId());
            if (candidate != null) {
                results.add(evaluateCandidate(candidate, job, app));
            }
        }
        return results;
    }

    /**
     * Ranks candidates from highest to lowest matching score.
     * Uses a beginner-friendly Comparator with clear if-else statements.
     */
    public ArrayList<CandidateMatchResult> rankCandidates(ArrayList<CandidateMatchResult> results) {
        ArrayList<CandidateMatchResult> sortedList = new ArrayList<CandidateMatchResult>(results);

        // Simple custom comparator using readable if-else logic
        Collections.sort(sortedList, new Comparator<CandidateMatchResult>() {
            @Override
            public int compare(CandidateMatchResult r1, CandidateMatchResult r2) {
                // 1. Eligible candidates come first
                if (r1.isCgpaEligible() && !r2.isCgpaEligible()) {
                    return -1; // r1 comes first
                }
                if (!r1.isCgpaEligible() && r2.isCgpaEligible()) {
                    return 1;  // r2 comes first
                }

                // 2. Higher match percentage comes first
                if (r1.getMatchPercentage() > r2.getMatchPercentage()) {
                    return -1;
                } else if (r1.getMatchPercentage() < r2.getMatchPercentage()) {
                    return 1;
                }

                // 3. Higher CGPA comes first (tie-breaker)
                if (r1.getCandidate().getCgpa() > r2.getCandidate().getCgpa()) {
                    return -1;
                } else if (r1.getCandidate().getCgpa() < r2.getCandidate().getCgpa()) {
                    return 1;
                }

                // 4. Alphabetical by candidate name
                return r1.getCandidate().getName().compareToIgnoreCase(r2.getCandidate().getName());
            }
        });

        return sortedList;
    }

    /**
     * Shortlists candidates who pass the CGPA eligibility check and meet the score threshold.
     * Candidates failing CGPA are strictly excluded from shortlisting even if skill match is 100%!
     */
    public ArrayList<CandidateMatchResult> shortlistCandidates(Job job, ArrayList<CandidateMatchResult> rankedResults,
                                                              double cutoffScore, ApplicationManager applicationManager) {
        ArrayList<CandidateMatchResult> shortlisted = new ArrayList<CandidateMatchResult>();

        for (CandidateMatchResult result : rankedResults) {
            // Rule: Do NOT shortlist candidates who fail minimum CGPA eligibility!
            if (!result.isCgpaEligible()) {
                if (result.getApplication() != null && applicationManager != null) {
                    applicationManager.updateStatus(result.getApplication().getApplicationId(),
                            ApplicationStatus.REJECTED,
                            "Disqualified: CGPA (" + result.getCandidate().getCgpa() + ") below min required (" + job.getMinCgpa() + ")");
                }
                continue; // Skip this candidate
            }

            // If candidate is eligible and meets or exceeds the cutoff score
            if (result.getMatchPercentage() >= cutoffScore) {
                shortlisted.add(result);
                if (result.getApplication() != null && applicationManager != null) {
                    applicationManager.updateStatus(result.getApplication().getApplicationId(),
                            ApplicationStatus.SHORTLISTED,
                            "Shortlisted: Match " + result.getMatchPercentage() + "%, CGPA " + result.getCandidate().getCgpa());
                }
            }
        }

        return shortlisted;
    }
}
