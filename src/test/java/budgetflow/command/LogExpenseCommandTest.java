package budgetflow.command;

import budgetflow.exception.FinanceException;
import budgetflow.expense.ExpenseList;
import budgetflow.income.Income;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;


class LogExpenseCommandTest {
    @Test
    void logExpense_validInput_logsExpense() throws FinanceException {
        ExpenseList expenseList = new ExpenseList();
        List<Income> incomes = new ArrayList<>();
        Command c = new LogExpenseCommand(
                "log-expense category/Dining desc/DinnerWithFriends amt/45.75 d/15-03-2025");
        c.execute(incomes, expenseList);
        String expectedOutput = "Expense logged: Dining | DinnerWithFriends | $45.75 | 15-03-2025";
        assertEquals(expectedOutput, c.getOutputMessage());
    }

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

    @Test
    void logExpense_missingCategory_showsError() {
        ExpenseList expenseList = new ExpenseList();
        List<Income> incomes = new ArrayList<>();
        Command c = new LogExpenseCommand(
                "log-expense desc/DinnerWithFriends amt/45.75 d/15-03-2025");
        try {
            c.execute(incomes, expenseList);
            fail();
        } catch (FinanceException e) {
            String expectedError = "Error: Expense category is required.";
            assertEquals(expectedError, e.getMessage());
        }
    }

    @Test
    void logExpense_missingDescription_showsError() {
        ExpenseList expenseList = new ExpenseList();
        List<Income> incomes = new ArrayList<>();
        Command c = new LogExpenseCommand(
                "log-expense category/Dining amt/45.75 d/15-03-2025");
        try {
            c.execute(incomes, expenseList);
            fail();
        } catch (FinanceException e) {
            String expectedError = "Error: Expense description is required.";
            assertEquals(expectedError, e.getMessage());
        }
    }

    @Test
    void logExpense_missingAmount_showsError() {
        ExpenseList expenseList = new ExpenseList();
        List<Income> incomes = new ArrayList<>();
        Command c = new LogExpenseCommand(
                "log-expense category/Dining desc/DinnerWithFriends d/15-03-2025");
        try {
            c.execute(incomes, expenseList);
            fail();
        } catch (FinanceException e) {
            String expectedError = "Error: Expense amount is required.";
            assertEquals(expectedError, e.getMessage());
        }
    }

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

    @Test
    void logExpense_invalidAmountFormat_showsError() {
        ExpenseList expenseList = new ExpenseList();
        List<Income> incomes = new ArrayList<>();
        Command c = new LogExpenseCommand(
                "log-expense category/Dining desc/DinnerWithFriends amt/invalid d/15-03-2025");
        try {
            c.execute(incomes, expenseList);
            fail();
        } catch (FinanceException e) {
            String expectedError = "Error: Expense amount is required.";
            assertEquals(expectedError, e.getMessage());
        }
    }

    @Test
    void logExpense_invalidDateFormat_showsError() {
        ExpenseList expenseList = new ExpenseList();
        List<Income> incomes = new ArrayList<>();
        Command c = new LogExpenseCommand("log-expense category/Dining desc/DinnerWithFriends amt/45.75 d/2025-03-15");
        try {
            c.execute(incomes, expenseList);
        } catch (FinanceException e) {
            String expectedError = "Error: Income date is in wrong format." +
                    "please use DD-MM-YYYY format.";
            assertEquals(expectedError, e.getMessage());
        }
    }

    @Test
    void logExpense_invalidDate_showsError() {
        ExpenseList expenseList = new ExpenseList();
        List<Income> incomes = new ArrayList<>();
        Command c = new LogExpenseCommand("log-expense category/Dining desc/DinnerWithFriends amt/45.75 d/99-99-1234");
        try {
            c.execute(incomes, expenseList);
        } catch (FinanceException e) {
            String expectedError = "Error: Date is not a valid date";
            assertEquals(expectedError, e.getMessage());
        }
    }

}
