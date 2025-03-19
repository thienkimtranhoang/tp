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

class AddIncomeCommandTest {
    @Test
    void addIncome_validInput_addsIncome() throws FinanceException {
        ExpenseList expenseList = new ExpenseList();
        List<Income> incomes = new ArrayList<>();
        Command c = new AddIncomeCommand("add category/Salary amt/2500.00 d/2025-03-15");
        c.execute(incomes, expenseList);
        String expectedOutput = "Income added: Salary, Amount: $2500.00, Date: 2025-03-15";
        assertEquals(expectedOutput, c.getOutputMessage());
    }

    @Test
    void addIncome_missingCategory_showsError() {
        ExpenseList expenseList = new ExpenseList();
        List<Income> incomes = new ArrayList<>();
        Command c = new AddIncomeCommand("add amt/2500.00 d/2025-03-15");
        try {
            c.execute(incomes, expenseList);
        } catch (FinanceException e) {
            String expectedError = "Error: Income category is required.";
            assertEquals(expectedError, e.getMessage());
        }
    }

    @Test
    void addIncome_missingAmount_showsError() {
        ExpenseList expenseList = new ExpenseList();
        List<Income> incomes = new ArrayList<>();
        Command c = new AddIncomeCommand("add category/Salary d/2025-03-15");
        try {
            c.execute(incomes, expenseList);
        } catch (FinanceException e) {
            String expectedError = "Error: Income amount is required.";
            assertEquals(expectedError, e.getMessage());
        }
    }

    @Test
    void addIncome_missingDate_showsError() {
        ExpenseList expenseList = new ExpenseList();
        List<Income> incomes = new ArrayList<>();
        Command c = new AddIncomeCommand("add category/Salary amt/2500.00");
        try {
            c.execute(incomes, expenseList);
        } catch (FinanceException e) {
            String expectedError = "Error: Income date is required.";
            assertEquals(expectedError, e.getMessage());
        }
    }

    @Test
    void addIncome_invalidAmountFormat_showsError() {
        ExpenseList expenseList = new ExpenseList();
        List<Income> incomes = new ArrayList<>();
        Command c = new AddIncomeCommand("add category/Salary amt/invalid d/2025-03-15");
        try {
            c.execute(incomes, expenseList);
        } catch (FinanceException e) {
            String expectedError = "Error: Income amount is required.";
            assertEquals(expectedError, e.getMessage());
        }
    }

}
