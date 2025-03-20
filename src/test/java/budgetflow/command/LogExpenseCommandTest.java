package budgetflow.command;

import budgetflow.exception.FinanceException;
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
        Command c = new LogExpenseCommand(
                "log-expense category/Dining desc/DinnerWithFriends amt/45.75 d/2025-03-15");
        c.execute(incomes, expenseList);
        String expectedOutput = "Expense logged: Dining | DinnerWithFriends | $45.75 | 2025-03-15";
        assertEquals(expectedOutput, c.getOutputMessage());
    }

    /**
     * Tests handling of an empty input command.
     * Ensures that an appropriate exception is thrown.
     */
    //@@author dariusyawningwhiz
    @Test
    void logExpense_emptyInputTest() {
        ExpenseList expenseList = new ExpenseList();
        List<Income> incomes = new ArrayList<>();
        Command c = new LogExpenseCommand("log-expense ");
        try {
            c.execute(incomes, expenseList);
            fail();
        } catch (FinanceException e) {
            String expectedError = "Expense should not be empty";
            assertEquals(expectedError, e.getMessage());
        }
    }

    /**
     * Tests if the absence of a category results in an error message.
     */
    //@@author dariusyawningwhiz
    @Test
    void logExpense_missingCategory_showsError() {
        ExpenseList expenseList = new ExpenseList();
        List<Income> incomes = new ArrayList<>();
        Command c = new LogExpenseCommand(
                "log-expense desc/DinnerWithFriends amt/45.75 d/2025-03-15");
        try {
            c.execute(incomes, expenseList);
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
        Command c = new LogExpenseCommand(
                "log-expense category/Dining amt/45.75 d/2025-03-15");
        try {
            c.execute(incomes, expenseList);
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
        Command c = new LogExpenseCommand(
                "log-expense category/Dining desc/DinnerWithFriends d/2025-03-15");
        try {
            c.execute(incomes, expenseList);
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
        Command c = new LogExpenseCommand(
                "log-expense category/Dining desc/DinnerWithFriends amt/45.75");
        try {
            c.execute(incomes, expenseList);
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
        Command c = new LogExpenseCommand(
                "log-expense category/Dining desc/DinnerWithFriends amt/invalid d/2025-03-15");
        try {
            c.execute(incomes, expenseList);
            fail();
        } catch (FinanceException e) {
            String expectedError = "Error: Expense amount is required.";
            assertEquals(expectedError, e.getMessage());
        }
    }

    @Test
    void addIncome_extraParameters_ignoresExtraParams() throws FinanceException {
        ExpenseList expenseList = new ExpenseList();
        List<Income> incomes = new ArrayList<>();
        Command c = new AddIncomeCommand("add category/Salary amt/2500.00 d/2025-03-15 extra/parameter");
        c.execute(incomes, expenseList);
        String expectedOutput = "Income added: Salary, Amount: $2500.00, Date: 2025-03-15";
        assertEquals(expectedOutput, c.getOutputMessage());
    }

}
