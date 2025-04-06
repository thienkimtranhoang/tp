package budgetflow.command;

import budgetflow.expense.ExpenseList;
import budgetflow.income.Income;
import java.util.List;
import java.util.logging.Logger;
// @@author IgoyAI
/**
 * Displays the usage guide for filtering incomes.
 * <p>
 * When the user types only "filter-income", this command displays a user guide
 * showing the available ways to filter incomes.
 *
 *
 */
public class FilterIncomeCommand extends Command {
    private static final Logger logger = Logger.getLogger(FilterIncomeCommand.class.getName());

    // Constants for layout and formatting
    private static final int OUTPUT_WIDTH = 60;
    private static final String BORDER_CHAR = "=";
    private static final String LINE_SEPARATOR = System.lineSeparator();

    // Command validation constant
    private static final String COMMAND_FILTER_INCOME = "filter-income";

    // Constants for header text and usage guide sections
    private static final String HEADER_TEXT = "FILTER INCOME USAGE GUIDE";

    // Section 1: Filter by Category
    private static final String CATEGORY_SECTION_HEADER = "1. Filter by Category:";
    private static final String CATEGORY_USAGE = "   Usage   : filter-income category/<category>";
    private static final String CATEGORY_EXAMPLE = "   Example : filter-income category/Salary";

    // Section 2: Filter by Amount
    private static final String AMOUNT_SECTION_HEADER = "2. Filter by Amount:";
    private static final String AMOUNT_USAGE = "   Usage   : filter-income amount from/<minAmount> to/<maxAmount>";
    private static final String AMOUNT_EXAMPLE = "   Example : filter-income amount from/1000 to/5000";

    // Section 3: Filter by Date
    private static final String DATE_SECTION_HEADER = "3. Filter by Date:";
    private static final String DATE_USAGE = "   Usage   : filter-income date from/DD-MM-YYYY to/DD-MM-YYYY";
    private static final String DATE_EXAMPLE = "   Example : filter-income date from/01-01-2023 to/31-12-2023";

    // Constant for unknown command response
    private static final String UNKNOWN_COMMAND_MESSAGE =
            "Unknown filter-income command. Type 'filter-income' for usage instructions.";

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
        if (trimmedInput.equals(COMMAND_FILTER_INCOME)) {
            StringBuilder sb = new StringBuilder();
            String border = BORDER_CHAR.repeat(OUTPUT_WIDTH);
            sb.append(border).append(LINE_SEPARATOR);
            sb.append(centerText(HEADER_TEXT, OUTPUT_WIDTH)).append(LINE_SEPARATOR);
            sb.append(border).append(LINE_SEPARATOR);
            sb.append(LINE_SEPARATOR);

            // Section 1: Filter by Category
            sb.append(CATEGORY_SECTION_HEADER).append(LINE_SEPARATOR);
            sb.append(CATEGORY_USAGE).append(LINE_SEPARATOR);
            sb.append(CATEGORY_EXAMPLE).append(LINE_SEPARATOR);
            sb.append(LINE_SEPARATOR);

            // Section 2: Filter by Amount
            sb.append(AMOUNT_SECTION_HEADER).append(LINE_SEPARATOR);
            sb.append(AMOUNT_USAGE).append(LINE_SEPARATOR);
            sb.append(AMOUNT_EXAMPLE).append(LINE_SEPARATOR);
            sb.append(LINE_SEPARATOR);

            // Section 3: Filter by Date
            sb.append(DATE_SECTION_HEADER).append(LINE_SEPARATOR);
            sb.append(DATE_USAGE).append(LINE_SEPARATOR);
            sb.append(DATE_EXAMPLE).append(LINE_SEPARATOR);
            sb.append(LINE_SEPARATOR);

            sb.append(border);
            this.outputMessage = sb.toString();
            logger.info("Displayed filter-income usage guide.");
        } else {
            this.outputMessage = UNKNOWN_COMMAND_MESSAGE;
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
//@@author IgoyAI (modified)
