package budgetflow.command;

public class ExitCommand extends Command {
    public ExitCommand() {
    }
    public void execute() {
        this.isExit = true;
    }
}
