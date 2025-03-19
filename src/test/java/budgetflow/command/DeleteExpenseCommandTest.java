package budgetflow.command;

import budgetflow.FinanceTracker;
import budgetflow.exception.FinanceException;
import budgetflow.expense.Expense;
import budgetflow.expense.ExpenseList;
import budgetflow.income.Income;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DeleteExpenseCommandTest {
    private static ExpenseList getListWith3Expenses() {
        ExpenseList expenseList = new ExpenseList();
        expenseList.add(new Expense("food", "Lunch", 12.50, "2025-03-13"));
        expenseList.add(new Expense("transport", "Transport", 3.20, "2025-03-12"));
        expenseList.add(new Expense("food", "Groceries", 25.0, "2025-03-11"));
        return expenseList;
    }

    @Test
    void deleteExpense_validExpense_expectExpenseFound() throws FinanceException {
        ExpenseList expenseList = getListWith3Expenses();
        List<Income> incomes = new ArrayList<>();
        Command c = new DeleteExpenseCommand("delete-expense Lunch");
        c.execute(incomes, expenseList);
        String expectedOutput = "Expense deleted: Lunch";
        assertEquals(expectedOutput, c.getOutputMessage());
    }

    @Test
    void deleteExpense_invalidExpense_expectExpenseNotFound() throws FinanceException {
        ExpenseList expenseList = getListWith3Expenses();
        List<Income> incomes = new ArrayList<>();
        Command c = new DeleteExpenseCommand("delete-expense Dinner");
        try {
            c.execute(incomes, expenseList);
        } catch (FinanceException e) {
            String expectedError = "Expense not found: Dinner";
            assertEquals(expectedError, e.getMessage());
        }
    }
}
