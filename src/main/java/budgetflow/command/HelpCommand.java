package budgetflow.command;

import budgetflow.exception.FinanceException;
import budgetflow.expense.ExpenseList;
import budgetflow.income.Income;
import java.util.List;

/**
 * Represents the Help Command which provides detailed instructions on how to use Budgetflow.
 * <p>
 * The help menu is structured into sections and displays the command syntax along with examples.
 * It is designed to be clear, comprehensive, and easy to read.
 *
 * @author IgoyAI
 * @author Yikbing
 */
public class HelpCommand extends Command {

    public HelpCommand() {
        super();
        this.commandType = CommandType.READ;
    }

    @Override
    public void execute(List<Income> incomes, ExpenseList expenseList) throws FinanceException {
        StringBuilder helpMessage = new StringBuilder();
        String border = "=".repeat(60);

        helpMessage.append(border).append(System.lineSeparator());
        helpMessage.append(centerText("HELP MENU", 60)).append(System.lineSeparator());
        helpMessage.append(border).append(System.lineSeparator());
        helpMessage.append(System.lineSeparator());

        // 1. Add Income
        helpMessage.append("1. Add Income:" + System.lineSeparator());
        helpMessage.append("   Syntax : add category/[CATEGORY] amt/[AMOUNT] d/[DD-MM-YYYY]" + System.lineSeparator());
        helpMessage.append("   Example: add category/Salary amt/5000 d/15-03-2023" + System.lineSeparator());
        helpMessage.append(System.lineSeparator());

        // 2. Add Expense
        helpMessage.append("2. Add Expense:" + System.lineSeparator());
        helpMessage.append("   Syntax : log-expense category/[CATEGORY] desc/[DESCRIPTION] amt/[AMOUNT] d/[DD-MM-YYYY]" + System.lineSeparator());
        helpMessage.append("   Example: log-expense category/Food desc/Lunch amt/15 d/20-03-2023" + System.lineSeparator());
        helpMessage.append(System.lineSeparator());

        // 3. View All Expenses
        helpMessage.append("3. View All Expenses:" + System.lineSeparator());
        helpMessage.append("   Syntax : view-all-expense" + System.lineSeparator());
        helpMessage.append("   Example: view-all-expense" + System.lineSeparator());
        helpMessage.append(System.lineSeparator());

        // 4. View All Income
        helpMessage.append("4. View All Income:" + System.lineSeparator());
        helpMessage.append("   Syntax : list income" + System.lineSeparator());
        helpMessage.append("   Example: list income" + System.lineSeparator());
        helpMessage.append(System.lineSeparator());

        // 5. Delete Expense
        helpMessage.append("5. Delete Expense:" + System.lineSeparator());
        helpMessage.append("   Syntax : delete-expense [DESCRIPTION]" + System.lineSeparator());
        helpMessage.append("   Example: delete-expense Lunch" + System.lineSeparator());
        helpMessage.append(System.lineSeparator());

        // 6. Delete Income
        helpMessage.append("6. Delete Income:" + System.lineSeparator());
        helpMessage.append("   Syntax : delete-income [CATEGORY]" + System.lineSeparator());
        helpMessage.append("   Example: delete-income Salary" + System.lineSeparator());
        helpMessage.append(System.lineSeparator());

        // 7. Find Expense
        helpMessage.append("7. Find Expense:" + System.lineSeparator());
        helpMessage.append("   Syntax : find-expense /desc [DESCRIPTION] OR /d [DD-MM-YYYY] OR /amt [AMOUNT]" + System.lineSeparator());
        helpMessage.append("            OR /category [CATEGORY] OR /amtrange [AMOUNT 1] [AMOUNT 2] OR /drange [DATE 1] [DATE 2]" + System.lineSeparator());
        helpMessage.append("   Example: find-expense /desc Lunch" + System.lineSeparator());
        helpMessage.append(System.lineSeparator());

        // 8. Compare Expenses
        helpMessage.append("8. Compare Expenses:" + System.lineSeparator());
        helpMessage.append("   Syntax : compare [MM-YYYY] [MM-YYYY]" + System.lineSeparator());
        helpMessage.append("   Example: compare 03-2023 04-2023" + System.lineSeparator());
        helpMessage.append(System.lineSeparator());

        // 9. Update Expense
        helpMessage.append("9. Update Expense:" + System.lineSeparator());
        helpMessage.append("   Syntax : update-expense category/[CATEGORY] OR amt/[AMOUNT] OR desc/[DESCRIPTION] d/[DD-MM-YYYY]" + System.lineSeparator());
        helpMessage.append("   Example: update-expense desc/Brunch amt/20 d/21-03-2023" + System.lineSeparator());
        helpMessage.append(System.lineSeparator());

        // 10. Update Income
        helpMessage.append("10. Update Income:" + System.lineSeparator());
        helpMessage.append("    Syntax : update-income category/[CATEGORY] OR amt/[AMOUNT] OR d/[DD-MM-YYYY]" + System.lineSeparator());
        helpMessage.append("    Example: update-income amt/5500" + System.lineSeparator());
        helpMessage.append(System.lineSeparator());

        // 11. Filter Income
        helpMessage.append("11. Filter Income:" + System.lineSeparator());
        helpMessage.append("    a. By Date:" + System.lineSeparator());
        helpMessage.append("       Syntax : filter-income date from/DD-MM-YYYY to/DD-MM-YYYY" + System.lineSeparator());
        helpMessage.append("       Example: filter-income date from/01-03-2023 to/31-03-2023" + System.lineSeparator());
        helpMessage.append("    b. By Amount:" + System.lineSeparator());
        helpMessage.append("       Syntax : filter-income amount from/[AMOUNT 1] to/[AMOUNT 2]" + System.lineSeparator());
        helpMessage.append("       Example: filter-income amount from/1000 to/5000" + System.lineSeparator());
        helpMessage.append("    c. By Category:" + System.lineSeparator());
        helpMessage.append("       Syntax : filter-income category/[CATEGORY]" + System.lineSeparator());
        helpMessage.append("       Example: filter-income category/Salary" + System.lineSeparator());
        helpMessage.append(System.lineSeparator());

        // 12. Exit
        helpMessage.append("12. Exit:" + System.lineSeparator());
        helpMessage.append("    Syntax : exit" + System.lineSeparator());
        helpMessage.append("    Example: exit" + System.lineSeparator());
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
