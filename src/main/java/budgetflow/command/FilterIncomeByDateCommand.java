package budgetflow.command;

import budgetflow.exception.FinanceException;
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
 * dates fall within the specified range. If the input is incomplete,
 * the usage guide is displayed.
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
    private static final String FILTER_HEADER_FORMAT = "Filtered Incomes by Date (%s to %s):\n";
    private static final String NO_INCOMES_FOUND_MESSAGE = "No incomes found in the specified date range.";
    private static final String FROM_REGEX = "from/(\\S+)";
    private static final String TO_REGEX = "to/(\\S+)";

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
     * If the input is incomplete or invalid, the usage guide is displayed.
     *
     * @param incomes     list of incomes.
     * @param expenseList expense list (unused).
     * @throws FinanceException if an unexpected error occurs.
     */
    @Override
    public void execute(List<Income> incomes, ExpenseList expenseList) throws FinanceException {
        String trimmedInput = input.trim();
        // Check for incomplete input: "filter-income" or "filter-income date" (no parameters).
        if (trimmedInput.equals("filter-income") || trimmedInput.equals("filter-income date")) {
            this.outputMessage = USAGE_GUIDE;
            logger.info("Displayed usage guide for filter-income date command.");
            return;
        }
        // Remove the command prefix and trim remaining parameters.
        String params = input.substring(COMMAND_PREFIX.length()).trim();
        Pattern fromPattern = Pattern.compile(FROM_REGEX);
        Matcher fromMatcher = fromPattern.matcher(params);
        Pattern toPattern = Pattern.compile(TO_REGEX);
        Matcher toMatcher = toPattern.matcher(params);
        if (!fromMatcher.find() || !toMatcher.find()) {
            this.outputMessage = USAGE_GUIDE;
            logger.info("Displayed usage guide for filter-income date command due to invalid format.");
            return;
        }
        String fromDateStr = fromMatcher.group(1);
        String toDateStr = toMatcher.group(1);
        if (!DateValidator.isValidDate(fromDateStr) || !DateValidator.isValidDate(toDateStr)) {
            this.outputMessage = INVALID_DATE_MESSAGE;
            logger.info("Displayed error: one or both dates are invalid.");
            return;
        }
        LocalDate fromDate = LocalDate.parse(fromDateStr, DateValidator.getFullDateFormatter());
        LocalDate toDate = LocalDate.parse(toDateStr, DateValidator.getFullDateFormatter());
        if (fromDate.isAfter(toDate)) {
            this.outputMessage = DATE_RANGE_ERROR_MESSAGE;
            logger.info("Displayed error: start date is after end date.");
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(String.format(FILTER_HEADER_FORMAT, fromDateStr, toDateStr));
        boolean found = false;
        for (Income income : incomes) {
            String incomeDateStr = income.getDate();
            if (DateValidator.isValidDate(incomeDateStr)) {
                LocalDate incomeDate = LocalDate.parse(incomeDateStr, DateValidator.getFullDateFormatter());
                if ((incomeDate.isEqual(fromDate) || incomeDate.isAfter(fromDate)) &&
                        (incomeDate.isEqual(toDate) || incomeDate.isBefore(toDate))) {
                    sb.append(income.getCategory())
                            .append(" | $")
                            .append(String.format("%.2f", income.getAmount()))
                            .append(" | ")
                            .append(income.getDate())
                            .append("\n");
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
