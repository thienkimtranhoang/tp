package budgetflow.command;

import budgetflow.exception.MissingDateException;
import budgetflow.exception.InvalidNumberFormatException;
import budgetflow.exception.MissingAmountException;
import budgetflow.exception.MissingCategoryException;
import budgetflow.exception.MissingDescriptionException;
import budgetflow.exception.MissingExpenseException;
import budgetflow.exception.ExceedsMaxDigitException;
import budgetflow.exception.ExceedsMaxTotalExpense;

import budgetflow.expense.Expense;
import budgetflow.expense.ExpenseList;
import budgetflow.income.Income;
import budgetflow.parser.DateValidator;

import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Modified by @IgoyAI to support tag extraction in any order and improve fault tolerance.
 */
public class LogExpenseCommand extends Command {
    public static final String ERROR_INVALID_DATE = "Error: Date is not a valid date";
    private static final Logger logger =
            Logger.getLogger(LogExpenseCommand.class.getName());

    private static final String LOG_EXPENSE_COMMAND_PREFIX = "log-expense ";
    private static final int LOG_EXPENSE_COMMAND_PREFIX_LENGTH =
            LOG_EXPENSE_COMMAND_PREFIX.length();

    private static final String ERROR_EMPTY_EXPENSE = "Expense should not be empty";
    private static final String ERROR_MISSING_EXPENSE_CATEGORY =
            "Error: Expense category is required.";
    private static final String ERROR_MISSING_EXPENSE_DESCRIPTION =
            "Error: Expense description is required.";
    private static final String ERROR_MISSING_EXPENSE_AMOUNT =
            "Error: Expense amount is required.";
    private static final String ERROR_MISSING_EXPENSE_DATE =
            "Error: Expense date is required.";
    private static final String ERROR_INCORRECT_EXPENSE_DATE =
            "Error: Income date is in wrong format. please use DD-MM-YYYY format.";
    private static final String ERROR_INCORRECT_YEAR_FORMAT =
            "Error: Year must be exactly 4 digits in the format YYYY.";

    private static final String USAGE_GUIDE =
            "Usage: log-expense category/<category> desc/<description> amt/<amount> d/<date>\n"
                    + "Example: log-expense category/Food desc/Lunch amt/12.50 d/15-03-2025";

    // New constants for symbol validation
    private static final String ERROR_INVALID_CATEGORY =
            "Error: Category must contain only alphabets or digits.";
    private static final String ERROR_INVALID_DESCRIPTION =
            "Error: Description must contain only alphabets or digits.";
    private static final String ERROR_INVALID_INTEGRER_AMOUNT =
            "Amount exceeds 7 digits. Please enter a number with up to 7 digits.";
    private static final String ERROR_INVALID_DECIMAL_AMOUNT =
            "Amount must have at most 2 decimal places.";

    public LogExpenseCommand(String input) {
        super(input);
        this.commandType = CommandType.CREATE;
    }

    //@@author thienkimtranhoang
    //@@author dariusyawningwhiz
    /**
     * Logs new user expense into the expense list.
     * If the user types only "log-expense" (even with extra whitespace),
     * the usage guide is returned.
     *
     * @param expenseList the list storing all expenses
     * @throws MissingDateException if user misses the date of expense or date tag
     * @throws InvalidNumberFormatException if amount does not follow valid number format
     * @throws MissingAmountException if user misses the amount of expense or amount tag
     * @throws MissingCategoryException if user misses the category of expense or expense tag
     * @throws MissingDescriptionException if user misses description of expense or expense tag
     * @throws MissingExpenseException if expense details are missing entirely
     * @throws ExceedsMaxDigitException if the expense amount exceeds digit limitations
     */
    //@@author thienkimtranhoang
    //@@author dariusyawningwhiz
    @Override
    public void execute(List<Income> incomes, ExpenseList expenseList)
            throws MissingDateException, InvalidNumberFormatException, MissingAmountException,
            MissingCategoryException, MissingDescriptionException, MissingExpenseException,
            ExceedsMaxDigitException, ExceedsMaxTotalExpense {
        if (input.trim().equals("log-expense")) {
            this.outputMessage = USAGE_GUIDE;
            return;
        }
        Expense expense = extractExpense(input);
        expenseList.add(expense);
        this.outputMessage = "Expense logged: " + expense.getCategory() + " | " +
                expense.getDescription() + " | $" +
                String.format("%.2f", expense.getAmount()) + " | " +
                expense.getDate();
    }

