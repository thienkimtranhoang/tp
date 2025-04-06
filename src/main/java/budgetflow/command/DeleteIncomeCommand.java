package budgetflow.command;

import budgetflow.exception.InvalidNumberFormatException;
import budgetflow.exception.UnfoundIncomeException;
import budgetflow.exception.InvalidKeywordException;
import budgetflow.expense.ExpenseList;
import budgetflow.income.Income;

import java.util.List;
import java.util.logging.Logger;


/**
 * The DeleteIncomeCommand class processes the deletion of an income entry
 * based on its category. If the specified income category is not found,
 * an exception is thrown.
 */
//@@author Yikbing
public class DeleteIncomeCommand extends Command {
    private static final Logger logger = Logger.getLogger(DeleteIncomeCommand.class.getName());
    private static final String COMMAND_DELETE_INCOME = "delete-income";
    private static final String ERROR_EMPTY_INDEX = "Error: Income Index is required.";
    private static final String LOG_ATTEMPT_EMPTY_INDEX = "Attempted to delete an empty Income Index.";
    private static final String ERROR_INVALID_INCOME_INDEX = "Error: Invalid Income index.";
    private static final String LOG_INCOME_DELETED = "Income deleted: ";
    private static final String ERROR_INVALID_NUMBER = "Error: Please enter a valid numeric index.";
    public static final String LOG_INVALID_INDEX = "Invalid index format: ";
    public static final String LOG_ATTEMPT_TO_DELETE_INVALID_INDEX = "Attempted to delete with invalid index: ";
    private static final int CONVERT_TO_ZERO_INDEX = 1;

    /**
     * Constructs a DeleteIncomeCommand with the specified user input.
     *
     * @param input The command input containing the income category to be deleted.
     */
    //@@author Yikbing
    public DeleteIncomeCommand(String input) {
        super(input);
        this.commandType = CommandType.DELETE;
    }

    /**
     * Executes the income deletion command by searching for an income entry with the given category.
     * If the income entry is found, it is removed from the list; otherwise, an exception is thrown.
     *
     * @param incomes      The list of income entries.
     * @param expenseList  The list of expense entries (unused in this command but required for consistency).
     */
    //@@author Yikbing
    @Override
    public void execute(List<Income> incomes, ExpenseList expenseList) throws InvalidKeywordException,
            InvalidNumberFormatException {

        String incomeIndex = input.substring(COMMAND_DELETE_INCOME.length()).trim();


        checkEmptyIndex(incomeIndex);

        try {
            int index = Integer.parseInt(incomeIndex) - CONVERT_TO_ZERO_INDEX;

            checkValidIndex(incomes, index, incomeIndex);

            deleteIncomeAndOutput(incomes, index);

        } catch (NumberFormatException e) {
            logger.warning(LOG_INVALID_INDEX + incomeIndex);
            throw new InvalidNumberFormatException(ERROR_INVALID_NUMBER);
        }
    }

    /**
     * Checks if the provided index string is empty.
     *
     * @param incomeIndex The string representing the index to be checked.
     * @throws InvalidNumberFormatException If the index string is empty.
     */
    //@@ author Yikbing
    private static void checkEmptyIndex(String incomeIndex) throws InvalidNumberFormatException {
        if (incomeIndex.isEmpty()) {
            logger.warning(LOG_ATTEMPT_EMPTY_INDEX);
            throw new InvalidNumberFormatException(ERROR_EMPTY_INDEX);
        }
    }

    /**
     * Validates whether the provided index is within the valid range of the incomes list.
     *
     * @param incomes The list of incomes to validate the index against.
     * @param index       The index to be validated.
     * @param incomeIndex The index entered by the user.
     * @throws InvalidNumberFormatException If the index is out of bounds.
     */
    //@@author Yikbing
    private static void checkValidIndex(List<Income> incomes, int index, String incomeIndex) throws InvalidNumberFormatException {
        if (index < 0 || index >= incomes.size()) {
            logger.warning(LOG_ATTEMPT_TO_DELETE_INVALID_INDEX + incomeIndex);
            throw new InvalidNumberFormatException(ERROR_INVALID_INCOME_INDEX);
        }
    }

    /**
     * Deletes the specified Income from the incomes list and generates the output message.
     *
     * @param incomes The list of incomes from which the income will be deleted.
     * @param index       The index of the Income to be deleted.
     */
    //@@author Yikbing
    private void deleteIncomeAndOutput(List<Income> incomes, int index) {
        String deletedDesc = incomes.get(index).getCategory();
        String amount = Double.toString(incomes.get(index).getAmount());
        incomes.remove(index);
        this.outputMessage = LOG_INCOME_DELETED + deletedDesc + ", $" + amount;
        logger.info(LOG_INCOME_DELETED + deletedDesc);
    }
}
