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

/**
 * The {@code Parser} class is responsible for parsing user input strings
 * and instantiating the corresponding {@code Command} objects.
 * <p>
 * It supports commands for managing incomes, expenses, filtering incomes,
 * setting saving goals, and other functionalities of the Budgetflow application.
 * The parsing is done by tokenizing the input string and selecting the
 * appropriate command based on predefined command constants.
 * </p>
 *
 * <p>Supported commands include:</p>
 * <ul>
 *   <li>Add Income: "add category/..."</li>
 *   <li>Set Saving Goal: "set-saving-goal ..." </li>
 *   <li>Log Expense: "log-expense ..." </li>
 *   <li>Delete Income: "delete-income ..." </li>
 *   <li>List Income: "list income" </li>
 *   <li>Delete Expense: "delete-expense ..." </li>
 *   <li>View All Expenses: "view-all-expense" </li>
 *   <li>Find Expense: "find-expense ..." </li>
 *   <li>Exit: "exit" </li>
 *   <li>Compare Expenses: "compare ..." </li>
 *   <li>Update Expense: "update-expense ..." </li>
 *   <li>Update Income: "update-income ..." </li>
 *   <li>Filter Income: "filter-income" optionally followed by:
 *       <ul>
 *         <li>"date ..." for filtering by date,</li>
 *         <li>"amount ..." for filtering by amount,</li>
 *         <li>"category ..." for filtering by category.</li>
 *       </ul>
 *   </li>
 *   <li>Help: "help" </li>
 * </ul>
 *
 * @author IgoyAI (modified)
 * @version 2.1
 */
public class Parser {
  private static final Logger logger =
          Logger.getLogger(Parser.class.getName());

  // Command constants

  /**
   * Constant representing the add income command prefix.
   */
  private static final String COMMAND_ADD_INCOME = "add category/";

  /**
   * Constant representing the set saving goal command.
   */
  private static final String COMMAND_SET_SAVING_GOAL = "set-saving-goal";

  /**
   * Constant representing the log expense command prefix.
   */
  private static final String COMMAND_LOG_EXPENSE = "log-expense";

  /**
   * Constant representing the delete income command prefix.
   */
  private static final String COMMAND_DELETE_INCOME = "delete-income";

  /**
   * Constant representing the list income command.
   */
  private static final String COMMAND_LIST_INCOME = "list income";

  /**
   * Constant representing the delete expense command prefix.
   */
  private static final String COMMAND_DELETE_EXPENSE = "delete-expense";

  /**
   * Constant representing the view all expenses command.
   */
  private static final String COMMAND_VIEW_ALL_EXPENSES = "view-all-expense";

  /**
   * Constant representing the find expense command prefix.
   */
  private static final String COMMAND_FIND_EXPENSE = "find-expense";

  /**
   * Constant representing the exit command.
   */
  private static final String COMMAND_EXIT = "exit";

  /**
   * Constant representing the compare expenses command prefix.
   */
  private static final String COMMAND_COMPARE = "compare";

  /**
   * Constant representing the update expense command prefix.
   */
  private static final String COMMAND_UPDATE_EXPENSE = "update-expense";

  /**
   * Constant representing the update income command prefix.
   */
  private static final String COMMAND_UPDATE_INCOME = "update-income";

  /**
   * Constant representing the help command.
   */
  private static final String COMMAND_HELP = "help";

  // New command constants for filtering incomes

  /**
   * Constant representing the filter income command.
   */
  private static final String COMMAND_FILTER_INCOME = "filter-income";

  /**
   * Constant representing the specification for filtering by date.
   */
  private static final String COMMAND_FILTER_INCOME_DATE = "date";

  /**
   * Constant representing the specification for filtering by amount.
   */
  private static final String COMMAND_FILTER_INCOME_AMOUNT = "amount";

  /**
   * Constant representing the specification for filtering by category.
   */
  private static final String COMMAND_FILTER_INCOME_CATEGORY = "category";

  /**
   * Constant error message for unknown commands.
   */
  private static final String ERROR_UNKNOWN_COMMAND =
          "Unknown command received: ";

  /**
   * Parses the user's input and returns the corresponding {@code Command} object.
   *
   * <p>This method tokenizes the trimmed input string and uses the first token to determine
   * which command to execute. For the "filter-income" command, it further checks for a secondary
   * token to distinguish between filtering by date, amount, or category. If the input does not
   * match any known command, an {@code UnknownCommandException} is thrown.</p>
   *
   * @param input the user's input command string.
   * @return the corresponding {@code Command} instance.
   * @throws UnknownCommandException if the input command is not recognized.
   */
  public static Command getCommandFromInput(String input)
          throws UnknownCommandException {
    logger.info("Processing command: " + input);
    String trimmedInput = input.trim();
    String[] tokens = trimmedInput.split("\\s+");
    String firstToken = tokens[0];

    switch (firstToken) {
      case "add":
        if (trimmedInput.startsWith(COMMAND_ADD_INCOME)) {
          return new AddIncomeCommand(input);
        }
        break;
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
          switch (secondToken) {
            case COMMAND_FILTER_INCOME_DATE:
              return new FilterIncomeByDateCommand(input);
            case COMMAND_FILTER_INCOME_AMOUNT:
              return new FilterIncomeByAmountCommand(input);
            case COMMAND_FILTER_INCOME_CATEGORY:
              return new FilterIncomeByCategoryCommand(input);
            default:
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
