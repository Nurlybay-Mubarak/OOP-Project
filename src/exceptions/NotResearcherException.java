package exceptions;

/**
 * Thrown when an operation that requires a Researcher role is attempted by a non-researcher.
 */
public class NotResearcherException extends Exception {

    public NotResearcherException() {
        super("This user is not a researcher.");
    }

    public NotResearcherException(String message) {
        super(message);
    }
}