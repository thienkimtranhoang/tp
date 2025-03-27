package budgetflow.command;

import budgetflow.exception.FinanceException;
import budgetflow.expense.ExpenseList;
import budgetflow.income.Income;
import org.junit.jupiter.api.Test;

import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//@@author IgoyAI
class FilterIncomeByDateCommandTest {

    @Test
    void filterIncomeByDate_validRange_returnsMatchingIncomes() throws FinanceException {
        List<Income> incomes = new ArrayList<>();
        incomes.add(new Income("Salary", 2500.00, "15-03-2025"));
        incomes.add(new Income("Bonus", 500.00, "20-03-2025"));
        incomes.add(new Income("Gift", 100.00, "01-01-2020"));
        ExpenseList expenseList = new ExpenseList();

        // Filter incomes with dates between 10-03-2025 and 25-03-2025 should return Salary and Bonus
        Command command = new FilterIncomeByDateCommand("filter-income date from/10-03-2025 to/25-03-2025");
        command.execute(incomes, expenseList);
        String expectedOutput = "Filtered Incomes by Date (10-03-2025 to 25-03-2025):\n" +
                "Salary | $2500.00 | 15-03-2025\n" +
                "Bonus | $500.00 | 20-03-2025\n";
        assertEquals(expectedOutput, command.getOutputMessage());
    }

    @Test
    void filterIncomeByDate_noMatchingIncomes_returnsNoIncomesFoundMessage() throws FinanceException {
        List<Income> incomes = new ArrayList<>();
        incomes.add(new Income("Salary", 2500.00, "15-03-2025"));
        ExpenseList expenseList = new ExpenseList();

        // Date range does not include the income date
        Command command = new FilterIncomeByDateCommand("filter-income date from/01-01-2020 to/31-12-2020");
        command.execute(incomes, expenseList);
        String expectedOutput = "Filtered Incomes by Date (01-01-2020 to 31-12-2020):\n" +
                "No incomes found in the specified date range.";
        assertEquals(expectedOutput, command.getOutputMessage());
    }

    @Test
    void filterIncomeByDate_invalidFormat_missingTo_throwsException() {
        List<Income> incomes = new ArrayList<>();
        ExpenseList expenseList = new ExpenseList();
        Command command = new FilterIncomeByDateCommand("filter-income date from/15-03-2025");
        try {
            command.execute(incomes, expenseList);
            fail();
        } catch (FinanceException e) {
            String expectedError = "Invalid date filter format. Usage: filter-income date from/DD-MM-YYYY to/DD-MM-YYYY";
            assertEquals(expectedError, e.getMessage());
        }
    }

    @Test
    void filterIncomeByDate_invalidRange_startAfterEnd_throwsException() {
        List<Income> incomes = new ArrayList<>();
        ExpenseList expenseList = new ExpenseList();
        Command command = new FilterIncomeByDateCommand("filter-income date from/25-03-2025 to/15-03-2025");
        try {
            command.execute(incomes, expenseList);
            fail();
        } catch (FinanceException e) {
            String expectedError = "Start date must be before or equal to end date.";
            assertEquals(expectedError, e.getMessage());
        }
    }

    @Test
    void filterIncomeByDate_invalidDateFormat_throwsException() {
        List<Income> incomes = new ArrayList<>();
        ExpenseList expenseList = new ExpenseList();
        Command command = new FilterIncomeByDateCommand("filter-income date from/15-03-2025 to/2025-03-15");
        try {
            command.execute(incomes, expenseList);
            fail();
        } catch (FinanceException e) {
            String expectedError = "One or both dates are invalid. Please use DD-MM-YYYY format.";
            assertEquals(expectedError, e.getMessage());
        }
    }
}
