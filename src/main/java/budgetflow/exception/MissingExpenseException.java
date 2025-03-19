package budgetflow.exception;

public class MissingExpenseException extends FinanceException {
    public MissingExpenseException(String message) {
        super(message);
    }
}
