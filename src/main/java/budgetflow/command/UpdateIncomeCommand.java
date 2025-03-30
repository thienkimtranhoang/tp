package budgetflow.command;

import budgetflow.exception.MissingDateException;
import budgetflow.exception.InvalidNumberFormatException;
import budgetflow.exception.MissingAmountException;
import budgetflow.exception.MissingCategoryException;
import budgetflow.expense.ExpenseList;
import budgetflow.income.Income;
import budgetflow.parser.DateValidator;
import budgetflow.storage.Storage;

import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.regex.Matcher;


/**
 * The class handles the logic for updating an existing income record in the system.
 * It processes the input command to extract relevant income details (category, amount, date) and updates the
 * corresponding income entry in the list. It also saves the updated data to storage.
 */
public class UpdateIncomeCommand extends Command {
    private static final Logger logger = Logger.getLogger(UpdateIncomeCommand.class.getName());

    private static final String UPDATE_COMMAND_PREFIX = "update-income ";
    private static final int UPDATE_COMMAND_PREFIX_LENGTH = UPDATE_COMMAND_PREFIX.length();

    private static final String ERROR_INVALID_DATE = "Error: Date is not a valid date";
    private static final String ERROR_MISSING_INDEX = "Error: Index is required.";
    private static final String ERROR_INCOME_ENTRY_NOT_FOUND = "Error: Income entry not found.";
    private static final String ERROR_WRONG_INDEX_FORMAT = "Error: Index must be a number.";
    private static final String ERROR_INVALID_AMOUNT = "Invalid amount format.";

    private static final String CATEGORY_PATTERN = "category/([^ ]+)";
    private static final String AMT_PATTERN = "amt/([0-9]+(\\.[0-9]*)?)";
    private static final String DATE_PATTERN = "d/(\\d{2}-\\d{2}-\\d{4})";

    private static final int INDEX_POSITION_IN_1_INDEX = 0;
    private static final int MINIMUM_INDEX = 0;
    private static final int UPDATE_PARAMETER_GROUP = 1;
    private static final int MINIMUM_PARTS_FOR_UPDATE = 2;

    /**
     * Constructs an UpdateIncomeCommand with the given input string.
     *
     * @param input The input command string to be processed, containing the income ID and update details.
     */
    public UpdateIncomeCommand(String input) {
        super(input);
        this.commandType = CommandType.UPDATE;
    }

    /**
     * Executes the income update command by parsing the input, validating the index and other parameters,
     * and updating the corresponding income in the list.
     *
     * @param incomes The list of existing incomes.
     * @param expenseList The expense list (not used in this method, but passed to storage).
     * @throws MissingDateException If the provided date is invalid or missing.
     * @throws InvalidNumberFormatException If the index is in an invalid format.
     * @throws MissingAmountException If the amount is missing in the update input.
     * @throws MissingCategoryException If the category is missing in the update input.
     */
    @Override
    public void execute(List<Income> incomes, ExpenseList expenseList) throws MissingDateException,
            InvalidNumberFormatException, MissingAmountException, MissingCategoryException{
        String[] parts = input.substring(UPDATE_COMMAND_PREFIX_LENGTH).trim().split(" ", 2);
        if (parts.length < MINIMUM_PARTS_FOR_UPDATE) {
            throw new InvalidNumberFormatException(ERROR_MISSING_INDEX);
        }

        int index;
        try {
            index = Integer.parseInt(parts[INDEX_POSITION_IN_1_INDEX]) - 1;
        } catch (NumberFormatException e) {
            throw new InvalidNumberFormatException(ERROR_WRONG_INDEX_FORMAT);
        }

        if (index < MINIMUM_INDEX || index >= incomes.size()) {
            throw new InvalidNumberFormatException(ERROR_INCOME_ENTRY_NOT_FOUND);
        }

        Income existingIncome = incomes.get(index);
        Income updatedIncome = extractUpdatedIncome(parts[1], existingIncome);
        incomes.set(index, updatedIncome);

        updateStorage(incomes, expenseList);

        this.outputMessage = "Income updated: " + updatedIncome.getCategory() + ", Amount: $" +
                String.format("%.2f", updatedIncome.getAmount()) + ", Date: " + updatedIncome.getDate();
        logger.info("Income updated successfully: " + updatedIncome);
    }

