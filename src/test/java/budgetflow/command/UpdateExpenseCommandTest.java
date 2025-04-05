package budgetflow.command;

import budgetflow.exception.ExceedsMaxTotalExpense;
import budgetflow.exception.InvalidDateException;
import budgetflow.exception.InvalidNumberFormatException;
import budgetflow.expense.Expense;
import budgetflow.expense.ExpenseList;
import budgetflow.income.Income;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UpdateExpenseCommandTest {
    private ExpenseList expenseList;
    private List<Income> incomes;

    @BeforeEach
    void setUp() throws ExceedsMaxTotalExpense {
        expenseList = new ExpenseList();
        incomes = new ArrayList<>();

        expenseList.add(new Expense("Food", "Lunch", 10.50, "01-01-2024"));
        expenseList.add(new Expense("Transport", "Bus fare", 2.00, "02-01-2024"));
    }

    @Test
    void updateExpense_success() throws Exception {
        UpdateExpenseCommand command = new UpdateExpenseCommand(
                "update-expense 1 category/Dining amt/12.00 desc/Dinner d/03-01-2024");
        command.execute(incomes, expenseList);

        Expense updatedExpense = expenseList.get(0);
        assertEquals("Dining", updatedExpense.getCategory());
        assertEquals("Dinner", updatedExpense.getDescription());
        assertEquals(12.00, updatedExpense.getAmount());
        assertEquals("03-01-2024", updatedExpense.getDate());
    }

    @Test
    void updateExpense_missingIndex() {
        UpdateExpenseCommand command = new UpdateExpenseCommand("update-expense");
        Exception exception = assertThrows(InvalidNumberFormatException.class, () ->
                command.execute(incomes, expenseList));
        assertEquals("Error: Index is required.", exception.getMessage());
    }

    @Test
    void updateExpense_invalidIndexFormat() {
        UpdateExpenseCommand command = new UpdateExpenseCommand("update-expense abc category/Food");
        Exception exception = assertThrows(InvalidNumberFormatException.class, () ->
                command.execute(incomes, expenseList));
        assertEquals("Error: Index must be a number.", exception.getMessage());
    }

    @Test
    void updateExpense_indexOutOfBounds() {
        UpdateExpenseCommand command = new UpdateExpenseCommand("update-expense 5 category/Food");
        Exception exception = assertThrows(InvalidNumberFormatException.class, () ->
                command.execute(incomes, expenseList));
        assertEquals("Error: Expense entry not found.", exception.getMessage());
    }

    @Test
    void updateExpense_emptyExpenseList() {
        expenseList = new ExpenseList();
        UpdateExpenseCommand command = new UpdateExpenseCommand("update-expense 1 category/Food");
        Exception exception = assertThrows(InvalidNumberFormatException.class, () ->
                command.execute(incomes, expenseList));
        assertEquals("Error: No expense entries exist to update.", exception.getMessage());
    }

    @Test
    void updateExpense_noChange() throws Exception {
        UpdateExpenseCommand command = new UpdateExpenseCommand("update-expense 1");
        command.execute(incomes, expenseList);

        Expense expense = expenseList.get(0);
        assertEquals("Food", expense.getCategory());
        assertEquals("Lunch", expense.getDescription());
        assertEquals(10.50, expense.getAmount());
        assertEquals("01-01-2024", expense.getDate());
    }

    @Test
    void updateExpense_invalidDateFormat() {
        UpdateExpenseCommand command = new UpdateExpenseCommand(
                "update-expense 1 category/Food amt/15.00 desc/Dinner d/32-01-2024");
        Exception exception = assertThrows(InvalidDateException.class, () -> command.execute(incomes, expenseList));
        assertEquals("Error: Invalid date format. Usage: DD-MM-YYYY", exception.getMessage());
    }

    @Test
    void updateExpense_updateOnlyCategory() throws Exception {
        UpdateExpenseCommand command = new UpdateExpenseCommand("update-expense 1 category/Dining");
        command.execute(incomes, expenseList);

        Expense updatedExpense = expenseList.get(0);
        assertEquals("Dining", updatedExpense.getCategory());
        assertEquals("Lunch", updatedExpense.getDescription());
        assertEquals(10.50, updatedExpense.getAmount());
        assertEquals("01-01-2024", updatedExpense.getDate());
    }

    @Test
    void updateExpense_updateOnlyAmount() throws Exception {
        UpdateExpenseCommand command = new UpdateExpenseCommand("update-expense 1 amt/20.00");
        command.execute(incomes, expenseList);

        Expense updatedExpense = expenseList.get(0);
        assertEquals("Food", updatedExpense.getCategory());
        assertEquals("Lunch", updatedExpense.getDescription());
        assertEquals(20.00, updatedExpense.getAmount());
        assertEquals("01-01-2024", updatedExpense.getDate());
    }

    @Test
    void updateExpense_updateOnlyDescription() throws Exception {
        UpdateExpenseCommand command = new UpdateExpenseCommand("update-expense 1 desc/Dinner");
        command.execute(incomes, expenseList);

        Expense updatedExpense = expenseList.get(0);
        assertEquals("Food", updatedExpense.getCategory());
        assertEquals("Dinner", updatedExpense.getDescription());
        assertEquals(10.50, updatedExpense.getAmount());
        assertEquals("01-01-2024", updatedExpense.getDate());
    }

    @Test
    void updateExpense_updateOnlyDate() throws Exception {
        UpdateExpenseCommand command = new UpdateExpenseCommand("update-expense 1 d/02-02-2024");
        command.execute(incomes, expenseList);

        Expense updatedExpense = expenseList.get(0);
        assertEquals("Food", updatedExpense.getCategory());
        assertEquals("Lunch", updatedExpense.getDescription());
        assertEquals(10.50, updatedExpense.getAmount());
        assertEquals("02-02-2024", updatedExpense.getDate());
    }

    @Test
    void updateExpense_invalidDateFormatOnUpdate() {
        UpdateExpenseCommand command = new UpdateExpenseCommand(
                "update-expense 1 category/Food amt/10.00 desc/Lunch d/99-99-2024");
        Exception exception = assertThrows(InvalidDateException.class, () -> command.execute(incomes, expenseList));
        assertEquals("Error: Invalid date format. Usage: DD-MM-YYYY", exception.getMessage());
    }

    @Test
    void updateExpense_updateStorageCalled() throws Exception {
        UpdateExpenseCommand command = new UpdateExpenseCommand("update-expense 1 category/Transport amt/15.00");
        command.execute(incomes, expenseList);
    }

    @Test
    void updateExpense_largeIndex() {
        UpdateExpenseCommand command = new UpdateExpenseCommand("update-expense 999999 category/Food");
        Exception exception = assertThrows(InvalidNumberFormatException.class, () ->
                command.execute(incomes, expenseList));
        assertEquals("Error: Expense entry not found.", exception.getMessage());
    }

    @Test
    void updateExpense_updateMultipleFields() throws Exception {
        UpdateExpenseCommand command = new UpdateExpenseCommand(
                "update-expense 1 category/Transport amt/25.00 desc/Taxi d/15-03-2024");
        command.execute(incomes, expenseList);

        Expense updatedExpense = expenseList.get(0);
        assertEquals("Transport", updatedExpense.getCategory());
        assertEquals("Taxi", updatedExpense.getDescription());
        assertEquals(25.00, updatedExpense.getAmount());
        assertEquals("15-03-2024", updatedExpense.getDate());
    }

    @Test
    void updateExpense_updateWithOnlyCategory() throws Exception {
        UpdateExpenseCommand command = new UpdateExpenseCommand("update-expense 1 category/Leisure");
        command.execute(incomes, expenseList);

        Expense updatedExpense = expenseList.get(0);
        assertEquals("Leisure", updatedExpense.getCategory());
        assertEquals("Lunch", updatedExpense.getDescription());
        assertEquals(10.50, updatedExpense.getAmount());
        assertEquals("01-01-2024", updatedExpense.getDate());
    }
}



