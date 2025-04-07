package budgetflow.command;

import budgetflow.exception.FinanceException;
import budgetflow.expense.Expense;
import budgetflow.expense.ExpenseList;
import budgetflow.income.Income;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


class ViewAllExpensesCommandTest {
    //@@author QuyDatNguyen
    @Test
    void viewAllExpenses_normalTest_correctMessage() throws FinanceException {
        ExpenseList expenseList = new ExpenseList();
        List<Income> incomes = new ArrayList<>();
        expenseList.add(new Expense("food", "Lunch", 12.50, "13-03-2025"));
        expenseList.add(new Expense("transport", "Transport", 3.20, "12-03-2025"));
        expenseList.add(new Expense("food", "Groceries", 25.0, "11-03-2025"));
        Command command = new ViewAllExpensesCommand();
        command.execute(incomes, expenseList);
        String expectedOutput = "Expenses log:" + System.lineSeparator()
                + "1 | food | Lunch | $12.50 | 13-03-2025" + System.lineSeparator()
                + "2 | transport | Transport | $3.20 | 12-03-2025" + System.lineSeparator()
                + "3 | food | Groceries | $25.00 | 11-03-2025" + System.lineSeparator()
                + "Total Expenses: $40.70" + System.lineSeparator();
        assertEquals(expectedOutput, command.getOutputMessage());
        assertEquals(40.70, expenseList.getTotalExpenses(), 0.01);
    }
    //Something wrong here
    @Test
    void viewAllExpenses_normalTest_correctSum() throws FinanceException {
        ExpenseList expenseList = new ExpenseList();
        List<Income> incomes = new ArrayList<>();
        expenseList.add(new Expense("food", "Lunch", 12.50, "13-03-2025"));
        expenseList.add(new Expense("transport", "Transport", 3.20, "12-03-2025"));
        expenseList.add(new Expense("food", "Groceries", 25.0, "11-03-2025"));
        Command command = new ViewAllExpensesCommand();
        command.execute(incomes, expenseList);
        String expectedOutput = "Expenses log:" + System.lineSeparator()
                + "1 | food | Lunch | $12.50 | 13-03-2025" + System.lineSeparator()
                + "2 | transport | Transport | $3.20 | 12-03-2025" + System.lineSeparator()
                + "3 | food | Groceries | $25.00 | 11-03-2025" + System.lineSeparator()
                + "Total Expenses: $40.70" + System.lineSeparator();
        assertEquals(40.70, expenseList.getTotalExpenses(), 0.01);
    }

    @Test
    void viewAllExpenses_emptyList() throws FinanceException {
        ExpenseList expenseList = new ExpenseList();
        List<Income> incomes = new ArrayList<>();
        Command command = new ViewAllExpensesCommand();
        command.execute(incomes, expenseList);
        String expectedOutput = "No expenses have been logged yet." + System.lineSeparator();
        assertEquals(expectedOutput, command.getOutputMessage());
    }

    //@@author thienkimtranhoang
    @Test
    void viewAllExpenses_extremeValues_correctSum() throws FinanceException {
        ExpenseList expenseList = new ExpenseList();
        List<Income> incomes = new ArrayList<>();
        expenseList.add(new Expense("luxury", "Car", 1000000.99, "01-01-2025"));
        expenseList.add(new Expense("misc", "Pencil", 0.01, "02-01-2025"));
        Command command = new ViewAllExpensesCommand();
        command.execute(incomes, expenseList);
        String expectedOutput = "Expenses log:" + System.lineSeparator()
                + "1 | luxury | Car | $1000000.99 | 01-01-2025" + System.lineSeparator()
                + "2 | misc | Pencil | $0.01 | 02-01-2025" + System.lineSeparator()
                + "Total Expenses: $1000001.00" + System.lineSeparator();
        assertEquals(expectedOutput, command.getOutputMessage());
        assertEquals(1000001.00, expenseList.getTotalExpenses(), 0.01);
    }

    //@@author thienkimtranhoang
    @Test
    void viewAllExpenses_longDescription_loggedCorrectly() throws FinanceException {
        ExpenseList expenseList = new ExpenseList();
        List<Income> incomes = new ArrayList<>();
        String longDesc = "Bought lots of supplies from the supermarket including rice, " +
                "noodles, milk, bread, and snacks.";
        expenseList.add(new Expense("grocery", longDesc, 78.45, "15-03-2025"));
        Command command = new ViewAllExpensesCommand();
        command.execute(incomes, expenseList);
        String expectedOutput = "Expenses log:" + System.lineSeparator()
                + "1 | grocery | " + longDesc + " | $78.45 | 15-03-2025" + System.lineSeparator()
                + "Total Expenses: $78.45" + System.lineSeparator();
        assertEquals(expectedOutput, command.getOutputMessage());
        assertEquals(78.45, expenseList.getTotalExpenses(), 0.01);
    }

    //@@author thienkimtranhoang
    @Test
    void viewAllExpenses_duplicateEntries_allLoggedCorrectly() throws FinanceException {
        ExpenseList expenseList = new ExpenseList();
        List<Income> incomes = new ArrayList<>();
        expenseList.add(new Expense("food", "Coffee", 5.00, "10-03-2025"));
        expenseList.add(new Expense("food", "Coffee", 5.00, "10-03-2025"));
        Command command = new ViewAllExpensesCommand();
        command.execute(incomes, expenseList);
        String expectedOutput = "Expenses log:" + System.lineSeparator()
                + "1 | food | Coffee | $5.00 | 10-03-2025" + System.lineSeparator()
                + "2 | food | Coffee | $5.00 | 10-03-2025" + System.lineSeparator()
                + "Total Expenses: $10.00" + System.lineSeparator();
        assertEquals(expectedOutput, command.getOutputMessage());
        assertEquals(10.00, expenseList.getTotalExpenses(), 0.01);
    }

}
