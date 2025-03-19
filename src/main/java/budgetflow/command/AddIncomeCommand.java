package budgetflow.command;

import budgetflow.exception.*;
import budgetflow.expense.ExpenseList;
import budgetflow.income.Income;
import java.util.List;
import java.util.logging.Logger;

public class AddIncomeCommand extends Command {
    private static final Logger logger = Logger.getLogger(AddIncomeCommand.class.getName());
    private static final String ADD_COMMAND_PREFIX = "add ";
    private static final int ADD_COMMAND_PREFIX_LENGTH = ADD_COMMAND_PREFIX.length();

    private static final String ERROR_MISSING_INCOME_CATEGORY = "Error: Income category is required.";
    private static final String ERROR_MISSING_INCOME_AMOUNT = "Error: Income amount is required.";
    private static final String ERROR_MISSING_INCOME_DATE = "Error: Income date is required.";


    public AddIncomeCommand(String input) {
        super(input);
        this.commandType = CommandType.CREATE;
    }

    /**
     * Add the user income and save it to the income lists
     *
     * @param incomes list of incomes
     * @throws MissingDateException if user did not provide the date for income, or when use miss the date tag
     * @throws InvalidNumberFormatException if user enter income value in incorrect number format
     * @throws MissingAmountException if user did not provide any amount for income or use miss the amount tag
     * @throws MissingCategoryException if user did not provide category or miss tag for category
     */
    @Override
    public void execute(List<Income> incomes, ExpenseList expenseList) throws MissingDateException, InvalidNumberFormatException, MissingAmountException, MissingCategoryException, MissingIncomeException {
        Income income = extractIncome(input);
        incomes.add(income);
        this.outputMessage = "Income added: " + income.getCategory() + ", Amount: $" +
                String.format("%.2f", income.getAmount()) + ", Date: " + income.getDate();
        logger.info("Income added successfully: " + income);
    }


    private Income extractIncome(String input) throws InvalidNumberFormatException, MissingCategoryException, MissingAmountException,
            MissingDateException, MissingIncomeException {
        assert input.startsWith(ADD_COMMAND_PREFIX) : "Invalid add income command format";
        input = input.substring(ADD_COMMAND_PREFIX_LENGTH).trim();
        if (input.isEmpty()) {
            throw new MissingIncomeException("Income should not be empty");
        }

        String category = null;
        Double amount = null;
        String date = null;

        String categoryPattern = "category/(.*?) (amt/|d/|$)";
        String amtPattern = "amt/([0-9]+(\\.[0-9]*)?)";
        String datePattern = "d/([^ ]+)";

        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(categoryPattern);
        java.util.regex.Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            category = matcher.group(1).trim();
        }
        pattern = java.util.regex.Pattern.compile(amtPattern);
        matcher = pattern.matcher(input);
        if (matcher.find()) {
            try {
                amount = Double.parseDouble(matcher.group(1));
            } catch (NumberFormatException e) {
                logger.warning("Invalid amount format: " + input);
                throw new InvalidNumberFormatException();
            }
        }
        pattern = java.util.regex.Pattern.compile(datePattern);
        matcher = pattern.matcher(input);
        if (matcher.find()) {
            logger.warning("Invalid income input: " + input);
            date = matcher.group(1).trim();
        }

        if (category == null || category.isEmpty()) {
            logger.warning("Invalid income input: " + input);
            throw new MissingCategoryException(ERROR_MISSING_INCOME_CATEGORY);
        }
        if (amount == null) {
            logger.warning("Invalid income input: " + input);
            throw new MissingAmountException(ERROR_MISSING_INCOME_AMOUNT);
        }
        if (date == null) {
            logger.warning("Invalid income input: " + input);
            throw new MissingDateException(ERROR_MISSING_INCOME_DATE);
        }
        return new Income(category, amount, date);
    }
}
