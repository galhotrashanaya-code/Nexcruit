package exceptions;

/**
 * Base exception class for Nexcruit.
 * All our custom exceptions extend this class.
 * Concept used: Inheritance and Custom Exception Handling.
 */
public class NexcruitException extends Exception {
    private static final long serialVersionUID = 1L;

    public NexcruitException(String message) {
        super(message);
    }
}
