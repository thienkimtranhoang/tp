package budgetflow.command;

import budgetflow.income.Income;

import java.util.List;

public class ListIncomeCommand extends Command {

    private static final String EMPTY_INCOME_LIST_MESSAGE = "No incomes have been added yet.";

    public ListIncomeCommand() {
        super();
    }

    public void execute(List<Income> incomes) {
        if (incomes.isEmpty()) {
            System.out.println(EMPTY_INCOME_LIST_MESSAGE);
            return;
        }
        double totalIncome = 0.0;
        System.out.println("Income Log:");
        for (Income income : incomes) {
            System.out.println(income.getCategory() + " | $" +
                    String.format("%.2f", income.getAmount()) + " | " +
                    income.getDate());
            totalIncome += income.getAmount();
        }
        System.out.println("Total Income: $" + String.format("%.2f", totalIncome));
    }
}
