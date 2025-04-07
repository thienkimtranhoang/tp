package budgetflow.command;

import budgetflow.exception.FinanceException;
import budgetflow.exception.InvalidKeywordException;
import budgetflow.expense.ExpenseList;
import budgetflow.income.Income;
import budgetflow.parser.DateValidator;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Filters incomes based on a given date range.
 * <p>
 * Expected input format:
 * <code>filter-income date from/DD-MM-YYYY to/DD-MM-YYYY</code>
 * <p>
 * This command parses the start and end dates, validates them, and displays all incomes whose
 * dates fall within the specified range. If the input is incomplete or invalid,
 * an InvalidKeywordException is thrown with the correct format.
 *
 * @@author IgoyAI
 */
public class FilterIncomeByDateCommand extends Command {
    private static final Logger logger = Logger.getLogger(FilterIncomeByDateCommand.class.getName());
    private static final String COMMAND_PREFIX = "filter-income date";

    // Constants for messages and formats
    private static final String USAGE_GUIDE = "Usage: filter-income date from/DD-MM-YYYY to/DD-MM-YYYY\n" +
            "Example: filter-income date from/01-01-2023 to/31-12-2023";
    private static final String INVALID_DATE_MESSAGE = "One or both dates are invalid. Please use DD-MM-YYYY format.";
    private static final String DATE_RANGE_ERROR_MESSAGE = "Start date must be before or equal to end date.";
    private static final String NO_INCOMES_FOUND_MESSAGE = "No incomes found in the specified date range.";

    private static final String FROM_REGEX = "from/(\\S+)";
    private static final String TO_REGEX = "to/(\\S+)";

    // Format strings for table output:
    private static final String HEADER_FORMAT = "Filtered Incomes by Date (%s to %s):%n%n";
    private static final String TABLE_HEADER = "%-20s | %-10s | %-15s%n";
    private static final String SEPARATOR = "%-20s-+-%-10s-+-%-15s%n";
    private static final String ROW_FORMAT = "%-20s | $%-9.2f | %-15s%n";

    /**
     * Constructs a FilterIncomeByDateCommand with the specified user input.
     *
     * @param input the user command input string.
     */
    public FilterIncomeByDateCommand(String input) {
        super(input);
        this.commandType = CommandType.READ;
    }

    /**
     * Executes the command to filter incomes by date range.
     * If the input is incomplete or invalid, an InvalidKeywordException is thrown.
     *
     * @param incomes     list of incomes.
     * @param expenseList expense list (unused).
     * @throws FinanceException if an unexpected error occurs.
     */
    @Override
    public void execute(List<Income> incomes, ExpenseList expenseList) throws FinanceException {
        String trimmedInput = input.trim();
        // Incomplete input: "filter-income" or "filter-income date" (no parameters).
        if (trimmedInput.equals("filter-income") || trimmedInput.equals("filter-income date")) {
            logger.info("Invalid command: Incomplete input for filter-income date command.");
            throw new InvalidKeywordException("Invalid command. Correct format: " + USAGE_GUIDE);
        }
        // Remove the command prefix and trim remaining parameters.
        String params = input.substring(COMMAND_PREFIX.length()).trim();
        Pattern fromPattern = Pattern.compile(FROM_REGEX);
        Matcher fromMatcher = fromPattern.matcher(params);
        Pattern toPattern = Pattern.compile(TO_REGEX);
        Matcher toMatcher = toPattern.matcher(params);
        if (!fromMatcher.find() || !toMatcher.find()) {
            logger.info("Invalid command: Missing 'from' or 'to' parameter for filter-income date command.");
            throw new InvalidKeywordException("Invalid command. Correct format: " + USAGE_GUIDE);
        }
        String fromDateStr = fromMatcher.group(1);
        String toDateStr = toMatcher.group(1);
        if (!DateValidator.isValidDate(fromDateStr) || !DateValidator.isValidDate(toDateStr)) {
            logger.info("Invalid command: One or both dates are invalid.");
            throw new InvalidKeywordException("Invalid command. " + INVALID_DATE_MESSAGE
                    + "\nCorrect format: " + USAGE_GUIDE);
        }
        LocalDate fromDate = LocalDate.parse(fromDateStr, DateValidator.getFullDateFormatter());
        LocalDate toDate = LocalDate.parse(toDateStr, DateValidator.getFullDateFormatter());
        if (fromDate.isAfter(toDate)) {
            logger.info("Invalid command: Start date is after end date.");
            throw new InvalidKeywordException("Invalid command. " + DATE_RANGE_ERROR_MESSAGE
                    + "\nCorrect format: " + USAGE_GUIDE);
        }

        // Build table header and separator
        StringBuilder sb = new StringBuilder();
        sb.append(String.format(HEADER_FORMAT, fromDateStr, toDateStr));
        sb.append(String.format(TABLE_HEADER, "Category", "Amount", "Date"));
        sb.append(String.format(SEPARATOR, "-".repeat(20), "-".repeat(10), "-".repeat(15)));

        boolean found = false;
        for (Income income : incomes) {
            String incomeDateStr = income.getDate();
            if (DateValidator.isValidDate(incomeDateStr)) {
                LocalDate incomeDate = LocalDate.parse(incomeDateStr, DateValidator.getFullDateFormatter());
                if ((incomeDate.isEqual(fromDate) || incomeDate.isAfter(fromDate)) &&
                        (incomeDate.isEqual(toDate) || incomeDate.isBefore(toDate))) {
                    sb.append(String.format(ROW_FORMAT, income.getCategory(), income.getAmount(), income.getDate()));
                    found = true;
                }
            }
        }
        if (!found) {
            sb.append(NO_INCOMES_FOUND_MESSAGE);
        }
        this.outputMessage = sb.toString();
        logger.info("Filtered incomes by date: " + this.outputMessage);
    }
}
