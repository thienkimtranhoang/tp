package budgetflow.command;

import budgetflow.exception.FinanceException;
import budgetflow.exception.InvalidKeywordException;

import budgetflow.expense.ExpenseList;
import budgetflow.income.Income;

import java.util.List;

/**
 * Represents the Help Command which provides detailed instructions on how to use Budgetflow.
 * The help menu is a unified detailed reference, organized into clear sections for an improved UI experience.
 *
 * Usage instructions include the syntax and an example for each commands.
 *
 * @author IgoyAI
 * @author Yikbing
 */
public class HelpCommand extends Command {

    private static final String CYAN = "\u001B[36m";
    private static final String YELLOW = "\u001B[33m";
    private static final String RESET = "\u001B[0m";

    public HelpCommand() {
        super();
        this.commandType = CommandType.READ;
    }

    //@@ author Yikbing
    @Override
    public void execute(List<Income> incomes, ExpenseList expenseList) throws FinanceException{

        if(input != HELP_PATTERN) {
            throw new InvalidKeywordException("Invalid command. Did you mean 'help'?");
        }

        StringBuilder helpMessage = new StringBuilder();
        String border = "=".repeat(60);

        // Header Section
        helpMessage.append(border).append(System.lineSeparator());
        helpMessage.append(centerText("HELP MENU", 60)).append(System.lineSeparator());
        helpMessage.append(border).append(System.lineSeparator());
        helpMessage.append(System.lineSeparator());

        // Detailed Commands Section

        // Add Income
        helpMessage.append(CYAN).append("Add Income").append(RESET)
                .append(System.lineSeparator());
        helpMessage.append("  Syntax : add category/<CATEGORY> amt/<AMOUNT> d/<DD-MM-YYYY>")
                .append(System.lineSeparator());
        helpMessage.append("  Example: add category/Salary amt/5000 d/15-03-2023")
                .append(System.lineSeparator());
        helpMessage.append(System.lineSeparator());

        // Add Expense
        helpMessage.append(CYAN).append("Add Expense").append(RESET)
                .append(System.lineSeparator());
        helpMessage.append("  Syntax : log-expense category/<CATEGORY> desc/<DESCRIPTION> amt/<AMOUNT> d/<DD-MM-YYYY>")
                .append(System.lineSeparator());
        helpMessage.append("  Example: log-expense category/Food desc/Lunch amt/15 d/20-03-2023")
                .append(System.lineSeparator());
        helpMessage.append(System.lineSeparator());

        // View All Expenses
        helpMessage.append(CYAN).append("View All Expenses").append(RESET)
                .append(System.lineSeparator());
        helpMessage.append("  Syntax : view-all-expense").append(System.lineSeparator());
        helpMessage.append("  Example: view-all-expense").append(System.lineSeparator());
        helpMessage.append(System.lineSeparator());

        // View All Income
        helpMessage.append(CYAN).append("View All Income").append(RESET)
                .append(System.lineSeparator());
        helpMessage.append("  Syntax : list income").append(System.lineSeparator());
        helpMessage.append("  Example: list income").append(System.lineSeparator());
        helpMessage.append(System.lineSeparator());

        // Delete Expense
        helpMessage.append(CYAN).append("Delete Expense").append(RESET)
                .append(System.lineSeparator());
        helpMessage.append("  Syntax : delete-expense <INDEX>")
                .append(System.lineSeparator());
        helpMessage.append("  Example: delete-expense 1")
                .append(System.lineSeparator());
        helpMessage.append(System.lineSeparator());

        // Delete Income
        helpMessage.append(CYAN).append("Delete Income").append(RESET)
                .append(System.lineSeparator());
        helpMessage.append("  Syntax : delete-income <INDEX>")
                .append(System.lineSeparator());
        helpMessage.append("  Example: delete-income 1")
                .append(System.lineSeparator());
        helpMessage.append(System.lineSeparator());

        // Find Expense
        helpMessage.append(CYAN).append("Find Expense").append(RESET)
                .append(System.lineSeparator());
        helpMessage.append("  Syntax : find-expense [/desc <DESCRIPTION> | /d <DD-MM-YYYY> | /amt <AMOUNT> |")
                .append(System.lineSeparator());
        helpMessage.append("           /category <CATEGORY> | /amtrange <AMOUNT1> <AMOUNT2> | ")
                .append(System.lineSeparator());
        helpMessage.append("           /drange <DATE1> <DATE2>]")
                .append(System.lineSeparator());
        helpMessage.append("  Example: find-expense /desc Lunch")
                .append(System.lineSeparator());
        helpMessage.append(System.lineSeparator());

        // Compare Expenses
        helpMessage.append(CYAN).append("Compare Expenses").append(RESET)
                .append(System.lineSeparator());
        helpMessage.append("  Syntax : compare <MM-YYYY> <MM-YYYY>")
                .append(System.lineSeparator());
        helpMessage.append("  Example: compare 03-2023 04-2023")
                .append(System.lineSeparator());
        helpMessage.append(System.lineSeparator());

        // Update Expense
        helpMessage.append(CYAN).append("Update Expense").append(RESET)
                .append(System.lineSeparator());
        helpMessage.append("  Syntax : update-expense <INDEX> [category/<CATEGORY> | amt/<AMOUNT> |")
                .append(System.lineSeparator());
        helpMessage.append("           desc/<DESCRIPTION> | d/<DD-MM-YYYY>]")
                .append(System.lineSeparator());
        helpMessage.append("  Example: update-expense 2 desc/Brunch amt/20 d/21-03-2023")
                .append(System.lineSeparator());
        helpMessage.append(System.lineSeparator());

        // Update Income
        helpMessage.append(CYAN).append("Update Income").append(RESET)
                .append(System.lineSeparator());
        helpMessage.append("  Syntax : update-income [category/<CATEGORY> | amt/<AMOUNT> | d/<DD-MM-YYYY>]")
                .append(System.lineSeparator());
        helpMessage.append("  Example: update-income amt/5500")
                .append(System.lineSeparator());
        helpMessage.append(System.lineSeparator());

        // Filter Income
        helpMessage.append(CYAN).append("Filter Income").append(RESET)
                .append(System.lineSeparator());
        helpMessage.append("  By Date:").append(System.lineSeparator());
        helpMessage.append("    Syntax : filter-income date from/<DD-MM-YYYY> to/<DD-MM-YYYY>")
                .append(System.lineSeparator());
        helpMessage.append("    Example: filter-income date from/01-03-2023 to/31-03-2023")
                .append(System.lineSeparator());
        helpMessage.append("  By Amount:").append(System.lineSeparator());
        helpMessage.append("    Syntax : filter-income amount from/<AMOUNT1> to/<AMOUNT2>")
                .append(System.lineSeparator());
        helpMessage.append("    Example: filter-income amount from/1000 to/5000")
                .append(System.lineSeparator());
        helpMessage.append("  By Category:").append(System.lineSeparator());
        helpMessage.append("    Syntax : filter-income category/<CATEGORY>")
                .append(System.lineSeparator());
        helpMessage.append("    Example: filter-income category/Salary")
                .append(System.lineSeparator());
        helpMessage.append(System.lineSeparator());

        // Exit
        helpMessage.append(CYAN).append("Exit").append(RESET)
                .append(System.lineSeparator());
        helpMessage.append("  Syntax : exit").append(System.lineSeparator());
        helpMessage.append("  Example: exit").append(System.lineSeparator());
        helpMessage.append(System.lineSeparator());
        helpMessage.append(border).append(System.lineSeparator());

        this.outputMessage = helpMessage.toString();
    }

    /**
     * Centers the given text within the specified width.
     *
     * @param text  The text to center.
     * @param width The total width for centering.
     * @return A string with the text centered.
     */
    private String centerText(String text, int width) {
        if (text.length() >= width) {
            return text;
        }
        int padding = (width - text.length()) / 2;
        return " ".repeat(padding) + text;
    }
}
