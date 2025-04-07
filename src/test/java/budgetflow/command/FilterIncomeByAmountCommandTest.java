package budgetflow.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import budgetflow.expense.ExpenseList;
import budgetflow.income.Income;
import budgetflow.exception.InvalidKeywordException;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

class FilterIncomeByAmountCommandTest {

    @Test
    void amount_valid_returnsMatching() throws Exception {
        List<Income> incomes = new ArrayList<>();
        incomes.add(new Income("Salary", 2500.00, "15-03-2025"));
        incomes.add(new Income("Bonus", 500.00, "20-03-2025"));
        incomes.add(new Income("Gift", 100.00, "01-01-2020"));
        ExpenseList expenseList = new ExpenseList();

        // Filter incomes with amount between 500 and 3000 should return Salary and Bonus
        Command command = new FilterIncomeByAmountCommand(
                "filter-income amount from/500 to/3000");
        command.execute(incomes, expenseList);
        String expectedOutput = String.format(
                "Filtered Incomes by Amount Range: %.2f to %.2f%n%n", 500.0, 3000.0)
                + String.format("%-20s | %-10s | %-15s%n", "Category", "Amount", "Date")
                + String.format("%-20s-+-%-10s-+-%-15s%n",
                "-".repeat(20), "-".repeat(10), "-".repeat(15))
                + String.format("%-20s | $%-9.2f | %-15s%n",
                "Salary", 2500.00, "15-03-2025")
                + String.format("%-20s | $%-9.2f | %-15s%n",
                "Bonus", 500.00, "20-03-2025");
        assertEquals(expectedOutput, command.getOutputMessage());
    }

    @Test
    void amount_noMatch_returnsNone() throws Exception {
        List<Income> incomes = new ArrayList<>();
        incomes.add(new Income("Salary", 2500.00, "15-03-2025"));
        ExpenseList expenseList = new ExpenseList();

        // Filter range that does not match any income amounts
        Command command = new FilterIncomeByAmountCommand(
                "filter-income amount from/3000 to/5000");
        command.execute(incomes, expenseList);
        String expectedOutput = String.format(
                "Filtered Incomes by Amount Range: %.2f to %.2f%n%n", 3000.0, 5000.0)
                + String.format("%-20s | %-10s | %-15s%n", "Category", "Amount", "Date")
                + String.format("%-20s-+-%-10s-+-%-15s%n",
                "-".repeat(20), "-".repeat(10), "-".repeat(15))
                + "No incomes found in the specified amount range.";
        assertEquals(expectedOutput, command.getOutputMessage());
    }

    @Test
    void amount_invalidFormat_throwsException() {
        List<Income> incomes = new ArrayList<>();
        ExpenseList expenseList = new ExpenseList();
        // Missing the "to/<maxAmount>" part
        Command command = new FilterIncomeByAmountCommand(
                "filter-income amount from/2500");
        InvalidKeywordException exception = assertThrows(InvalidKeywordException.class,
                () -> command.execute(incomes, expenseList));
        String expectedMessage = "Invalid command. Correct format: Usage: filter-income amount " +
                "from/<minAmount> to/<maxAmount>\nExample: filter-income amount from/1000 to/5000";
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void amount_rangeInvalid_throwsException() {
        List<Income> incomes = new ArrayList<>();
        ExpenseList expenseList = new ExpenseList();
        // Invalid range: minAmount > maxAmount
        Command command = new FilterIncomeByAmountCommand(
                "filter-income amount from/3000 to/2500");
        InvalidKeywordException exception = assertThrows(InvalidKeywordException.class,
                () -> command.execute(incomes, expenseList));
        String expectedMessage = "Invalid command. Minimum amount should not be greater than maximum " +
                "amount.\nCorrect format: Usage: filter-income amount from/<minAmount> to/<maxAmount>\n" +
                "Example: filter-income amount from/1000 to/5000";
        assertEquals(expectedMessage, exception.getMessage());
    }

    //@@author thienkimtranhoang
    @Test
    void amount_noIncomesBelowMinimum_returnsEmpty() throws Exception {
        // Testing where no incomes match the minimum amount
        List<Income> incomes = new ArrayList<>();
        incomes.add(new Income("Salary", 2000.00, "15-03-2025"));
        incomes.add(new Income("Bonus", 2500.00, "20-03-2025"));
        ExpenseList expenseList = new ExpenseList();

        Command command = new FilterIncomeByAmountCommand("filter-income amount from/3000 to/4000");
        command.execute(incomes, expenseList);
        String expectedOutput = String.format("Filtered Incomes by Amount Range: %.2f to %.2f%n%n", 3000.0, 4000.0)
                + String.format("%-20s | %-10s | %-15s%n", "Category", "Amount", "Date")
                + String.format("%-20s-+-%-10s-+-%-15s%n", "-".repeat(20), "-".repeat(10), "-".repeat(15))
                + "No incomes found in the specified amount range.";

        assertEquals(expectedOutput, command.getOutputMessage());
    }

    //@@author thienkimtranhoang
    @Test
    void amount_exactMatch_returnsMatching() throws Exception {
        // Testing where income amounts match the exact bounds of the range
        List<Income> incomes = new ArrayList<>();
        incomes.add(new Income("Salary", 3000.00, "15-03-2025"));
        incomes.add(new Income("Bonus", 1000.00, "20-03-2025"));
        ExpenseList expenseList = new ExpenseList();

        Command command = new FilterIncomeByAmountCommand("filter-income amount from/1000 to/3000");
        command.execute(incomes, expenseList);
        String expectedOutput = String.format("Filtered Incomes by Amount Range: %.2f to %.2f%n%n", 1000.0, 3000.0)
                + String.format("%-20s | %-10s | %-15s%n", "Category", "Amount", "Date")
                + String.format("%-20s-+-%-10s-+-%-15s%n", "-".repeat(20), "-".repeat(10), "-".repeat(15))
                + String.format("%-20s | $%-9.2f | %-15s%n", "Salary", 3000.00, "15-03-2025")
                + String.format("%-20s | $%-9.2f | %-15s%n", "Bonus", 1000.00, "20-03-2025");

        assertEquals(expectedOutput, command.getOutputMessage());
    }

    //@@author thienkimtranhoang
    @Test
    void amount_edgeCase_ZeroAmount() throws Exception {
        // Testing with zero as the amount, checking if it properly includes or excludes
        List<Income> incomes = new ArrayList<>();
        incomes.add(new Income("Freelance", 0.00, "01-01-2025"));
        incomes.add(new Income("Bonus", 500.00, "15-02-2025"));
        ExpenseList expenseList = new ExpenseList();

        Command command = new FilterIncomeByAmountCommand("filter-income amount from/0 to/500");
        command.execute(incomes, expenseList);
        String expectedOutput = String.format("Filtered Incomes by Amount Range: %.2f to %.2f%n%n", 0.0, 500.0)
                + String.format("%-20s | %-10s | %-15s%n", "Category", "Amount", "Date")
                + String.format("%-20s-+-%-10s-+-%-15s%n", "-".repeat(20), "-".repeat(10), "-".repeat(15))
                + String.format("%-20s | $%-9.2f | %-15s%n", "Freelance", 0.00, "01-01-2025")
                + String.format("%-20s | $%-9.2f | %-15s%n", "Bonus", 500.00, "15-02-2025");

        assertEquals(expectedOutput, command.getOutputMessage());
    }
}
