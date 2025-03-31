package budgetflow.command;

import budgetflow.exception.MissingDateException;
import budgetflow.exception.InvalidNumberFormatException;
import budgetflow.exception.MissingAmountException;
import budgetflow.exception.MissingCategoryException;
import budgetflow.exception.MissingDescriptionException;
import budgetflow.expense.Expense;
import budgetflow.expense.ExpenseList;
import budgetflow.income.Income;
import budgetflow.storage.Storage;
import budgetflow.parser.DateValidator;

import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.logging.Logger;

public class UpdateExpenseCommand extends Command {
    private static final Logger logger = Logger.getLogger(UpdateExpenseCommand.class.getName());

    private static final String UPDATE_EXPENSE_COMMAND_PREFIX = "update-expense ";
    private static final int UPDATE_EXPENSE_COMMAND_PREFIX_LENGTH = UPDATE_EXPENSE_COMMAND_PREFIX.length();

    private static final String ERROR_MISSING_INDEX = "Error: Index is required.";
    private static final String ERROR_EXPENSE_ENTRY_NOT_FOUND = "Error: Expense entry not found.";
    private static final String ERROR_WRONG_INDEX_FORMAT = "Error: Index must be a number.";
    private static final String ERROR_EMPTY_EXPENSE_LIST = "Error: No expense entries exist to update.";
    private static final String ERROR_WRONG_DATE_FORMAT = "Error: Invalid date format. Usage: DD-MM-YYYY";
    private static final String ERROR_INVALID_AMOUNT = "Error: Invalid amount format.";
    private static final String ERROR_MISSING_DESCRIPTION = "Error: Missing description.";
    private static final String ERROR_MISSING_CATEGORY = "Error: Missing category.";
    private static final String ERROR_MISSING_AMOUNT = "Error: Missing amount.";
    private static final String ERROR_MISSING_DATE = "Error: Missing date.";

    private static final String CATEGORY_PATTERN = "category/([^ ]+)";
    private static final String AMT_PATTERN = "amt/([^ ]+)";
    private static final String DESC_PATTERN = "desc/([^ ]+)";
    private static final String DATE_PATTERN = "d/([^ ]+)";
    private static final String CORRECT_AMOUNT_PATTERN = "[0-9]+(\\.[0-9]*)?";
    private static final String CORRECT_DATE_PATTERN = "\\d{2}-\\d{2}-\\d{4}";
    private static final String EMPTY_SPACE = " ";

    private static final int MINIMUM_INDEX = 0;
    private static final int INDEX_POSITION_IN_1_INDEX = 0;
    private static final int UPDATE_PARAMETER_GROUP = 1;
    private static final int MINIMUM_PARTS_FOR_UPDATE = 2;

    public UpdateExpenseCommand(String input) {
        super(input);
        this.commandType = CommandType.UPDATE;
    }

    @Override
    public void execute(List<Income> incomes, ExpenseList expenseList) throws MissingDateException,
            InvalidNumberFormatException, MissingAmountException, MissingCategoryException, MissingDescriptionException {

        String[] parts = input.substring(UPDATE_EXPENSE_COMMAND_PREFIX_LENGTH).trim().split(EMPTY_SPACE,
                MINIMUM_PARTS_FOR_UPDATE);

        if (parts.length < MINIMUM_PARTS_FOR_UPDATE) {
            throw new InvalidNumberFormatException(ERROR_MISSING_INDEX);
        }

        if (expenseList.getSize() == MINIMUM_INDEX) {
            throw new InvalidNumberFormatException(ERROR_EMPTY_EXPENSE_LIST);
        }

        int index;
        try {
            index = Integer.parseInt(parts[INDEX_POSITION_IN_1_INDEX]) - 1;
        } catch (NumberFormatException e) {
            throw new InvalidNumberFormatException(ERROR_WRONG_INDEX_FORMAT);
        }

        if (index < MINIMUM_INDEX || index >= expenseList.getSize()) {
            throw new InvalidNumberFormatException(ERROR_EXPENSE_ENTRY_NOT_FOUND);
        }

        Expense existingExpense = expenseList.get(index);
        extractUpdatedExpense(parts[1], existingExpense);

        expenseList.updateTotalExpenses();
        updateStorage(incomes, expenseList);

        this.outputMessage = "Expense updated: " + existingExpense.getCategory() + ", Description: " +
                existingExpense.getDescription() + ", Amount: $" + String.format("%.2f", existingExpense.getAmount()) +
                ", Date: " + existingExpense.getDate();
        logger.info("Expense updated successfully: " + existingExpense);
    }

    private static void updateStorage(List<Income> incomes, ExpenseList expenseList) {
        Storage storage = new Storage();
        storage.saveData(incomes, expenseList);
    }

    private void extractUpdatedExpense(String input, Expense existingExpense)
            throws MissingAmountException, MissingDateException, MissingCategoryException, MissingDescriptionException {

        String category = getUpdatedCategory(input);
        if (category == null) throw new MissingCategoryException(ERROR_MISSING_CATEGORY);
        existingExpense.setCategory(category);

        Double amount = getUpdatedAmount(input);
        if (amount == null) throw new MissingAmountException(ERROR_MISSING_AMOUNT);
        existingExpense.setAmount(amount);

        String description = getUpdatedDescription(input);
        if (description == null) throw new MissingDescriptionException(ERROR_MISSING_DESCRIPTION);
        existingExpense.setDescription(description);

        String date = getUpdatedDate(input);
        if (date == null) throw new MissingDateException(ERROR_MISSING_DATE);
        existingExpense.setDate(date);
    }

    private static String getUpdatedCategory(String input) {
        Matcher matcher = Pattern.compile(CATEGORY_PATTERN).matcher(input);
        return matcher.find() ? matcher.group(UPDATE_PARAMETER_GROUP).trim() : null;
    }

    private static String getUpdatedDescription(String input) {
        Matcher matcher = Pattern.compile(DESC_PATTERN).matcher(input);
        return matcher.find() ? matcher.group(UPDATE_PARAMETER_GROUP).trim() : null;
    }

    private static String getUpdatedDate(String input) throws MissingDateException {
        Matcher matcher = Pattern.compile(DATE_PATTERN).matcher(input);
        if (matcher.find()) {
            String extractedDate = matcher.group(UPDATE_PARAMETER_GROUP).trim();
            if (!extractedDate.matches(CORRECT_DATE_PATTERN) || !DateValidator.isValidDate(extractedDate)) {
                throw new MissingDateException(ERROR_WRONG_DATE_FORMAT);
            }
            return extractedDate;
        }
        return null;
    }

    private static Double getUpdatedAmount(String input) {
        Matcher matcher = Pattern.compile(AMT_PATTERN).matcher(input);
        if (matcher.find()) {
            String extractedAmount = matcher.group(UPDATE_PARAMETER_GROUP).trim();
            if (extractedAmount.matches(CORRECT_AMOUNT_PATTERN)) {
                return Double.parseDouble(extractedAmount);
            }
        }
        return null;
    }
}
