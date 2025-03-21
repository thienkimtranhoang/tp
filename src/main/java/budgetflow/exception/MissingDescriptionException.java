package budgetflow.exception;

/**
 * Raise error when the description about expense is not provided
 */
public class MissingDescriptionException extends FinanceException {
    public MissingDescriptionException(String message) {
        super(message);
    }
}
