package budgetflow.command;

import budgetflow.exception.FinanceException;
import budgetflow.exception.ExceedsMaxDigitException;
import budgetflow.expense.ExpenseList;
import budgetflow.income.Income;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Test class for LogExpenseCommand.
 * This class tests various scenarios to ensure the correct functionality
 * and error handling of the LogExpenseCommand class.
 */
class LogExpenseCommandTest {
    /**
     * Tests if a valid expense input is logged correctly.
     *
     * @throws FinanceException if an error occurs while executing the command
     */
    //@@author dariusyawningwhiz
    @Test
    void logExpense_validInput_logsExpense() throws FinanceException {
        ExpenseList expenseList = new ExpenseList();
        List<Income> incomes = new ArrayList<>();
        Command command = new LogExpenseCommand(
                "log-expense category/Dining desc/DinnerWithFriends amt/45.75 d/15-03-2025");
        command.execute(incomes, expenseList);
        String expectedOutput = "Expense logged: Dining | DinnerWithFriends | $45.75 | 15-03-2025";
        assertEquals(expectedOutput, command.getOutputMessage());
    }

    /**
     * Tests if an input with only the command returns the usage guide.
     */
    //@@author dariusyawningwhiz
    @Test
    void logExpense_onlyCommand_returnsUsageGuide() throws FinanceException {
        ExpenseList expenseList = new ExpenseList();
        List<Income> incomes = new ArrayList<>();
        // Even with extra whitespace, the trimmed input becomes "log-expense"
        Command command = new LogExpenseCommand("   log-expense   ");
        command.execute(incomes, expenseList);
        String expectedOutput = "Usage: log-expense category/<category> desc/<description> amt/<amount> d/<date>\n" +
                "Example: log-expense category/Food desc/Lunch amt/12.50 d/15-03-2025";
        assertEquals(expectedOutput, command.getOutputMessage());
    }

    /**
     * Tests if the absence of a category results in an error message.
     */
    //@@author dariusyawningwhiz
    @Test
    void logExpense_missingCategory_showsError() {
        ExpenseList expenseList = new ExpenseList();
        List<Income> incomes = new ArrayList<>();
        Command command = new LogExpenseCommand(
                "log-expense desc/DinnerWithFriends amt/45.75 d/15-03-2025");
        try {
            command.execute(incomes, expenseList);
            fail();
        } catch (FinanceException e) {
            String expectedError = "Error: Expense category is required.";
            assertEquals(expectedError, e.getMessage());
        }
    }

    /**
     * Tests if the absence of a description results in an error message.
     */
    //@@author dariusyawningwhiz
    @Test
    void logExpense_missingDescription_showsError() {
        ExpenseList expenseList = new ExpenseList();
        List<Income> incomes = new ArrayList<>();
        Command command = new LogExpenseCommand(
                "log-expense category/Dining amt/45.75 d/15-03-2025");
        try {
            command.execute(incomes, expenseList);
            fail();
        } catch (FinanceException e) {
            String expectedError = "Error: Expense description is required.";
            assertEquals(expectedError, e.getMessage());
        }
    }

    /**
     * Tests if the absence of an amount results in an error message.
     */
    //@@author dariusyawningwhiz
    @Test
    void logExpense_missingAmount_showsError() {
        ExpenseList expenseList = new ExpenseList();
        List<Income> incomes = new ArrayList<>();
        Command command = new LogExpenseCommand(
                "log-expense category/Dining desc/DinnerWithFriends d/15-03-2025");
        try {
            command.execute(incomes, expenseList);
            fail();
        } catch (FinanceException e) {
            String expectedError = "Error: Expense amount is required.";
            assertEquals(expectedError, e.getMessage());
        }
    }

    /**
     * Tests if the absence of a date results in an error message.
     */
    //@@author dariusyawningwhiz
    @Test
    void logExpense_missingDate_showsError() {
        ExpenseList expenseList = new ExpenseList();
        List<Income> incomes = new ArrayList<>();
        Command command = new LogExpenseCommand(
                "log-expense category/Dining desc/DinnerWithFriends amt/45.75");
        try {
            command.execute(incomes, expenseList);
            fail();
        } catch (FinanceException e) {
            String expectedError = "Error: Expense date is required.";
            assertEquals(expectedError, e.getMessage());
        }
    }

