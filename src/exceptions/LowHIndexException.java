package exceptions;

/**
 * Thrown when a researcher's H-Index is below the required minimum for a given action
 * (e.g., supervising a graduate student requires H-Index >= 3).
 */
public class LowHIndexException extends Exception {

    public LowHIndexException() {
        super("Researcher H-Index is too low.");
    }

    public LowHIndexException(String message) {
        super(message);
    }
}