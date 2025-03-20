package budgetflow.command;

import budgetflow.expense.ExpenseList;
import budgetflow.income.Income;

import java.util.List;
import java.util.logging.Logger;

/**
 * Represents a command to list all recorded incomes.
 * If no incomes have been recorded, an appropriate message is displayed.
 */
public class ListIncomeCommand extends Command {
    private static final Logger logger = Logger.getLogger(ListIncomeCommand.class.getName());
    private static final String EMPTY_INCOME_LIST_MESSAGE = "No incomes have been added yet.";

    /**
     * Constructs a ListIncomeCommand.
     * This command is used to display all recorded incomes.
     */
    public ListIncomeCommand() {
        super();
        this.commandType = CommandType.READ;
    }

    /**
     * Executes the command to list all incomes.
     * If the income list is empty, a message indicating no incomes are recorded is displayed.
     * Otherwise, all incomes along with their total amount are displayed.
     *
     * @param incomes     The list of incomes to be displayed.
     * @param expenseList The list of expenses (not used in this command).
     */
    @Override
    public void execute(List<Income> incomes, ExpenseList expenseList) {
        if (incomes.isEmpty()) {
            logger.info("Reading empty income list");
            this.outputMessage = EMPTY_INCOME_LIST_MESSAGE + System.lineSeparator();
            return;
        }
        double totalIncome = 0.0;
        String message = "Income Log:" + System.lineSeparator();
        for (Income income : incomes) {
            message += income.getCategory() + " | $" +
                    String.format("%.2f", income.getAmount()) + " | " +
                    income.getDate() + System.lineSeparator();
            totalIncome += income.getAmount();
        }
        message += "Total Income: $" + String.format("%.2f", totalIncome) + System.lineSeparator();
        this.outputMessage = message;
        logger.info("Reading non-empty income list");
    }
}
