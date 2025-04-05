package budgetflow.command;

import budgetflow.exception.FinanceException;
import budgetflow.expense.Expense;
import budgetflow.expense.ExpenseList;
import budgetflow.income.Income;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class DeleteExpenseCommandTest {
    //@@author Yikbing
    private static ExpenseList getListWith3Expenses() {
        ExpenseList expenseList = new ExpenseList();
        expenseList.add(new Expense("food", "Lunch", 12.50, "13-03-2025"));
        expenseList.add(new Expense("transport", "Transport", 3.20, "12-03-2025"));
        expenseList.add(new Expense("food", "Groceries", 25.0, "11-03-2025"));
        return expenseList;
    }

    //@@author Yikbing
    @Test
    void deleteExpense_validIndex_expenseDeleted() throws FinanceException {
        ExpenseList expenseList = getListWith3Expenses();
        List<Income> incomes = new ArrayList<>();
        Command command = new DeleteExpenseCommand("delete-expense 1");
        command.execute(incomes, expenseList);
        assertEquals("Expense deleted: Lunch, $12.5", command.getOutputMessage());
    }

    //@@author Yikbing
    @Test
    void deleteExpense_invalidNumericIndex_throwsException() throws FinanceException {
        ExpenseList expenseList = getListWith3Expenses();
        List<Income> incomes = new ArrayList<>();
        Command command = new DeleteExpenseCommand("delete-expense 4");
        try{
            command.execute(incomes, expenseList);
            fail();
        } catch (FinanceException e) {
            String expectedError = "Error: Invalid expense index.";
            assertEquals(expectedError, e.getMessage());
        }
    }

    //@@author Yikbing
    @Test
    void deleteExpense_invalidNonNumericIndex_throwsException() throws FinanceException {
        ExpenseList expenseList = getListWith3Expenses();
        List<Income> incomes = new ArrayList<>();
        Command command = new DeleteExpenseCommand("delete-expense a");
        try{
            command.execute(incomes, expenseList);
            fail();
        } catch (FinanceException e) {
            String expectedError = "Error: Please enter a valid numeric index.";
            assertEquals(expectedError, e.getMessage());
        }
    }

    //@@author Yikbing
    @Test
    void deleteExpense_noIndex_throwsException() throws FinanceException {
        ExpenseList expenseList = getListWith3Expenses();
        List<Income> incomes = new ArrayList<>();
        Command command = new DeleteExpenseCommand("delete-expense");
        try{
            command.execute(incomes, expenseList);
            fail();
        } catch (FinanceException e) {
            String expectedError = "Error: Expense Index is required.";
            assertEquals(expectedError, e.getMessage());
        }
    }


}
