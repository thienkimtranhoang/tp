package budgetflow.exception;

public class UnfoundExpenseException extends FinanceException{
    public UnfoundExpenseException(String message) {
        super(message);
    }
}
