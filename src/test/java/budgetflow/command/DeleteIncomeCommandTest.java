package budgetflow.command;

import budgetflow.exception.FinanceException;
import budgetflow.expense.ExpenseList;
import budgetflow.income.Income;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
    void deleteIncome_invalidIncome_expectNoIncomeFound() throws FinanceException {
        ExpenseList expenseList = new ExpenseList();
        List<Income> incomes = get3Incomes();
        Command c = new DeleteIncomeCommand("delete-income Housework");
        c.execute(incomes, expenseList);

        String expectedOutput = "Income not found: Housework";
        assertEquals(expectedOutput, c.getOutputMessage());
    }

    @Test
    void deleteIncome_invalidCommandFormat_expectInvalidCommandError() throws FinanceException {
        ExpenseList expenseList = new ExpenseList();
        List<Income> incomes = get3Incomes();
        Command c = new DeleteIncomeCommand("delete-something Part-timeJob");
        c.execute(incomes, expenseList);
        String expectedOutput = "Invalid delete income command format.";
        assertEquals(expectedOutput, c.getOutputMessage());
    }

    @Test
    void deleteIncome_emptyCategory_expectEmptyCategoryError() throws FinanceException {
        ExpenseList expenseList = new ExpenseList();
        List<Income> incomes = get3Incomes();
        Command c = new DeleteIncomeCommand("delete-income ");
        c.execute(incomes, expenseList);
        String expectedOutput = "Error: Income category is required.";
        assertEquals(expectedOutput, c.getOutputMessage());
    }

    @Test
    void deleteIncome_validIncomeInMiddleOfList_expectIncomeDeleted() throws FinanceException {
        ExpenseList expenseList = new ExpenseList();
        List<Income> incomes = new ArrayList<>();
        incomes.add(new Income("Part-timeJob", 300.00, "12-06-2025"));
        incomes.add(new Income("freelance", 100.00, "29-05-2025"));
        incomes.add(new Income("fulltime-job", 5000.00, "01-01-2025"));
        incomes.add(new Income("contract", 2000.00, "05-05-2025"));
        Command c = new DeleteIncomeCommand("delete-income freelance");
        c.execute(incomes, expenseList);
        String expectedOutput = "Income deleted: freelance";
        assertEquals(expectedOutput, c.getOutputMessage());
    }
}
