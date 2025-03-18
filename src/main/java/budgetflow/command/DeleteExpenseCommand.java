package budgetflow.command;

import budgetflow.exception.InvalidNumberFormatException;
import budgetflow.exception.UnfoundExpenseException;
import budgetflow.expense.Expense;
import budgetflow.expense.ExpenseList;
import budgetflow.income.Income;

import java.util.List;

public class DeleteExpenseCommand extends Command{
    private static final String COMMAND_DELETE_EXPENSE = "delete-expense ";

    public DeleteExpenseCommand(String input) {
        super(input);
        this.commandType = CommandType.DELETE;
    }

    @Override
    public void execute(List<Income> incomes, ExpenseList expenseList) throws UnfoundExpenseException {
        if (input.startsWith(COMMAND_DELETE_EXPENSE)) {
            input = input.substring(COMMAND_DELETE_EXPENSE.length()).trim();
        }
        boolean found = false;
        for (int i = 0; i < expenseList.getSize(); i++) {
            if (expenseList.get(i).getDescription().equalsIgnoreCase(input)) {
                expenseList.delete(i);
                this.outputMessage = "Expense deleted: " + input;
                found = true;
                break;
            }
        }
        if (!found) {
            throw new UnfoundExpenseException("Expense not found: " + input);
        }
//        } else {
//            storage.saveData(incomes, expenseList);
//        }
    }
}
