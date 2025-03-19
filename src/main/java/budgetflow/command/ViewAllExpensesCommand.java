package budgetflow.command;

import budgetflow.expense.Expense;
import budgetflow.expense.ExpenseList;
import budgetflow.income.Income;

import java.util.List;

public class ViewAllExpensesCommand extends Command {

    private static final String EMPTY_EXPENSE_LIST_MESSAGE = "No expenses have been logged yet.";

    public ViewAllExpensesCommand() {
        super();
        this.commandType = CommandType.READ;
    }

    @Override
    public void execute(List<Income> incomes, ExpenseList expenseList) {
        if (expenseList.getSize() == 0) {
            this.outputMessage = EMPTY_EXPENSE_LIST_MESSAGE + System.lineSeparator();
            return;
        }
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
    }
}
