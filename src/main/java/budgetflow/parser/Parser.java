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
import budgetflow.command.HelpCommand;
import budgetflow.command.ListIncomeCommand;
import budgetflow.command.LogExpenseCommand;
import budgetflow.command.SetSavingGoalCommand;
import budgetflow.command.UpdateExpenseCommand;
import budgetflow.command.UpdateIncomeCommand;
import budgetflow.command.ViewAllExpensesCommand;
import budgetflow.exception.UnknownCommandException;
import java.util.logging.Logger;

//@@author thienkimtranhoang
// Original Parser implementation
//@@author IgoyAI
// Modified: Updated "add" branch to accept "add" command without parameters.
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

    public static Command getCommandFromInput(String input)
            throws UnknownCommandException {
        logger.info("Processing command: " + input);
        String trimmedInput = input.trim();
        String[] tokens = trimmedInput.split("\\s+");
        String firstToken = tokens[0];

        switch (firstToken) {
        case "add":
            // Modified by IgoyAI: Accept "add" command even when no parameters are given.
            return new AddIncomeCommand(input);
        case "set-saving-goal":
            return new SetSavingGoalCommand(input);
        case "log-expense":
            return new LogExpenseCommand(input);
        case "delete-income":
            return new DeleteIncomeCommand(input);
        case "list":
            if (trimmedInput.equals(COMMAND_LIST_INCOME)) {
                return new ListIncomeCommand();
            }
            break;
        case "delete-expense":
            return new DeleteExpenseCommand(input);
        case "view-all-expense":
            if (trimmedInput.equals(COMMAND_VIEW_ALL_EXPENSES)) {
                return new ViewAllExpensesCommand();
            }
            break;
        case "find-expense":
            return new FindExpenseCommand(input);
        case "exit":
            if (trimmedInput.equals(COMMAND_EXIT)) {
                return new ExitCommand();
            }
            break;
        case "compare":
            return new CompareExpenseCommand(input);
        case "update-expense":
            return new UpdateExpenseCommand(input);
        case "update-income":
            return new UpdateIncomeCommand(input);
        case "filter-income":
            if (tokens.length == 1) {
                return new FilterIncomeCommand(input);
            } else {
                String secondToken = tokens[1];
                // Split on "/" to extract the filter type.
                String filterType = secondToken.contains("/")
                        ? secondToken.split("/")[0] : secondToken;
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
        case "help":
            return new HelpCommand();
        default:
            break;
        }
        logger.warning(ERROR_UNKNOWN_COMMAND + input);
        throw new UnknownCommandException();
    }
}
