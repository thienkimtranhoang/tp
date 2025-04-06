package budgetflow.command;

import budgetflow.exception.InvalidDateException;
import budgetflow.exception.InvalidNumberFormatException;
import budgetflow.exception.MissingCategoryException;
import budgetflow.exception.MissingDescriptionException;
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
    void updateExpense_invalidCategoryFormat() {
        UpdateExpenseCommand command = new UpdateExpenseCommand("update-expense 1 category/Food#123");
        Exception exception = assertThrows(MissingCategoryException.class, () -> command.execute(incomes, expenseList));
        assertEquals("Error: Invalid category. It must contain only alphanumeric characters.",
                exception.getMessage());
    }

    @Test
    void updateExpense_invalidDescriptionFormat() {
        UpdateExpenseCommand command = new UpdateExpenseCommand("update-expense 1 desc/Lunch!!!");
        Exception exception = assertThrows(MissingDescriptionException.class, () -> command.execute(incomes,
                expenseList));
        assertEquals("Error: Invalid description. It must contain only alphanumeric characters.",
                exception.getMessage());
    }

    @Test
    void updateExpense_amountExceeds7Digits() {
        UpdateExpenseCommand command = new UpdateExpenseCommand("update-expense 1 amt/10000000.00");
        Exception exception = assertThrows(InvalidNumberFormatException.class, () -> command.execute(incomes,
                expenseList));
        assertEquals("Amount exceeds 7 digits. Please enter a number with up to 7 digits.", exception.
                getMessage());
    }

    @Test
    void updateExpense_amountMoreThan2DecimalPlaces() {
        UpdateExpenseCommand command = new UpdateExpenseCommand("update-expense 1 amt/10.123");
        Exception exception = assertThrows(InvalidNumberFormatException.class, () -> command.execute(incomes,
                expenseList));
        assertEquals("Amount must have at most 2 decimal places.", exception.getMessage());
    }

    @Test
    void updateExpense_noValidUpdates() throws Exception {
        UpdateExpenseCommand command = new UpdateExpenseCommand("update-expense 1");
        command.execute(incomes, expenseList);
        Expense updatedExpense = expenseList.get(0);
        assertEquals("Food", updatedExpense.getCategory());
        assertEquals("Lunch", updatedExpense.getDescription());
        assertEquals(10.50, updatedExpense.getAmount());
        assertEquals("01-01-2024", updatedExpense.getDate());
    }

    @Test
    void updateExpense_validFloatingPointAmount() throws Exception {
        UpdateExpenseCommand command = new UpdateExpenseCommand("update-expense 1 amt/99.99");
        command.execute(incomes, expenseList);
        Expense updatedExpense = expenseList.get(0);
        assertEquals(99.99, updatedExpense.getAmount());
    }

    @Test
    void updateExpense_storageUpdateCalled() throws Exception {
        UpdateExpenseCommand command = new UpdateExpenseCommand(
                "update-expense 1 category/Entertainment amt/20.00");
        command.execute(incomes, expenseList);

        Expense updatedExpense = expenseList.get(0);
        assertEquals("Entertainment", updatedExpense.getCategory());
        assertEquals(20.00, updatedExpense.getAmount());
    }

    @Test
    void updateExpense_missingIndex() {
        UpdateExpenseCommand command = new UpdateExpenseCommand("update-expense");
        Exception exception = assertThrows(InvalidNumberFormatException.class, () -> command.execute(incomes,
                expenseList));
        assertEquals("Error: Index is required.", exception.getMessage());
    }

    @Test
    void updateExpense_nonNumericIndex() {
        UpdateExpenseCommand command = new UpdateExpenseCommand("update-expense abc category/Food");
        Exception exception = assertThrows(InvalidNumberFormatException.class, () -> command.execute(incomes,
                expenseList));
        assertEquals("Error: Index must be a number.", exception.getMessage());
    }

    @Test
    void updateExpense_indexOutOfBounds() {
        UpdateExpenseCommand command = new UpdateExpenseCommand("update-expense 5 category/Food");
        Exception exception = assertThrows(InvalidNumberFormatException.class, () -> command.execute(incomes,
                expenseList));
        assertEquals("Error: Expense entry not found.", exception.getMessage());
    }
    @Test
    void updateExpense_emptyExpenseList() {
        ExpenseList emptyExpenseList = new ExpenseList();
        UpdateExpenseCommand command = new UpdateExpenseCommand("update-expense 1 category/Food");
        Exception exception = assertThrows(InvalidNumberFormatException.class, () -> command.execute(incomes,
                emptyExpenseList));
        assertEquals("Error: No expense entries exist to update.", exception.getMessage());
    }

    @Test
    void updateExpense_multipleValidUpdates() throws Exception {
        UpdateExpenseCommand command = new UpdateExpenseCommand(
                "update-expense 1 category/Entertainment desc/Movie amt/15.00 d/05-04-2024");
        command.execute(incomes, expenseList);
        Expense updatedExpense = expenseList.get(0);
        assertEquals("Entertainment", updatedExpense.getCategory());
        assertEquals("Movie", updatedExpense.getDescription());
        assertEquals(15.00, updatedExpense.getAmount());
        assertEquals("05-04-2024", updatedExpense.getDate());
    }

    @Test
    void updateExpense_largeAmountBoundary() throws Exception {
        UpdateExpenseCommand command = new UpdateExpenseCommand("update-expense 1 amt/9999999.99");
        command.execute(incomes, expenseList);
        Expense updatedExpense = expenseList.get(0);
        assertEquals(9999999.99, updatedExpense.getAmount());
    }

    @Test
    void updateExpense_aboveMaxAmount() {
        UpdateExpenseCommand command = new UpdateExpenseCommand("update-expense 1 amt/10000000.00");
        Exception exception = assertThrows(InvalidNumberFormatException.class, () -> command.execute(incomes,
                expenseList));
        assertEquals("Amount exceeds 7 digits. Please enter a number with up to 7 digits.",
                exception.getMessage());
    }

    @Test
    void updateExpense_multipleInvalidUpdates() {
        UpdateExpenseCommand command = new UpdateExpenseCommand(
                "update-expense 1 category/@Home amt/-30.50 d/32-13-2024");
        Exception exception = assertThrows(MissingCategoryException.class, () -> command.execute(incomes,
                expenseList));
        assertEquals("Error: Invalid category. It must contain only alphanumeric characters.",
                exception.getMessage());
    }

    @Test
    void updateExpense_amountWithMoreThan2DecimalPlaces() {
        UpdateExpenseCommand command = new UpdateExpenseCommand("update-expense 1 amt/25.123");
        Exception exception = assertThrows(InvalidNumberFormatException.class, () -> command.execute(incomes,
                expenseList));
        assertEquals("Amount must have at most 2 decimal places.", exception.getMessage());
    }

    @Test
    void updateExpense_validDateFormat() throws Exception {
        UpdateExpenseCommand command = new UpdateExpenseCommand("update-expense 1 d/15-03-2024");
        command.execute(incomes, expenseList);
        Expense updatedExpense = expenseList.get(0);
        assertEquals("15-03-2024", updatedExpense.getDate());
    }

    @Test
    void updateExpense_noCategoryOrDescription() throws Exception {
        UpdateExpenseCommand command = new UpdateExpenseCommand("update-expense 1 amt/30.50");
        command.execute(incomes, expenseList);
        Expense updatedExpense = expenseList.get(0);
        assertEquals("Food", updatedExpense.getCategory());  // Category should remain the same
        assertEquals("Lunch", updatedExpense.getDescription());  // Description should remain the same
        assertEquals(30.50, updatedExpense.getAmount());
    }

    @Test
    void updateExpense_categoryOnlyUpdate() throws Exception {
        UpdateExpenseCommand command = new UpdateExpenseCommand("update-expense 1 category/Drinks");
        command.execute(incomes, expenseList);
        Expense updatedExpense = expenseList.get(0);
        assertEquals("Drinks", updatedExpense.getCategory());
        assertEquals("Lunch", updatedExpense.getDescription());
        assertEquals(10.50, updatedExpense.getAmount());
    }

    @Test
    void updateExpense_amountOnlyUpdate() throws Exception {
        UpdateExpenseCommand command = new UpdateExpenseCommand("update-expense 1 amt/50.75");
        command.execute(incomes, expenseList);
        Expense updatedExpense = expenseList.get(0);
        assertEquals(50.75, updatedExpense.getAmount());
    }

    @Test
    void updateExpense_multipleCategories() throws Exception {
        UpdateExpenseCommand command = new UpdateExpenseCommand(
                "update-expense 1 category/Transport category/Food");
        command.execute(incomes, expenseList);
        Expense updatedExpense = expenseList.get(0);
        assertEquals("Food", updatedExpense.getCategory());
    }

    @Test
    void updateExpense_validDateWithDifferentFormats() throws Exception {
        UpdateExpenseCommand command = new UpdateExpenseCommand("update-expense 1 d/05-04-2024");
        command.execute(incomes, expenseList);
        Expense updatedExpense = expenseList.get(0);
        assertEquals("05-04-2024", updatedExpense.getDate());
    }

    @Test
    void updateExpense_invalidMultipleUpdates() {
        UpdateExpenseCommand command = new UpdateExpenseCommand(
                "update-expense 1 amt/50.00 category/Invalid#Category");
        Exception exception = assertThrows(MissingCategoryException.class, () -> command.execute(incomes,
                expenseList));
        assertEquals("Error: Invalid category. It must contain only alphanumeric characters.",
                exception.getMessage());
    }

    @Test
    void updateExpense_noValidUpdatesWithMultipleSpaces() throws Exception {
        UpdateExpenseCommand command = new UpdateExpenseCommand("update-expense 1   ");
        command.execute(incomes, expenseList);
        Expense updatedExpense = expenseList.get(0);
        assertEquals("Food", updatedExpense.getCategory());
        assertEquals("Lunch", updatedExpense.getDescription());
        assertEquals(10.50, updatedExpense.getAmount());
    }

    @Test
    void updateExpense_invalidCategoryWithSymbol() {
        UpdateExpenseCommand command = new UpdateExpenseCommand("update-expense 1 category/Food#123");
        Exception exception = assertThrows(MissingCategoryException.class, () -> command.execute(incomes,
                expenseList));
        assertEquals("Error: Invalid category. It must contain only alphanumeric characters.",
                exception.getMessage());
    }

    @Test
    void updateExpense_validAmountWithMaxDigits() throws Exception {
        UpdateExpenseCommand command = new UpdateExpenseCommand("update-expense 1 amt/9999999.99");
        command.execute(incomes, expenseList);
        Expense updatedExpense = expenseList.get(0);
        assertEquals(9999999.99, updatedExpense.getAmount());
    }

    @Test
    void updateExpense_invalidDateFormat() {
        UpdateExpenseCommand command = new UpdateExpenseCommand("update-expense 1 d/31-04-2024");
        Exception exception = assertThrows(InvalidDateException.class, () -> command.execute(incomes, expenseList));
        assertEquals("Error: Invalid date format. Usage: DD-MM-YYYY", exception.getMessage());
    }
}
