package exceptions;

/**
 * Thrown when a candidate does not meet the basic eligibility criteria
 * such as the minimum required CGPA.
 * Concept used: Custom Exception and Inheritance.
 */
public class IneligibleCandidateException extends NexcruitException {
    private static final long serialVersionUID = 1L;

    public IneligibleCandidateException(String message) {
        super(message);
    }
}
