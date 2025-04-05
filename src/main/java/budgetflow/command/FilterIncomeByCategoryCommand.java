package budgetflow.command;

import budgetflow.exception.FinanceException;
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
 * that match the provided category (case-insensitive). If the input is incomplete,
 * it displays the usage guide.
 *
 * @@author IgoyAI
 */
public class FilterIncomeByCategoryCommand extends Command {
    private static final Logger logger = Logger.getLogger(FilterIncomeByCategoryCommand.class.getName());
    private static final String COMMAND_PREFIX = "filter-income category/";

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
     * If the command is incomplete, the usage guide is displayed.
     *
     * @param incomes     list of incomes.
     * @param expenseList expense list (unused).
     * @throws FinanceException if an unexpected error occurs.
     */
    @Override
    public void execute(List<Income> incomes, ExpenseList expenseList) throws FinanceException {
        String trimmedInput = input.trim();
        // If input equals "filter-income" or "filter-income category/" (no category provided)
        if (trimmedInput.equals("filter-income") || trimmedInput.equals("filter-income category/")) {
            this.outputMessage = "Usage: filter-income category/<category>\n" +
                    "Example: filter-income category/Salary";
            logger.info("Displayed usage guide for filter-income category command.");
            return;
        }
        // Remove the command prefix and trim to extract the category.
        String category = input.substring(COMMAND_PREFIX.length()).trim();
        if (category.isEmpty()) {
            this.outputMessage = "Usage: filter-income category/<category>\n" +
                    "Example: filter-income category/Salary";
            logger.info("Displayed usage guide for filter-income category command due to empty category.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Filtered Incomes by Category: %s%n%n", category));
        // Define the table header format with fixed column widths.
        String rowFormat = "%-20s | %-10s | %-15s%n";
        sb.append(String.format(rowFormat, "Category", "Amount", "Date"));
        sb.append(String.format("%-20s-+-%-10s-+-%-15s%n", "-".repeat(20), "-".repeat(10), "-".repeat(15)));
        boolean found = false;
        for (Income income : incomes) {
            if (income.getCategory().equalsIgnoreCase(category)) {
                sb.append(String.format(rowFormat, income.getCategory(),
                        "$" + String.format("%.2f", income.getAmount()), income.getDate()));
                found = true;
            }
        }
        if (!found) {
            sb.append("No incomes found under the specified category.");
        }
        this.outputMessage = sb.toString();
        logger.info("Filtered incomes by category: " + this.outputMessage);
    }
}
