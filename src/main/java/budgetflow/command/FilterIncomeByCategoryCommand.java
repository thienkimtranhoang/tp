package budgetflow.command;

import budgetflow.exception.FinanceException;
import budgetflow.exception.InvalidKeywordException;
import budgetflow.expense.ExpenseList;
import budgetflow.income.Income;
import java.util.List;
import java.util.logging.Logger;

/**
 * Filters incomes based on a given category.
 * <p>
 * Expected input format:
 * <code>filter-income category/&lt;category&gt;</code>
 * <p>
 * This command extracts the category from the input and displays all incomes
 * that match the provided category (case-insensitive). If the input is incomplete
 * or invalid, an InvalidKeywordException is thrown with the correct format.
 *
 * @@author IgoyAI
 */
public class FilterIncomeByCategoryCommand extends Command {
    private static final Logger logger = Logger.getLogger(FilterIncomeByCategoryCommand.class.getName());
    private static final String COMMAND_PREFIX = "filter-income category/";

    // Constants for messages and formats
    private static final String USAGE_GUIDE = "Usage: filter-income category/<category>\n" +
            "Example: filter-income category/Salary";
    private static final String HEADER_FORMAT = "Filtered Incomes by Category: %s%n%n";
    private static final String TABLE_ROW_FORMAT = "%-20s | %-10s | %-15s%n";
    private static final String TABLE_SEPARATOR_FORMAT = "%-20s-+-%-10s-+-%-15s%n";
    private static final String NO_INCOMES_FOUND = "No incomes found under the specified category.";

    /**
     * Constructs a FilterIncomeByCategoryCommand with the specified user input.
     *
     * @param input the user command input string.
     */
    public FilterIncomeByCategoryCommand(String input) {
        super(input);
        this.commandType = CommandType.READ;
    }

    /**
     * Executes the command to filter incomes based on the provided category.
     * If the input is incomplete or invalid, an InvalidKeywordException is thrown.
     *
     * @param incomes     list of incomes.
     * @param expenseList expense list (unused).
     * @throws FinanceException if an unexpected error occurs.
     */
    @Override
    public void execute(List<Income> incomes, ExpenseList expenseList) throws FinanceException {
        String trimmedInput = input.trim();
        // Incomplete input: "filter-income" or "filter-income category/" (no category provided).
        if (trimmedInput.equals("filter-income") || trimmedInput.equals(COMMAND_PREFIX)) {
            logger.info("Invalid command: Incomplete input for filter-income category command.");
            throw new InvalidKeywordException("Invalid command. Correct format: " + USAGE_GUIDE);
        }
        // Remove the command prefix and trim to extract the category.
        String category = input.substring(COMMAND_PREFIX.length()).trim();
        if (category.isEmpty()) {
            logger.info("Invalid command: Empty category provided for filter-income category command.");
            throw new InvalidKeywordException("Invalid command. Correct format: " + USAGE_GUIDE);
        }

        StringBuilder sb = new StringBuilder();
        sb.append(String.format(HEADER_FORMAT, category));
        sb.append(String.format(TABLE_ROW_FORMAT, "Category", "Amount", "Date"));
        sb.append(String.format(TABLE_SEPARATOR_FORMAT, "-".repeat(20), "-".repeat(10), "-".repeat(15)));
        boolean found = false;
        for (Income income : incomes) {
            if (income.getCategory().equalsIgnoreCase(category)) {
                sb.append(String.format(TABLE_ROW_FORMAT, income.getCategory(),
                        "$" + String.format("%.2f", income.getAmount()), income.getDate()));
                found = true;
            }
        }
        if (!found) {
            sb.append(NO_INCOMES_FOUND);
        }
        this.outputMessage = sb.toString();
        logger.info("Filtered incomes by category: " + this.outputMessage);
    }
}
