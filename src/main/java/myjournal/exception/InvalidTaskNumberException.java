package myjournal.exception;

/**
 * A class to create invalid task number exceptions.
 *
 * @author Felissa Faustine
 */
public class InvalidTaskNumberException extends MyJournalException {

    /**
     * The constructor for the InvalidTaskNumberExceptions.
     *
     * @param message The error message.
     */
    public InvalidTaskNumberException(String message) {
        super(message);
    }

    /**
     * Gets the string representation of the exception.
     *
     * @return The string representation of the exception.
     */
    @Override
    public String toString() {
        return "InvalidTaskNumberException: " + getMessage();
    }
}