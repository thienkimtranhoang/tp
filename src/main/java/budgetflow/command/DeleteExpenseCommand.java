package budgetflow.command;

import budgetflow.exception.UnfoundExpenseException;
import budgetflow.expense.ExpenseList;
import budgetflow.income.Income;

import java.util.List;
import java.util.logging.Logger;

//@@author Yikbing
/**
 * Represents a command to delete an expense from the expense list.
 */
public class DeleteExpenseCommand extends Command{
    private static final Logger logger = Logger.getLogger(DeleteExpenseCommand.class.getName());
    private static final String COMMAND_DELETE_EXPENSE = "delete-expense ";
    private static final String ERROR_EXPENSE_NOT_FOUND = "Expense not found: ";
    private static final String ASSERT_INVALID_COMMAND = "Invalid delete expense command format";
    private static final String ASSERT_NULL_ENTRY = "Expense list contains a null entry";
    private static final String ASSERT_NULL_DESCRIPTION = "Expense entry has a null description";

    /**
     * Constructs a DeleteExpenseCommand with the specified user input.
     *
     * @param input The user input string containing the expense to be deleted.
     */
    public DeleteExpenseCommand(String input) {
        super(input);
        this.commandType = CommandType.DELETE;
    }

    //@@author Yikbing
    /**
     * Executes the command to delete an expense from the expense list.
     *
     * @param incomes      The list of incomes (unused in this command).
     * @param expenseList  The list of expenses from which the specified expense will be deleted.
     * @throws UnfoundExpenseException If the specified expense is not found in the list.
     */
    @Override
    public void execute(List<Income> incomes, ExpenseList expenseList) throws UnfoundExpenseException {
        assert input.startsWith(COMMAND_DELETE_EXPENSE) : ASSERT_INVALID_COMMAND;
        String expenseDesc = input.substring(COMMAND_DELETE_EXPENSE.length()).trim();
        boolean found = false;
        for (int i = 0; i < expenseList.getSize(); i++) {
            assert expenseList.get(i) != null : ASSERT_NULL_ENTRY;
            assert expenseList.get(i).getDescription() != null : ASSERT_NULL_DESCRIPTION;
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
