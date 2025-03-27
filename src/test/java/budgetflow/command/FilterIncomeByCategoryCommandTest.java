package budgetflow.command;

import budgetflow.exception.FinanceException;
import budgetflow.expense.ExpenseList;
import budgetflow.income.Income;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//@@author IgoyAI
class FilterIncomeByCategoryCommandTest {

    @Test
    void filterIncomeByCategory_validCategory_returnsMatchingIncomes() throws FinanceException {
        List<Income> incomes = new ArrayList<>();
        incomes.add(new Income("Salary", 2500.00, "15-03-2025"));
        incomes.add(new Income("Bonus", 500.00, "20-03-2025"));
        incomes.add(new Income("Salary", 3000.00, "10-04-2025"));
        ExpenseList expenseList = new ExpenseList();

        Command command = new FilterIncomeByCategoryCommand("filter-income category/Salary");
        command.execute(incomes, expenseList);
        String expectedOutput = "Filtered Incomes by Category (Salary):\n" +
                "Salary | $2500.00 | 15-03-2025\n" +
                "Salary | $3000.00 | 10-04-2025\n";
        assertEquals(expectedOutput, command.getOutputMessage());
    }

    @Test
    void filterIncomeByCategory_noMatchingCategory_returnsNoIncomesFoundMessage() throws FinanceException {
        List<Income> incomes = new ArrayList<>();
        incomes.add(new Income("Salary", 2500.00, "15-03-2025"));
        ExpenseList expenseList = new ExpenseList();

        Command command = new FilterIncomeByCategoryCommand("filter-income category/Bonus");
        command.execute(incomes, expenseList);
        String expectedOutput = "Filtered Incomes by Category (Bonus):\n" +
                "No incomes found under the specified category.";
        assertEquals(expectedOutput, command.getOutputMessage());
    }

    @Test
    void filterIncomeByCategory_missingCategory_throwsException() {
        List<Income> incomes = new ArrayList<>();
        ExpenseList expenseList = new ExpenseList();
        Command command = new FilterIncomeByCategoryCommand("filter-income category/");
        try {
            command.execute(incomes, expenseList);
            fail();
        } catch (FinanceException e) {
            String expectedError = "Invalid category filter format. Usage: filter-income category/<category>";
            assertEquals(expectedError, e.getMessage());
        }
    }
}
