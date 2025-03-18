package budgetflow.command;

import budgetflow.expense.ExpenseList;
import budgetflow.income.Income;

import java.util.List;

public class ExitCommand extends Command {
    public ExitCommand() {
        this.commandType = CommandType.EXIT;
    }

    @Override
    public void execute(List<Income> incomes, ExpenseList expenseList) {
        this.outputMessage = "Goodbye!";
        this.isExit = true;
    }
}
