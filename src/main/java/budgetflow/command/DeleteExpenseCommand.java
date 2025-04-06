package budgetflow.command;

import budgetflow.expense.ExpenseList;
import budgetflow.income.Income;
import budgetflow.exception.InvalidNumberFormatException;

import java.util.List;
import java.util.logging.Logger;


/**
 * Represents a command to delete an expense from the expense list.
 */
//@@author Yikbing
public class DeleteExpenseCommand extends Command {
    private static final Logger logger = Logger.getLogger(DeleteExpenseCommand.class.getName());
    private static final String COMMAND_DELETE_EXPENSE = "delete-expense";
    private static final String ERROR_EMPTY_INDEX = "Error: Expense Index is required.";
    private static final String LOG_ATTEMPT_EMPTY_INDEX = "Attempted to delete an empty expense Index.";
    private static final String LOG_EXPENSE_DELETED = "Expense deleted: ";
    private static final String ERROR_INVALID_EXPENSE_INDEX = "Error: Invalid expense index.";
    private static final String ERROR_INVALID_NUMBER = "Error: Please enter a valid numeric index.";
    private static final int CONVERT_TO_ZERO_INDEX = 1;

    /**
     * Constructs a DeleteExpenseCommand with the specified user input.
     *
     * @param input The user input string containing the expense to be deleted.
     */
    //@@author Yikbing
    public DeleteExpenseCommand(String input) {
        super(input);
        this.commandType = CommandType.DELETE;
    }

    /**
     * Checks if the provided index string is empty.
     *
     * @param indexString The string representing the index to be checked.
     * @throws InvalidNumberFormatException If the index string is empty.
     */
    //@@ author Yikbing
    private static void checkEmptyIndex(String indexString) throws InvalidNumberFormatException {
        if (indexString.isEmpty()) {
            logger.warning(LOG_ATTEMPT_EMPTY_INDEX);
            throw new InvalidNumberFormatException(ERROR_EMPTY_INDEX);
        }
    }

    /**
     * Executes the command to delete an expense from the expense list.
     *
     * @param incomes      The list of incomes (unused in this command).
     * @param expenseList  The list of expenses from which the specified expense will be deleted.
     */
    //@@author Yikbing
    @Override
    public void execute(List<Income> incomes, ExpenseList expenseList) throws InvalidNumberFormatException {

        String indexString = input.substring(COMMAND_DELETE_EXPENSE.length()).trim();

        checkEmptyIndex(indexString);

        try {
            int index = Integer.parseInt(indexString) - CONVERT_TO_ZERO_INDEX;

            checkValidIndex(expenseList, index, indexString);

            deleteExpenseAndOutput(expenseList, index);

        } catch (NumberFormatException e) {
            logger.warning("Invalid index format: " + indexString);
            throw new InvalidNumberFormatException(ERROR_INVALID_NUMBER);
        }

    }

    /**
     * Validates whether the provided index is within the valid range of the expense list.
     *
     * @param expenseList The list of expenses to validate the index against.
     * @param index       The index to be validated.
     * @param indexString The index entered by the user.
     * @throws InvalidNumberFormatException If the index is out of bounds.
     */
    //@@author Yikbing
    private void checkValidIndex(ExpenseList expenseList, int index, String indexString)
            throws InvalidNumberFormatException {
        if (index < 0 || index >= expenseList.getSize()) {
            logger.warning("Attempted to delete with invalid index: " + (indexString));
            throw new InvalidNumberFormatException(ERROR_INVALID_EXPENSE_INDEX);
        }
    }


    /**
     * Deletes the specified expense from the expense list and generates the output message.
     *
     * @param expenseList The list of expenses from which the expense will be deleted.
     * @param index       The index of the expense to be deleted.
     */
    //@@author Yikbing
    private void deleteExpenseAndOutput(ExpenseList expenseList, int index) {
        String deletedDesc = expenseList.get(index).getDescription();
        String amount = Double.toString(expenseList.get(index).getAmount());
        expenseList.delete(index);
        this.outputMessage = LOG_EXPENSE_DELETED + deletedDesc + ", $" + amount;
        logger.info(LOG_EXPENSE_DELETED + deletedDesc);
    }

}
