package budgetflow.command;

import budgetflow.expense.ExpenseList;
import budgetflow.income.Income;

import java.util.List;

/**
 * Represents a command to exit the application.
 * When executed, this command sets the exit flag and provides a farewell message.
 */
public class ExitCommand extends Command {
    /**
     * Constructs an ExitCommand and sets its type to EXIT.
     */
    public ExitCommand() {
        this.commandType = CommandType.EXIT;
    }

    /**
     * Executes the exit command by setting the output message and marking the command as an exit action.
     *
     * @param incomes      The list of incomes (not modified by this command).
     * @param expenseList  The list of expenses (not modified by this command).
     */
    @Override
    public void execute(List<Income> incomes, ExpenseList expenseList) {
        this.outputMessage = "Goodbye!";
        this.isExit = true;
    }
}
