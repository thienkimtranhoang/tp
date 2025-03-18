package budgetflow.command;

import budgetflow.expense.Expense;
import budgetflow.expense.ExpenseList;

public class ViewAllExpensesCommand extends Command {

    private static final String EMPTY_EXPENSE_LIST_MESSAGE = "No expenses have been logged yet.";

    public ViewAllExpensesCommand() {
        super();
    }

    public void execute(ExpenseList expenseList) {
        if (expenseList.getSize() == 0) {
            System.out.println(EMPTY_EXPENSE_LIST_MESSAGE);
            return;
        }
        System.out.println("Expenses log:");
        for (int i = 0; i < expenseList.getSize(); i++) {
            Expense expense = expenseList.get(i);
            System.out.println((i + 1) + " | " + expense.getCategory() + " | " +
                    expense.getDescription() + " | $" +
                    String.format("%.2f", expense.getAmount()) + " | " +
                    expense.getDate());
        }
        System.out.println("Total Expenses: $" + String.format("%.2f", expenseList.getTotalExpenses()));
    }
}
