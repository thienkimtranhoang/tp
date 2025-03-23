package budgetflow.command;

import budgetflow.exception.FinanceException;
import budgetflow.exception.MissingExpenseException;
import budgetflow.expense.Expense;
import budgetflow.expense.ExpenseList;
import budgetflow.income.Income;
import budgetflow.parser.DateValidator;

import java.time.LocalDate;
import java.util.List;
import java.util.logging.Logger;

/**
 * Represents a command to compare expenses between two months.
 * It calculates and displays the total expenses for each month.
 */
public class CompareExpenseCommand extends Command {
    public static final String ERROR_INVALID_MONTH_FORMAT = "Invalid input format. Usage: compare MM-YYYY MM-YYYY";

    public static final int EMPTY_EXPENSE_LIST = 0;
    public static final int KEYWORD_POSITION = 0;
    public static final int MONTH_FIRST_DAY = 0;

    public static final int FIRST_MONTH_POSITION = 1;
    public static final int MONTH_LAST_DAY = 1;

    public static final int SECOND_MONTH_POSITION = 2;

    public static final int MINIMUM_PARTS_FOR_COMPARE = 3;
    private static final String ERROR_NO_EXPENSE_IN_RANGE = "No expenses in range";
    private static final Logger logger = Logger.getLogger(CompareExpenseCommand.class.getName());


    /**
     * Constructs a CompareExpenseCommand with the given input.
     *
     * @param input The command input string.
     */
    public CompareExpenseCommand(String input) {
        super(input);
        this.commandType = CommandType.COMPARE;
    }

    /**
     * Executes the compare command by calculating the total expenses for two specified months
     * and logging the results.
     *
     * @param incomes      List of user incomes (not used in this command).
     * @param expenseList  The list of recorded expenses.
     * @throws FinanceException If the input format is invalid or there are no expenses in the given range.
     */
    @Override
    public void execute(List<Income> incomes, ExpenseList expenseList) throws FinanceException {
        assert input.startsWith("compare");

        if (expenseList.getSize() == EMPTY_EXPENSE_LIST) {
            logger.info("No expenses recorded.");
            this.outputMessage = "No expenses recorded." + System.lineSeparator();
            throw new MissingExpenseException(ERROR_NO_EXPENSE_IN_RANGE);
        }

        String[] parts = parseAndValidateInput(input);
        LocalDate[] range1 = getValidatedDateRange(parts[FIRST_MONTH_POSITION]);
        LocalDate[] range2 = getValidatedDateRange(parts[SECOND_MONTH_POSITION]);

        double totalMonth1 = calculateTotalExpenses(expenseList, range1[MONTH_FIRST_DAY], range1[MONTH_LAST_DAY]);
        double totalMonth2 = calculateTotalExpenses(expenseList, range2[MONTH_FIRST_DAY], range2[MONTH_LAST_DAY]);

        this.outputMessage = getCompareMessage(parts[FIRST_MONTH_POSITION], totalMonth1,
                parts[SECOND_MONTH_POSITION], totalMonth2);
        logger.info("Expense totals calculated: " + this.outputMessage);
    }

    /**
     * Parses and validates the input command for the compare operation.
     *
     * @param input The raw input string from the user.
     * @return A string array containing the parsed command components.
     * @throws FinanceException If the input format is invalid.
     */
    private String[] parseAndValidateInput(String input) throws FinanceException {
        String[] parts = input.split("\\s+");
        if (parts.length < MINIMUM_PARTS_FOR_COMPARE || !parts[KEYWORD_POSITION].equalsIgnoreCase("compare")) {
            throw new FinanceException(ERROR_INVALID_MONTH_FORMAT);
        }
        return parts;
    }

    /**
     * Retrieves and validates the date range for a given month-year string.
     *
     * @param monthYear The month and year in the format MM-YYYY.
     * @return An array containing the start and end dates for the specified month.
     * @throws FinanceException If the date format is invalid.
     */
    private LocalDate[] getValidatedDateRange(String monthYear) throws FinanceException {
        if (!DateValidator.isValidMonthYear(monthYear)) {
            throw new FinanceException(ERROR_INVALID_MONTH_FORMAT);
        }
        return DateValidator.getMonthRange(monthYear);
    }

    /**
     * Generates a message comparing the total expenses between two months.
     *
     * @param monthYear1 The first month in the format MM-YYYY.
     * @param totalMonth1 The total expenses for the first month.
     * @param monthYear2 The second month in the format MM-YYYY.
     * @param totalMonth2 The total expenses for the second month.
     * @return A formatted string displaying the expense comparison.
     */
    private static String getCompareMessage(String monthYear1, double totalMonth1,
                                            String monthYear2, double totalMonth2) {
        return "Total expenses for " + monthYear1 + ": $" +
                String.format("%.2f", totalMonth1) + System.lineSeparator() +
                "Total expenses for " + monthYear2 + ": $" +
                String.format("%.2f", totalMonth2) + System.lineSeparator();
    }

    /**
     * Calculates the total expenses within a given date range.
     *
     * @param expenseList The list of all expenses.
     * @param startDate   The start date of the range (inclusive).
     * @param endDate     The end date of the range (inclusive).
     * @return The total amount of expenses within the date range.
     */
    private double calculateTotalExpenses(ExpenseList expenseList, LocalDate startDate, LocalDate endDate) {
        double total = 0.0;
        for (int i = 0; i < expenseList.getSize(); i++) {
            Expense expense = expenseList.get(i);
            LocalDate expenseDate = LocalDate.parse(expense.getDate(), DateValidator.getFullDateFormatter());
            if (!expenseDate.isBefore(startDate) && !expenseDate.isAfter(endDate)) {
                total += expense.getAmount();
            }
        }
        return total;
    }
}
