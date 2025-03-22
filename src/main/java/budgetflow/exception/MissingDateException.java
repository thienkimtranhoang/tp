package budgetflow.exception;

/**
 * Raise error when no exact date of income/ expense is provided
 */
public class MissingDateException extends FinanceException {
    public MissingDateException(String message) {
        super(message);
    }
}
