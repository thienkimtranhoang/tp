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
import budgetflow.exception.UnknownCommandException;

import java.util.logging.Logger;

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

    /**
     * Parsing the user's input and extract corresponding command
     * @param input user's input command
     * @return corresponding command to user's command
     * @throws UnknownCommandException if user's command is unrecognizable
     */
    public static Command getCommandFromInput(String input) throws UnknownCommandException {
        logger.info("Processing command: " + input);
        if (input.startsWith(COMMAND_ADD_INCOME)) {
            return new AddIncomeCommand(input);
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
        } else {
            logger.warning("Unknown command received: " + input);
            throw new UnknownCommandException();
        }
    }
}