    /**
     * Saves the updated incomes and expense list to storage.
     *
     * @param incomes The updated list of incomes.
     * @param expenseList The expense list to be saved.
     */
    private static void updateStorage(List<Income> incomes, ExpenseList expenseList) {
        Storage storage = new Storage();
        storage.saveData(incomes, expenseList);
    }

    /**
     * Extracts the updated income details from the input and creates a new Income object.
     *
     * @param input The input string containing the updated income details.
     * @param existingIncome The existing income entry to use for any missing information.
     * @return The updated Income object.
     * @throws MissingAmountException If the amount is missing in the input.
     * @throws MissingDateException If the date is invalid or missing in the input.
     * @throws MissingCategoryException If the category is missing or invalid in the input.
     */
    private Income extractUpdatedIncome(String input, Income existingIncome) throws MissingAmountException,
             MissingDateException, MissingCategoryException {
        String category = existingIncome.getCategory();
        Double amount = existingIncome.getAmount();
        String date = existingIncome.getDate();


        Pattern categoryPattern = Pattern.compile(CATEGORY_PATTERN);
        Pattern amtPattern = Pattern.compile(AMT_PATTERN);
        Pattern datePattern = Pattern.compile(DATE_PATTERN);


        category = getUpdatedCategory(input, categoryPattern, category);

        amount = getUpdatedAmount(input, amtPattern, amount);

        date = getUpdatedDate(input, datePattern, date);

        return new Income(category, amount, date);
    }

    /**
     * Extracts and validates the updated date from the input string.
     *
     * @param input The input string containing the updated date.
     * @param datePattern The compiled regular expression pattern for the date.
     * @param date The current date to fall back on if not updated.
     * @return The updated date as a String.
     * @throws MissingDateException If the date is invalid.
     */
    private static String getUpdatedDate(String input, Pattern datePattern, String date) throws MissingDateException {
        Matcher matcher;
        matcher = datePattern.matcher(input);
        if (matcher.find()) {
            date = matcher.group(UPDATE_PARAMETER_GROUP).trim();
            if (!DateValidator.isValidDate(date)) {
                throw new MissingDateException(ERROR_INVALID_DATE);
            }
        }
        return date;
    }

    /**
     * Extracts and validates the updated amount from the input string.
     *
     * @param input The input string containing the updated amount.
     * @param amtPattern The compiled regular expression pattern for the amount.
     * @param amount The current amount to fall back on if not updated.
     * @return The updated amount as a Double.
     * @throws MissingAmountException If the amount is invalid.
     */
    private static Double getUpdatedAmount(String input, Pattern amtPattern, Double amount) throws MissingAmountException {
        Matcher matcher;
        matcher = amtPattern.matcher(input);
        if (matcher.find()) {
            try {
                amount = Double.parseDouble(matcher.group(UPDATE_PARAMETER_GROUP));
            } catch (NumberFormatException e) {
                throw new MissingAmountException(ERROR_INVALID_AMOUNT);
            }
        }
        return amount;
    }

    /**
     * Extracts and validates the updated category from the input string.
     *
     * @param input The input string containing the updated category.
     * @param categoryPattern The compiled regular expression pattern for the category.
     * @param category The current category to fall back on if not updated.
     * @return The updated category as a String.
     * @throws MissingCategoryException If the category is missing or invalid.
     */
    private static String getUpdatedCategory(String input, Pattern categoryPattern, String category) throws MissingCategoryException {
        Matcher matcher;
        matcher = categoryPattern.matcher(input);
        if (matcher.find()) {
            String updatedCategory = matcher.group(UPDATE_PARAMETER_GROUP).trim();
            if (updatedCategory.isEmpty()) {
                throw new MissingCategoryException("Error: Category cannot be empty. Please provide a valid category.");
            }
            category = updatedCategory;
        }
        return category;
    }
}

