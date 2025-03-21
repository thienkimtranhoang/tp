package budgetflow.exception;

/**
 * Raise error when no category of income/ expense is provided
 */
public class MissingCategoryException extends FinanceException {
    public MissingCategoryException(String message) {
        super(message);
    }
}
