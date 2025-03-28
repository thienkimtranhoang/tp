package budgetflow.parser;

import budgetflow.command.AddIncomeCommand;
import budgetflow.command.Command;
import budgetflow.command.DeleteIncomeCommand;
import budgetflow.command.LogExpenseCommand;
import budgetflow.command.ListIncomeCommand;
import budgetflow.command.DeleteExpenseCommand;
import budgetflow.command.ViewAllExpensesCommand;
import budgetflow.command.FindExpenseCommand;
import budgetflow.command.ExitCommand;
import budgetflow.command.CompareExpenseCommand;
import budgetflow.command.UpdateExpenseCommand;
import budgetflow.command.FilterIncomeByDateCommand;
import budgetflow.command.FilterIncomeByAmountCommand;
import budgetflow.command.FilterIncomeByCategoryCommand;
import budgetflow.command.SetSavingGoalCommand;
import budgetflow.exception.UnknownCommandException;

import java.util.logging.Logger;

/**
 * Parses the user's input and extracts the corresponding command.
 *
 * <p>New filtering commands for income (by date, amount, and category) have been added.
 *
 * @@author IgoyAI (modified)
 */
public class Parser {
    private static final Logger logger = Logger.getLogger(Parser.class.getName());

    // Command constants
    private static final String COMMAND_ADD_INCOME = "add category/";
    private static final String COMMAND_LOG_EXPENSE = "log-expense ";
    private static final String COMMAND_DELETE_INCOME = "delete-income";
    private static final String COMMAND_LIST_INCOME = "list income";
    private static final String COMMAND_DELETE_EXPENSE = "delete-expense";
    private static final String COMMAND_VIEW_ALL_EXPENSES = "view-all-expense";
    private static final String COMMAND_FIND_EXPENSE = "find-expense";
    private static final String COMMAND_EXIT = "exit";
    private static final String COMMAND_COMPARE = "compare";
    private static final String COMMAND_UPDATE_EXPENSE = "update-expense";
    // New command constants for filtering incomes
    private static final String COMMAND_FILTER_INCOME_DATE = "filter-income date";
    private static final String COMMAND_FILTER_INCOME_AMOUNT = "filter-income amount";
    private static final String COMMAND_FILTER_INCOME_CATEGORY = "filter-income category";
    // New command constant for saving goal
    private static final String COMMAND_SET_SAVING_GOAL = "set-saving-goal";

    /**
     * Parses the user's input and extracts the corresponding command.
     *
     * @param input the user's input command.
     * @return the corresponding command for the user's input.
     * @throws UnknownCommandException if the user's command is unrecognizable.
     */
    public static Command getCommandFromInput(String input) throws UnknownCommandException {
        logger.info("Processing command: " + input);
        if (input.startsWith(COMMAND_ADD_INCOME)) {
            return new AddIncomeCommand(input);
        } else if (input.startsWith(COMMAND_SET_SAVING_GOAL)) {
            return new SetSavingGoalCommand(input);
        } else if (input.startsWith(COMMAND_LOG_EXPENSE)) {
            return new LogExpenseCommand(input);
        } else if (input.startsWith(COMMAND_DELETE_INCOME)) {
            return new DeleteIncomeCommand(input);
        } else if (COMMAND_LIST_INCOME.equals(input)) {
            return new ListIncomeCommand();
        } else if (input.startsWith(COMMAND_DELETE_EXPENSE)) {
            return new DeleteExpenseCommand(input);
        } else if (input.equals(COMMAND_VIEW_ALL_EXPENSES)) {
            return new ViewAllExpensesCommand();
        } else if (input.startsWith(COMMAND_FIND_EXPENSE)) {
            return new FindExpenseCommand(input);
        } else if (input.equals(COMMAND_EXIT)) {
            return new ExitCommand();
        } else if (input.startsWith(COMMAND_COMPARE)) {
            return new CompareExpenseCommand(input);
        } else if (input.startsWith(COMMAND_UPDATE_EXPENSE)) {
            return new UpdateExpenseCommand(input);
        } else if (input.startsWith(COMMAND_FILTER_INCOME_DATE)) {
            return new FilterIncomeByDateCommand(input);
        } else if (input.startsWith(COMMAND_FILTER_INCOME_AMOUNT)) {
            return new FilterIncomeByAmountCommand(input);
        } else if (input.startsWith(COMMAND_FILTER_INCOME_CATEGORY)) {
            return new FilterIncomeByCategoryCommand(input);
        } else {
            logger.warning("Unknown command received: " + input);
            throw new UnknownCommandException();
        }
    }
}
