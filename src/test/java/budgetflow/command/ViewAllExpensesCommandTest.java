package budgetflow.command;

import budgetflow.exception.FinanceException;
import budgetflow.expense.Expense;
import budgetflow.expense.ExpenseList;
import budgetflow.income.Income;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

//@@author QuyDatNguyen
class ViewAllExpensesCommandTest {
    @Test
    void viewAllExpenses_normalTest_correctMessage() throws FinanceException {
        ExpenseList expenseList = new ExpenseList();
        List<Income> incomes = new ArrayList<>();
        expenseList.add(new Expense("food", "Lunch", 12.50, "2025-03-13"));
        expenseList.add(new Expense("transport", "Transport", 3.20, "2025-03-12"));
        expenseList.add(new Expense("food", "Groceries", 25.0, "2025-03-11"));
        Command c = new ViewAllExpensesCommand();
        c.execute(incomes, expenseList);
        String expectedOutput = "Expenses log:" + System.lineSeparator()
                + "1 | food | Lunch | $12.50 | 2025-03-13" + System.lineSeparator()
                + "2 | transport | Transport | $3.20 | 2025-03-12" + System.lineSeparator()
                + "3 | food | Groceries | $25.00 | 2025-03-11" + System.lineSeparator()
                + "Total Expenses: $40.70" + System.lineSeparator();
        assertEquals(expectedOutput, c.getOutputMessage());
        assertEquals(40.70, expenseList.getTotalExpenses(), 0.01);
    }

    @Test
    void viewAllExpenses_normalTest_correctSum() throws FinanceException {
        ExpenseList expenseList = new ExpenseList();
        List<Income> incomes = new ArrayList<>();
        expenseList.add(new Expense("food", "Lunch", 12.50, "2025-03-13"));
        expenseList.add(new Expense("transport", "Transport", 3.20, "2025-03-12"));
        expenseList.add(new Expense("food", "Groceries", 25.0, "2025-03-11"));
        Command c = new ViewAllExpensesCommand();
        c.execute(incomes, expenseList);
        String expectedOutput = "Expenses log:" + System.lineSeparator()
                + "1 | food | Lunch | $12.50 | 2025-03-13" + System.lineSeparator()
                + "2 | transport | Transport | $3.20 | 2025-03-12" + System.lineSeparator()
                + "3 | food | Groceries | $25.00 | 2025-03-11" + System.lineSeparator()
                + "Total Expenses: $40.70" + System.lineSeparator();
        assertEquals(40.70, expenseList.getTotalExpenses(), 0.01);
    }

    @Test
    void viewAllExpenses_emptyList() throws FinanceException {
        ExpenseList expenseList = new ExpenseList();
        List<Income> incomes = new ArrayList<>();
        Command c = new ViewAllExpensesCommand();
        c.execute(incomes, expenseList);
        String expectedOutput = "No expenses have been logged yet." + System.lineSeparator();
        assertEquals(expectedOutput, c.getOutputMessage());
    }
}
