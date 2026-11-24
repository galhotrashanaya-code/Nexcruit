package matching;

import applications.Application;
import auth.Candidate;
import jobs.Job;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Helper class that stores the evaluation result for a candidate.
 * Contains match percentage, CGPA eligibility status, matched and missing skills.
 * Concepts used:
 * - Encapsulation (private fields with getters)
 * - Serializable
 */
public class CandidateMatchResult implements Serializable {
    private static final long serialVersionUID = 1L;

    private Candidate candidate;
    private Application application;
    private Job job;
    private double matchPercentage;
    private boolean cgpaEligible;
    private ArrayList<String> matchedSkills;
    private ArrayList<String> missingSkills;
    private double weightedScore;

    public CandidateMatchResult(Candidate candidate, Application application, Job job,
                                double matchPercentage, boolean cgpaEligible,
                                ArrayList<String> matchedSkills, ArrayList<String> missingSkills,
                                double weightedScore) {
        this.candidate = candidate;
        this.application = application;
        this.job = job;
        this.matchPercentage = matchPercentage;
        this.cgpaEligible = cgpaEligible;
        this.matchedSkills = (matchedSkills != null) ? matchedSkills : new ArrayList<String>();
        this.missingSkills = (missingSkills != null) ? missingSkills : new ArrayList<String>();
        this.weightedScore = weightedScore;
    }

    // Getters
    public Candidate getCandidate() {
        return candidate;
    }

    public Application getApplication() {
        return application;
    }

    public Job getJob() {
        return job;
    }

    public double getMatchPercentage() {
        return matchPercentage;
    }

    public boolean isCgpaEligible() {
        return cgpaEligible;
    }

    public ArrayList<String> getMatchedSkills() {
        return matchedSkills;
    }

    public ArrayList<String> getMissingSkills() {
        return missingSkills;
    }

    public double getWeightedScore() {
        return weightedScore;
    }

    @Override
    public String toString() {
        return "[" + candidate.getUserId() + "] " + candidate.getName() + " | Match: " +
                matchPercentage + "% | Eligible: " + (cgpaEligible ? "YES" : "NO");
    }
}
