package budgetflow.command;

import budgetflow.exception.FinanceException;
import budgetflow.expense.ExpenseList;
import budgetflow.income.Income;

import java.util.List;

public class Command {
    protected CommandType commandType;
    protected String input;
    protected String outputMessage;
    protected boolean isExit = false;

    public Command() {}

    public Command(String input) {
        this.input = input;
    }

    public void execute(List<Income> incomes, ExpenseList expenseList) throws FinanceException {
        throw new FinanceException("This method is operated by child classes");
    }
    public boolean isExit() {
        return isExit;
    }

    public String getOutputMessage() {
        return outputMessage;
    }

    public CommandType getCommandType() {
        return commandType;
    }
}
