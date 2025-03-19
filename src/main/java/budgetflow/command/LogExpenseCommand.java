package budgetflow.command;

import budgetflow.exception.InvalidNumberFormatException;
import budgetflow.exception.MissingDateException;
import budgetflow.exception.MissingAmountException;
import budgetflow.exception.MissingCategoryException;
import budgetflow.exception.MissingDescriptionException;
import budgetflow.exception.MissingExpenseException;
import budgetflow.expense.Expense;
import budgetflow.expense.ExpenseList;
import budgetflow.income.Income;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogExpenseCommand extends Command{
    private static final String LOG_EXPENSE_COMMAND_PREFIX = "log-expense ";
    private static final int LOG_EXPENSE_COMMAND_PREFIX_LENGTH = LOG_EXPENSE_COMMAND_PREFIX.length();

    public static final String ERROR_EMPTY_EXPENSE = "Expense should not be empty";
    private static final String ERROR_MISSING_EXPENSE_CATEGORY = "Error: Expense category is required.";
    private static final String ERROR_MISSING_EXPENSE_DESCRIPTION = "Error: Expense description is required.";
    private static final String ERROR_MISSING_EXPENSE_AMOUNT = "Error: Expense amount is required.";
    private static final String ERROR_MISSING_EXPENSE_DATE = "Error: Expense date is required.";

    public LogExpenseCommand(String input) {
        super(input);
        this.commandType = CommandType.CREATE;
    }

    /**
     * Log new user expense into the expense list
     * @param expenseList the list storing all expenses
     * @throws MissingDateException if user miss the date of expense or date tag
     * @throws InvalidNumberFormatException if amount does not follow valid number format
     * @throws MissingAmountException if user miss the amount of expense or amount tag
     * @throws MissingCategoryException if user miss the category of expense or expense tag
     * @throws MissingDescriptionException if user miss description of expense or expense tag
     */
    @Override
    public void execute(List<Income> incomes, ExpenseList expenseList) throws MissingDateException,
            InvalidNumberFormatException, MissingAmountException, MissingCategoryException,
            MissingDescriptionException, MissingExpenseException {
        Expense expense = extractExpense(input);
        expenseList.add(expense);
        this.outputMessage = "Expense logged: " + expense.getCategory() + " | " + expense.getDescription() +
                " | $" + String.format("%.2f", expense.getAmount()) + " | " + expense.getDate();
    }

    private Expense extractExpense (String input) throws InvalidNumberFormatException,
            MissingCategoryException, MissingAmountException, MissingDateException,
            MissingDescriptionException, MissingExpenseException {
        assert input != null && !input.isEmpty() : "Expense input should not be empty";
        assert input.startsWith(LOG_EXPENSE_COMMAND_PREFIX) : "Invalid log expense command format";

        input = input.substring(LOG_EXPENSE_COMMAND_PREFIX_LENGTH).trim();

        if (input.isEmpty()) {
            throw new MissingExpenseException(ERROR_EMPTY_EXPENSE);
        }

        String category = null;
        String description = null;
        Double amount = null;
        String date = null;

        String categoryPattern = "category/(.*?) (desc/|amt/|d/|$)";
        String descPattern = "desc/(.*?) (amt/|d/|$)";
        String amtPattern = "amt/([0-9]+(\\.[0-9]*)?)";
        String datePattern = "d/([^ ]+)";

        java.util.regex.Pattern pattern;
        java.util.regex.Matcher matcher;

        pattern = java.util.regex.Pattern.compile(categoryPattern);
        matcher = pattern.matcher(input);
        if (matcher.find()) {
            category = matcher.group(1).trim();
        }
        pattern = java.util.regex.Pattern.compile(descPattern);
        matcher = pattern.matcher(input);
        if (matcher.find()) {
            description = matcher.group(1).trim();
        }
        pattern = java.util.regex.Pattern.compile(amtPattern);
        matcher = pattern.matcher(input);
        if (matcher.find()) {
            try {
                amount = Double.parseDouble(matcher.group(1));
            } catch (NumberFormatException e) {
                throw new InvalidNumberFormatException();
            }
        }
        pattern = java.util.regex.Pattern.compile(datePattern);
        matcher = pattern.matcher(input);
        if (matcher.find()) {
            date = matcher.group(1).trim();
        }

        if (category == null || category.isEmpty()) {
            throw new MissingCategoryException(ERROR_MISSING_EXPENSE_CATEGORY);
        }
        if (description == null || description.isEmpty()) {
            throw new MissingDescriptionException(ERROR_MISSING_EXPENSE_DESCRIPTION);
        }
        if (amount == null) {
            throw new MissingAmountException(ERROR_MISSING_EXPENSE_AMOUNT);
        }
        if (date == null) {
            throw new MissingDateException(ERROR_MISSING_EXPENSE_DATE);
        }
        return new Expense(category, description, amount, date);
    }

    private String extractPattern(String input, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        return matcher.find() ? matcher.group(1).trim() : null;
    }
}
