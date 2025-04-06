package budgetflow.command;

import budgetflow.exception.InvalidNumberFormatException;
import budgetflow.exception.MissingDateException;
import budgetflow.exception.MissingAmountException;
import budgetflow.exception.MissingCategoryException;
import budgetflow.exception.MissingIncomeException;
import budgetflow.exception.ExceedsMaxDigitException;
import budgetflow.expense.ExpenseList;
import budgetflow.income.Income;
import java.util.List;
import java.util.logging.Logger;
import budgetflow.parser.DateValidator;

//@@author thienkimtranhoang
// Original implementation of AddIncomeCommand.java
//@@author IgoyAI
// Modified: Added error message with example usage in extractIncome() when wrong command is entered.
public class AddIncomeCommand extends Command {
    public static final String ERROR_INVALID_DATE = "Error: Date is not a valid date";

    private static final Logger logger = Logger.getLogger(
            AddIncomeCommand.class.getName());
    private static final String ADD_COMMAND_PREFIX = "add ";
    private static final int ADD_COMMAND_PREFIX_LENGTH = ADD_COMMAND_PREFIX.length();

    private static final String ERROR_MISSING_INCOME_CATEGORY =
            "Error: Income category is required.";
    private static final String ERROR_MISSING_INCOME_AMOUNT =
            "Error: Income amount is required.";
    private static final String ERROR_MISSING_INCOME_DATE =
            "Error: Income date is required.";
    private static final String ERROR_INCORRECT_INCOME_DATE =
            "Error: Income date is in wrong format." +
                    "please use DD-MM-YYYY format.";

    // Example usage constant
    private static final String EXAMPLE_USAGE =
            "Ex: add category/salary amt/1000.00 d/01-01-2020";

    public AddIncomeCommand(String input) {
        super(input);
        this.commandType = CommandType.CREATE;
    }

    /**
     * Adds the user income and saves it to the income list.
     *
     * @param incomes list of incomes
     * @throws MissingDateException if the date is missing or in wrong format
     * @throws InvalidNumberFormatException if the income value is in an invalid format
     * @throws MissingAmountException if the income amount is missing
     * @throws MissingCategoryException if the income category is missing
     */
    @Override
    public void execute(List<Income> incomes, ExpenseList expenseList)
            throws MissingDateException, InvalidNumberFormatException,
            MissingAmountException, MissingCategoryException,
            MissingIncomeException, ExceedsMaxDigitException {
        Income income = extractIncome(input);
        incomes.add(income);
        this.outputMessage = "Income added: " + income.getCategory() + ", Amount: $" +
                String.format("%.2f", income.getAmount()) + ", Date: " + income.getDate();
        logger.info("Income added successfully: " + income);
    }

    private Income extractIncome(String input)
            throws InvalidNumberFormatException, MissingCategoryException,
            MissingAmountException, MissingDateException,
            MissingIncomeException, ExceedsMaxDigitException {
        // Modified by IgoyAI: Check for correct command prefix and add example usage in error.
        if (!input.startsWith(ADD_COMMAND_PREFIX)) {
            throw new MissingIncomeException("Invalid add command. " + EXAMPLE_USAGE);
        }
        input = input.substring(ADD_COMMAND_PREFIX_LENGTH).trim();
        if (input.isEmpty()) {
            throw new MissingIncomeException("Income cannot be empty. " + EXAMPLE_USAGE);
        }

        String category = null;
        Double amount = null;
        String date = null;

        String categoryPattern = "category/(.*?) (amt/|d/|$)";
        String amtPattern = "amt/\\s*([1-9][0-9]*(\\.[0-9]*[1-9])?|0\\.[0-9]*[1-9])";
        String datePattern = "d/\\s*(\\d{2}-\\d{2}-\\d{4})";

        java.util.regex.Pattern pattern =
                java.util.regex.Pattern.compile(categoryPattern);
        java.util.regex.Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            category = matcher.group(1).trim();
        }
        pattern = java.util.regex.Pattern.compile(amtPattern);
        matcher = pattern.matcher(input);
        if (matcher.find()) {
            try {
                amount = Double.parseDouble(matcher.group(1));
                String[] parts = matcher.group(1).split("\\.");
                String integerPart = parts[0];
                if (integerPart.length() > 7) {
                    logger.warning("Amount exceeds 7 digit limit: " + integerPart);
                    throw new ExceedsMaxDigitException("Amount exceeds 7 digits. "
                            + "Please enter a number with up to 7 digits.");
                }
                if (parts.length > 1) {
                    String decimalPart = parts[1];
                    if (decimalPart.length() > 2) {
                        logger.warning("Amount has more than 2 decimal digits: " +
                                decimalPart);
                        throw new ExceedsMaxDigitException(
                                "Amount must have at most 2 decimal places.");
                    }
                }
            } catch (NumberFormatException e) {
                logger.warning("Invalid amount format: " + input);
                throw new InvalidNumberFormatException();
            }
        }
        pattern = java.util.regex.Pattern.compile(datePattern);
        matcher = pattern.matcher(input);
        if (matcher.find()) {
            date = matcher.group(1).trim();
            if (!DateValidator.isValidDate(date)) {
                logger.warning("Invalid date input: " + date);
                throw new MissingDateException(ERROR_INVALID_DATE);
            }
        } else {
            verifyMissingOrIncorrect(input);
        }

        if (category == null || category.isEmpty()) {
            logger.warning("Invalid income input: " + input);
            throw new MissingCategoryException(ERROR_MISSING_INCOME_CATEGORY);
        }
        if (amount == null) {
            logger.warning("Invalid income input: " + input);
            throw new MissingAmountException(ERROR_MISSING_INCOME_AMOUNT);
        }
        return new Income(category, amount, date);
    }

    private static void verifyMissingOrIncorrect(String input)
            throws MissingDateException {
        String invalidDatePattern = "d/(\\S+)";
        java.util.regex.Pattern pattern =
                java.util.regex.Pattern.compile(invalidDatePattern);
        java.util.regex.Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            String invalidDate = matcher.group(1).trim();
            logger.warning("Invalid date input: " + invalidDate);
            throw new MissingDateException(ERROR_INCORRECT_INCOME_DATE);
        } else {
            logger.warning("Missing date input");
            throw new MissingDateException(ERROR_MISSING_INCOME_DATE);
        }
    }
}
