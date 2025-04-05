package budgetflow.parser;

import budgetflow.command.AddIncomeCommand;
import budgetflow.command.Command;
import budgetflow.command.CompareExpenseCommand;
import budgetflow.command.DeleteExpenseCommand;
import budgetflow.command.DeleteIncomeCommand;
import budgetflow.command.ExitCommand;
import budgetflow.command.FilterIncomeByAmountCommand;
import budgetflow.command.FilterIncomeByCategoryCommand;
import budgetflow.command.FilterIncomeByDateCommand;
import budgetflow.command.FilterIncomeCommand;
import budgetflow.command.FindExpenseCommand;
import budgetflow.command.ListIncomeCommand;
import budgetflow.command.LogExpenseCommand;
import budgetflow.command.SetSavingGoalCommand;
import budgetflow.command.UpdateExpenseCommand;
import budgetflow.command.UpdateIncomeCommand;
import budgetflow.command.ViewAllExpensesCommand;
import budgetflow.command.HelpCommand;
import budgetflow.exception.UnknownCommandException;

import java.util.logging.Logger;

/**
 * Parses the user's input and extracts the corresponding command.
 *
 * <p>New filtering commands for income (by date, amount, and category) have been added. ok
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
    private static final String COMMAND_UPDATE_INCOME = "update-income";
    private static final String COMMAND_HELP = "help";
    // New command constants for filtering incomes
    private static final String COMMAND_FILTER_INCOME = "filter-income";
    private static final String COMMAND_FILTER_INCOME_DATE = "filter-income date";
    private static final String COMMAND_FILTER_INCOME_AMOUNT = "filter-income amount";
    private static final String COMMAND_FILTER_INCOME_CATEGORY = "filter-income category";
    // New command constant for saving goal
    private static final String COMMAND_SET_SAVING_GOAL = "set-saving-goal";
    private static final String ERROR_UNKNOWN_COMMAND = "Unknown command received: ";

    /**
     * Parses the user's input and extracts the corresponding command.
     *
     * @param input the user's input command.
     * @return the corresponding command for the user's input.
     * @throws UnknownCommandException if the user's command is unrecognizable.
     */
    public static Command getCommandFromInput(String input) throws UnknownCommandException {
        logger.info("Processing command: " + input);
        CommandIdentifier commandId = identifyCommand(input.trim());
        switch (commandId) {
            case ADD_INCOME:
                return new AddIncomeCommand(input);
            case SET_SAVING_GOAL:
                return new SetSavingGoalCommand(input);
            case LOG_EXPENSE:
                return new LogExpenseCommand(input);
            case DELETE_INCOME:
                return new DeleteIncomeCommand(input);
            case LIST_INCOME:
                return new ListIncomeCommand();
            case DELETE_EXPENSE:
                return new DeleteExpenseCommand(input);
            case VIEW_ALL_EXPENSES:
                return new ViewAllExpensesCommand();
            case FIND_EXPENSE:
                return new FindExpenseCommand(input);
            case EXIT:
                return new ExitCommand();
            case COMPARE:
                return new CompareExpenseCommand(input);
            case UPDATE_EXPENSE:
                return new UpdateExpenseCommand(input);
            case UPDATE_INCOME:
                return new UpdateIncomeCommand(input);
            case FILTER_INCOME:
                return new FilterIncomeCommand(input);
            case FILTER_INCOME_DATE:
                return new FilterIncomeByDateCommand(input);
            case FILTER_INCOME_AMOUNT:
                return new FilterIncomeByAmountCommand(input);
            case FILTER_INCOME_CATEGORY:
                return new FilterIncomeByCategoryCommand(input);
            case HELP:
                return new HelpCommand();
            default:
                logger.warning(ERROR_UNKNOWN_COMMAND + input);
                throw new UnknownCommandException();
        }
    }

    /**
     * Identifies the command type based on the input string.
     *
     * @param input the trimmed user input.
     * @return the corresponding CommandIdentifier.
     */
    private static CommandIdentifier identifyCommand(String input) {
        if (input.startsWith(COMMAND_ADD_INCOME)) {
            return CommandIdentifier.ADD_INCOME;
        } else if (input.startsWith(COMMAND_SET_SAVING_GOAL)) {
            return CommandIdentifier.SET_SAVING_GOAL;
        } else if (input.startsWith(COMMAND_LOG_EXPENSE)) {
            return CommandIdentifier.LOG_EXPENSE;
        } else if (input.startsWith(COMMAND_DELETE_INCOME)) {
            return CommandIdentifier.DELETE_INCOME;
        } else if (COMMAND_LIST_INCOME.equals(input)) {
            return CommandIdentifier.LIST_INCOME;
        } else if (input.startsWith(COMMAND_DELETE_EXPENSE)) {
            return CommandIdentifier.DELETE_EXPENSE;
        } else if (COMMAND_VIEW_ALL_EXPENSES.equals(input)) {
            return CommandIdentifier.VIEW_ALL_EXPENSES;
        } else if (input.startsWith(COMMAND_FIND_EXPENSE)) {
            return CommandIdentifier.FIND_EXPENSE;
        } else if (COMMAND_EXIT.equals(input)) {
            return CommandIdentifier.EXIT;
        } else if (input.startsWith(COMMAND_COMPARE)) {
            return CommandIdentifier.COMPARE;
        } else if (input.startsWith(COMMAND_UPDATE_EXPENSE)) {
            return CommandIdentifier.UPDATE_EXPENSE;
        } else if (input.startsWith(COMMAND_UPDATE_INCOME)) {
            return CommandIdentifier.UPDATE_INCOME;
        } else if (COMMAND_FILTER_INCOME.equals(input)) {
            return CommandIdentifier.FILTER_INCOME;
        } else if (input.startsWith(COMMAND_FILTER_INCOME_DATE)) {
            return CommandIdentifier.FILTER_INCOME_DATE;
        } else if (input.startsWith(COMMAND_FILTER_INCOME_AMOUNT)) {
            return CommandIdentifier.FILTER_INCOME_AMOUNT;
        } else if (input.startsWith(COMMAND_FILTER_INCOME_CATEGORY)) {
            return CommandIdentifier.FILTER_INCOME_CATEGORY;
        } else if (input.startsWith(COMMAND_HELP)) {
            return CommandIdentifier.HELP;
        }
        return CommandIdentifier.UNKNOWN;
    }

    /**
     * Enum to classify command types based on user input.
     */
    private enum CommandIdentifier {
        ADD_INCOME,
        SET_SAVING_GOAL,
        LOG_EXPENSE,
        DELETE_INCOME,
        LIST_INCOME,
        DELETE_EXPENSE,
        VIEW_ALL_EXPENSES,
        FIND_EXPENSE,
        EXIT,
        COMPARE,
        UPDATE_EXPENSE,
        UPDATE_INCOME,
        FILTER_INCOME,
        FILTER_INCOME_DATE,
        FILTER_INCOME_AMOUNT,
        FILTER_INCOME_CATEGORY,
        HELP,
        UNKNOWN
    }
}
