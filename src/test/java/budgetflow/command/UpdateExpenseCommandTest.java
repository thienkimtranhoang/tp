package budgetflow.command;


import budgetflow.exception.ExceedsMaxTotalExpense;
import budgetflow.exception.InvalidNumberFormatException;
import budgetflow.exception.MissingCategoryException;
import budgetflow.exception.MissingDescriptionException;
import budgetflow.exception.InvalidDateException;
import budgetflow.exception.FinanceException;
import budgetflow.expense.Expense;
import budgetflow.expense.ExpenseList;
import budgetflow.income.Income;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

//@@author dariusyawningwhiz
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
        UpdateExpenseCommand command = new UpdateExpenseCommand("update-expense 1 amt/9999997.99");
        command.execute(incomes, expenseList);
        Expense updatedExpense = expenseList.get(0);
        assertEquals(9999997.99, updatedExpense.getAmount());
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
    void updateExpense_invalidDateFormat() {
        UpdateExpenseCommand command = new UpdateExpenseCommand("update-expense 1 d/31-04-2024");
        Exception exception = assertThrows(InvalidDateException.class, () -> command.execute(incomes, expenseList));
        assertEquals("Error: Invalid date format. Usage: DD-MM-YYYY", exception.getMessage());
    }

    @Test
    void updateExpense_invalidAmountTooLarge() {
        UpdateExpenseCommand command = new UpdateExpenseCommand("update-expense 1 amt/10000000.00");
        Exception exception = assertThrows(InvalidNumberFormatException.class, () -> command.execute(incomes,
                expenseList));
        assertEquals("Amount exceeds 7 digits. Please enter a number with up to 7 digits.",
                exception.getMessage());
    }

    @Test
    void updateExpense_invalidDateFormatWithMonthGreaterThan12() {
        UpdateExpenseCommand command = new UpdateExpenseCommand("update-expense 1 d/31-13-2024");
        Exception exception = assertThrows(InvalidDateException.class, () -> command.execute(incomes, expenseList));
        assertEquals("Error: Invalid date format. Usage: DD-MM-YYYY", exception.getMessage());
    }

    @Test
    void updateExpense_updateMultipleFieldsSuccessfully() throws Exception {
        UpdateExpenseCommand command = new UpdateExpenseCommand(
                "update-expense 1 category/Entertainment desc/Movie amt/15.99 d/05-04-2024");
        command.execute(incomes, expenseList);
        Expense updatedExpense = expenseList.get(0);
        assertEquals("Entertainment", updatedExpense.getCategory());
        assertEquals("Movie", updatedExpense.getDescription());
        assertEquals(15.99, updatedExpense.getAmount());
        assertEquals("05-04-2024", updatedExpense.getDate());
    }

    @Test
    void updateExpense_invalidDateWithTooManyDays() {
        UpdateExpenseCommand command = new UpdateExpenseCommand("update-expense 1 d/32-01-2024");
        Exception exception = assertThrows(InvalidDateException.class, () -> command.execute(incomes, expenseList));
        assertEquals("Error: Invalid date format. Usage: DD-MM-YYYY", exception.getMessage());
    }

    @Test
    void updateExpense_handleEmptyExpenseList() {
        ExpenseList emptyExpenseList = new ExpenseList();
        UpdateExpenseCommand command = new UpdateExpenseCommand("update-expense 1 category/Utilities");
        Exception exception = assertThrows(InvalidNumberFormatException.class, () -> command.execute(incomes,
                emptyExpenseList));
        assertEquals("Error: No expense entries exist to update.", exception.getMessage());
    }
    @Test
    public void testCompareExpenseCommand_validInputBothMonths() throws FinanceException {
        ExpenseList expenseList = new ExpenseList();
        List<Income> incomes = new ArrayList<>();
        Command c1 = new LogExpenseCommand(
                "log-expense category/Dining desc/spendA amt/100 d/15-03-2025");
        Command c2 = new LogExpenseCommand(
                "log-expense category/Dining desc/spendB amt/500 d/15-04-2025");
        c1.execute(incomes, expenseList);
        c2.execute(incomes, expenseList);

        Command c3 = new CompareExpenseCommand("compare 03-2025 04-2025");
        c3.execute(incomes, expenseList);
        String expectedOutput = "Total expenses for 03-2025: $100.00" + System.lineSeparator() +
                "Total expenses for 04-2025: $500.00" + System.lineSeparator();
        assertEquals(expectedOutput, c3.getOutputMessage());
    }

    @Test
    public void testCompareExpenseCommand_emptyInputOneMonth() throws FinanceException {
        ExpenseList expenseList = new ExpenseList();
        List<Income> incomes = new ArrayList<>();
        Command c1 = new LogExpenseCommand(
                "log-expense category/Dining desc/spendA amt/100 d/15-03-2025");
        c1.execute(incomes, expenseList);
        Command c2 = new CompareExpenseCommand("compare 03-2025 04-2025");
        c2.execute(incomes, expenseList);
        String expectedOutput = "Total expenses for 03-2025: $100.00" + System.lineSeparator() +
                "Total expenses for 04-2025: $0.00" + System.lineSeparator();
        assertEquals(expectedOutput, c2.getOutputMessage());
    }

    @Test
    public void testCompareExpenseCommand_incorrectMonthFormat() throws FinanceException {
        ExpenseList expenseList = new ExpenseList();
        List<Income> incomes = new ArrayList<>();
        Command c1 = new LogExpenseCommand(
                "log-expense category/Dining desc/spendA amt/100 d/15-03-2025");
        Command c2 = new LogExpenseCommand(
                "log-expense category/Dining desc/spendB amt/500 d/15-04-2025");
        c1.execute(incomes, expenseList);
        c2.execute(incomes, expenseList);
        Command c3 = new CompareExpenseCommand("compare 2025-03 2025-04");
        try {
            c3.execute(incomes, expenseList);
            fail();
        } catch (FinanceException e) {
            String expectedError = "Invalid input format. Usage: compare MM-YYYY MM-YYYY";
            assertEquals(expectedError, e.getMessage());
        }
    }

    @Test
    public void testCompareExpenseCommand_multipleExpensesPerMonth() throws FinanceException {
        ExpenseList expenseList = new ExpenseList();
        List<Income> incomes = new ArrayList<>();
        Command c1 = new LogExpenseCommand("log-expense category/Food desc/Lunch amt/50 d/10-03-2025");
        Command c2 = new LogExpenseCommand("log-expense category/Transport desc/Taxi amt/30 d/12-03-2025");
        Command c3 = new LogExpenseCommand("log-expense category/Groceries desc/Supermarket amt/70 d/20-04-2025");
        c1.execute(incomes, expenseList);
        c2.execute(incomes, expenseList);
        c3.execute(incomes, expenseList);

        Command compare = new CompareExpenseCommand("compare 03-2025 04-2025");
        compare.execute(incomes, expenseList);
        String expectedOutput = "Total expenses for 03-2025: $80.00" + System.lineSeparator() +
                "Total expenses for 04-2025: $70.00" + System.lineSeparator();
        assertEquals(expectedOutput, compare.getOutputMessage());
    }

    @Test
    public void testCompareExpenseCommand_noExpensesAtAll() throws FinanceException {
        ExpenseList expenseList = new ExpenseList();
        List<Income> incomes = new ArrayList<>();
        Command compare = new CompareExpenseCommand("compare 03-2025 04-2025");
        try {
            compare.execute(incomes, expenseList);
            fail();
        } catch (FinanceException e) {
            assertEquals("No expenses in range", e.getMessage());
        }
    }

    @Test
    public void testCompareExpenseCommand_largeExpenses() throws FinanceException {
        ExpenseList expenseList = new ExpenseList();
        List<Income> incomes = new ArrayList<>();
        Command c1 = new LogExpenseCommand("log-expense category/Travel desc/Flight amt/5000 d/10-03-2025");
        Command c2 = new LogExpenseCommand("log-expense category/Rent desc/Apartment amt/2000 d/15-04-2025");
        c1.execute(incomes, expenseList);
        c2.execute(incomes, expenseList);

        Command compare = new CompareExpenseCommand("compare 03-2025 04-2025");
        compare.execute(incomes, expenseList);
        String expectedOutput = "Total expenses for 03-2025: $5000.00" + System.lineSeparator() +
                "Total expenses for 04-2025: $2000.00" + System.lineSeparator();
        assertEquals(expectedOutput, compare.getOutputMessage());
    }
}
