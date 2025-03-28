package budgetflow.command;

import budgetflow.expense.ExpenseList;
import budgetflow.income.Income;
import java.util.List;
import java.util.logging.Logger;

/**
 * Represents a command to list all recorded incomes and display saving goal progress.
 */
public class ListIncomeCommand extends Command {
    private static final Logger logger = Logger.getLogger(ListIncomeCommand.class.getName());
    private static final String EMPTY_INCOME_LIST_MESSAGE = "No incomes have been added yet.";

    // Static variable to store the saving goal across command instances
    private static double savingGoal = 0.0;

    /**
     * Constructs a ListIncomeCommand.
     * This command is used to display all recorded incomes and saving goal progress.
     */
    public ListIncomeCommand() {
        super();
        this.commandType = CommandType.READ;
    }

    /**
     * Sets the saving goal for all income listings.
     *
     * @param amount The amount to set as the saving goal
     */
    public static void setSavingGoal(double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Saving goal amount cannot be negative");
        }
        savingGoal = amount;
    }

    /**
     * Retrieves the current saving goal.
     *
     * @return The current saving goal amount
     */
    public static double getSavingGoal() {
        return savingGoal;
    }

    /**
     * Calculates the progress towards the saving goal.
     *
     * @param totalIncome The total income
     * @param totalExpenses The total expenses
     * @return The progress percentage towards the saving goal
     */
    private double calculateSavingProgress(double totalIncome, double totalExpenses) {
        if (savingGoal == 0) {
            return 0.0;
        }

        double savings = totalIncome - totalExpenses;
        double progressPercentage = (savings / savingGoal) * 100;
        return Math.min(progressPercentage, 100.0);
    }

    /**
     * Executes the command to list all incomes and show saving goal progress.
     *
     * @param incomes The list of incomes to be displayed.
     * @param expenseList The list of expenses used for calculating savings.
     */
    @Override
    public void execute(List<Income> incomes, ExpenseList expenseList) {
        // Calculate total income
        double totalIncome = 0.0;
        String message = "Income Log:" + System.lineSeparator();

        if (incomes.isEmpty()) {
            logger.info("Reading empty income list");
            this.outputMessage = EMPTY_INCOME_LIST_MESSAGE + System.lineSeparator();

//            // Include saving goal information if set
//            if (savingGoal > 0) {
//                message += "Saving Goal: $" + String.format("%.2f", savingGoal) +
//                        System.lineSeparator() + "Progress: 0%" + System.lineSeparator();
//            }

//            this.outputMessage = message;
            return;
        }

        // Calculate total income
        for (Income income : incomes) {
            message += income.getCategory() + " | $" +
                    String.format("%.2f", income.getAmount()) + " | " +
                    income.getDate() + System.lineSeparator();
            totalIncome += income.getAmount();
        }

        // Calculate total expenses
        double totalExpenses = expenseList.getTotalExpenses();

        // Add total income to message
        message += "Total Income: $" + String.format("%.2f", totalIncome) ;

        // Add saving goal information if set
        if (savingGoal > 0) {
            double savings = totalIncome - totalExpenses;
            double progress = calculateSavingProgress(totalIncome, totalExpenses);

            message += System.lineSeparator() + "Saving Goal: $" + String.format("%.2f", savingGoal) +
                    System.lineSeparator() +
                    "Current Savings: $" + String.format("%.2f", savings) +
                    System.lineSeparator() +
                    "Progress: " + String.format("%.2f", progress) + "%" +
                    System.lineSeparator();
        }

        this.outputMessage = message;
        logger.info("Reading non-empty income list with potential saving goal");
    }
}
