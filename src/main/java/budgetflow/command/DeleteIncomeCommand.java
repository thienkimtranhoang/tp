package budgetflow.command;

import budgetflow.exception.UnfoundIncomeException;
import budgetflow.expense.ExpenseList;
import budgetflow.income.Income;

import java.util.List;

public class DeleteIncomeCommand extends Command {
    private static final String COMMAND_DELETE_INCOME = "delete-income ";
    private static final String ERROR_INCOME_NOT_FOUND = "Income not found: ";

    public DeleteIncomeCommand(String input) {
        super(input);
        this.commandType = CommandType.DELETE;
    }

    @Override
    public void execute(List<Income> incomes, ExpenseList expenseList) throws UnfoundIncomeException {
        if (input.startsWith(COMMAND_DELETE_INCOME)) {
            input = input.substring(COMMAND_DELETE_INCOME.length()).trim();
        }
        boolean found = false;
        for (int i = 0; i < incomes.size(); i++) {
            if (incomes.get(i).getCategory().equalsIgnoreCase(input)) {
                incomes.remove(i);
                this.outputMessage = "Income deleted: " + input;
                found = true;
                break;
            }
        }
        if (!found) {
            throw new UnfoundIncomeException(ERROR_INCOME_NOT_FOUND + input);
        }
    }
}
