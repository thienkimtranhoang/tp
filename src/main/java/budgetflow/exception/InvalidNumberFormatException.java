package budgetflow.exception;

/**
 * Raise error when user attempt to parse number in incorrect format/ data type
 */
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
