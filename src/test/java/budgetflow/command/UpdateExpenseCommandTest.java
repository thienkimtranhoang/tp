package budgetflow.command;

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
    void setUp() {
        expenseList = new ExpenseList();
        incomes = new ArrayList<>();

        expenseList.add(new Expense("Food", "Lunch", 10.50, "01-01-2024"));
        expenseList.add(new Expense("Transport", "Bus fare", 2.00, "02-01-2024"));
    }

    @Test
    void testUpdateExpense_Success() throws Exception {
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
    void testUpdateExpense_MissingIndex() {
        UpdateExpenseCommand command = new UpdateExpenseCommand("update-expense");
        Exception exception = assertThrows(InvalidNumberFormatException.class, () ->
                command.execute(incomes, expenseList));
        assertEquals("Error: Index is required.", exception.getMessage());
    }

    @Test
    void testUpdateExpense_InvalidIndexFormat() {
        UpdateExpenseCommand command = new UpdateExpenseCommand("update-expense abc category/Food");
        Exception exception = assertThrows(InvalidNumberFormatException.class, () ->
                command.execute(incomes, expenseList));
        assertEquals("Error: Index must be a number.", exception.getMessage());
    }

    @Test
    void testUpdateExpense_IndexOutOfBounds() {
        UpdateExpenseCommand command = new UpdateExpenseCommand("update-expense 5 category/Food");
        Exception exception = assertThrows(InvalidNumberFormatException.class, () ->
                command.execute(incomes, expenseList));
        assertEquals("Error: Expense entry not found.", exception.getMessage());
    }

    @Test
    void testUpdateExpense_EmptyExpenseList() {
        expenseList = new ExpenseList();
        UpdateExpenseCommand command = new UpdateExpenseCommand("update-expense 1 category/Food");
        Exception exception = assertThrows(InvalidNumberFormatException.class, () ->
                command.execute(incomes, expenseList));
        assertEquals("Error: No expense entries exist to update.", exception.getMessage());
    }

    @Test
    void testUpdateExpense_NoChange() throws Exception {
        UpdateExpenseCommand command = new UpdateExpenseCommand("update-expense 1");
        command.execute(incomes, expenseList);

        Expense expense = expenseList.get(0);
        assertEquals("Food", expense.getCategory());
        assertEquals("Lunch", expense.getDescription());
        assertEquals(10.50, expense.getAmount());
        assertEquals("01-01-2024", expense.getDate());
    }

    @Test
    void testUpdateExpense_InvalidDateFormat() {
        UpdateExpenseCommand command = new UpdateExpenseCommand(
                "update-expense 1 category/Food amt/15.00 desc/Dinner d/32-01-2024");
        Exception exception = assertThrows(IllegalArgumentException.class, () -> command.execute(incomes, expenseList));
        assertEquals("Error: Invalid date format. Usage: DD-MM-YYYY", exception.getMessage());
    }

    @Test
    void testUpdateExpense_UpdateOnlyCategory() throws Exception {
        UpdateExpenseCommand command = new UpdateExpenseCommand("update-expense 1 category/Dining");
        command.execute(incomes, expenseList);

        Expense updatedExpense = expenseList.get(0);
        assertEquals("Dining", updatedExpense.getCategory());
        assertEquals("Lunch", updatedExpense.getDescription());
        assertEquals(10.50, updatedExpense.getAmount());
        assertEquals("01-01-2024", updatedExpense.getDate());
    }

    @Test
    void testUpdateExpense_UpdateOnlyAmount() throws Exception {
        UpdateExpenseCommand command = new UpdateExpenseCommand("update-expense 1 amt/20.00");
        command.execute(incomes, expenseList);

        Expense updatedExpense = expenseList.get(0);
        assertEquals("Food", updatedExpense.getCategory());
        assertEquals("Lunch", updatedExpense.getDescription());
        assertEquals(20.00, updatedExpense.getAmount());
        assertEquals("01-01-2024", updatedExpense.getDate());
    }

    @Test
    void testUpdateExpense_UpdateOnlyDescription() throws Exception {
        UpdateExpenseCommand command = new UpdateExpenseCommand("update-expense 1 desc/Dinner");
        command.execute(incomes, expenseList);

        Expense updatedExpense = expenseList.get(0);
        assertEquals("Food", updatedExpense.getCategory());
        assertEquals("Dinner", updatedExpense.getDescription());
        assertEquals(10.50, updatedExpense.getAmount());
        assertEquals("01-01-2024", updatedExpense.getDate());
    }

    @Test
    void testUpdateExpense_UpdateOnlyDate() throws Exception {
        UpdateExpenseCommand command = new UpdateExpenseCommand("update-expense 1 d/02-02-2024");
        command.execute(incomes, expenseList);

        Expense updatedExpense = expenseList.get(0);
        assertEquals("Food", updatedExpense.getCategory());
        assertEquals("Lunch", updatedExpense.getDescription());
        assertEquals(10.50, updatedExpense.getAmount());
        assertEquals("02-02-2024", updatedExpense.getDate());
    }

    @Test
    void testUpdateExpense_InvalidDateFormatOnUpdate() {
        UpdateExpenseCommand command = new UpdateExpenseCommand(
                "update-expense 1 category/Food amt/10.00 desc/Lunch d/99-99-2024");
        Exception exception = assertThrows(IllegalArgumentException.class, () -> command.execute(incomes, expenseList));
        assertEquals("Error: Invalid date format. Usage: DD-MM-YYYY", exception.getMessage());
    }

    @Test
    void testUpdateExpense_UpdateStorageCalled() throws Exception {
        UpdateExpenseCommand command = new UpdateExpenseCommand("update-expense 1 category/Transport amt/15.00");
        command.execute(incomes, expenseList);
    }

    @Test
    void testUpdateExpense_LargeIndex() {
        UpdateExpenseCommand command = new UpdateExpenseCommand("update-expense 999999 category/Food");
        Exception exception = assertThrows(InvalidNumberFormatException.class, () ->
                command.execute(incomes, expenseList));
        assertEquals("Error: Expense entry not found.", exception.getMessage());
    }

    @Test
    void testUpdateExpense_UpdateMultipleFields() throws Exception {
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
    void testUpdateExpense_UpdateWithOnlyCategory() throws Exception {
        UpdateExpenseCommand command = new UpdateExpenseCommand("update-expense 1 category/Leisure");
        command.execute(incomes, expenseList);

        Expense updatedExpense = expenseList.get(0);
        assertEquals("Leisure", updatedExpense.getCategory());
        assertEquals("Lunch", updatedExpense.getDescription());
        assertEquals(10.50, updatedExpense.getAmount());
        assertEquals("01-01-2024", updatedExpense.getDate());
    }
}



