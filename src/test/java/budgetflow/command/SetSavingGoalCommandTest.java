package budgetflow.command;

import budgetflow.expense.ExpenseList;
import budgetflow.income.Income;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SetSavingGoalCommandTest {

    @Test
    void setSavingGoal_validInput_expectSuccessMessage() {
        String input = "set-saving-goal 1000.50";
        SetSavingGoalCommand command = new SetSavingGoalCommand(input);

        List<Income> incomes = new ArrayList<>();
        ExpenseList expenseList = new ExpenseList();
        command.execute(incomes, expenseList);

        assertEquals(1000.50, command.getSavingGoalAmount(), 0.01);
        assertEquals("Saving goal set to: $1000.50", command.getOutputMessage());
    }

    @Test
    void setSavingGoal_invalidInput_expectErrorMessage() {
        // Invalid saving goal input (non-numeric value)
        String input = "set-saving-goal abc";
        SetSavingGoalCommand command = new SetSavingGoalCommand(input);

        // Execute command
        List<Income> incomes = new ArrayList<>();
        ExpenseList expenseList = new ExpenseList();
        command.execute(incomes, expenseList);

        // Check error message for invalid number
        assertEquals(0.0, command.getSavingGoalAmount(), 0.01);
        assertEquals("Invalid saving goal amount. Please enter a valid number.", command.getOutputMessage());
    }

    @Test
    void setSavingGoal_emptyInput_expectErrorMessage() {
        String input = "set-saving-goal";
        SetSavingGoalCommand command = new SetSavingGoalCommand(input);

        List<Income> incomes = new ArrayList<>();
        ExpenseList expenseList = new ExpenseList();
        command.execute(incomes, expenseList);

        assertEquals(0.0, command.getSavingGoalAmount(), 0.01);
        assertEquals("Invalid saving goal amount. Please enter a valid number.", command.getOutputMessage());
    }
}
