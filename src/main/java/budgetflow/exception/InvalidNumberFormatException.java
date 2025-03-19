package budgetflow.exception;

public class InvalidNumberFormatException extends FinanceException{

    public static final String DEFAULT_INVALID_NUMBER_FORMAT_MESSAGE =
            "Error: Invalid amount format. Please enter a valid number.";

    public InvalidNumberFormatException(String message) {
        super(message);
    }
    public InvalidNumberFormatException() {
        this(DEFAULT_INVALID_NUMBER_FORMAT_MESSAGE);
    }
}
