package budgetflow.exception;

/**
 * Raise error if user attempts to enter unknown/ invalid command
 */
public class UnknownCommandException extends FinanceException {

    public static final String DEFAULT_UNKNOWN_COMMAND_MESSAGE = "I don't understand that command. Try again.";

    public UnknownCommandException(String message) {
        super(message);
    }
    public UnknownCommandException() {
        this(DEFAULT_UNKNOWN_COMMAND_MESSAGE);
    }
}
