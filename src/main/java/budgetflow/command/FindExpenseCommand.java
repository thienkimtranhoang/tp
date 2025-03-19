package budgetflow.command;
import budgetflow.exception.MissingKeywordException;
import budgetflow.exception.UnfoundExpenseException;
import budgetflow.expense.ExpenseList;
import budgetflow.income.Income;

import java.util.List;
import java.util.logging.Logger;

public class FindExpenseCommand extends Command {
    private static final Logger logger = Logger.getLogger(FindExpenseCommand.class.getName());
    private static final String COMMAND_FIND_EXPENSE = "find-expense";
    private static final String ERROR_MISSING_KEYWORD = "Error: Missing keyword";

    public FindExpenseCommand(String input) {
        super(input);
        this.commandType = CommandType.READ;
    }

    @Override
    public void execute(List<Income> incomes, ExpenseList expenseList) throws UnfoundExpenseException, MissingKeywordException {
        assert input.startsWith(COMMAND_FIND_EXPENSE) : "Invalid find expense command format";
        String keyword = input.substring(COMMAND_FIND_EXPENSE.length()).trim();
        if (keyword.isEmpty()) {
            logger.warning("Attempted to query expense without keyword");
            throw new MissingKeywordException(ERROR_MISSING_KEYWORD);
        }

        ExpenseList matchingExpenses = expenseList.get(keyword);
        if (matchingExpenses.getSize() == 0) {
            logger.warning("Attempted to find non-existent expense: " + keyword);
            throw new UnfoundExpenseException("Sorry, I cannot find any expenses matching your keyword: " + keyword);
        } else {
            this.outputMessage = "Here are all matching expenses: " + System.lineSeparator() +
                                matchingExpenses;
            logger.info("Matching found: " + matchingExpenses);
        }
    }
}
