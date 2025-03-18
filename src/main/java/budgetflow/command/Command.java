package budgetflow.command;

import budgetflow.exception.FinanceException;

public class Command {
    protected String input;
    protected String commandMessage;
    protected boolean isExit = false;

    public Command() {}

    public Command(String input) {
        this.input = input;
    }

    public void executeCommand() throws FinanceException {
        throw new FinanceException("This method is operated by child classes");
    }
}
