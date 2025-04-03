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

//@@author Yikbing
class DeleteExpenseCommandTest {
    private static ExpenseList getListWith3Expenses() {
        ExpenseList expenseList = new ExpenseList();
        expenseList.add(new Expense("food", "Lunch", 12.50, "13-03-2025"));
        expenseList.add(new Expense("transport", "Transport", 3.20, "12-03-2025"));
        expenseList.add(new Expense("food", "Groceries", 25.0, "11-03-2025"));
        return expenseList;
    }

    //@@author Yikbing
    @Test
    void deleteExpense_validExpense_expectExpenseFound() throws FinanceException {
        ExpenseList expenseList = getListWith3Expenses();
        List<Income> incomes = new ArrayList<>();
        Command command = new DeleteExpenseCommand("delete-expense Lunch");
        command.execute(incomes, expenseList);
        String expectedOutput = "Expense deleted: Lunch";
        assertEquals(expectedOutput, command.getOutputMessage());
    }

    //@@author Yikbing
    @Test
    void deleteExpense_invalidExpense_expectExpenseNotFound() throws FinanceException {
        ExpenseList expenseList = getListWith3Expenses();
        List<Income> incomes = new ArrayList<>();
        Command command = new DeleteExpenseCommand("delete-expense Dinner");
        try {
            command.execute(incomes, expenseList);
            fail();
        } catch (FinanceException e) {
            String expectedError = "Expense not found: Dinner";
            assertEquals(expectedError, e.getMessage());
        }
    }
}
