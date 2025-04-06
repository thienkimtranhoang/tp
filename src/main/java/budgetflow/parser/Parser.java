package budgetflow.parser;

import budgetflow.command.*;
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

    private static final String COMMAND_ADD_INCOME = "add category/";
    private static final String COMMAND_SET_SAVING_GOAL = "set-saving-goal";
    private static final String COMMAND_LOG_EXPENSE = "log-expense";
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

    private static final String COMMAND_FILTER_INCOME = "filter-income";
    private static final String COMMAND_FILTER_INCOME_DATE = "date";
    private static final String COMMAND_FILTER_INCOME_AMOUNT = "amount";
    private static final String COMMAND_FILTER_INCOME_CATEGORY = "category";

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
        String trimmedInput = input.trim();
        String[] tokens = trimmedInput.split("\\s+");
        String firstToken = tokens[0];

        switch (firstToken) {
        case "add":
            // Modified by IgoyAI: Accept "add" command even when no parameters are given.
            return new AddIncomeCommand(input);
        case COMMAND_SET_SAVING_GOAL:
            return new SetSavingGoalCommand(input);
        case COMMAND_LOG_EXPENSE:
            return new LogExpenseCommand(input);
        case COMMAND_DELETE_INCOME:
            return new DeleteIncomeCommand(input);
        case "list":
            if (trimmedInput.equals(COMMAND_LIST_INCOME)) {
                return new ListIncomeCommand();
            }
            break;
        case COMMAND_DELETE_EXPENSE:
            return new DeleteExpenseCommand(input);
        case COMMAND_VIEW_ALL_EXPENSES:
            if (trimmedInput.equals(COMMAND_VIEW_ALL_EXPENSES)) {
                return new ViewAllExpensesCommand();
            }
            break;
        case COMMAND_FIND_EXPENSE:
            return new FindExpenseCommand(input);
        case COMMAND_EXIT:
            if (trimmedInput.equals(COMMAND_EXIT)) {
                return new ExitCommand();
            }
            break;
        case COMMAND_COMPARE:
            return new CompareExpenseCommand(input);
        case COMMAND_UPDATE_EXPENSE:
            return new UpdateExpenseCommand(input);
        case COMMAND_UPDATE_INCOME:
            return new UpdateIncomeCommand(input);
        case COMMAND_FILTER_INCOME:
            if (tokens.length == 1) {
                return new FilterIncomeCommand(input);
            } else {
                String secondToken = tokens[1];
                // Split on "/" to extract the filter type.
                String filterType = secondToken.contains("/") ? secondToken.split("/")[0] : secondToken;
                if (filterType.equals(COMMAND_FILTER_INCOME_DATE)) {
                    return new FilterIncomeByDateCommand(input);
                } else if (filterType.equals(COMMAND_FILTER_INCOME_AMOUNT)) {
                    return new FilterIncomeByAmountCommand(input);
                } else if (filterType.equals(COMMAND_FILTER_INCOME_CATEGORY)) {
                    return new FilterIncomeByCategoryCommand(input);
                } else {
                    return new FilterIncomeCommand(input);
                }
            }
        case COMMAND_HELP:
            return new HelpCommand();
        default:
            break;
        }
        logger.warning(ERROR_UNKNOWN_COMMAND + input);
        throw new UnknownCommandException();
    }
}
