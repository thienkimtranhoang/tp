package budgetflow;
import java.util.Collection;

import budgetflow.command.Command;
import budgetflow.command.CommandType;
import budgetflow.exception.FinanceException;
import budgetflow.exception.UnknownCommandException;
import budgetflow.expense.Expense;
import budgetflow.expense.ExpenseList;
import budgetflow.income.Income;
import budgetflow.parser.Parser;
import budgetflow.storage.Storage;
import budgetflow.ui.Ui;
import budgetflow.parser.Parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FinanceTracker {

    private List<Income> incomes;
    private ExpenseList expenseList;
    private Storage storage;
    private Ui ui;


    public FinanceTracker() {
        this.incomes = new ArrayList<>();
        this.expenseList = new ExpenseList();
        this.storage = new Storage();
        this.ui = new Ui();
        if (!Boolean.getBoolean("skipPersistentLoad")) {
            this.storage.loadData(incomes, this.expenseList);
        }
    }

    public void run() {
        ui.showWelcome();
        while (true) {
            try {
                String input = ui.readCommand();
                Command c = Parser.getCommandFromInput(input);
                c.execute(incomes, expenseList);
                if (c.isExit()) {
                    ui.printCommandMessage(c.getOutputMessage());
                    break;
                }
                if (c.getCommandType() == CommandType.CREATE || c.getCommandType() == CommandType.DELETE) {
                    storage.saveData(incomes, expenseList);
                }
                ui.printCommandMessage(c.getOutputMessage());
            } catch (FinanceException e) {
                ui.printError(e.getMessage());
            }
        }
    }
}
