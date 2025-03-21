package budgetflow.exception;

/**
 * Raise error when no information regarding the expense is provided by user
 */
public class MissingExpenseException extends FinanceException {
    public MissingExpenseException(String message) {
        super(message);
    }
}