    //@@author thienkimtranhoang
    private static void verifyMissingOrIncorrect(String input)
            throws MissingDateException {
        String invalidDatePattern = "d/(\\S+)";
        Pattern pattern = Pattern.compile(invalidDatePattern);
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            String invalidDate = matcher.group(1).trim();
            logger.warning("Invalid date input: " + invalidDate);
            throw new MissingDateException(ERROR_INCORRECT_EXPENSE_DATE);
        } else {
            logger.warning("Missing date input");
            throw new MissingDateException(ERROR_MISSING_EXPENSE_DATE);
        }
    }
    //@@author thienkimtranhoang
    private String extractPattern(String input, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        return matcher.find() ? matcher.group(1).trim() : null;
    }

    /**
     * Extracts the expense details from the given input string.
     * Modified by @IgoyAI to use improved lookahead-based regex patterns,
     * supporting tags in any order.
     */
    //@@author thienkimtranhoang
    private Expense extractExpense(String input)
            throws InvalidNumberFormatException, MissingCategoryException,
            MissingAmountException, MissingDateException, MissingDescriptionException,
            MissingExpenseException, ExceedsMaxDigitException {
        assert input != null && !input.isEmpty() : "Expense input should not be empty";
        assert input.startsWith(LOG_EXPENSE_COMMAND_PREFIX) :
                "Invalid log expense format";

        input = input.substring(LOG_EXPENSE_COMMAND_PREFIX_LENGTH).trim();

        if (input.isEmpty()) {
            throw new MissingExpenseException(ERROR_EMPTY_EXPENSE);
        }

        String category = null;
        String description = null;
        Double amount = null;
        String date = null;

        // Improved regex patterns using lookahead to capture tag values regardless of order.
        String categoryPattern = "category/(.*?)(?=(\\s+(desc/|amt/|d/)|$))";
        String descPattern = "desc/(.*?)(?=(\\s+(amt/|d/|category/)|$))";
        String amtPattern = "amt/\\s*([1-9][0-9]*(\\.[0-9]*[1-9])?|0\\.[0-9]*[1-9])";
        String datePattern = "d/\\s*(\\d{2}-\\d{2}-\\d{4})";

        Pattern pattern;
        Matcher matcher;

        pattern = Pattern.compile(categoryPattern);
        matcher = pattern.matcher(input);
        if (matcher.find()) {
            category = matcher.group(1).trim();
        }

        pattern = Pattern.compile(descPattern);
        matcher = pattern.matcher(input);
        if (matcher.find()) {
            description = matcher.group(1).trim();
        }

        pattern = Pattern.compile(amtPattern);
        matcher = pattern.matcher(input);
        if (matcher.find()) {
            try {
                amount = Double.parseDouble(matcher.group(1));
                String[] parts = matcher.group(1).split("\\.");
                String integerPart = parts[0];

                if (integerPart.length() > 7) {
                    logger.warning("Amount exceeds 7 digit limit: " + integerPart);
                    throw new ExceedsMaxDigitException(ERROR_INVALID_INTEGRER_AMOUNT);
                }

                if (parts.length > 1) {
                    String decimalPart = parts[1];
                    if (decimalPart.length() > 2) {
                        logger.warning("Amount has more than 2 decimal digits: " + decimalPart);
                        throw new ExceedsMaxDigitException(ERROR_INVALID_DECIMAL_AMOUNT);
                    }
                }
            } catch (NumberFormatException e) {
                logger.warning("Invalid amount format: " + input);
                throw new InvalidNumberFormatException();
            }
        }

        pattern = Pattern.compile(datePattern);
        matcher = pattern.matcher(input);
        if (matcher.find()) {
            date = matcher.group(1).trim();
            if (!DateValidator.isValidDate(date)) {
                logger.warning("Invalid date input: " + date);
                throw new MissingDateException(ERROR_INVALID_DATE);
            }
            validateYearFormat(date);
        } else {
            verifyMissingOrIncorrect(input);
        }

        if (category == null || category.isEmpty()) {
            throw new MissingCategoryException(ERROR_MISSING_EXPENSE_CATEGORY);
        }

        // New: check for symbols in category
        if (!category.matches("^[a-zA-Z0-9]+$")) {
            throw new MissingCategoryException(ERROR_INVALID_CATEGORY);
        }

        if (description == null || description.isEmpty()) {
            throw new MissingDescriptionException(ERROR_MISSING_EXPENSE_DESCRIPTION);
        }

        // New: check for symbols in description
        if (!description.matches("^[a-zA-Z0-9]+$")) {
            throw new MissingDescriptionException(ERROR_INVALID_DESCRIPTION);
        }

        if (amount == null) {
            throw new MissingAmountException(ERROR_MISSING_EXPENSE_AMOUNT);
        }
        if (date == null) {
            throw new MissingDateException(ERROR_MISSING_EXPENSE_DATE);
        }

        return new Expense(category, description, amount, date);
    }

    //@@author thienkimtranhoang
    private void validateYearFormat(String date) throws MissingDateException {
        String[] dateParts = date.split("-");
        if (dateParts.length == 3) {
            String year = dateParts[2];
            if (year.length() != 4) {
                logger.warning("Invalid year format (not 4 digits): " + year);
                throw new MissingDateException(ERROR_INCORRECT_YEAR_FORMAT);
            }
        }
    }
}
