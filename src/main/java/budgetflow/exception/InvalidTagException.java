package budgetflow.exception;

/**
 * Raise error when invalid/ unknown tag is parsed
 */
public class InvalidTagException extends FinanceException {
    public InvalidTagException(String message) {
        super(message);
    }
}
