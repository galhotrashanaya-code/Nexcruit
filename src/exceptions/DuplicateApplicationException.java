package exceptions;

/**
 * Thrown when a candidate tries to apply for the same job more than once.
 * Concept used: Custom Exception and Inheritance.
 */
public class DuplicateApplicationException extends NexcruitException {
    private static final long serialVersionUID = 1L;

    public DuplicateApplicationException(String message) {
        super(message);
    }
}