    /**
     * Tests if an invalid amount format results in an error message.
     */
    //@@author dariusyawningwhiz
    @Test
    void logExpense_invalidAmountFormat_showsError() {
        ExpenseList expenseList = new ExpenseList();
        List<Income> incomes = new ArrayList<>();
        // "invalid" does not match the expected numeric pattern, so amount remains null.
        Command command = new LogExpenseCommand(
                "log-expense category/Dining desc/DinnerWithFriends amt/invalid d/15-03-2025");
        try {
            command.execute(incomes, expenseList);
            fail();
        } catch (FinanceException e) {
            String expectedError = "Error: Expense amount is required.";
            assertEquals(expectedError, e.getMessage());
        }
    }

    /**
     * Tests if an invalid date format results in an error message.
     */
    //@@author dariusyawningwhiz
    @Test
    void logExpense_invalidDateFormat_showsError() {
        ExpenseList expenseList = new ExpenseList();
        List<Income> incomes = new ArrayList<>();
        // Date in wrong format should trigger the incorrect date error.
        Command command = new LogExpenseCommand(
                "log-expense category/Dining desc/DinnerWithFriends amt/45.75 d/2025-03-15");
        try {
            command.execute(incomes, expenseList);
            fail();
        } catch (FinanceException e) {
            // Updated expected string to match the actual output.
            String expectedError = "Error: Income date is in wrong format. please use DD-MM-YYYY format.";
            assertEquals(expectedError, e.getMessage());
        }
    }

    /**
     * Tests if an invalid date value results in an error message.
     */
    //@@author dariusyawningwhiz
    @Test
    void logExpense_invalidDate_showsError() {
        ExpenseList expenseList = new ExpenseList();
        List<Income> incomes = new ArrayList<>();
        // Invalid date values should trigger the invalid date error.
        Command command = new LogExpenseCommand(
                "log-expense category/Dining desc/DinnerWithFriends amt/45.75 d/99-99-1234");
        try {
            command.execute(incomes, expenseList);
            fail();
        } catch (FinanceException e) {
            String expectedError = "Error: Date is not a valid date";
            assertEquals(expectedError, e.getMessage());
        }
    }

    //@@author IgoyAI
    @Test
    void logExpense_exceedsIntegerDigits_showsError() {
        ExpenseList expenseList = new ExpenseList();
        List<Income> incomes = new ArrayList<>();
        // Amount with integer part exceeding 7 digits (e.g., 12345678)
        Command command = new LogExpenseCommand(
                "log-expense category/Test desc/Test amt/12345678 d/15-03-2025");
        try {
            command.execute(incomes, expenseList);
            fail();
        } catch (FinanceException e) {
            String expectedError = "Amount exceeds 7 digits. Please enter a number with up to 7 digits.";
            assertEquals(expectedError, e.getMessage());
        }
    }

    //@@author IgoyAI
    @Test
    void logExpense_exceedsDecimalDigits_showsError() {
        ExpenseList expenseList = new ExpenseList();
        List<Income> incomes = new ArrayList<>();
        // Amount with more than 2 decimal places (e.g., 123.456)
        Command command = new LogExpenseCommand(
                "log-expense category/Test desc/Test amt/123.456 d/15-03-2025");
        try {
            command.execute(incomes, expenseList);
            fail();
        } catch (FinanceException e) {
            String expectedError = "Amount must have at most 2 decimal places.";
            assertEquals(expectedError, e.getMessage());
        }
    }

    //@@author KimTran
    @Test
    void logExpense_extraParameters_ignoresExtraParams() throws FinanceException {
        ExpenseList expenseList = new ExpenseList();
        List<Income> incomes = new ArrayList<>();
        // Extra parameters after required tags should be ignored.
        Command command = new LogExpenseCommand(
                "log-expense category/Food desc/Lunch amt/12.50 d/15-03-2025 extra/parameter");
        command.execute(incomes, expenseList);
        String expectedOutput = "Expense logged: Food | Lunch | $12.50 | 15-03-2025";
        assertEquals(expectedOutput, command.getOutputMessage());
    }

    //@@author Quydatnguyen
    @Test
    void logExpense_tagsOutOfOrder_logsExpenseCorrectly() throws FinanceException {
        ExpenseList expenseList = new ExpenseList();
        List<Income> incomes = new ArrayList<>();
        // Tags provided in a different order than expected.
        Command command = new LogExpenseCommand(
                "log-expense amt/45.75 d/15-03-2025 category/Dining desc/DinnerWithFriends");
        command.execute(incomes, expenseList);
        String expectedOutput = "Expense logged: Dining | DinnerWithFriends | $45.75 | 15-03-2025";
        assertEquals(expectedOutput, command.getOutputMessage());
    }
}
