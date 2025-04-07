package budgetflow.command;

import budgetflow.expense.ExpenseList;
import budgetflow.income.Income;
import java.util.List;
import java.util.logging.Logger;

/**
 * Command to set a saving goal for the budget application.
 */
public class SetSavingGoalCommand extends Command {
    private static final Logger logger = Logger.getLogger(SetSavingGoalCommand.class.getName());
    private double savingGoalAmount;

    /**
     * Constructs a SetSavingGoalCommand by parsing the input.
     *
     * @param input The user input string containing the saving goal amount.
     */
    //@@author thienkimtranhoang
    // Original implementation of SetSavingGoalCommand.java.
    public SetSavingGoalCommand(String input) {
        super();
        this.commandType = CommandType.UPDATE;
        try {
            String amountStr = input.replace("set-saving-goal", "").trim();
            if (amountStr.isEmpty()) {
                throw new NumberFormatException("No saving goal amount provided");
            }
            this.savingGoalAmount = Double.parseDouble(amountStr);
            if (this.savingGoalAmount < 0) {
                throw new IllegalArgumentException("Saving goal amount cannot be negative");
            }
            ListIncomeCommand.setSavingGoal(this.savingGoalAmount);
        } catch (IllegalArgumentException e) {
            logger.warning("Invalid saving goal amount: " + e.getMessage());
            this.outputMessage = "Invalid saving goal amount. Please enter a valid number.";
            this.savingGoalAmount = 0.0;
        }
    }

    /**
     * Executes the command to set the saving goal.
     *
     * @param incomes     The list of incomes (not used in this command).
     * @param expenseList The list of expenses (not used in this command).
     */
    //@@author thienkimtranhoang
    @Override
    public void execute(List<Income> incomes, ExpenseList expenseList) {
        // If an error message is already set (by the constructor), do not override it.
        if (this.outputMessage != null) {
            logger.info("Saving goal error: " + this.outputMessage);
            return;
        }
        this.outputMessage = "Saving goal set to: $" + String.format("%.2f", savingGoalAmount);
        logger.info("Saving goal set to: " + savingGoalAmount);
    }

    /**
     * Gets the saving goal amount.
     *
     * @return The saving goal amount.
     */
    public double getSavingGoalAmount() {
        return this.savingGoalAmount;
    }
}
