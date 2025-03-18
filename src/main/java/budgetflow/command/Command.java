package budgetflow.command;

import budgetflow.exception.FinanceException;

public class Command {
    protected String input;
    protected String outputMessage;
    protected boolean isExit = false;

    public Command() {}

    public Command(String input) {
        this.input = input;
    }

    public void executeCommand() throws FinanceException {
        throw new FinanceException("This method is operated by child classes");
    }
    public boolean isExit() {
        return isExit;
    }

    public String getOutputMessage() {
        return outputMessage;
    }
}
