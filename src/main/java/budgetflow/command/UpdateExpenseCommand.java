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
            InvalidNumberFormatException, MissingAmountException, MissingCategoryException, MissingDescriptionException
    {
        String[] parts = input.substring(UPDATE_EXPENSE_COMMAND_PREFIX_LENGTH).trim().split(EMPTY_SPACE, MINIMUM_PARTS_FOR_UPDATE);
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
        Pattern categoryPattern = Pattern.compile(CATEGORY_PATTERN);
        Pattern amtPattern = Pattern.compile(AMT_PATTERN);
        Pattern descPattern = Pattern.compile(DESC_PATTERN);
        Pattern datePattern = Pattern.compile(DATE_PATTERN);

        existingExpense.setCategory(getUpdatedCategory(input, categoryPattern, existingExpense.getCategory()));
        existingExpense.setAmount(getUpdatedAmount(input, amtPattern, existingExpense.getAmount()));
        existingExpense.setDescription(getUpdatedDescription(input, descPattern, existingExpense.getDescription()));
        existingExpense.setDate(getUpdatedDate(input, datePattern, existingExpense.getDate()));
    }

    private static String getUpdatedCategory(String input, Pattern categoryPattern, String category)
            throws MissingCategoryException {
        Matcher matcher = categoryPattern.matcher(input);
        if (matcher.find()) {
            String extractedCategory = matcher.group(UPDATE_PARAMETER_GROUP).trim();
            if (extractedCategory.isEmpty()) {
                throw new MissingCategoryException("Error: Invalid category.");
            }
            return extractedCategory;
        }
        return category;
    }

    private static String getUpdatedDescription(String input, Pattern descPattern, String description)
            throws MissingDescriptionException {
        Matcher matcher = descPattern.matcher(input);
        if (matcher.find()) {
            String extractedDescription = matcher.group(UPDATE_PARAMETER_GROUP).trim();
            if (extractedDescription.isEmpty()) {
                throw new MissingDescriptionException("Error: Invalid description.");
            }
            return extractedDescription;
        }
        return description;
    }

    private static String getUpdatedDate(String input, Pattern datePattern, String date)
            throws MissingDateException {
        Matcher matcher = datePattern.matcher(input);
        if (matcher.find()) {
            String extractedDate = matcher.group(UPDATE_PARAMETER_GROUP).trim();
            if (!extractedDate.matches(CORRECT_DATE_PATTERN)) {
                throw new MissingDateException(ERROR_WRONG_DATE_FORMAT);
            }
            if (!DateValidator.isValidDate(extractedDate)) {
                throw new MissingDateException(ERROR_WRONG_DATE_FORMAT);
            }
            return extractedDate;
        }
        return date;
    }

    private static Double getUpdatedAmount(String input, Pattern amtPattern, Double amount)
            throws MissingAmountException {
        Matcher matcher = amtPattern.matcher(input);
        if (matcher.find()) {
            String extractedAmount = matcher.group(UPDATE_PARAMETER_GROUP).trim();
            if (!extractedAmount.matches(CORRECT_AMOUNT_PATTERN)) {
                throw new MissingAmountException(ERROR_INVALID_AMOUNT);
            }
            return Double.parseDouble(extractedAmount);
        }
        return amount;
    }
}

