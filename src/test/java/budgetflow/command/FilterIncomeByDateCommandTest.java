package budgetflow.command;

import static org.junit.jupiter.api.Assertions.assertEquals;

import budgetflow.expense.ExpenseList;
import budgetflow.income.Income;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class FilterIncomeByDateCommandTest {

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
        String expectedOutput = "Filtered Incomes by Date (10-03-2025 to 25-03-2025):\n"
                + "Salary | $2500.00 | 15-03-2025\n"
                + "Bonus | $500.00 | 20-03-2025\n";
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
        String expectedOutput = "Filtered Incomes by Date (01-01-2020 to 31-12-2020):\n"
                + "No incomes found in the specified date range.";
        assertEquals(expectedOutput, command.getOutputMessage());
    }

    @Test
    void date_invalidFormat_returnsUsageGuide() throws Exception {
        List<Income> incomes = new ArrayList<>();
        ExpenseList expenseList = new ExpenseList();
        // Missing the "to/..." part
        Command command = new FilterIncomeByDateCommand(
                "filter-income date from/15-03-2025");
        command.execute(incomes, expenseList);
        String expectedOutput = "Usage: filter-income date from/DD-MM-YYYY to/DD-MM-YYYY\n"
                + "Example: filter-income date from/01-01-2023 to/31-12-2023";
        assertEquals(expectedOutput, command.getOutputMessage());
    }

    @Test
    void date_rangeInvalid_returnsErrorMessage() throws Exception {
        List<Income> incomes = new ArrayList<>();
        ExpenseList expenseList = new ExpenseList();
        // Invalid range: start date is after end date.
        Command command = new FilterIncomeByDateCommand(
                "filter-income date from/25-03-2025 to/15-03-2025");
        command.execute(incomes, expenseList);
        String expectedOutput = "Start date must be before or equal to end date.";
        assertEquals(expectedOutput, command.getOutputMessage());
    }

    @Test
    void date_badFormat_returnsErrorMessage() throws Exception {
        List<Income> incomes = new ArrayList<>();
        ExpenseList expenseList = new ExpenseList();
        // "to" date is in bad format.
        Command command = new FilterIncomeByDateCommand(
                "filter-income date from/15-03-2025 to/2025-03-15");
        command.execute(incomes, expenseList);
        String expectedOutput = "One or both dates are invalid. Please use DD-MM-YYYY format.";
        assertEquals(expectedOutput, command.getOutputMessage());
    }
}
