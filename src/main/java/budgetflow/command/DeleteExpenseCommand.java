package budgetflow.command;

import budgetflow.exception.UnfoundExpenseException;
import budgetflow.expense.Expense;
import budgetflow.expense.ExpenseList;

public class DeleteExpenseCommand extends Command{
    private static final String COMMAND_DELETE_EXPENSE = "delete-expense ";

    public DeleteExpenseCommand(String input) {
        super(input);
    }

    public void execute(ExpenseList expenseList) throws UnfoundExpenseException {
        if (input.startsWith(COMMAND_DELETE_EXPENSE)) {
            input = input.substring(COMMAND_DELETE_EXPENSE.length()).trim();
        }
        boolean found = false;
        for (int i = 0; i < expenseList.getSize(); i++) {
            if (expenseList.get(i).getDescription().equalsIgnoreCase(input)) {
                expenseList.delete(i);
                System.out.println("Expense deleted: " + input);
                found = true;
                break;
            }
        }
        if (!found) {
            throw new UnfoundExpenseException("Expense not found: " + input);
        }
//        if (!found) {
//            System.out.println("Expense not found: " + input);
//        } else {
//            storage.saveData(incomes, expenseList);
//        }
    }
}
