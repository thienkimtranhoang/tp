package budgetflow.command;

import budgetflow.exception.FinanceException;
import budgetflow.expense.ExpenseList;
import budgetflow.income.Income;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Filters incomes based on a given amount range.
 * <p>
 * Expected input format:
 * <code>filter-income amount from/&lt;minAmount&gt; to/&lt;maxAmount&gt;</code>
 * <p>
 * This command extracts the minimum and maximum amounts, validates them, and displays all incomes
 * whose amounts fall within the specified range in a table format. If the input is incomplete,
 * the usage guide is displayed.
 *
 * @@author IgoyAI
 */
public class FilterIncomeByAmountCommand extends Command {
    private static final Logger logger =
            Logger.getLogger(FilterIncomeByAmountCommand.class.getName());
    private static final String COMMAND_PREFIX = "filter-income amount";
    private static final Pattern FROM_PATTERN =
            Pattern.compile("from/([0-9]+(\\.[0-9]+)?)");
    private static final Pattern TO_PATTERN =
            Pattern.compile("to/([0-9]+(\\.[0-9]+)?)");

    // Constants for messages and formats
    private static final String USAGE_GUIDE =
            "Usage: filter-income amount from/<minAmount> to/<maxAmount>\n"
                    + "Example: filter-income amount from/1000 to/5000";
    private static final String ERROR_MIN_GREATER_THAN_MAX =
            "Minimum amount should not be greater than maximum amount.";
    private static final String FILTERED_HEADER =
            "Filtered Incomes by Amount Range: %.2f to %.2f%n%n";
    private static final String TABLE_ROW_FORMAT =
            "%-20s | %-10s | %-15s%n";
    private static final String TABLE_SEPARATOR_FORMAT =
            "%-20s-+-%-10s-+-%-15s%n";
    private static final String EMPTY_RESULT_MESSAGE =
            "No incomes found in the specified amount range.";

    /**
     * Constructs a FilterIncomeByAmountCommand with the specified user input.
     *
     * @param input the user command input string.
     */
    public FilterIncomeByAmountCommand(String input) {
        super(input);
        this.commandType = CommandType.READ;
    }

    /**
     * Executes the command to filter incomes by amount range.
     * If the input is incomplete or in an invalid format, the usage guide is displayed.
     *
     * @param incomes     list of incomes.
     * @param expenseList expense list (unused).
     * @throws FinanceException if an unexpected error occurs.
     */
    @Override
    public void execute(List<Income> incomes, ExpenseList expenseList)
            throws FinanceException {
        String trimmedInput = input.trim();
        // Check for incomplete input: "filter-income" or "filter-income amount" (no parameters).
        if (trimmedInput.equals("filter-income") || trimmedInput.equals(COMMAND_PREFIX)) {
            this.outputMessage = USAGE_GUIDE;
            logger.info("Displayed usage guide for filter-income amount command.");
            return;
        }

        // Extract parameters after the command prefix.
        String params = input.substring(COMMAND_PREFIX.length()).trim();
        Matcher fromMatcher = FROM_PATTERN.matcher(params);
        Matcher toMatcher = TO_PATTERN.matcher(params);
        if (!fromMatcher.find() || !toMatcher.find()) {
            this.outputMessage = USAGE_GUIDE;
            logger.info("Displayed usage guide for filter-income amount command due to invalid format.");
            return;
        }

        double minAmount = Double.parseDouble(fromMatcher.group(1));
        double maxAmount = Double.parseDouble(toMatcher.group(1));
        if (minAmount > maxAmount) {
            this.outputMessage = ERROR_MIN_GREATER_THAN_MAX;
            logger.info("Displayed error: Minimum amount is greater than maximum amount.");
            return;
        }

        // Filter incomes within the given amount range using Java streams.
        List<Income> filteredIncomes = incomes.stream()
                .filter(income -> income.getAmount() >= minAmount
                        && income.getAmount() <= maxAmount)
                .collect(Collectors.toList());

        StringBuilder sb = new StringBuilder();
        sb.append(String.format(FILTERED_HEADER, minAmount, maxAmount));
        // Define table header format with fixed column widths.
        sb.append(String.format(TABLE_ROW_FORMAT, "Category", "Amount", "Date"));
        sb.append(String.format(TABLE_SEPARATOR_FORMAT,
                "-".repeat(20), "-".repeat(10), "-".repeat(15)));
        if (filteredIncomes.isEmpty()) {
            sb.append(EMPTY_RESULT_MESSAGE);
        } else {
            for (Income income : filteredIncomes) {
                sb.append(String.format(TABLE_ROW_FORMAT, income.getCategory(),
                        "$" + String.format("%.2f", income.getAmount()), income.getDate()));
            }
        }
        this.outputMessage = sb.toString();
        logger.info("Filtered incomes by amount: " + this.outputMessage);
    }
}
