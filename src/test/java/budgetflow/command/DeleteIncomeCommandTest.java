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
    void deleteIncome_validIncome_expectIncomeFound() throws FinanceException {
        ExpenseList expenseList = new ExpenseList();
        List<Income> incomes = get3Incomes();
        Command c = new DeleteIncomeCommand("delete-income Part-timeJob");
        c.execute(incomes, expenseList);
        String expectedOutput = "Income deleted: Part-timeJob";
        assertEquals(expectedOutput, c.getOutputMessage());
    }

    //@@author Yikbing
    @Test
    void deleteIncome_invalidIncome_expectNoIncomeFound() {
        ExpenseList expenseList = new ExpenseList();
        List<Income> incomes = get3Incomes();
        Command c = new DeleteIncomeCommand("delete-income Housework");
        try {
            c.execute(incomes, expenseList);
            fail();
        } catch (FinanceException e) {
            String expectedError = "Income not found: Housework";
            assertEquals(expectedError, e.getMessage());
        }
    }

}
