package matching;

import auth.Candidate;
import jobs.Job;

import java.util.ArrayList;

/**
 * Interface defining the methods for candidate evaluation and skill matching.
 * Concept used: Java Interface (defines what methods must be implemented).
 */
public interface Matchable {

    // Calculates skill match percentage: (Matched Skills / Total Required Skills) * 100
    double calculateMatchScore(Candidate candidate, Job job);

    // Checks if candidate meets minimum CGPA requirement
    boolean checkCgpaEligibility(Candidate candidate, Job job);

    // Returns the list of skills that matched
    ArrayList<String> getMatchedSkills(Candidate candidate, Job job);

    // Returns the list of required skills that the candidate is missing
    ArrayList<String> getMissingSkills(Candidate candidate, Job job);

    // Extensible method for future weighted scoring (Skills 50%, CGPA 25%, Edu 15%, Exp 10%)
    double calculateWeightedScore(Candidate candidate, Job job);
}
