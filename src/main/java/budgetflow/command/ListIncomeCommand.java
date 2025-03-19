package budgetflow.command;

import budgetflow.expense.ExpenseList;
import budgetflow.income.Income;

import java.util.List;
import java.util.logging.Logger;

public class ListIncomeCommand extends Command {
    private static final Logger logger = Logger.getLogger(ListIncomeCommand.class.getName());
    private static final String EMPTY_INCOME_LIST_MESSAGE = "No incomes have been added yet.";

    public ListIncomeCommand() {
        super();
        this.commandType = CommandType.READ;
    }

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
