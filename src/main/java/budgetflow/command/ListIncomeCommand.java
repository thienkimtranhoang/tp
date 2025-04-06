package budgetflow.command;

import budgetflow.expense.ExpenseList;
import budgetflow.income.Income;
import java.util.List;
import java.util.logging.Logger;

/**
 * Represents a command to list all recorded incomes and display saving goal progress.
 * <p>
 * This command prints the incomes in a formatted table along with a summary table.
 * The summary table shows Total Income, Saving Goal, Current Savings, and Progress.
 * A large, centered header is displayed at the top and all tables share a matching style.
 *
 * @@author IgoyAI
 */
public class ListIncomeCommand extends Command {
    private static final Logger logger = Logger.getLogger(ListIncomeCommand.class.getName());
    private static final String EMPTY_INCOME_LIST_MESSAGE = "No incomes have been added yet.";

    // Static variable to store the saving goal across command instances.
    private static double savingGoal = 0.0;

    // Border style constants
    private static final String BORDER_CHAR = "=";
    // Define widths for income table and summary table
    private static final int INCOME_TABLE_WIDTH = 51;  // 20 + 3 + 10 + 3 + 15 = 51
    private static final int SUMMARY_TABLE_WIDTH = 71;   // 15 + 3 + 15 + 3 + 17 + 3 + 15 = 71
    // Use the maximum width for the overall header so it matches the widest table.
    private static final int OUTPUT_WIDTH = Math.max(INCOME_TABLE_WIDTH, SUMMARY_TABLE_WIDTH);

    /**
     * Constructs a ListIncomeCommand.
     * This command is used to display all recorded incomes and saving goal progress.
     */
    public ListIncomeCommand() {
        super();
        this.commandType = CommandType.READ;
    }

    /**
     * Sets the saving goal for all income listings.
     *
     * @param amount the amount to set as the saving goal
     * @throws IllegalArgumentException if the saving goal amount is negative.
     */
    public static void setSavingGoal(double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Saving goal amount cannot be negative");
        }
        savingGoal = amount;
    }

    /**
     * Retrieves the current saving goal.
     *
     * @return the current saving goal amount
     */
    public static double getSavingGoal() {
        return savingGoal;
    }

    /**
     * Calculates the progress towards the saving goal.
     *
     * @param totalIncome   the total income
     * @param totalExpenses the total expenses
     * @return the progress percentage towards the saving goal, capped at 100%
     */
    private double calculateSavingProgress(double totalIncome, double totalExpenses) {
        if (savingGoal == 0) {
            return 0.0;
        }
        double savings = totalIncome - totalExpenses;
        double progressPercentage = (savings / savingGoal) * 100;
        return Math.min(progressPercentage, 100.0);
    }

    /**
     * Builds a big, centered header with a matching style.
     *
     * @param title the header title.
     * @return a formatted header string.
     */
    private String buildBigHeader(String title) {
        String border = BORDER_CHAR.repeat(OUTPUT_WIDTH);
        int leftPadding = (OUTPUT_WIDTH - title.length()) / 2;
        String padding = " ".repeat(Math.max(0, leftPadding));
        return border + System.lineSeparator() +
                padding + title + System.lineSeparator() +
                border + System.lineSeparator();
    }

    /**
     * Builds the header for the income table.
     *
     * @return a formatted string representing the table header.
     */
    private String buildTableHeader() {
        String rowFormat = "%-20s | %-10s | %-15s%n";
        String header = String.format(rowFormat, "Category", "Amount", "Date");
        // Use the BORDER_CHAR for the divider.
        header += String.format("%-20s=+-%-10s=+-%-15s%n",
                BORDER_CHAR.repeat(20), BORDER_CHAR.repeat(10), BORDER_CHAR.repeat(15));
        return header;
    }

    /**
     * Builds a formatted table row for an income entry.
     *
     * @param income the income entry.
     * @return a formatted string representing the income row.
     */
    private String buildIncomeRow(Income income) {
        String rowFormat = "%-20s | $%-9.2f | %-15s%n";
        return String.format(rowFormat, income.getCategory(), income.getAmount(), income.getDate());
    }

    /**
     * Builds the summary table displaying Total Income, Saving Goal, Current Savings, and Progress.
     *
     * @param totalIncome   the total income.
     * @param totalExpenses the total expenses.
     * @return a formatted summary table as a String.
     */
    private String buildSummaryTable(double totalIncome, double totalExpenses) {
        double savings = totalIncome - totalExpenses;
        double progress = calculateSavingProgress(totalIncome, totalExpenses);
        String headerFormat = "%-15s | %-15s | %-17s | %-15s%n";
        String divider = String.format("%-15s=+-%-15s=+-%-17s=+-%-15s%n",
                BORDER_CHAR.repeat(15), BORDER_CHAR.repeat(15), BORDER_CHAR.repeat(17), BORDER_CHAR.repeat(15));
        String header = String.format(headerFormat, "Total Income", "Saving Goal", "Current Savings", "Progress");
        String row = String.format(headerFormat,
                "$" + String.format("%.2f", totalIncome),
                "$" + String.format("%.2f", savingGoal),
                "$" + String.format("%.2f", savings),
                String.format("%.2f%%", progress));
        return "Summary:" + System.lineSeparator() + header + divider + row;
    }

    /**
     * Executes the command to list all incomes and show saving goal progress.
     *
     * @param incomes     the list of incomes to be displayed.
     * @param expenseList the list of expenses used for calculating savings.
     */
    @Override
    public void execute(List<Income> incomes, ExpenseList expenseList) {
        logger.info("Executing ListIncomeCommand.");
        StringBuilder sb = new StringBuilder();

        // Check if the incomes list is null or empty.
        if (incomes == null || incomes.isEmpty()) {
            logger.info("Income list is empty.");
            this.outputMessage = EMPTY_INCOME_LIST_MESSAGE;
            return;
        }

        // Append a big header for the income log.
        sb.append(buildBigHeader("INCOME LOG"));

        // Build the income table.
        sb.append(buildTableHeader());

        double totalIncome = 0.0;
        // Append each income entry as a row in the table.
        for (Income income : incomes) {
            sb.append(buildIncomeRow(income));
            totalIncome += income.getAmount();
        }

        sb.append(System.lineSeparator());

        // If a saving goal is set, build and append the summary table.
        if (savingGoal > 0) {
            double totalExpenses = expenseList != null ? expenseList.getTotalExpenses() : 0.0;
            sb.append(buildSummaryTable(totalIncome, totalExpenses));
        } else {
            // Otherwise, just display the total income.
            sb.append(String.format("Total Income: $%.2f%n", totalIncome));
        }

        this.outputMessage = sb.toString();
        logger.info("ListIncomeCommand executed successfully.");
    }
}
