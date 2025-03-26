package budgetflow.command;

import budgetflow.exception.MissingDateException;
import budgetflow.exception.InvalidNumberFormatException;
import budgetflow.exception.MissingAmountException;
import budgetflow.exception.MissingCategoryException;
import budgetflow.exception.MissingDescriptionException;
import budgetflow.exception.MissingExpenseException;
import budgetflow.expense.Expense;
import budgetflow.expense.ExpenseList;
import budgetflow.income.Income;
import budgetflow.parser.DateValidator;
import budgetflow.storage.Storage; 

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.logging.Logger;

public class UpdateExpenseCommand extends Command {
    private static final Logger logger = Logger.getLogger(UpdateExpenseCommand.class.getName());

    private static final String UPDATE_EXPENSE_COMMAND_PREFIX = "update-expense ";
    private static final int UPDATE_EXPENSE_COMMAND_PREFIX_LENGTH = UPDATE_EXPENSE_COMMAND_PREFIX.length();

    private static final String ERROR_EMPTY_EXPENSE = "Expense should not be empty";
    private static final String ERROR_MISSING_EXPENSE_CATEGORY = "Error: Expense category is required.";
    private static final String ERROR_MISSING_EXPENSE_DESCRIPTION = "Error: Expense description is required.";
    private static final String ERROR_MISSING_EXPENSE_AMOUNT = "Error: Expense amount is required.";
    private static final String ERROR_MISSING_EXPENSE_DATE = "Error: Expense date is required.";
    private static final String ERROR_INCORRECT_EXPENSE_DATE = "Error: Expense date is in wrong format. " +
            "Please use DD-MM-YYYY format.";
    private static final String ERROR_INVALID_INDEX = "Error: Invalid index. Expense not found.";

    public UpdateExpenseCommand(String input) {
        super(input);
        this.commandType = CommandType.UPDATE;
    }

    /**
     * Executes the command to update an expense based on the input.
     * @param expenseList The list storing all expenses
     * @throws MissingDateException if the date of expense is missing or incorrect
     * @throws InvalidNumberFormatException if amount does not follow valid number format
     * @throws MissingAmountException if the amount of expense is missing
     * @throws MissingCategoryException if the category of expense is missing
     * @throws MissingDescriptionException if the description of expense is missing
     */
    @Override
    public void execute(List<Income> incomes, ExpenseList expenseList) throws MissingDateException,
            InvalidNumberFormatException, MissingAmountException, MissingCategoryException, MissingDescriptionException,
            MissingExpenseException {
        int index = extractIndex(input);
        Expense updatedExpense = extractExpense(input);

        if (index < 0 || index >= expenseList.getSize()) {
            logger.warning("Invalid index: " + index);
            throw new MissingExpenseException(ERROR_INVALID_INDEX);
        }

        // Update the expense at the found index
        Expense existingExpense = expenseList.get(index);
        existingExpense.setCategory(updatedExpense.getCategory());
        existingExpense.setDescription(updatedExpense.getDescription());
        existingExpense.setAmount(updatedExpense.getAmount());
        existingExpense.setDate(updatedExpense.getDate());

        // Update the total expenses
        expenseList.updateTotalExpenses();

        // Save the updated data to persistent storage
        Storage storage = new Storage();
        storage.saveData(incomes, expenseList);

        this.outputMessage = "Expense updated at index " + (index + 1) + ": " + existingExpense.getCategory() + " | " +
                existingExpense.getDescription() + " | $" + String.format("%.2f", existingExpense.getAmount()) + " | " +
                existingExpense.getDate() + ". Total Expenses: $" + String.format("%.2f",
                expenseList.getTotalExpenses());
    }

    // Extract the updated expense details from the input string
    private Expense extractExpense(String input) throws InvalidNumberFormatException,
            MissingCategoryException, MissingAmountException, MissingDateException,
            MissingDescriptionException {
        assert input != null && !input.isEmpty() : "Expense input should not be empty";
        assert input.startsWith(UPDATE_EXPENSE_COMMAND_PREFIX) : "Invalid update expense format";

        input = input.substring(UPDATE_EXPENSE_COMMAND_PREFIX_LENGTH).trim();

        if (input.isEmpty()) {
            throw new MissingDescriptionException(ERROR_EMPTY_EXPENSE);
        }

        String category = null;
        String description = null;
        Double amount = null;
        String date = null;

        // Patterns to extract expense details from the input
        String categoryPattern = "category/(.*?) (desc/|amt/|d/|$)";
        String descPattern = "desc/(.*?) (amt/|d/|$)";
        String amtPattern = "amt/([0-9]+(\\.[0-9]*)?)";
        String datePattern = "d/(\\d{2}-\\d{2}-\\d{4})";

        Pattern pattern;
        Matcher matcher;

        // Extract category
        pattern = Pattern.compile(categoryPattern);
        matcher = pattern.matcher(input);
        if (matcher.find()) {
            category = matcher.group(1).trim();
        }

        // Extract description
        pattern = Pattern.compile(descPattern);
        matcher = pattern.matcher(input);
        if (matcher.find()) {
            description = matcher.group(1).trim();
        }

        // Extract amount
        pattern = Pattern.compile(amtPattern);
        matcher = pattern.matcher(input);
        if (matcher.find()) {
            try {
                amount = Double.parseDouble(matcher.group(1));
            } catch (NumberFormatException e) {
                throw new InvalidNumberFormatException();
            }
        }

        // Extract date
        pattern = Pattern.compile(datePattern);
        matcher = pattern.matcher(input);
        if (matcher.find()) {
            date = matcher.group(1).trim();
            if (!DateValidator.isValidDate(date)) {
                logger.warning("Invalid date input: " + date);
                throw new MissingDateException(ERROR_INCORRECT_EXPENSE_DATE);
            }
        } else {
            throw new MissingDateException(ERROR_MISSING_EXPENSE_DATE);
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

    // Extract the index from the input
    private int extractIndex(String input) throws MissingExpenseException {
        String[] parts = input.split(" ");
        for (String part : parts) {
            if (part.startsWith("index/")) {
                try {
                    return Integer.parseInt(part.substring("index/".length())) - 1; // Adjust for 0-based index
                } catch (NumberFormatException e) {
                    throw new MissingExpenseException("Invalid index format");
                }
            }
        }
        throw new MissingExpenseException("Index is required");
    }
}
