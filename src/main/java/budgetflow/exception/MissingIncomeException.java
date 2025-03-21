package budgetflow.exception;

/**
 * Raising error when no information about income is provided by user
 */
public class MissingIncomeException extends FinanceException {
    public MissingIncomeException(String message) {
        super(message);
    }
}
