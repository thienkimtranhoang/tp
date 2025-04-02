package budgetflow.command;

import budgetflow.expense.Expense;
import budgetflow.expense.ExpenseList;
import budgetflow.income.Income;

import java.util.List;
import java.util.logging.Logger;

//@@author QuyDatNguyen
/**
 * Represents a command to view all logged expenses.
 * If no expenses have been recorded, an appropriate message is displayed.
 */
public class ViewAllExpensesCommand extends Command {
    private static final Logger logger = Logger.getLogger(ViewAllExpensesCommand.class.getName());

    private static final String EMPTY_EXPENSE_LIST_MESSAGE = "No expenses have been logged yet.";

    /**
     * Constructs a ViewAllExpensesCommand.
     * This command is used to display all recorded expenses.
     */
    public ViewAllExpensesCommand() {
        super();
        this.commandType = CommandType.READ;
    }

    /**
     * Executes the command to list all logged expenses.
     * If the expense list is empty, a message indicating no expenses are recorded is displayed.
     * Otherwise, all expenses along with their total amount are displayed.
     *
     * @param incomes     The list of incomes (not used in this command).
     * @param expenseList The list of expenses to be displayed.
     */
    @Override
    public void execute(List<Income> incomes, ExpenseList expenseList) {
        if (expenseList.getSize() == 0) {
            logger.info("Viewing empty expense list");
            this.outputMessage = EMPTY_EXPENSE_LIST_MESSAGE + System.lineSeparator();
            return;
        }
        assert expenseList.getSize() > 0 : "List size must be positive";
        String message = "Expenses log:" + System.lineSeparator();
        for (int i = 0; i < expenseList.getSize(); i++) {
            Expense expense = expenseList.get(i);
            message += (i + 1) + " | " + expense.getCategory() + " | " +
                    expense.getDescription() + " | $" +
                    String.format("%.2f", expense.getAmount()) + " | " +
                    expense.getDate() + System.lineSeparator();
        }
        message += "Total Expenses: $" + String.format("%.2f", expenseList.getTotalExpenses()) + System.lineSeparator();
        this.outputMessage = message;
        logger.info("Viewing expense list: " + expenseList);
    }
}
