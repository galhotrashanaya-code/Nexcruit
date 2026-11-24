package nexcruit;

import auth.Candidate;
import java.util.ArrayList;

/**
 * Interface defining the contract for Candidate Matching and Eligibility checks.
 * Developed by: Shanaya (Matching Engine Module - Evaluation 1)
 *
 * Concepts used:
 * - Java Interface (abstraction)
 * - ArrayList<String>
 * - Method Overloading
 */
public interface Matchable {

    /**
     * Calculates the skill matching percentage between candidate skills and required skills.
     * Works directly with ArrayList<String>.
     *
     * Formula: (Matched Skills / Total Required Skills) * 100
     *
     * @param candidateSkills List of skills possessed by the candidate
     * @param requiredSkills List of skills required by the job
     * @return Matching percentage from 0.0 to 100.0
     */
    double calculateMatchScore(ArrayList<String> candidateSkills, ArrayList<String> requiredSkills);

    /**
     * Overloaded method that calculates match score using actual Candidate and Job objects.
     *
     * @param candidate The Candidate object
     * @param job The Job object
     * @return Matching percentage from 0.0 to 100.0
     */
    double calculateMatchScore(Candidate candidate, Job job);

    /**
     * Checks if the candidate's CGPA meets or exceeds the minimum required CGPA.
     *
     * @param candidateCgpa The CGPA of the candidate
     * @param minCgpa The minimum required CGPA for the job
     * @return true if candidate meets requirement, false otherwise
     */
    boolean checkCgpaEligibility(double candidateCgpa, double minCgpa);

    /**
     * Overloaded method that checks CGPA eligibility using actual Candidate and Job objects.
     *
     * @param candidate The Candidate object
     * @param job The Job object
     * @return true if candidate meets or exceeds job's minimum CGPA
     */
    boolean checkCgpaEligibility(Candidate candidate, Job job);

    /**
     * Returns the list of skills that successfully matched.
     *
     * @param candidateSkills List of skills possessed by the candidate
     * @param requiredSkills List of skills required by the job
     * @return ArrayList of matched skills
     */
    ArrayList<String> getMatchedSkills(ArrayList<String> candidateSkills, ArrayList<String> requiredSkills);
}
