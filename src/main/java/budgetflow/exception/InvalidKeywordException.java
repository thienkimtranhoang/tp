package budgetflow.exception;

/**
 * Raise error when the keyword does not match to the tag's format
 */
public class InvalidKeywordException extends FinanceException {
    public InvalidKeywordException(String message) {
        super(message);
    }
}
