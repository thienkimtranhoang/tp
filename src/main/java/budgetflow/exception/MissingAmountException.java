package budgetflow.exception;

/**
 * Raise error if no amount regarding income/ expense is provided
 */
public class MissingAmountException extends FinanceException {
    public MissingAmountException(String message) {
        super(message);
    }
}
