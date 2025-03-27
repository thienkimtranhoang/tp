package budgetflow.command;

import budgetflow.exception.FinanceException;
import budgetflow.expense.ExpenseList;
import budgetflow.income.Income;
import budgetflow.parser.DateValidator;

import java.time.LocalDate;
import java.util.List;
import java.util.logging.Logger;

/**
 * Filters incomes based on a given date range.
 * <p>
 * Expected input format:
 * <code>filter-income date from/DD-MM-YYYY to/DD-MM-YYYY</code>
 * <p>
 * This command parses the start and end dates, validates them, and displays all incomes whose
 * dates fall within the specified range.
 *
 * @@author IgoyAI
 */
public class FilterIncomeByDateCommand extends Command {
    private static final Logger logger = Logger.getLogger(FilterIncomeByDateCommand.class.getName());
    private static final String COMMAND_PREFIX = "filter-income date";

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
     * Executes the command to filter incomes based on the provided date range.
     *
     * @param incomes     list of incomes.
     * @param expenseList expense list (unused in this command).
     * @throws FinanceException if the filter format is invalid or dates are incorrect.
     */
    @Override
    public void execute(List<Income> incomes, ExpenseList expenseList) throws FinanceException {
        // Remove the command prefix and trim remaining parameters.
        String params = input.substring(COMMAND_PREFIX.length()).trim();
        // Use non-space tokens to extract parameters.
        java.util.regex.Pattern fromPattern = java.util.regex.Pattern.compile("from/(\\S+)");
        java.util.regex.Matcher fromMatcher = fromPattern.matcher(params);
        java.util.regex.Pattern toPattern = java.util.regex.Pattern.compile("to/(\\S+)");
        java.util.regex.Matcher toMatcher = toPattern.matcher(params);

        if (!fromMatcher.find() || !toMatcher.find()) {
            throw new FinanceException("Invalid date filter format. Usage: filter-income date from/DD-MM-YYYY to/DD-MM-YYYY");
        }

        String fromDateStr = fromMatcher.group(1);
        String toDateStr = toMatcher.group(1);

        if (!DateValidator.isValidDate(fromDateStr) || !DateValidator.isValidDate(toDateStr)) {
            throw new FinanceException("One or both dates are invalid. Please use DD-MM-YYYY format.");
        }

        LocalDate fromDate = LocalDate.parse(fromDateStr, DateValidator.getFullDateFormatter());
        LocalDate toDate = LocalDate.parse(toDateStr, DateValidator.getFullDateFormatter());
        if (fromDate.isAfter(toDate)) {
            throw new FinanceException("Start date must be before or equal to end date.");
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Filtered Incomes by Date (").append(fromDateStr).append(" to ").append(toDateStr).append("):\n");
        boolean found = false;
        for (Income income : incomes) {
            String incomeDateStr = income.getDate();
            if (DateValidator.isValidDate(incomeDateStr)) {
                LocalDate incomeDate = LocalDate.parse(incomeDateStr, DateValidator.getFullDateFormatter());
                if ((incomeDate.isEqual(fromDate) || incomeDate.isAfter(fromDate)) &&
                        (incomeDate.isEqual(toDate) || incomeDate.isBefore(toDate))) {
                    sb.append(income.getCategory())
                            .append(" | $").append(String.format("%.2f", income.getAmount()))
                            .append(" | ").append(income.getDate()).append("\n");
                    found = true;
                }
            }
        }
        if (!found) {
            sb.append("No incomes found in the specified date range.");
        }
        this.outputMessage = sb.toString();
        logger.info("Filtered incomes by date: " + this.outputMessage);
    }
}
