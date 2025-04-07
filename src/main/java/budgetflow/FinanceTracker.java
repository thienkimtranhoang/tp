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

/**
 * The main class for the BudgetFlow finance tracker application.
 * <p>
 * It initializes and manages the core components of the system,
 * including incomes, expenses, persistent storage, user interface,
 * and saving goal manager.
 * </p>
 * <p>
 * The FinanceTracker class acts as the controller, coordinating user inputs,
 * command parsing and execution, and data persistence.
 * </p>
 */
//@@author thienkimtranhoang
public class FinanceTracker {
    /** List to store all income records. */
    private final List<Income> incomes;

    /** List to store all expense records. */
    private final ExpenseList expenseList;

    /** Handles saving and loading of income and expense data. */
    private final Storage storage;

    /** Manages saving and loading of the user's saving goal. */
    private final SavingGoalManager savingGoalManager;

    /** Handles user interactions (I/O). */
    private final Ui ui;

    /**
     * Constructs a new FinanceTracker instance and initializes necessary components.
     * If persistent loading is not disabled (via system property), previous data and saving goals are loaded.
     */
    public FinanceTracker() {
        this.incomes = new ArrayList<>();
        this.expenseList = new ExpenseList();
        this.storage = new Storage();
        this.savingGoalManager = new SavingGoalManager();
        this.ui = new Ui();
        if (!Boolean.getBoolean("skipPersistentLoad")) {
            this.storage.loadData(incomes, this.expenseList);
            this.savingGoalManager.loadSavingGoal();
        }
    }

    /**
     * Runs the main loop of the finance tracker program.
     * Continuously reads and executes user commands until the exit command is issued.
     * Data and saving goal are saved when relevant commands are executed.
     */
    //@@author thienkimtranhoang
    public void run() {
        ui.showWelcome();
        while (true) {
            try {
                String input = ui.readCommand();
                Command command = Parser.getCommandFromInput(input);
                command.execute(incomes, expenseList);

                // Persist the saving goal after every command execution
                savingGoalManager.saveSavingGoal(ListIncomeCommand.getSavingGoal());

                // Exit if user issues an exit command
                if (command.isExit()) {
                    ui.printCommandMessage(command.getOutputMessage());
                    break;
                }

                // Persist data if user adds or deletes income/expenses
                if (command.getCommandType() == CommandType.CREATE
                        || command.getCommandType() == CommandType.DELETE) {
                    storage.saveData(incomes, expenseList);
                    System.out.println("Saving goal retrieved: " + ListIncomeCommand.getSavingGoal());
                }

                ui.printCommandMessage(command.getOutputMessage());

            } catch (FinanceException error) {
                ui.printError(error.getMessage());
            }
        }
    }
}
