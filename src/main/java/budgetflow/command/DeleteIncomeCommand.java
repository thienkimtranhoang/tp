package budgetflow.command;

import budgetflow.exception.UnfoundIncomeException;
import budgetflow.expense.ExpenseList;
import budgetflow.income.Income;

import java.util.List;
import java.util.logging.Logger;

//@@author Yikbing
/**
 * The DeleteIncomeCommand class processes the deletion of an income entry
 * based on its category. If the specified income category is not found,
 * an exception is thrown.
 */
public class DeleteIncomeCommand extends Command {
    private static final Logger logger = Logger.getLogger(DeleteIncomeCommand.class.getName());
    private static final String COMMAND_DELETE_INCOME = "delete-income ";
    private static final String ERROR_INCOME_NOT_FOUND = "Income not found: ";
    private static final String ERROR_INVALID_COMMAND = "Invalid delete income command format.";
    private static final String ASSERT_NULL_ENTRY = "Income list contains a null entry";
    private static final String ASSERT_NULL_CATEGORY = "Income entry has a null category";
    private static final String ERROR_EMPTY_CATEGORY = "Error: Income category is required.";
    private static final String INCOME_DELETED_HEADER = "Income deleted: ";

    //@@author Yikbing
    /**
     * Constructs a DeleteIncomeCommand with the specified user input.
     *
     * @param input The command input containing the income category to be deleted.
     */
    public DeleteIncomeCommand(String input) {
        super(input);
        this.commandType = CommandType.DELETE;
    }

    //@@author Yikbing
    /**
     * Executes the income deletion command by searching for an income entry with the given category.
     * If the income entry is found, it is removed from the list; otherwise, an exception is thrown.
     *
     * @param incomes      The list of income entries.
     * @param expenseList  The list of expense entries (unused in this command but required for consistency).
     * @throws UnfoundIncomeException If the specified income category is not found.
     */
    //@@author dariusyawningwhiz
    @Override
    public void execute(List<Income> incomes, ExpenseList expenseList) {
        if (!input.startsWith(COMMAND_DELETE_INCOME)) {
            this.outputMessage = ERROR_INVALID_COMMAND;
            return;
        }

        String incomeCategory = input.substring(COMMAND_DELETE_INCOME.length()).trim();

        if (incomeCategory.isEmpty()) {
            this.outputMessage = ERROR_EMPTY_CATEGORY;
            return;
        }

        boolean found = false;
        for (int i = 0; i < incomes.size(); i++) {
            Income income = incomes.get(i);

            assert income != null : ASSERT_NULL_ENTRY;
            assert income.getCategory() != null : ASSERT_NULL_CATEGORY;

            if (income.getCategory().equalsIgnoreCase(incomeCategory)) {
                incomes.remove(i);
                this.outputMessage = INCOME_DELETED_HEADER + incomeCategory;
                found = true;
                logger.info(INCOME_DELETED_HEADER + incomeCategory);
                break;
            }
        }

        if (!found) {
            this.outputMessage = ERROR_INCOME_NOT_FOUND + incomeCategory;
            logger.warning("Attempted to delete non-existent income: " + incomeCategory);
        }
    }
}
