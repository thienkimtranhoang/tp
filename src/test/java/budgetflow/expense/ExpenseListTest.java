package budgetflow.expense;

import budgetflow.exception.FinanceException;
import budgetflow.exception.InvalidDateException;
import budgetflow.exception.InvalidNumberFormatException;
import budgetflow.exception.InvalidTagException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class ExpenseListTest {

    private static ExpenseList getListWith5Expenses() {
        ExpenseList expenseList = new ExpenseList();
        expenseList.add(new Expense("food", "Lunch", 12.50, "13-03-2025"));
        expenseList.add(new Expense("transport", "Transport", 3.20, "12-03-2025"));
        expenseList.add(new Expense("food", "LateLunch", 13.50, "14-03-2025"));
        expenseList.add(new Expense("food", "Groceries", 25.0, "11-03-2025"));
        expenseList.add(new Expense("food", "ExpensiveLunch", 30.00, "15-03-2025"));
        return expenseList;
    }

    @Test
    void getByTag_normalTest() throws InvalidTagException,
            InvalidNumberFormatException, InvalidDateException {
        ExpenseList testList = getListWith5Expenses();
        String tag = "/desc";
        String keyword = "Lunch";
        ExpenseList matchingExpenses = testList.getByTag(tag, keyword);
        ExpenseList expectedMatch = new ExpenseList();
        expectedMatch.add(new Expense("food", "Lunch", 12.50, "13-03-2025"));
        expectedMatch.add(new Expense("food", "LateLunch", 13.50, "14-03-2025"));
        expectedMatch.add(new Expense("food", "ExpensiveLunch", 30.00, "15-03-2025"));
        assertEquals(expectedMatch.toString(), matchingExpenses.toString());
    }
    @Test
    void getByTag_invalidTagTest() {
        ExpenseList testList = getListWith5Expenses();
        String tag = "/de";
        String keyword = "Lunch";
        try {
            ExpenseList matchingExpenses = testList.getByTag(tag, keyword);
            fail();
        } catch (FinanceException e) {
            String expectedError = "Please enter valid tag: /desc | /amt| /d| /category";
            assertEquals(expectedError, e.getMessage());
        }
    }
    @Test
    void getByTag_invalidNumberFormatTest() {
        ExpenseList testList = getListWith5Expenses();
        String tag = "/amt";
        String keyword = "foo ";
        try {
            ExpenseList matchingExpenses = testList.getByTag(tag, keyword);
            fail();
        } catch (FinanceException e) {
            String expectedError = "Please enter valid float number after /amt";
            assertEquals(expectedError, e.getMessage());
        }
    }
    @Test
    void getByTag_invalidDateTest() {
        ExpenseList testList = getListWith5Expenses();
        String tag = "/d";
        String keyword = "March 17";
        try {
            ExpenseList matchingExpenses = testList.getByTag(tag, keyword);
            fail();
        } catch (FinanceException e) {
            String expectedError = "Please enter valid date format: dd-MM-yyyy";
            assertEquals(expectedError, e.getMessage());
        }
    }
}
