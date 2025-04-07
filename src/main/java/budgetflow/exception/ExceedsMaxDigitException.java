package budgetflow.exception;

//@@author thienkimtranhoang

/**
 * Exception thrown when a numerical input exceeds the maximum allowed number of digits.
 * This is typically used to prevent values that are too large to be processed or displayed.
 */
public class ExceedsMaxDigitException extends FinanceException {

    /**
     * Constructs a new ExceedsMaxDigitException with the specified detail message.
     *
     * @param message The detail message explaining the reason for the exception.
     */
    public ExceedsMaxDigitException(String message) {
        super(message);
    }
}
