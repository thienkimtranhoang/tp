package budgetflow.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import budgetflow.expense.ExpenseList;
import budgetflow.income.Income;
import budgetflow.exception.InvalidKeywordException;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

class FilterIncomeByDateCommandTest {

    private static final String HEADER_FORMAT = "Filtered Incomes by Date (%s to %s):%n%n";
    private static final String TABLE_HEADER = "%-20s | %-10s | %-15s%n";
    private static final String SEPARATOR = "%-20s-+-%-10s-+-%-15s%n";
    private static final String NO_INCOMES_FOUND_MESSAGE =
            "No incomes found in the specified date range.";

    @Test
    void date_valid_returnsMatching() throws Exception {
        List<Income> incomes = new ArrayList<>();
        incomes.add(new Income("Salary", 2500.00, "15-03-2025"));
        incomes.add(new Income("Bonus", 500.00, "20-03-2025"));
        incomes.add(new Income("Gift", 100.00, "01-01-2020"));
        ExpenseList expenseList = new ExpenseList();

        // Filter incomes with dates between 10-03-2025 and 25-03-2025 should return Salary and Bonus
        Command command = new FilterIncomeByDateCommand(
                "filter-income date from/10-03-2025 to/25-03-2025");
        command.execute(incomes, expenseList);
        String expectedOutput = String.format(HEADER_FORMAT, "10-03-2025", "25-03-2025")
                + String.format(TABLE_HEADER, "Category", "Amount", "Date")
                + String.format(SEPARATOR, "-".repeat(20), "-".repeat(10), "-".repeat(15))
                + String.format("%-20s | $%-9.2f | %-15s%n", "Salary", 2500.00, "15-03-2025")
                + String.format("%-20s | $%-9.2f | %-15s%n", "Bonus", 500.00, "20-03-2025");
        assertEquals(expectedOutput, command.getOutputMessage());
    }

    @Test
    void date_noMatch_returnsNone() throws Exception {
        List<Income> incomes = new ArrayList<>();
        incomes.add(new Income("Salary", 2500.00, "15-03-2025"));
        ExpenseList expenseList = new ExpenseList();

        // Date range does not include the income date
        Command command = new FilterIncomeByDateCommand(
                "filter-income date from/01-01-2020 to/31-12-2020");
        command.execute(incomes, expenseList);
        String expectedOutput = String.format(HEADER_FORMAT, "01-01-2020", "31-12-2020")
                + String.format(TABLE_HEADER, "Category", "Amount", "Date")
                + String.format(SEPARATOR, "-".repeat(20), "-".repeat(10), "-".repeat(15))
                + NO_INCOMES_FOUND_MESSAGE;
        assertEquals(expectedOutput, command.getOutputMessage());
    }

    @Test
    void date_invalidFormat_throwsException() {
        List<Income> incomes = new ArrayList<>();
        ExpenseList expenseList = new ExpenseList();
        // Missing the "to/..." part makes the format invalid.
        Command command = new FilterIncomeByDateCommand(
                "filter-income date from/15-03-2025");
        InvalidKeywordException exception = assertThrows(InvalidKeywordException.class,
                () -> command.execute(incomes, expenseList));
        String expectedMessage = "Invalid command. Correct format: Usage: filter-income date " +
                "from/DD-MM-YYYY to/DD-MM-YYYY\nExample: filter-income date from/01-01-2023 " +
                "to/31-12-2023";
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void date_rangeInvalid_throwsException() {
        List<Income> incomes = new ArrayList<>();
        ExpenseList expenseList = new ExpenseList();
        // Invalid range: start date is after end date.
        Command command = new FilterIncomeByDateCommand(
                "filter-income date from/25-03-2025 to/15-03-2025");
        InvalidKeywordException exception = assertThrows(InvalidKeywordException.class,
                () -> command.execute(incomes, expenseList));
        String expectedMessage = "Invalid command. Start date must be before or equal to end date.\n" +
                "Correct format: Usage: filter-income date from/DD-MM-YYYY to/DD-MM-YYYY\n" +
                "Example: filter-income date from/01-01-2023 to/31-12-2023";
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void date_badFormat_throwsException() {
        List<Income> incomes = new ArrayList<>();
        ExpenseList expenseList = new ExpenseList();
        // "to" date is in bad format.
        Command command = new FilterIncomeByDateCommand(
                "filter-income date from/15-03-2025 to/2025-03-15");
        InvalidKeywordException exception = assertThrows(InvalidKeywordException.class,
                () -> command.execute(incomes, expenseList));
        String expectedMessage = "Invalid command. One or both dates are invalid. Please use DD-MM-YYYY " +
                "format.\nCorrect format: Usage: filter-income date from/DD-MM-YYYY to/DD-MM-YYYY\n" +
                "Example: filter-income date from/01-01-2023 to/31-12-2023";
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void date_emptyIncomeList_returnsNoResults() throws Exception {
        List<Income> incomes = new ArrayList<>();
        ExpenseList expenseList = new ExpenseList();
        Command command = new FilterIncomeByDateCommand(
                "filter-income date from/01-01-2025 to/31-12-2025");
        command.execute(incomes, expenseList);
        String expectedOutput = String.format(HEADER_FORMAT, "01-01-2025", "31-12-2025")
                + String.format(TABLE_HEADER, "Category", "Amount", "Date")
                + String.format(SEPARATOR, "-".repeat(20), "-".repeat(10), "-".repeat(15))
                + NO_INCOMES_FOUND_MESSAGE;
        assertEquals(expectedOutput, command.getOutputMessage());
    }

    @Test
    void date_extraWhitespace_returnsMatching() throws Exception {
        List<Income> incomes = new ArrayList<>();
        incomes.add(new Income("Salary", 2500.00, "15-03-2025"));
        ExpenseList expenseList = new ExpenseList();
        // Add extra spaces around parameters
        Command command = new FilterIncomeByDateCommand(
                "  filter-income date   from/10-03-2025    to/25-03-2025  ");
        command.execute(incomes, expenseList);
        String expectedOutput = String.format(HEADER_FORMAT, "10-03-2025", "25-03-2025")
                + String.format(TABLE_HEADER, "Category", "Amount", "Date")
                + String.format(SEPARATOR, "-".repeat(20), "-".repeat(10), "-".repeat(15))
                + String.format("%-20s | $%-9.2f | %-15s%n", "Salary", 2500.00, "15-03-2025");
        assertEquals(expectedOutput, command.getOutputMessage());
    }
}
