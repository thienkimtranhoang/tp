package budgetflow.command;

import budgetflow.exception.UnfoundIncomeException;
import budgetflow.expense.ExpenseList;
import budgetflow.income.Income;

import java.util.List;
import java.util.logging.Logger;

/**
 * The DeleteIncomeCommand class processes the deletion of an income entry
 * based on its category. If the specified income category is not found,
 * an exception is thrown.
 */
public class DeleteIncomeCommand extends Command {
    private static final Logger logger = Logger.getLogger(DeleteIncomeCommand.class.getName());
    private static final String COMMAND_DELETE_INCOME = "delete-income ";
    private static final String ERROR_INCOME_NOT_FOUND = "Income not found: ";

    /**
     * Constructs a DeleteIncomeCommand with the specified user input.
     *
     * @param input The command input containing the income category to be deleted.
     */
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
     * @throws UnfoundIncomeException If the specified income category is not found.
     */
    @Override
    public void execute(List<Income> incomes, ExpenseList expenseList) throws UnfoundIncomeException {
        assert input.startsWith(COMMAND_DELETE_INCOME) : "Invalid delete income command format";
        String incomeCategory = input.substring(COMMAND_DELETE_INCOME.length()).trim();
        boolean found = false;
        for (int i = 0; i < incomes.size(); i++) {
            if (incomes.get(i).getCategory().equalsIgnoreCase(incomeCategory)) {
                incomes.remove(i);
                this.outputMessage = "Income deleted: " + incomeCategory;
                found = true;
                logger.info("Income deleted: " + incomeCategory);
                break;
            }
        }
        if (!found) {
            logger.warning("Attempted to delete non-existent income: " + incomeCategory);
            throw new UnfoundIncomeException(ERROR_INCOME_NOT_FOUND + incomeCategory);
        }
    }
}
