package budgetflow.exception;

/**
 * Raise error when there is no expense matching user's keyword
 */
public class UnfoundExpenseException extends FinanceException{
    public UnfoundExpenseException(String message) {
        super(message);
    }
}
