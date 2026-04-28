package exceptions;

/**
 * Thrown when researcher does not meet minimum h-index requirement
 */
public class LowHIndexException extends Exception {

    public LowHIndexException() {
        super("H-index is too low");
    }

    public LowHIndexException(String message) {
        super(message);
    }
}