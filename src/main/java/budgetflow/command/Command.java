package budgetflow.command;

import budgetflow.exception.FinanceException;
import budgetflow.expense.ExpenseList;
import budgetflow.income.Income;

import java.util.List;

/**
 * Represents an abstract base class for commands in the BudgetFlow application.
 * This class defines the basic structure and behavior that all commands should follow.
 * Each specific command (e.g., `AddExpenseCommand`, `UpdateExpenseCommand`) will extend this class and implement its own logic.
 */
public abstract class Command {

    // The type of the command (e.g., Add, Update, Delete)
    protected CommandType commandType;

    // The raw input received from the user
    protected String input;

    // The output message to be displayed after command execution
    protected String outputMessage;

    // Flag indicating if the application should exit after executing this command
    protected boolean isExit = false;

    /**
     * Default constructor for Command.
     * Initializes a new command object without any input.
     */
    public Command() {}

    /**
     * Constructs a Command with the specified user input.
     *
     * @param input The raw input received from the user.
     */
    public Command(String input) {
        this.input = input;
    }

    /**
     * Executes the command on the provided income and expense list.
     * This method should be implemented by concrete subclasses of Command.
     *
     * @param incomes      The list of incomes to operate on.
     * @param expenseList  The list of expenses to operate on.
     * @throws FinanceException If the command cannot be executed due to an error.
     */
    public void execute(List<Income> incomes, ExpenseList expenseList) throws FinanceException {
        throw new FinanceException("This method is operated by child classes");
    }

    /**
     * Returns whether the application should exit after this command is executed.
     *
     * @return `true` if the application should exit, `false` otherwise.
     */
    public boolean isExit() {
        return isExit;
    }

    /**
     * Gets the output message to be displayed after the command is executed.
     *
     * @return The output message.
     */
    public String getOutputMessage() {
        return outputMessage;
    }

    /**
     * Gets the type of the command.
     *
     * @return The command type.
     */
    public CommandType getCommandType() {
        return commandType;
    }
}