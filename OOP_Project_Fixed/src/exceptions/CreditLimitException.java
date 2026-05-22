package exceptions;

/**
 * Thrown when a student attempts to register for a course that would exceed the 21-credit limit.
 */
public class CreditLimitException extends Exception {

    public CreditLimitException() {
        super("Credit limit exceeded.");
    }

    public CreditLimitException(String message) {
        super(message);
    }
}