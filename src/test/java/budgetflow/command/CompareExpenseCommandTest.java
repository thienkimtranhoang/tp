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
}
