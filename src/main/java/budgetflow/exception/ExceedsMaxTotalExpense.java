package budgetflow.exception;

public class ExceedsMaxTotalExpense extends FinanceException {
    public ExceedsMaxTotalExpense(String message) {
        super(message);
    }
}
