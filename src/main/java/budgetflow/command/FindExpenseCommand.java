package budgetflow.command;
import budgetflow.exception.MissingKeywordException;
import budgetflow.exception.UnfoundExpenseException;
import budgetflow.expense.ExpenseList;
import budgetflow.income.Income;

import java.util.List;

public class FindExpenseCommand extends Command {

    private static final String COMMAND_FIND_EXPENSE = "find-expense";
    private static final String ERROR_MISSING_KEYWORD = "Error: Missing keyword";

    public FindExpenseCommand(String input) {
        super(input);
        this.commandType = CommandType.READ;
    }

    @Override
    public void execute(List<Income> incomes, ExpenseList expenseList) throws UnfoundExpenseException, MissingKeywordException {

        String keyword = "";

        if (input.startsWith(COMMAND_FIND_EXPENSE)) {
            keyword += input.substring(COMMAND_FIND_EXPENSE.length()).trim();
        }
        if (keyword.isEmpty()) {
            throw new MissingKeywordException(ERROR_MISSING_KEYWORD);
        }

        ExpenseList matchingExpenses = expenseList.get(keyword);
        if (matchingExpenses.getSize() == 0) {
            throw new UnfoundExpenseException("Sorry, I cannot find any expenses matching your keyword: " + keyword);
        } else {
            this.outputMessage = "Here are all matching expenses: " + System.lineSeparator() +
                                matchingExpenses;
        }
    }
}
