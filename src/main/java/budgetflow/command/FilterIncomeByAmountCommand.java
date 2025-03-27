package budgetflow.command;

import budgetflow.exception.FinanceException;
import budgetflow.expense.ExpenseList;
import budgetflow.income.Income;

import java.util.List;
import java.util.logging.Logger;

/**
 * Filters incomes based on a given amount range.
 * <p>
 * Expected input format:
 * <code>filter-income amount from/&lt;minAmount&gt; to/&lt;maxAmount&gt;</code>
 * <p>
 * This command extracts the minimum and maximum amounts, validates them, and displays all incomes
 * whose amounts fall within the specified range.
 *
 * @@author IgoyAI
 */
public class FilterIncomeByAmountCommand extends Command {
    private static final Logger logger = Logger.getLogger(FilterIncomeByAmountCommand.class.getName());
    private static final String COMMAND_PREFIX = "filter-income amount";

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
     * Executes the command to filter incomes based on the provided amount range.
     *
     * @param incomes     list of incomes.
     * @param expenseList expense list (unused in this command).
     * @throws FinanceException if the amount filter format is invalid.
     */
    @Override
    public void execute(List<Income> incomes, ExpenseList expenseList) throws FinanceException {
        String params = input.substring(COMMAND_PREFIX.length()).trim();
        // Expected parameters: "from/<minAmount> to/<maxAmount>"
        java.util.regex.Pattern fromPattern = java.util.regex.Pattern.compile("from/([0-9]+(\\.[0-9]+)?)");
        java.util.regex.Matcher fromMatcher = fromPattern.matcher(params);
        java.util.regex.Pattern toPattern = java.util.regex.Pattern.compile("to/([0-9]+(\\.[0-9]+)?)");
        java.util.regex.Matcher toMatcher = toPattern.matcher(params);

        if (!fromMatcher.find() || !toMatcher.find()) {
            throw new FinanceException("Invalid amount filter format. Usage: filter-income amount " +
                    "from/<minAmount> to/<maxAmount>");
        }

        double minAmount = Double.parseDouble(fromMatcher.group(1));
        double maxAmount = Double.parseDouble(toMatcher.group(1));
        if (minAmount > maxAmount) {
            throw new FinanceException("Minimum amount should not be greater than maximum amount.");
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Filtered Incomes by Amount (").append(minAmount)
                .append(" to ").append(maxAmount).append("):\n");
        boolean found = false;
        for (Income income : incomes) {
            double amount = income.getAmount();
            if (amount >= minAmount && amount <= maxAmount) {
                sb.append(income.getCategory())
                        .append(" | $").append(String.format("%.2f", amount))
                        .append(" | ").append(income.getDate()).append("\n");
                found = true;
            }
        }
        if (!found) {
            sb.append("No incomes found in the specified amount range.");
        }
        this.outputMessage = sb.toString();
        logger.info("Filtered incomes by amount: " + this.outputMessage);
    }
}
