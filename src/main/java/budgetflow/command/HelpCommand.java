package budgetflow.command;

import budgetflow.exception.FinanceException;

import budgetflow.expense.ExpenseList;
import budgetflow.income.Income;

import java.util.List;

public class HelpCommand extends Command {

    public HelpCommand() {
        super();
        this.commandType = CommandType.READ;
    }

    @Override
    public void execute(List<Income> incomes, ExpenseList expenseList) throws FinanceException {
        StringBuilder helpMessage = new StringBuilder();
        helpMessage.append("To add an Income: add category/[CATEGORY] amt/[AMOUNT] d/[DD-MM-YYYY]\n");
        helpMessage.append("To add an Expense: log-expense category/[CATEGORY] desc/[DESCRIPTION] amt/[AMOUNT]" +
                " d/[DD-MM-YYYY]\n");
        helpMessage.append("To View all expenses: view-all-expense\n");
        helpMessage.append("To view all Income: list income\n");
        helpMessage.append("To delete an Expense: delete-expense [DESCRIPTION]\n");
        helpMessage.append("To delete an Income: delete-income [CATEGORY]\n");
        helpMessage.append("To find an Expense: find-expense /desc [DESCRIPTION] OR /d [DD-MM-YYYY] OR /amt [AMOUNT] "
                + "OR /category [CATEGORY] OR /amtrange [AMOUNT 1] [AMOUNT 2] OR /drange [DATE 1] [DATE 2]\n");
        helpMessage.append("To compare expenses from 2 months: compare [MM-YYYY] [MM-YYYY]\n");
        helpMessage.append("To Update an expense: update-expense category/[CATEGORY] OR amt/[AMOUNT] OR " +
                "desc/[DESCRIPTION] d/[DD-MM-YYYY]\n");
        helpMessage.append("To Update an Income : update-income category/[CATEGORY] OR amt/[AMOUNT] " +
                "OR d/[DD-MM-YYYY]\n");
        helpMessage.append("To filter income by date: filter-income date from/DD-MM-YYYY to/DD-MM-YYYY\n");
        helpMessage.append("To filter income by amount: filter-income amount from/[AMOUNT 1] to/[AMOUNT 2]\n");
        helpMessage.append("To filter income by category: filter-income category/[CATEGORY]\n");
        helpMessage.append("To exit the application: exit");

        this.outputMessage = helpMessage.toString();
    }

}

