package budgetflow.command;

import budgetflow.exception.InvalidNumberFormatException;
import budgetflow.exception.MissingAmountException;
import budgetflow.exception.MissingCategoryException;
import budgetflow.exception.MissingDateException;
import budgetflow.income.Income;
import java.util.List;

public class AddIncomeCommand extends Command {
    private static final String ADD_COMMAND_PREFIX = "add ";
    private static final int ADD_COMMAND_PREFIX_LENGTH = ADD_COMMAND_PREFIX.length();

    private static final String ERROR_MISSING_INCOME_CATEGORY = "Error: Income category is required.";
    private static final String ERROR_MISSING_INCOME_AMOUNT = "Error: Income amount is required.";
    private static final String ERROR_MISSING_INCOME_DATE = "Error: Income date is required.";

    public AddIncomeCommand(String input) {
        super(input);
    }

    /**
     * Add the user income and save it to the income lists
     *
     * @param input user string input
     * @param incomes list of incomes
     * @throws MissingDateException if user did not provide the date for income, or when use miss the date tag
     * @throws InvalidNumberFormatException if user enter income value in incorrect number format
     * @throws MissingAmountException if user did not provide any amount for income or use miss the amount tag
     * @throws MissingCategoryException if user did not provide category or miss tag for category
     */
    public void execute(String input, List<Income> incomes) throws MissingDateException, InvalidNumberFormatException, MissingAmountException, MissingCategoryException {
        Income income = extractIncome(input);
        incomes.add(income);
        // This is for UI and Storage, which will be dealt later
//        System.out.println("Income added: " + category + ", Amount: $" +
//                String.format("%.2f", amount) + ", Date: " + date);
//        storage.saveData(incomes, expenseList);
    }


    private Income extractIncome(String input) throws InvalidNumberFormatException, MissingCategoryException, MissingAmountException, MissingDateException {
        input = input.substring(ADD_COMMAND_PREFIX_LENGTH).trim();

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
                throw new InvalidNumberFormatException();
            }
        }
        pattern = java.util.regex.Pattern.compile(datePattern);
        matcher = pattern.matcher(input);
        if (matcher.find()) {
            date = matcher.group(1).trim();
        }

        if (category == null || category.isEmpty()) {
            throw new MissingCategoryException(ERROR_MISSING_INCOME_CATEGORY);
        }
        if (amount == null) {
            throw new MissingAmountException(ERROR_MISSING_INCOME_AMOUNT);
        }
        if (date == null) {
            throw new MissingDateException(ERROR_MISSING_INCOME_DATE);
        }
        return new Income(category, amount, date);
    }
}
