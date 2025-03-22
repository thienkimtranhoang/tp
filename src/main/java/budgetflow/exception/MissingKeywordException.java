package budgetflow.exception;

/**
 * Raise error when user attempts to query expense without any keyword
 */
public class MissingKeywordException extends FinanceException {
    public MissingKeywordException(String message) {
        super(message);
    }
}
