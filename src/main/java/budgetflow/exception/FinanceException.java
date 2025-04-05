package budgetflow.exception;

/**
 * A robust exception class for finance-related errors.
 * <p>
 * This exception includes an error code, a timestamp, and supports exception chaining.
 * It can be easily extended to include more contextual information if needed.
 *
 * @@author IgoyAI
 */
public class FinanceException extends Exception {
    private final String errorCode;
    private final long timestamp;

    /**
     * Constructs a FinanceException with a default error code.
     *
     * @param message the detail message.
     */
    public FinanceException(String message) {
        super(message);
        this.errorCode = "FINANCE_ERROR";
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * Constructs a FinanceException with a default error code and a cause.
     *
     * @param message the detail message.
     * @param cause   the cause of this exception.
     */
    public FinanceException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "FINANCE_ERROR";
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * Constructs a FinanceException with a specified error code.
     *
     * @param errorCode the specific error code.
     * @param message   the detail message.
     */
    public FinanceException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * Constructs a FinanceException with a specified error code and a cause.
     *
     * @param errorCode the specific error code.
     * @param message   the detail message.
     * @param cause     the cause of this exception.
     */
    public FinanceException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * Retrieves the error code associated with this exception.
     *
     * @return the error code.
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * Retrieves the timestamp when this exception was created.
     *
     * @return the timestamp in milliseconds.
     */
    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "FinanceException{" +
                "errorCode='" + errorCode + '\'' +
                ", timestamp=" + timestamp +
                ", message=" + getMessage() +
                '}';
    }
}
