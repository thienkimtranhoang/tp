package budgetflow.command;

import budgetflow.expense.ExpenseList;
import budgetflow.income.Income;

import java.util.List;
import java.util.logging.Logger;

//@@author Yikbing
/**
 * Represents a command to delete an expense from the expense list.
 */
public class DeleteExpenseCommand extends Command {
    private static final Logger logger = Logger.getLogger(DeleteExpenseCommand.class.getName());
    private static final String COMMAND_DELETE_EXPENSE = "delete-expense ";
    private static final String ERROR_INVALID_COMMAND = "Invalid delete expense command format.";
    private static final String ERROR_EMPTY_DESCRIPTION = "Error: Expense description is required.";
    private static final String ERROR_EXPENSE_NOT_FOUND = "Expense not found: ";
    private static final String LOG_ATTEMPT_EMPTY_DESCRIPTION = "Attempted to delete an empty expense description.";
    private static final String LOG_ATTEMPT_NON_EXISTENT_EXPENSE = "Attempted to delete non-existent expense: ";
    private static final String LOG_EXPENSE_DELETED = "Expense deleted: ";

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
     */
    @Override
    public void execute(List<Income> incomes, ExpenseList expenseList) {
        if (!input.startsWith(COMMAND_DELETE_EXPENSE)) {
            logger.warning(ERROR_INVALID_COMMAND);
            this.outputMessage = ERROR_INVALID_COMMAND;
            return;
        }

        String expenseDesc = input.substring(COMMAND_DELETE_EXPENSE.length()).trim();

        if (expenseDesc.isEmpty()) {
            logger.warning(LOG_ATTEMPT_EMPTY_DESCRIPTION);
            this.outputMessage = ERROR_EMPTY_DESCRIPTION;
            return;
        }

        for (int i = 0; i < expenseList.getSize(); i++) {
            if (expenseList.get(i) != null && expenseList.get(i).getDescription() != null &&
                    expenseList.get(i).getDescription().equalsIgnoreCase(expenseDesc)) {
                expenseList.delete(i);
                this.outputMessage = LOG_EXPENSE_DELETED + expenseDesc;
                logger.info(LOG_EXPENSE_DELETED + expenseDesc);
                return;
            }
        }

        logger.warning(LOG_ATTEMPT_NON_EXISTENT_EXPENSE + expenseDesc);
        this.outputMessage = ERROR_EXPENSE_NOT_FOUND + expenseDesc;
    }
}
