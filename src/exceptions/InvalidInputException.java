package exceptions;

/**
 * Thrown when the user provides invalid input in the console.
 * For example: empty name, negative CGPA, or invalid email.
 * Concept used: Custom Exception and Inheritance.
 */
public class InvalidInputException extends NexcruitException {
    private static final long serialVersionUID = 1L;

    public InvalidInputException(String message) {
        super(message);
    }
}
