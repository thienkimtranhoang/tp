package budgetflow.exception;

/**
 * Raise error when there is no income matching user's keyword
 */
public class UnfoundIncomeException extends FinanceException {
    public UnfoundIncomeException(String message) {
        super(message);
    }
}
