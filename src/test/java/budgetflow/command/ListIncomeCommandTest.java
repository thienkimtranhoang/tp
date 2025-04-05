package budgetflow.command;

import budgetflow.expense.ExpenseList;
import budgetflow.income.Income;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit tests for the ListIncomeCommand class.
 */
public class ListIncomeCommandTest {

    // Dummy implementation of Income for testing purposes.
    private static class DummyIncome extends Income {
        private final String category;
        private final double amount;
        private final String date;

        public DummyIncome(String category, double amount, String date) {
            super(category, amount, date); // Call the superclass constructor.
            this.category = category;
            this.amount = amount;
            this.date = date;
        }

        @Override
        public String getCategory() {
            return category;
        }

        @Override
        public double getAmount() {
            return amount;
        }

        @Override
        public String getDate() {
            return date;
        }
    }

    // Dummy implementation of ExpenseList for testing purposes.
    private static class DummyExpenseList extends ExpenseList {
        private final double totalExpenses;

        public DummyExpenseList(double totalExpenses) {
            this.totalExpenses = totalExpenses;
        }

        @Override
        public double getTotalExpenses() {
            return totalExpenses;
        }
    }

    // Reset the static savingGoal before each test to avoid cross-test interference.
    @BeforeEach
    public void setUp() {
        ListIncomeCommand.setSavingGoal(0.0);
    }

    /**
     * Test that execute returns the expected message when incomes is null.
     */
    @Test
    public void testExecuteWithNullIncomes() {
        ListIncomeCommand command = new ListIncomeCommand();
        command.execute(null, null);
        assertEquals("No incomes have been added yet.", command.outputMessage,
                "Expected output message for null income list.");
    }

    /**
     * Test that execute returns the expected message when incomes list is empty.
     */
    @Test
    public void testExecuteWithEmptyIncomes() {
        ListIncomeCommand command = new ListIncomeCommand();
        command.execute(new ArrayList<>(), null);
        assertEquals("No incomes have been added yet.", command.outputMessage,
                "Expected output message for empty income list.");
    }

    /**
     * Test execute with a non-empty incomes list and no saving goal set.
     * Expected to only display the income log and total income.
     */
    @Test
    public void testExecuteWithIncomesNoSavingGoal() {
        ListIncomeCommand command = new ListIncomeCommand();

        List<Income> incomes = new ArrayList<>();
        incomes.add(new DummyIncome("Salary", 1000.00, "2025-04-01"));
        incomes.add(new DummyIncome("Bonus", 500.00, "2025-04-02"));

        // No saving goal set (remains 0.0)
        command.execute(incomes, null);
        String output = command.outputMessage;

        assertTrue(output.contains("INCOME LOG"), "Output should contain the header 'INCOME LOG'.");
        assertTrue(output.contains("Salary"), "Output should contain income category 'Salary'.");
        assertTrue(output.contains("Bonus"), "Output should contain income category 'Bonus'.");
        assertTrue(output.contains("Total Income: $1500.00"), "Output should display the correct total income.");
        assertFalse(output.contains("Summary:"), "Summary table should not be displayed when saving goal is 0.");
    }

    /**
     * Test execute with a non-empty incomes list and a saving goal set.
     * The summary table should be included with correct progress calculation.
     */
    @Test
    public void testExecuteWithIncomesAndSavingGoal() {
        ListIncomeCommand command = new ListIncomeCommand();

        // Set a saving goal of $500.00.
        ListIncomeCommand.setSavingGoal(500.00);

        List<Income> incomes = new ArrayList<>();
        incomes.add(new DummyIncome("Salary", 200.00, "2025-04-01"));
        incomes.add(new DummyIncome("Freelance", 300.00, "2025-04-03")); // Total income = $500.00

        // Create a dummy expense list with total expenses of $100.00.
        ExpenseList dummyExpenses = new DummyExpenseList(100.00);

        command.execute(incomes, dummyExpenses);
        String output = command.outputMessage;

        // Verify that the income log header, table, and summary table are present.
        assertTrue(output.contains("INCOME LOG"), "Output should contain the header 'INCOME LOG'.");
        assertTrue(output.contains("Salary"), "Output should contain income category 'Salary'.");
        assertTrue(output.contains("Freelance"), "Output should contain income category 'Freelance'.");
        assertTrue(output.contains("Total Income"), "Output should display 'Total Income' in the summary table.");
        assertTrue(output.contains("Saving Goal"), "Output should display 'Saving Goal' in the summary table.");
        assertTrue(output.contains("Current Savings"), "Output should display 'Current Savings' in the summary table.");
        assertTrue(output.contains("Progress"), "Output should display 'Progress' in the summary table.");

        // Total income = 500, expenses = 100, so savings = 400.
        // Progress = (400 / 500) * 100 = 80.00%
        assertTrue(output.contains("80.00%"), "Output should display progress of 80.00%.");
    }

    /**
     * Test that setting a negative saving goal throws an IllegalArgumentException.
     */
    @Test
    public void testSetSavingGoalNegative() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                        ListIncomeCommand.setSavingGoal(-50.00),
                "Setting a negative saving goal should throw IllegalArgumentException.");
        assertEquals("Saving goal amount cannot be negative", exception.getMessage(),
                "Exception message should match expected text.");
    }

    /**
     * Test that setSavingGoal and getSavingGoal work as expected.
     */
    @Test
    public void testSetAndGetSavingGoal() {
        ListIncomeCommand.setSavingGoal(1000.00);
        assertEquals(1000.00, ListIncomeCommand.getSavingGoal(), 0.001,
                "getSavingGoal should return the value set by setSavingGoal.");
    }

    /**
     * Test that the progress calculation is capped at 100%.
     * For incomes exceeding the saving goal, progress should not exceed 100%.
     */
    @Test
    public void testSummaryTableProgressCap() {
        ListIncomeCommand command = new ListIncomeCommand();

        // Set a saving goal where the progress would exceed 100%.
        ListIncomeCommand.setSavingGoal(500.00);

        List<Income> incomes = new ArrayList<>();
        incomes.add(new DummyIncome("Salary", 800.00, "2025-04-01"));  // Total income = $800.00

        // Create a dummy expense list with total expenses of $100.00.
        ExpenseList dummyExpenses = new DummyExpenseList(100.00); // Savings = 700.00

        command.execute(incomes, dummyExpenses);
        String output = command.outputMessage;

        // Expected progress: (700/500)*100 = 140, but capped to 100.00%
        assertTrue(output.contains("100.00%"), "Progress should be capped at 100.00%.");
    }

    /**
     * Test the scenario where expenses exceed income resulting in negative savings.
     */
    @Test
    public void testNegativeSavingProgress() {
        ListIncomeCommand command = new ListIncomeCommand();

        // Set a saving goal of $500.00.
        ListIncomeCommand.setSavingGoal(500.00);

        List<Income> incomes = new ArrayList<>();
        incomes.add(new DummyIncome("Salary", 200.00, "2025-04-01"));  // Total income = $200.00

        // Create a dummy expense list with total expenses of $300.00.
        ExpenseList dummyExpenses = new DummyExpenseList(300.00); // Savings = -100.00

        command.execute(incomes, dummyExpenses);
        String output = command.outputMessage;

        // Expected progress: (-100/500)*100 = -20.00%
        assertTrue(output.contains("-20.00%"), "Output should display negative progress when expenses exceed income.");
    }
}
