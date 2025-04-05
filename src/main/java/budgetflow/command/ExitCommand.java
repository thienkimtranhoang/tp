package budgetflow.command;

import budgetflow.expense.ExpenseList;
import budgetflow.income.Income;
import java.util.List;

/**
 * Represents a command to exit the application.
 * <p>
 * When executed, this command sets the exit flag and displays a formatted farewell message.
 *
 * @@author IgoyAI
 */
public class ExitCommand extends Command {
    private static final int OUTPUT_WIDTH = 60;
    private static final String BORDER_CHAR = "=";

    /**
     * Constructs an ExitCommand and sets its type to EXIT.
     */
    public ExitCommand() {
        this.commandType = CommandType.EXIT;
    }

    /**
     * Executes the exit command by setting a formatted farewell message and marking the command as an exit action.
     *
     * @param incomes     The list of incomes (not modified by this command).
     * @param expenseList The list of expenses (not modified by this command).
     */
    @Override
    public void execute(List<Income> incomes, ExpenseList expenseList) {
        String border = BORDER_CHAR.repeat(OUTPUT_WIDTH);
        String farewellMessage = "Thank you for using Budgetflow!";
        String goodbye = "Goodbye!";

        String centeredFarewell = centerText(farewellMessage, OUTPUT_WIDTH);
        String centeredGoodbye = centerText(goodbye, OUTPUT_WIDTH);

        StringBuilder sb = new StringBuilder();
        sb.append(border).append(System.lineSeparator());
        sb.append(centeredFarewell).append(System.lineSeparator());
        sb.append(centeredGoodbye).append(System.lineSeparator());
        sb.append(border);

        this.outputMessage = sb.toString();
        this.isExit = true;
    }

    /**
     * Centers the provided text within the specified width.
     *
     * @param text  the text to center.
     * @param width the total width for centering.
     * @return a string with the text centered.
     */
    private String centerText(String text, int width) {
        if (text.length() >= width) {
            return text;
        }
        int padding = (width - text.length()) / 2;
        return " ".repeat(padding) + text;
    }
}
