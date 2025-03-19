package budgetflow.command;

import budgetflow.FinanceTracker;
import budgetflow.exception.InvalidNumberFormatException;
import budgetflow.exception.UnfoundExpenseException;
import budgetflow.expense.Expense;
import budgetflow.expense.ExpenseList;
import budgetflow.income.Income;

import java.util.List;
import java.util.logging.Logger;

public class DeleteExpenseCommand extends Command{
    private static final Logger logger = Logger.getLogger(DeleteExpenseCommand.class.getName());
    private static final String COMMAND_DELETE_EXPENSE = "delete-expense ";
    private static final String ERROR_EXPENSE_NOT_FOUND = "Expense not found: ";

    public DeleteExpenseCommand(String input) {
        super(input);
        this.commandType = CommandType.DELETE;
    }

    @Override
    public void execute(List<Income> incomes, ExpenseList expenseList) throws UnfoundExpenseException {
        assert input.startsWith(COMMAND_DELETE_EXPENSE) : "Invalid delete expense command format";
        String expenseDesc = input.substring(COMMAND_DELETE_EXPENSE.length()).trim();

        boolean found = false;
        for (int i = 0; i < expenseList.getSize(); i++) {
            if (expenseList.get(i).getDescription().equalsIgnoreCase(expenseDesc)) {
                expenseList.delete(i);
                this.outputMessage = "Expense deleted: " + expenseDesc;
                found = true;
                logger.info("Expense deleted: " + expenseDesc);
                break;
            }
        }
        if (!found) {
            logger.warning("Attempted to delete non-existent expense: " + expenseDesc);
            throw new UnfoundExpenseException(ERROR_EXPENSE_NOT_FOUND + expenseDesc);
        }
    }
}
