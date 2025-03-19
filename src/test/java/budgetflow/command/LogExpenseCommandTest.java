package budgetflow.command;

import budgetflow.FinanceTracker;
import budgetflow.exception.FinanceException;
import budgetflow.expense.ExpenseList;
import budgetflow.income.Income;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class LogExpenseCommandTest {
    @Test
    void logExpense_validInput_logsExpense() throws FinanceException {
        ExpenseList expenseList = new ExpenseList();
        List<Income> incomes = new ArrayList<>();
        Command c = new LogExpenseCommand("log-expense category/Dining desc/DinnerWithFriends amt/45.75 d/2025-03-15");
        c.execute(incomes, expenseList);
        String expectedOutput = "Expense logged: Dining | DinnerWithFriends | $45.75 | 2025-03-15";
        assertEquals(expectedOutput, c.getOutputMessage());
    }

    @Test
    void logExpense_emptyInputTest() {
        ExpenseList expenseList = new ExpenseList();
        List<Income> incomes = new ArrayList<>();
        Command c = new LogExpenseCommand("log-expense ");
        try {
            c.execute(incomes, expenseList);
        } catch (FinanceException e) {
            String expectedError = "Expense should not be empty";
            assertEquals(expectedError, e.getMessage());
        }
    }

    @Test
    void logExpense_missingCategory_showsError() {
        ExpenseList expenseList = new ExpenseList();
        List<Income> incomes = new ArrayList<>();
        Command c = new LogExpenseCommand("log-expense desc/DinnerWithFriends amt/45.75 d/2025-03-15");
        try {
            c.execute(incomes, expenseList);
        } catch (FinanceException e) {
            String expectedError = "Error: Expense category is required.";
            assertEquals(expectedError, e.getMessage());
        }
    }

    @Test
    void logExpense_missingDescription_showsError() {
        ExpenseList expenseList = new ExpenseList();
        List<Income> incomes = new ArrayList<>();
        Command c = new LogExpenseCommand("log-expense category/Dining amt/45.75 d/2025-03-15");
        try {
            c.execute(incomes, expenseList);
        } catch (FinanceException e) {
            String expectedError = "Error: Expense description is required.";
            assertEquals(expectedError, e.getMessage());
        }
    }

    @Test
    void logExpense_missingAmount_showsError() {
        ExpenseList expenseList = new ExpenseList();
        List<Income> incomes = new ArrayList<>();
        Command c = new LogExpenseCommand("log-expense category/Dining desc/DinnerWithFriends d/2025-03-15");
        try {
            c.execute(incomes, expenseList);
        } catch (FinanceException e) {
            String expectedError = "Error: Expense amount is required.";
            assertEquals(expectedError, e.getMessage());
        }
    }

    @Test
    void logExpense_missingDate_showsError() {
        ExpenseList expenseList = new ExpenseList();
        List<Income> incomes = new ArrayList<>();
        Command c = new LogExpenseCommand("log-expense category/Dining desc/DinnerWithFriends amt/45.75");
        try {
            c.execute(incomes, expenseList);
        } catch (FinanceException e) {
            String expectedError = "Error: Expense date is required.";
            assertEquals(expectedError, e.getMessage());
        }
    }

    @Test
    void logExpense_invalidAmountFormat_showsError() {
        ExpenseList expenseList = new ExpenseList();
        List<Income> incomes = new ArrayList<>();
        Command c = new LogExpenseCommand("log-expense category/Dining desc/DinnerWithFriends amt/invalid d/2025-03-15");
        try {
            c.execute(incomes, expenseList);
        } catch (FinanceException e) {
            String expectedError = "Error: Expense amount is required.";
            assertEquals(expectedError, e.getMessage());
        }
    }

}
