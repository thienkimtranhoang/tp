package budgetflow.command;

import budgetflow.exception.FinanceException;
import budgetflow.expense.ExpenseList;
import budgetflow.income.Income;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

//@@author thienkimtranhoang
class AddIncomeCommandTest {
    @Test
    void addIncome_validInput_addsIncome() throws FinanceException {
        ExpenseList expenseList = new ExpenseList();
        List<Income> incomes = new ArrayList<>();
        Command command = new AddIncomeCommand("add category/Salary amt/2500.00 d/15-03-2025");
        command.execute(incomes, expenseList);
        String expectedOutput = "Income added: Salary, Amount: $2500.00, Date: 15-03-2025";
        assertEquals(expectedOutput, command.getOutputMessage());
    }

    @Test
    void addIncome_missingCategory_showsError() {
        ExpenseList expenseList = new ExpenseList();
        List<Income> incomes = new ArrayList<>();
        Command command = new AddIncomeCommand("add amt/2500.00 d/15-03-2025");
        try {
            command.execute(incomes, expenseList);
            fail();
        } catch (FinanceException e) {
            String expectedError = "Error: Income category is required.";
            assertEquals(expectedError, e.getMessage());
        }
    }

    @Test
    void addIncome_missingAmount_showsError() {
        ExpenseList expenseList = new ExpenseList();
        List<Income> incomes = new ArrayList<>();
        Command command = new AddIncomeCommand("add category/Salary d/15-03-2025");
        try {
            command.execute(incomes, expenseList);
        } catch (FinanceException e) {
            String expectedError = "Error: Income amount is required.";
            assertEquals(expectedError, e.getMessage());
        }
    }

    @Test
    void addIncome_missingDate_showsError() {
        ExpenseList expenseList = new ExpenseList();
        List<Income> incomes = new ArrayList<>();
        Command command = new AddIncomeCommand("add category/Salary amt/2500.00");
        try {
            command.execute(incomes, expenseList);
        } catch (FinanceException e) {
            String expectedError = "Error: Income date is required.";
            assertEquals(expectedError, e.getMessage());
        }
    }

    @Test
    void addIncome_invalidAmountFormat_showsError() {
        ExpenseList expenseList = new ExpenseList();
        List<Income> incomes = new ArrayList<>();
        Command command = new AddIncomeCommand("add category/Salary amt/invalid d/15-03-2025");
        try {
            command.execute(incomes, expenseList);
        } catch (FinanceException e) {
            String expectedError = "Error: Income amount is required.";
            assertEquals(expectedError, e.getMessage());
        }
    }
    
    @Test
    void addIncome_extraParameters_ignoresExtraParams() throws FinanceException {
        ExpenseList expenseList = new ExpenseList();
        List<Income> incomes = new ArrayList<>();
        Command command = new AddIncomeCommand("add category/Salary amt/2500.00 d/15-03-2025 extra/parameter");
        command.execute(incomes, expenseList);
        String expectedOutput = "Income added: Salary, Amount: $2500.00, Date: 15-03-2025";
        assertEquals(expectedOutput, command.getOutputMessage());
    }

    @Test
    void addIncome_invalidDateFormat_showsError() {
        ExpenseList expenseList = new ExpenseList();
        List<Income> incomes = new ArrayList<>();
        Command command = new AddIncomeCommand("add category/Salary amt/5000 d/2025-03-15");
        try {
            command.execute(incomes, expenseList);
        } catch (FinanceException e) {
            String expectedError = "Error: Income date is in wrong format." +
                    "please use DD-MM-YYYY format.";
            assertEquals(expectedError, e.getMessage());
        }
    }

    @Test
    void addIncome_invalidDate_showsError() {
        ExpenseList expenseList = new ExpenseList();
        List<Income> incomes = new ArrayList<>();
        Command command = new AddIncomeCommand("add category/Salary amt/5000 d/99-99-1234");
        try {
            command.execute(incomes, expenseList);
        } catch (FinanceException e) {
            String expectedError = "Error: Date is not a valid date. Please use DD-MM-YYYY format.";
            assertEquals(expectedError, e.getMessage());
        }
    }

}
