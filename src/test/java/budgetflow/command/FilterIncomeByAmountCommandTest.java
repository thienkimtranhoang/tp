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
        Command command = new FilterIncomeByAmountCommand("filter-income amount from/500 to/3000");
        command.execute(incomes, expenseList);
        String expectedOutput = String.format("Filtered Incomes by Amount Range: %.2f to %.2f%n%n", 500.0, 3000.0)
                + String.format("%-20s | %-10s | %-15s%n", "Category", "Amount", "Date")
                + String.format("%-20s-+-%-10s-+-%-15s%n", "-".repeat(20), "-".repeat(10), "-".repeat(15))
                + String.format("%-20s | $%-9.2f | %-15s%n", "Salary", 2500.00, "15-03-2025")
                + String.format("%-20s | $%-9.2f | %-15s%n", "Bonus", 500.00, "20-03-2025");
        assertEquals(expectedOutput, command.getOutputMessage());
    }

    @Test
    void amount_noMatch_returnsNone() throws Exception {
        List<Income> incomes = new ArrayList<>();
        incomes.add(new Income("Salary", 2500.00, "15-03-2025"));
        ExpenseList expenseList = new ExpenseList();

        // Filter range that does not match any income amounts
        Command command = new FilterIncomeByAmountCommand("filter-income amount from/3000 to/5000");
        command.execute(incomes, expenseList);
        String expectedOutput = String.format("Filtered Incomes by Amount Range: %.2f to %.2f%n%n", 3000.0, 5000.0)
                + String.format("%-20s | %-10s | %-15s%n", "Category", "Amount", "Date")
                + String.format("%-20s-+-%-10s-+-%-15s%n", "-".repeat(20), "-".repeat(10), "-".repeat(15))
                + "No incomes found in the specified amount range.";
        assertEquals(expectedOutput, command.getOutputMessage());
    }

    @Test
    void amount_invalidFormat_throwsException() {
        List<Income> incomes = new ArrayList<>();
        ExpenseList expenseList = new ExpenseList();
        // Missing the "to/<maxAmount>" part
        Command command = new FilterIncomeByAmountCommand("filter-income amount from/2500");
        InvalidKeywordException exception = assertThrows(InvalidKeywordException.class,
                () -> command.execute(incomes, expenseList));
        String expectedMessage = "Invalid command. Correct format: Usage: filter-income amount from/<minAmount> to/<maxAmount>\n"
                + "Example: filter-income amount from/1000 to/5000";
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void amount_rangeInvalid_throwsException() {
        List<Income> incomes = new ArrayList<>();
        ExpenseList expenseList = new ExpenseList();
        // Invalid range: minAmount > maxAmount
        Command command = new FilterIncomeByAmountCommand("filter-income amount from/3000 to/2500");
        InvalidKeywordException exception = assertThrows(InvalidKeywordException.class,
                () -> command.execute(incomes, expenseList));
        String expectedMessage = "Invalid command. Minimum amount should not be greater than maximum amount.\n"
                + "Correct format: Usage: filter-income amount from/<minAmount> to/<maxAmount>\n"
                + "Example: filter-income amount from/1000 to/5000";
        assertEquals(expectedMessage, exception.getMessage());
    }
}
