package budgetflow.command;

import budgetflow.expense.ExpenseList;
import budgetflow.income.Income;
import java.util.List;
import java.util.logging.Logger;

/**
 * Displays the usage guide for filtering incomes.
 * <p>
 * When the user types only "filter-income", this command displays a user guide
 * showing the available ways to filter incomes.
 *
 * @@author IgoyAI
 */
public class FilterIncomeCommand extends Command {
    private static final Logger logger = Logger.getLogger(FilterIncomeCommand.class.getName());
    private static final int OUTPUT_WIDTH = 60;
    private static final String BORDER_CHAR = "=";

    /**
     * Constructs a FilterIncomeCommand with the specified user input.
     *
     * @param input the user command input string.
     */
    public FilterIncomeCommand(String input) {
        super(input);
        this.commandType = CommandType.READ;
    }

    /**
     * Executes the command. If the input is exactly "filter-income", it displays
     * a well-formatted usage guide for filtering incomes. Otherwise, it informs the user that
     * the command is unrecognized.
     *
     * @param incomes     list of incomes.
     * @param expenseList expense list.
     */
    @Override
    public void execute(List<Income> incomes, ExpenseList expenseList) {
        String trimmedInput = input.trim();
        if (trimmedInput.equals("filter-income")) {
            StringBuilder sb = new StringBuilder();
            String border = BORDER_CHAR.repeat(OUTPUT_WIDTH);
            sb.append(border).append(System.lineSeparator());
            sb.append(centerText("FILTER INCOME USAGE GUIDE", OUTPUT_WIDTH)).append(System.lineSeparator());
            sb.append(border).append(System.lineSeparator());
            sb.append(System.lineSeparator());

            // Section 1: Filter by Category
            sb.append("1. Filter by Category:").append(System.lineSeparator());
            sb.append("   Usage   : filter-income category/<category>").append(System.lineSeparator());
            sb.append("   Example : filter-income category/Salary").append(System.lineSeparator());
            sb.append(System.lineSeparator());

            // Section 2: Filter by Amount
            sb.append("2. Filter by Amount:").append(System.lineSeparator());
            sb.append("   Usage   : filter-income amount from/<minAmount> to/<maxAmount>").append(System.lineSeparator());
            sb.append("   Example : filter-income amount from/1000 to/5000").append(System.lineSeparator());
            sb.append(System.lineSeparator());

            // Section 3: Filter by Date
            sb.append("3. Filter by Date:").append(System.lineSeparator());
            sb.append("   Usage   : filter-income date from/DD-MM-YYYY to/DD-MM-YYYY").append(System.lineSeparator());
            sb.append("   Example : filter-income date from/01-01-2023 to/31-12-2023").append(System.lineSeparator());
            sb.append(System.lineSeparator());

            sb.append(border);
            this.outputMessage = sb.toString();
            logger.info("Displayed filter-income usage guide.");
        } else {
            this.outputMessage = "Unknown filter-income command. Type 'filter-income' for usage instructions.";
            logger.info("Unknown filter-income command input: " + input);
        }
    }

    /**
     * Centers the given text within the specified width.
     *
     * @param text  the text to center.
     * @param width the total width for centering.
     * @return a centered string.
     */
    private String centerText(String text, int width) {
        if (text.length() >= width) {
            return text;
        }
        int padding = (width - text.length()) / 2;
        return " ".repeat(padding) + text;
    }
}
