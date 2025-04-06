package budgetflow.command;

import budgetflow.exception.FinanceException;
import budgetflow.expense.ExpenseList;
import budgetflow.income.Income;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class DeleteIncomeCommandTest {

    //@@author Yikbing
    private static List<Income> get3Incomes() {
        List<Income> incomes = new ArrayList<>();
        incomes.add(new Income("Part-timeJob", 300.00, "12-06-2025"));
        incomes.add(new Income("freelance", 100.00, "29-05-2025"));
        incomes.add(new Income("fulltime-job", 5000.00, "01-01-2025"));
        return incomes;
    }

    //@@author Yikbing
    @Test
    void deleteIncome_validIndex_incomeDeleted() throws FinanceException {
        ExpenseList expenseList = new ExpenseList();
        List<Income> incomes = get3Incomes();
        Command command = new DeleteIncomeCommand("delete-income 1");
        command.execute(incomes, expenseList);
        String expectedOutput = "Income deleted: Part-timeJob, $300.0";
        assertEquals(expectedOutput, command.getOutputMessage());
    }

    //@@author Yikbing
    @Test
    void deleteIncome_invalidNumericIndex_throwsException() throws FinanceException {
        ExpenseList expenseList = new ExpenseList();
        List<Income> incomes = get3Incomes();
        Command command = new DeleteIncomeCommand("delete-income 4");
        try{
            command.execute(incomes, expenseList);
            fail();
        } catch (FinanceException e) {
            String expectedError = "Error: Invalid Income index.";
            assertEquals(expectedError, e.getMessage());
        }
    }

    //@@author Yikbing
    @Test
    void deleteIncome_invalidNonNumericIndex_throwsException() throws FinanceException {
        ExpenseList expenseList = new ExpenseList();
        List<Income> incomes = get3Incomes();
        Command command = new DeleteIncomeCommand("delete-income a");
        try{
            command.execute(incomes, expenseList);
            fail();
        } catch (FinanceException e) {
            String expectedError = "Error: Please enter a valid numeric index.";
            assertEquals(expectedError, e.getMessage());
        }
    }

    //@@author Yikbing
    @Test
    void deleteIncome_noIndex_throwsException() throws FinanceException {
        ExpenseList expenseList = new ExpenseList();
        List<Income> incomes = get3Incomes();
        Command command = new DeleteIncomeCommand("delete-income");
        try{
            command.execute(incomes, expenseList);
            fail();
        } catch (FinanceException e) {
            String expectedError = "Error: Income Index is required.";
            assertEquals(expectedError, e.getMessage());
        }
    }

}
