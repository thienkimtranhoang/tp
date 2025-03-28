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
 * that match the provided category (case-insensitive).
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
     *
     * @param incomes     list of incomes.
     * @param expenseList expense list (unused in this command).
     * @throws FinanceException if no category is provided.
     */
    @Override
    public void execute(List<Income> incomes, ExpenseList expenseList) throws FinanceException {
        // Remove the command prefix and trim to extract the category.
        String category = input.substring(COMMAND_PREFIX.length()).trim();
        if (category.isEmpty()) {
            throw new FinanceException("Invalid category filter format. Usage: filter-income category/<category>");
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Filtered Incomes by Category (").append(category).append("):\n");
        boolean found = false;
        for (Income income : incomes) {
            if (income.getCategory().equalsIgnoreCase(category)) {
                sb.append(income.getCategory())
                        .append(" | $").append(String.format("%.2f", income.getAmount()))
                        .append(" | ").append(income.getDate()).append("\n");
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
