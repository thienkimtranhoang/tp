package budgetflow.command;

import budgetflow.exception.FinanceException;
import budgetflow.exception.MissingCategoryException;
import budgetflow.exception.MissingDescriptionException;
import budgetflow.exception.MissingExpenseException;
import budgetflow.exception.MissingAmountException;
import budgetflow.exception.MissingDateException;
import budgetflow.expense.Expense;
import budgetflow.expense.ExpenseList;
import budgetflow.income.Income;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UpdateExpenseCommandTest {

    @Test
    void updateExpense_validInput_updatesExpense() throws FinanceException {
        ExpenseList expenseList = new ExpenseList();
        List<Income> incomes = new ArrayList<>();
        expenseList.add(new Expense("Food", "Lunch", 12.50, "13-03-2025"));

        Command c = new UpdateExpenseCommand(
                "update-expense index/1 category/Dining desc/Dinner amt/20.00 d/14-03-2025"
        );
        c.execute(incomes, expenseList);

        String expectedOutput =
                "Expense updated at index 1: Dining | Dinner | $20.00 | 14-03-2025. Total Expenses: $20.00";
        assertEquals(expectedOutput, c.getOutputMessage());
    }

    @Test
    void updateExpense_invalidIndex_throwsException() {
        ExpenseList expenseList = new ExpenseList();
        List<Income> incomes = new ArrayList<>();
        Command c = new UpdateExpenseCommand(
                "update-expense index/5 category/Food desc/Lunch amt/10.00 d/12-03-2025"
        );

        Exception exception = assertThrows(MissingExpenseException.class, () -> c.execute(incomes, expenseList));
        assertEquals("Error: Invalid index. Expense not found.", exception.getMessage());
    }

    @Test
    void updateExpense_missingIndex_throwsException() {
        ExpenseList expenseList = new ExpenseList();
        List<Income> incomes = new ArrayList<>();
        Command c = new UpdateExpenseCommand("update-expense category/Food desc/Lunch amt/10.00 d/12-03-2025");

        Exception exception = assertThrows(MissingExpenseException.class, () -> c.execute(incomes, expenseList));
        assertEquals("Index is required", exception.getMessage());
    }

    @Test
    void updateExpense_missingCategory_throwsException() {
        ExpenseList expenseList = new ExpenseList();
        List<Income> incomes = new ArrayList<>();
        expenseList.add(new Expense("Food", "Lunch", 12.50, "13-03-2025"));

        Command c = new UpdateExpenseCommand("update-expense index/1 desc/Dinner amt/20.00 d/14-03-2025");
        Exception exception = assertThrows(MissingCategoryException.class, () -> c.execute(incomes, expenseList));
        assertEquals("Error: Expense category is required.", exception.getMessage());
    }

    @Test
    void updateExpense_missingDescription_throwsException() {
        ExpenseList expenseList = new ExpenseList();
        List<Income> incomes = new ArrayList<>();
        expenseList.add(new Expense("Food", "Lunch", 12.50, "13-03-2025"));

        Command c = new UpdateExpenseCommand("update-expense index/1 category/Dining amt/20.00 d/14-03-2025");
        Exception exception = assertThrows(MissingDescriptionException.class, () -> c.execute(incomes, expenseList));
        assertEquals("Error: Expense description is required.", exception.getMessage());
    }

    @Test
    void updateExpense_missingAmount_throwsException() {
        ExpenseList expenseList = new ExpenseList();
        List<Income> incomes = new ArrayList<>();
        expenseList.add(new Expense("Food", "Lunch", 12.50, "13-03-2025"));

        Command c = new UpdateExpenseCommand("update-expense index/1 category/Dining desc/Dinner d/14-03-2025");
        Exception exception = assertThrows(MissingAmountException.class, () -> c.execute(incomes, expenseList));
        assertEquals("Error: Expense amount is required.", exception.getMessage());
    }

    @Test
    void updateExpense_missingDate_throwsException() {
        ExpenseList expenseList = new ExpenseList();
        List<Income> incomes = new ArrayList<>();
        expenseList.add(new Expense("Food", "Lunch", 12.50, "13-03-2025"));

        Command c = new UpdateExpenseCommand("update-expense index/1 category/Dining desc/Dinner amt/20.00");
        Exception exception = assertThrows(MissingDateException.class, () -> c.execute(incomes, expenseList));
        assertEquals("Error: Expense date is required.", exception.getMessage());
    }

    @Test
    void updateExpense_invalidCategory_throwsException() {
        ExpenseList expenseList = new ExpenseList();
        List<Income> incomes = new ArrayList<>();
        expenseList.add(new Expense("Food", "Lunch", 12.50, "13-03-2025"));

        // Using invalid category format that shouldn't match
        Command c = new UpdateExpenseCommand(
                "update-expense index/1 category/!@#$ desc/Dinner amt/20.00 d/14-03-2025"
        );

        Exception exception = assertThrows(MissingCategoryException.class, () -> c.execute(incomes, expenseList));
        assertEquals("Error: Invalid category format.", exception.getMessage());
    }

    @Test
    void updateExpense_invalidAmountFormat_throwsException() {
        ExpenseList expenseList = new ExpenseList();
        List<Income> incomes = new ArrayList<>();
        expenseList.add(new Expense("Food", "Lunch", 12.50, "13-03-2025"));

        Command c = new UpdateExpenseCommand(
                "update-expense index/1 category/Dining desc/Dinner amt/invalid d/14-03-2025"
        );
        Exception exception = assertThrows(MissingAmountException.class, () -> c.execute(incomes, expenseList));
    }

    @Test
    void updateExpense_invalidDateFormat_throwsException() {
        ExpenseList expenseList = new ExpenseList();
        List<Income> incomes = new ArrayList<>();
        expenseList.add(new Expense("Food", "Lunch", 12.50, "13-03-2025"));

        Command c = new UpdateExpenseCommand(
                "update-expense index/1 category/Dining desc/Dinner amt/20.00 d/2025-03-14"
        );
        Exception exception = assertThrows(MissingDateException.class, () -> c.execute(incomes, expenseList));
        assertEquals("Error: Expense date is required."
                , exception.getMessage());
    }

    @Test
    void updateExpense_extraParameters_ignoresExtraParams() throws FinanceException {
        ExpenseList expenseList = new ExpenseList();
        List<Income> incomes = new ArrayList<>();
        expenseList.add(new Expense("Food", "Lunch", 12.50, "13-03-2025"));

        Command c = new UpdateExpenseCommand(
                "update-expense index/1 category/Dining desc/Dinner amt/20.00 d/14-03-2025 extra/param"
        );
        c.execute(incomes, expenseList);

        String expectedOutput =
                "Expense updated at index 1: Dining | Dinner | $20.00 | 14-03-2025. Total Expenses: $20.00";
        assertEquals(expectedOutput, c.getOutputMessage());
    }

    @Test
    void updateExpense_emptyInput_throwsException() {
        ExpenseList expenseList = new ExpenseList();
        List<Income> incomes = new ArrayList<>();
        Command c = new UpdateExpenseCommand("");

        Exception exception = assertThrows(MissingExpenseException.class, () -> c.execute(incomes, expenseList));
        assertEquals("Index is required", exception.getMessage());
    }

    @Test
    void updateExpense_negativeIndex_throwsException() {
        ExpenseList expenseList = new ExpenseList();
        List<Income> incomes = new ArrayList<>();
        Command c = new UpdateExpenseCommand(
                "update-expense index/-1 category/Food desc/Lunch amt/10.00 d/12-03-2025"
        );

        Exception exception = assertThrows(MissingExpenseException.class, () -> c.execute(incomes, expenseList));
        assertEquals("Error: Invalid index. Expense not found.", exception.getMessage());
    }

    @Test
    void updateExpense_withExtraParameters_ignoresExtraParams() throws FinanceException {
        ExpenseList expenseList = new ExpenseList();
        List<Income> incomes = new ArrayList<>();
        expenseList.add(new Expense("Food", "Lunch", 12.50, "13-03-2025"));

        // Input with extra parameters like `extra/param`
        Command c = new UpdateExpenseCommand(
                "update-expense index/1 category/Dining desc/Dinner amt/20.00 d/14-03-2025 extra/param"
        );
        c.execute(incomes, expenseList);

        String expectedOutput =
                "Expense updated at index 1: Dining | Dinner | $20.00 | 14-03-2025. Total Expenses: $20.00";
        assertEquals(expectedOutput, c.getOutputMessage());
    }
}
