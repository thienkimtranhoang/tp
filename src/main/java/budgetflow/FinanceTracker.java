package budgetflow;

import budgetflow.command.Command;
import budgetflow.command.CommandType;
import budgetflow.exception.FinanceException;
import budgetflow.command.ListIncomeCommand;
import budgetflow.expense.ExpenseList;
import budgetflow.income.Income;
import budgetflow.parser.Parser;
import budgetflow.storage.SavingGoalManager;
import budgetflow.storage.Storage;
import budgetflow.ui.Ui;

import java.util.ArrayList;
import java.util.List;

public class FinanceTracker {
    private final List<Income> incomes;
    private final ExpenseList expenseList;
    private final Storage storage;
    private final SavingGoalManager savingGoalManager; // Add SavingGoalManager instance
    private final Ui ui;

    public FinanceTracker() {
        this.incomes = new ArrayList<>();
        this.expenseList = new ExpenseList();
        this.storage = new Storage();
        this.savingGoalManager = new SavingGoalManager(); // Initialize SavingGoalManager
        this.ui = new Ui();
        if (!Boolean.getBoolean("skipPersistentLoad")) {
            this.storage.loadData(incomes, this.expenseList);
            this.savingGoalManager.loadSavingGoal(); // Load saving goal
        }
    }

    /**
     * Handle the main run of the finance tracker program
     */
    public void run() {
        ui.showWelcome();
        while (true) {
            try {
                String input = ui.readCommand();
                Command c = Parser.getCommandFromInput(input);
                c.execute(incomes, expenseList);
                savingGoalManager.saveSavingGoal(ListIncomeCommand.getSavingGoal()); // Save saving goal
                // Save data and saving goal for specific commands
                if (c.isExit()) {
                    ui.printCommandMessage(c.getOutputMessage());
                    break;
                }
                if (c.getCommandType() == CommandType.CREATE || c.getCommandType() == CommandType.DELETE) {
                    storage.saveData(incomes, expenseList);

                    System.out.println("Saving goal retrieved: " + ListIncomeCommand.getSavingGoal());

                }
                ui.printCommandMessage(c.getOutputMessage());
            } catch (FinanceException e) {
                ui.printError(e.getMessage());
            }
        }
    }
}
