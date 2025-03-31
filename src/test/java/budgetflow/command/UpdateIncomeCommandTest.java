package budgetflow.command;

import budgetflow.exception.FinanceException;
import budgetflow.expense.ExpenseList;
import budgetflow.income.Income;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class UpdateIncomeCommandTest {

    @Test
    public void updateIncome_validInputCategory_updatesIncome() throws FinanceException {
        ExpenseList expenseList = new ExpenseList();
        List<Income> incomes = new ArrayList<>();
        Command c1 = new AddIncomeCommand("add category/Salary amt/2500.00 d/15-03-2025");
        c1.execute(incomes, expenseList);
        Command c2 = new UpdateIncomeCommand("update-income 1 category/Salary1");
        c2.execute(incomes, expenseList);
        String expectedOutput = "Income updated: Salary1, Amount: $2500.00, Date: 15-03-2025";
        assertEquals(expectedOutput, c2.getOutputMessage());

    }

    @Test
    public void updateIncome_validInputAmt_updatesIncome() throws FinanceException {
        ExpenseList expenseList = new ExpenseList();
        List<Income> incomes = new ArrayList<>();
        Command c1 = new AddIncomeCommand("add category/Salary amt/2500.00 d/15-03-2025");
        c1.execute(incomes, expenseList);
        Command c2 = new UpdateIncomeCommand("update-income 1 amt/5000");
        c2.execute(incomes, expenseList);
        String expectedOutput = "Income updated: Salary, Amount: $5000.00, Date: 15-03-2025";
        assertEquals(expectedOutput, c2.getOutputMessage());

    }

    @Test
    public void updateIncome_validInputDate_updatesIncome() throws FinanceException {
        ExpenseList expenseList = new ExpenseList();
        List<Income> incomes = new ArrayList<>();
        Command c1 = new AddIncomeCommand("add category/Salary amt/2500.00 d/15-03-2025");
        c1.execute(incomes, expenseList);
        Command c2 = new UpdateIncomeCommand("update-income 1 d/28-02-2025");
        c2.execute(incomes, expenseList);
        String expectedOutput = "Income updated: Salary, Amount: $2500.00, Date: 28-02-2025";
        assertEquals(expectedOutput, c2.getOutputMessage());

    }

    @Test
    public void updateIncome_allValidInputs_updatesIncome() throws FinanceException {
        ExpenseList expenseList = new ExpenseList();
        List<Income> incomes = new ArrayList<>();
        Command c1 = new AddIncomeCommand("add category/Salary amt/2500.00 d/15-03-2025");
        c1.execute(incomes, expenseList);
        Command c2 = new UpdateIncomeCommand("update-income 1 category/Salary1 amt/5000 d/28-02-2025");
        c2.execute(incomes, expenseList);
        String expectedOutput = "Income updated: Salary1, Amount: $5000.00, Date: 28-02-2025";
        assertEquals(expectedOutput, c2.getOutputMessage());

    }

    @Test
    public void updateIncome_inValidInputCategory_noChangeToIncome() throws FinanceException {
        ExpenseList expenseList = new ExpenseList();
        List<Income> incomes = new ArrayList<>();
        Command c1 = new AddIncomeCommand("add category/Salary amt/2500.00 d/15-03-2025");
        c1.execute(incomes, expenseList);
        Command c2 = new UpdateIncomeCommand("update-income 1 category/");
        c2.execute(incomes, expenseList);
        String expectedOutput = "Income updated: Salary, Amount: $2500.00, Date: 15-03-2025";
        assertEquals(expectedOutput, c2.getOutputMessage());


    }

    @Test
    public void updateIncome_inValidInputAmt_throwsException() throws FinanceException {
        ExpenseList expenseList = new ExpenseList();
        List<Income> incomes = new ArrayList<>();
        Command c1 = new AddIncomeCommand("add category/Salary amt/2500.00 d/15-03-2025");
        c1.execute(incomes, expenseList);
        Command c2 = new UpdateIncomeCommand("update-income 1 amt/invalidamount");

        try{
            c2.execute(incomes, expenseList);
            fail();
        } catch (FinanceException e) {
            String expectedError = "Error: Invalid amount format.";
            assertEquals(expectedError, e.getMessage());
        }

    }

    @Test
    public void updateIncome_inValidInputDate_throwsException() throws FinanceException {
        ExpenseList expenseList = new ExpenseList();
        List<Income> incomes = new ArrayList<>();
        Command c1 = new AddIncomeCommand("add category/Salary amt/2500.00 d/15-03-2025");
        c1.execute(incomes, expenseList);
        Command c2 = new UpdateIncomeCommand("update-income 1 d/99-99-9999");

        try{
            c2.execute(incomes, expenseList);
            fail();
        } catch (FinanceException e) {
            String expectedError = "Error: Date is not a valid date";
            assertEquals(expectedError, e.getMessage());
        }

    }

    @Test
    public void updateIncome_emptyList_throwsException(){
        ExpenseList expenseList = new ExpenseList();
        List<Income> incomes = new ArrayList<>();
        Command c = new UpdateIncomeCommand("update-income 1 amt/200");

        try{
            c.execute(incomes, expenseList);
            fail();
        } catch (FinanceException e) {
            String expectedError = "Error: No income entries exist to update.";
            assertEquals(expectedError, e.getMessage());
        }

    }

}
