package budgetflow.command;

import budgetflow.exception.FinanceException;
import budgetflow.expense.ExpenseList;
import budgetflow.income.Income;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

//@@author IgoyAI
class FilterIncomeByAmountCommandTest {

    @Test
    void amount_valid_returnsMatching() throws FinanceException {
        List<Income> incomes = new ArrayList<>();
        incomes.add(new Income("Salary", 2500.00, "15-03-2025"));
        incomes.add(new Income("Bonus", 500.00, "20-03-2025"));
        incomes.add(new Income("Gift", 100.00, "01-01-2020"));
        ExpenseList expenseList = new ExpenseList();

        // Filter incomes with amount between 500 and 3000 should return Salary and Bonus
        Command command = new FilterIncomeByAmountCommand(
                "filter-income amount from/500 to/3000");
        command.execute(incomes, expenseList);
        String expectedOutput = "Filtered Incomes by Amount (500.0 to 3000.0):\n"
                + "Salary | $2500.00 | 15-03-2025\n"
                + "Bonus | $500.00 | 20-03-2025\n";
        assertEquals(expectedOutput, command.getOutputMessage());
    }

    @Test
    void amount_noMatch_returnsNone() throws FinanceException {
        List<Income> incomes = new ArrayList<>();
        incomes.add(new Income("Salary", 2500.00, "15-03-2025"));
        ExpenseList expenseList = new ExpenseList();

        // Filter range that does not match any income amounts
        Command command = new FilterIncomeByAmountCommand(
                "filter-income amount from/3000 to/5000");
        command.execute(incomes, expenseList);
        String expectedOutput = "Filtered Incomes by Amount (3000.0 to 5000.0):\n"
                + "No incomes found in the specified amount range.";
        assertEquals(expectedOutput, command.getOutputMessage());
    }

    @Test
    void amount_invalidFormat_throws() {
        List<Income> incomes = new ArrayList<>();
        ExpenseList expenseList = new ExpenseList();
        Command command = new FilterIncomeByAmountCommand(
                "filter-income amount from/2500");
        try {
            command.execute(incomes, expenseList);
            fail();
        } catch (FinanceException e) {
            String expectedError = "Invalid amount filter format. Usage: filter-income amount " +
                    "from/<minAmount> to/<maxAmount>";
            assertEquals(expectedError, e.getMessage());
        }
    }

    @Test
    void amount_rangeInvalid_throws() {
        List<Income> incomes = new ArrayList<>();
        ExpenseList expenseList = new ExpenseList();
        Command command = new FilterIncomeByAmountCommand(
                "filter-income amount from/3000 to/2500");
        try {
            command.execute(incomes, expenseList);
            fail();
        } catch (FinanceException e) {
            String expectedError = "Minimum amount should not be greater than maximum amount.";
            assertEquals(expectedError, e.getMessage());
        }
    }
}
