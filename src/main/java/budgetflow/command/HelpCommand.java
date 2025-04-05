package budgetflow.command;

import budgetflow.exception.FinanceException;

import budgetflow.expense.ExpenseList;
import budgetflow.income.Income;

import java.util.List;

//@@ author Yikbing
public class HelpCommand extends Command {

    private static final String CYAN = "\u001B[36m";
    private static final String YELLOW = "\u001B[33m";
    private static final String RESET = "\u001B[0m";

    public HelpCommand() {
        super();
        this.commandType = CommandType.READ;
    }

    //@@ author Yikbing
    @Override
    public void execute(List<Income> incomes, ExpenseList expenseList) throws FinanceException {
        StringBuilder helpMessage = new StringBuilder();
        helpMessage.append("To add an Income: add category/<CATEGORY> amt/<AMOUNT> d/<DD-MM-YYYY>\n" + CYAN);
        helpMessage.append("To add an Expense: log-expense category/<CATEGORY> desc/<DESCRIPTION> amt/<AMOUNT>" +
                " d/<DD-MM-YYYY>\n" + RESET);
        helpMessage.append("To View all expenses: view-all-expense\n" + CYAN);
        helpMessage.append("To view all Income: list income\n" + RESET);
        helpMessage.append("To delete an Expense: delete-expense <INDEX>>\n" + CYAN);
        helpMessage.append("To delete an Income: delete-income <INDEX>\n" + RESET);
        helpMessage.append("To find an Expense:\n" + CYAN +
                "find-expense /desc <DESCRIPTION>\n" + YELLOW +
                "OR find-expense /d <DD-MM-YYYY>\n" + CYAN +
                "OR find-expense /amt <AMOUNT>\n" + YELLOW +
                "OR find-expense /category <CATEGORY>\n" + CYAN +
                "OR find-expense /amtrange <AMOUNT 1> <AMOUNT 2>\n" + YELLOW +
                "OR find-expense /drange <DATE 1> <DATE 2>\n" + CYAN);
        helpMessage.append("To compare expenses from 2 months: compare <MM-YYYY> <MM-YYYY>\n" + RESET);
        helpMessage.append("To Update an expense: update-expense <INDEX> category/<CATEGORY> OR amt/<AMOUNT> OR " +
                "desc/<DESCRIPTION> d/<DD-MM-YYYY>\n" + CYAN);
        helpMessage.append("To Update an Income :\n" + RESET +
                        "update-income category/<CATEGORY>\n" + YELLOW +
                        "OR update-income amt/<AMOUNT>\n" + RESET +
                        "OR update-income d/<DD-MM-YYYY>\n" + YELLOW +
                "OR any other combination of the three\n" + RESET);
        helpMessage.append("To filter income by date: filter-income date from/<DD-MM-YYYY> to/<DD-MM-YYYY>\n" + CYAN);
        helpMessage.append("To filter income by amount: filter-income amount from/<AMOUNT 1> to/<AMOUNT 2>\n" + RESET);
        helpMessage.append("To filter income by category: filter-income category/<CATEGORY>\n" + CYAN);
        helpMessage.append("To exit the application: exit" + RESET);

        this.outputMessage = helpMessage + RESET;
    }

}

