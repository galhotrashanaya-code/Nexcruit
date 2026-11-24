package exceptions;

/**
 * Thrown when login fails or when a user cannot be found in the system.
 * Concept used: Custom Exception and Inheritance.
 */
public class UserNotFoundException extends NexcruitException {
    private static final long serialVersionUID = 1L;

    public UserNotFoundException(String message) {
        super(message);
    }
}
