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


    public CompareExpenseCommand(String input) {
        super(input);
        this.commandType = CommandType.COMPARE;
    }

    @Override
    public void execute(List<Income> incomes, ExpenseList expenseList) throws FinanceException {
        assert input.startsWith("compare");
        if (expenseList.getSize() == EMPTY_EXPENSE_LIST) {
            logger.info("No expenses recorded.");
            this.outputMessage = "No expenses recorded." + System.lineSeparator();
            throw new MissingExpenseException(ERROR_NO_EXPENSE_IN_RANGE);
        }

        String[] parts = input.split("\\s+");
        if (parts.length < MINIMUM_PARTS_FOR_COMPARE || !parts[KEYWORD_POSITION].equalsIgnoreCase("compare")) {
            throw new FinanceException(ERROR_INVALID_MONTH_FORMAT);
        }

        String monthYear1 = parts[FIRST_MONTH_POSITION];
        String monthYear2 = parts[SECOND_MONTH_POSITION];

        if (!DateValidator.isValidMonthYear(monthYear1) || !DateValidator.isValidMonthYear(monthYear2)) {
            throw new FinanceException(ERROR_INVALID_MONTH_FORMAT);
        }

        LocalDate[] range1 = DateValidator.getMonthRange(monthYear1);
        LocalDate[] range2 = DateValidator.getMonthRange(monthYear2);

        double totalMonth1 = calculateTotalExpenses(expenseList, range1[MONTH_FIRST_DAY], range1[MONTH_LAST_DAY]);
        double totalMonth2 = calculateTotalExpenses(expenseList, range2[MONTH_FIRST_DAY], range2[MONTH_LAST_DAY]);

        String message = "Total expenses for " + monthYear1 + ": $" +
                String.format("%.2f", totalMonth1) + System.lineSeparator() +
                "Total expenses for " + monthYear2 + ": $" +
                String.format("%.2f", totalMonth2) + System.lineSeparator();

        this.outputMessage = message;
        logger.info("Expense totals calculated: " + this.outputMessage);
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