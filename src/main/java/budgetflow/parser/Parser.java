package budgetflow.parser;

import budgetflow.command.*;
import budgetflow.exception.UnknownCommandException;

public class Parser {

    // Command constants
    public static final String COMMAND_ADD_INCOME = "add category/";
    public static final String COMMAND_LOG_EXPENSE = "log-expense ";
    public static final String COMMAND_LIST_INCOME = "list income";
    public static final String COMMAND_EXIT = "exit";
    public static final String COMMAND_DELETE_INCOME = "delete-income ";
    public static final String COMMAND_DELETE_EXPENSE = "delete-expense ";
    public static final String COMMAND_VIEW_ALL_EXPENSES = "view-all-expense";
    public static final String COMMAND_FIND_EXPENSE = "find-expense";


    public static Command getCommandFromInput(String input) throws UnknownCommandException {
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
            throw new UnknownCommandException();
        }
    }
}

