package budgetflow.exception;

public class MissingAmountException extends FinanceException {
    public MissingAmountException(String message) {
        super(message);
    }
}
