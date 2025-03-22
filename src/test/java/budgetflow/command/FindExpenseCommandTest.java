package budgetflow.command;

import budgetflow.exception.FinanceException;
import budgetflow.exception.UnfoundExpenseException;
import budgetflow.expense.Expense;
import budgetflow.expense.ExpenseList;
import budgetflow.income.Income;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class FindExpenseCommandTest {

    private static ExpenseList getListWith5Expenses() {
        ExpenseList expenseList = new ExpenseList();
        expenseList.add(new Expense("food", "Lunch", 12.50, "13-03-2025"));
        expenseList.add(new Expense("transport", "Transport", 3.20, "12-03-2025"));
        expenseList.add(new Expense("food", "LateLunch", 13.50, "14-03-2025"));
        expenseList.add(new Expense("food", "Groceries", 25.0, "11-03-2025"));
        expenseList.add(new Expense("food", "ExpensiveLunch", 30.00, "15-03-2025"));
        return expenseList;
    }

    private static ExpenseList getListWith3Expenses() {
        ExpenseList expenseList = new ExpenseList();
        expenseList.add(new Expense("food", "Lunch", 12.50, "13-03-2025"));
        expenseList.add(new Expense("transport", "Transport", 3.20, "12-03-2025"));
        expenseList.add(new Expense("food", "Groceries", 25.0, "11-03-2025"));
        return expenseList;
    }

    @Test
    void findExpense_found1ExpenseTest() throws FinanceException {
        List<Income> incomes = new ArrayList<>();
        ExpenseList expenseList = getListWith3Expenses();
        Command c = new FindExpenseCommand("find-expense Lunch");
        c.execute(incomes, expenseList);
        String expectedOutput = "Here are all matching expenses: " + System.lineSeparator()
                + "food | Lunch | $12.50 | 13-03-2025" + System.lineSeparator();
        assertEquals(expectedOutput, c.getOutputMessage());
    }

    @Test
    void findExpense_foundMultipleExpenseTest() throws FinanceException {
        List<Income> incomes = new ArrayList<>();
        ExpenseList expenseList = getListWith5Expenses();
        Command c = new FindExpenseCommand("find-expense Lunch");
        c.execute(incomes, expenseList);
        String expectedOutput = "Here are all matching expenses: " + System.lineSeparator()
                + "food | Lunch | $12.50 | 13-03-2025" + System.lineSeparator()
                + "food | LateLunch | $13.50 | 14-03-2025" + System.lineSeparator()
                + "food | ExpensiveLunch | $30.00 | 15-03-2025" + System.lineSeparator();
        assertEquals(expectedOutput, c.getOutputMessage());
    }

    @Test

    void findExpense_partialMatch() throws FinanceException {
        List<Income> incomes = new ArrayList<>();
        ExpenseList expenseList = getListWith3Expenses();
        Command c = new FindExpenseCommand("find-expense Groc");
        c.execute(incomes, expenseList);
        String expectedOutput = "Here are all matching expenses: " + System.lineSeparator() +
                "food | Groceries | $25.00 | 11-03-2025" + System.lineSeparator();
        assertEquals(expectedOutput, c.getOutputMessage());
    }

    @Test
    void findExpense_foundNoExpenseTest() throws UnfoundExpenseException {
        ExpenseList expenseList = new ExpenseList();
        List<Income> incomes = new ArrayList<>();
        try {
            Command c = new FindExpenseCommand("find-expense IAmDummy");
            c.execute(incomes, expenseList);
            fail();
        } catch (FinanceException e) {
            String expectedError = "Sorry, I cannot find any expenses matching your keyword: IAmDummy";
            assertEquals(expectedError, e.getMessage());
        }
    }

    @Test
    void findExpense_noKeywordsTest() {
        ExpenseList expenseList = getListWith3Expenses();
        List<Income> incomes = new ArrayList<>();
        try {
            Command c = new FindExpenseCommand("find-expense");
            c.execute(incomes, expenseList);
        } catch (FinanceException e) {
            String expectedError = "Error: Missing keyword";
            assertEquals(expectedError, e.getMessage());
        }
    }
}
