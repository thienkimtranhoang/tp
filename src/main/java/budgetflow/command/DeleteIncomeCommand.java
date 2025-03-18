package budgetflow.command;

import budgetflow.exception.UnfoundIncomeException;
import budgetflow.income.Income;

import java.util.List;

public class DeleteIncomeCommand extends Command {
    public static final String COMMAND_DELETE_INCOME = "delete-income ";
    public DeleteIncomeCommand(String input) {
        super(input);
    }

    public void execute(List<Income> incomes) throws UnfoundIncomeException {
        if (input.startsWith(COMMAND_DELETE_INCOME)) {
            input = input.substring(COMMAND_DELETE_INCOME.length()).trim();
        }
        boolean found = false;
        for (int i = 0; i < incomes.size(); i++) {
            if (incomes.get(i).getCategory().equalsIgnoreCase(input)) {
                incomes.remove(i);
                System.out.println("Income deleted: " + input);
                found = true;
                break;
            }
        }
        if (!found) {
            throw new UnfoundIncomeException("Income not found: " + input);
        }
        //Deal with Storage and UI. Take notes of saving data as well
//        if (!found) {
//            System.out.println("Income not found: " + income);
//        } else {
//            storage.saveData(incomes, expenseList);
//        }
    }
}
