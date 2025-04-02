package budgetflow.exception;

/**
 * Raise error when the data does not follow valid format dd-MM-yyyy or MM-yyyy
 */
public class InvalidDateException extends FinanceException {
    public InvalidDateException(String message) {
        super(message);
    }
}
