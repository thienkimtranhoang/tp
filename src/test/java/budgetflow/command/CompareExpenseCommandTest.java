package budgetflow.command;

import budgetflow.exception.FinanceException;
import budgetflow.expense.ExpenseList;
import budgetflow.income.Income;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class CompareExpenseCommandTest {
    // Error here
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

    //@@author dariusyawningwhiz
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

    //@@author dariusyawningwhiz
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

    //@@author dariusyawningwhiz
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

    //@@author dariusyawningwhiz
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

    //@@author dariusyawningwhiz
    @Test
    public void testCompareExpenseCommand_expensesWithCents() throws FinanceException {
        ExpenseList expenseList = new ExpenseList();
        List<Income> incomes = new ArrayList<>();
        Command c1 = new LogExpenseCommand("log-expense category/Drinks desc/Coffee amt/5.75 d/02-06-2025");
        Command c2 = new LogExpenseCommand("log-expense category/Snacks desc/Cake amt/4.50 d/10-07-2025");
        c1.execute(incomes, expenseList);
        c2.execute(incomes, expenseList);

        Command compare = new CompareExpenseCommand("compare 06-2025 07-2025");
        compare.execute(incomes, expenseList);
        String expectedOutput = "Total expenses for 06-2025: $5.75" + System.lineSeparator() +
                "Total expenses for 07-2025: $4.50" + System.lineSeparator();
        assertEquals(expectedOutput, compare.getOutputMessage());
    }

    //@@author dariusyawningwhiz
    @Test
    public void testCompareExpenseCommand_nonSequentialMonths() throws FinanceException {
        ExpenseList expenseList = new ExpenseList();
        List<Income> incomes = new ArrayList<>();
        Command c1 = new LogExpenseCommand("log-expense category/Food desc/Dinner amt/80 d/10-02-2025");
        Command c2 = new LogExpenseCommand("log-expense category/Rent desc/Lease amt/2000 d/12-12-2025");
        c1.execute(incomes, expenseList);
        c2.execute(incomes, expenseList);

        Command compare = new CompareExpenseCommand("compare 02-2025 12-2025");
        compare.execute(incomes, expenseList);
        String expectedOutput = "Total expenses for 02-2025: $80.00" + System.lineSeparator() +
                "Total expenses for 12-2025: $2000.00" + System.lineSeparator();
        assertEquals(expectedOutput, compare.getOutputMessage());
    }

    //@@author dariusyawningwhiz
    @Test
    public void testCompareExpenseCommand_sameMonthComparison() throws FinanceException {
        ExpenseList expenseList = new ExpenseList();
        List<Income> incomes = new ArrayList<>();
        Command c1 = new LogExpenseCommand("log-expense category/Entertainment desc/Movie amt/15 d/10-05-2025");
        c1.execute(incomes, expenseList);

        Command compare = new CompareExpenseCommand("compare 05-2025 05-2025");
        compare.execute(incomes, expenseList);
        String expectedOutput = "Total expenses for 05-2025: $15.00" + System.lineSeparator() +
                "Total expenses for 05-2025: $15.00" + System.lineSeparator();
        assertEquals(expectedOutput, compare.getOutputMessage());
    }
}
