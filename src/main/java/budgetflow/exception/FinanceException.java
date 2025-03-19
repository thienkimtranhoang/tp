package budgetflow.exception;

public class FinanceException extends Exception {
    private String message;
    public FinanceException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
