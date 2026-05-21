package exceptions;

/**
 * Thrown when a student has failed too many courses and is subject to academic dismissal.
 * The threshold is typically 3 failed courses in one semester.
 */
public class TooManyFailsException extends Exception {

    public TooManyFailsException() {
        super("Student has failed too many courses.");
    }

    public TooManyFailsException(String message) {
        super(message);
    }
}