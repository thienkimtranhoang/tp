package budgetflow.exception;

public class ExceedsMaxDigitException extends FinanceException {
    public ExceedsMaxDigitException(String message) {
        super(message);
    }
}
