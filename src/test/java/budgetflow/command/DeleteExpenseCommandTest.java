package budgetflow.command;

import budgetflow.exception.FinanceException;
import budgetflow.expense.Expense;
import budgetflow.expense.ExpenseList;
import budgetflow.income.Income;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

//@@author Yikbing
class DeleteExpenseCommandTest {
    private static ExpenseList getListWith3Expenses() {
        ExpenseList expenseList = new ExpenseList();
        expenseList.add(new Expense("food", "Lunch", 12.50, "13-03-2025"));
        expenseList.add(new Expense("transport", "Transport", 3.20, "12-03-2025"));
        expenseList.add(new Expense("food", "Groceries", 25.0, "11-03-2025"));
        return expenseList;
    }

    @Test
    void deleteExpense_invalidExpense_expectExpenseNotFoundMessage() throws FinanceException {
        ExpenseList expenseList = getListWith3Expenses();
        List<Income> incomes = new ArrayList<>();
        Command command = new DeleteExpenseCommand("delete-expense Dinner");

        command.execute(incomes, expenseList);

        assertEquals("Expense not found: Dinner", command.getOutputMessage());
        assertEquals(3, expenseList.getSize()); // Ensure list size remains unchanged
    }

    @Test
    void deleteExpense_emptyDescription_expectErrorMessage() throws FinanceException {
        ExpenseList expenseList = getListWith3Expenses();
        List<Income> incomes = new ArrayList<>();
        Command command = new DeleteExpenseCommand("delete-expense ");

        command.execute(incomes, expenseList);

        assertEquals("Error: Expense description is required.", command.getOutputMessage());
        assertEquals(3, expenseList.getSize()); // Ensure no expenses were removed
    }

    @Test
    void deleteExpense_existingExpense_expectSuccessMessage() throws FinanceException {
        ExpenseList expenseList = getListWith3Expenses();
        List<Income> incomes = new ArrayList<>();
        Command command = new DeleteExpenseCommand("delete-expense Lunch");

        command.execute(incomes, expenseList);

        assertEquals("Expense deleted: Lunch", command.getOutputMessage());
        assertEquals(2, expenseList.getSize()); // Ensure list size is reduced by 1
    }

    //@@author Yikbing
    @Test
    void deleteExpense_invalidCommandFormat_expectErrorMessage() throws FinanceException {
        ExpenseList expenseList = getListWith3Expenses();
        List<Income> incomes = new ArrayList<>();
        Command command = new DeleteExpenseCommand("remove-expense Lunch");

        command.execute(incomes, expenseList);

        assertEquals("Invalid delete expense command format.", command.getOutputMessage());
        assertEquals(3, expenseList.getSize()); // Ensure list size remains unchanged
    }

    @Test
    void deleteExpense_caseInsensitiveDescription_expectSuccessMessage() throws FinanceException {
        ExpenseList expenseList = getListWith3Expenses();
        List<Income> incomes = new ArrayList<>();
        Command command = new DeleteExpenseCommand("delete-expense lunch"); // lowercase input

        command.execute(incomes, expenseList);

        assertEquals("Expense deleted: lunch", command.getOutputMessage());
        assertEquals(2, expenseList.getSize()); // Ensure list size is reduced by 1
    }

    @Test
    void deleteExpense_emptyList_expectErrorMessage() throws FinanceException {
        ExpenseList expenseList = new ExpenseList(); // Empty list
        List<Income> incomes = new ArrayList<>();
        Command c = new DeleteExpenseCommand("delete-expense Lunch");

        c.execute(incomes, expenseList);

        assertEquals("Expense not found: Lunch", c.getOutputMessage());
        assertEquals(0, expenseList.getSize()); // Ensure list size remains unchanged
    }

    @Test
    void deleteExpense_multipleExpensesWithSameName_expectCorrectExpenseDeleted() throws FinanceException {
        ExpenseList expenseList = new ExpenseList();
        expenseList.add(new Expense("food", "Lunch", 12.50, "13-03-2025"));
        expenseList.add(new Expense("food", "Lunch", 15.00, "14-03-2025")); // Duplicate entry
        expenseList.add(new Expense("food", "Lunch", 20.00, "15-03-2025")); // Another duplicate
        List<Income> incomes = new ArrayList<>();
        Command c = new DeleteExpenseCommand("delete-expense Lunch");

        c.execute(incomes, expenseList);

        assertEquals("Expense deleted: Lunch", c.getOutputMessage());
        assertEquals(2, expenseList.getSize()); // Ensure list size is reduced by 1
    }

    @Test
    void deleteExpense_nullExpenseDescription_expectErrorMessage() throws FinanceException {
        ExpenseList expenseList = getListWith3Expenses();
        List<Income> incomes = new ArrayList<>();
        Command c = new DeleteExpenseCommand("delete-expense null");

        c.execute(incomes, expenseList);

        assertEquals("Expense not found: null", c.getOutputMessage());
        assertEquals(3, expenseList.getSize()); // Ensure list size remains unchanged
    }
}
