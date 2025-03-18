package budgetflow.parser;

import budgetflow.command.*;
import budgetflow.expense.Expense;
import budgetflow.expense.ExpenseList;
import budgetflow.income.Income;
import budgetflow.storage.Storage;

import java.util.List;
import java.util.Scanner;

public class Parser {

    // Command constants
    public static final String COMMAND_ADD_INCOME = "add category/";
    public static final String COMMAND_LOG_EXPENSE = "log-expense ";
    public static final String COMMAND_LIST_INCOME = "list income";
    public static final String COMMAND_EXIT = "exit";
    public static final String COMMAND_DELETE_INCOME = "delete-income ";
    public static final String COMMAND_DELETE_EXPENSE = "delete-expense ";
    public static final String COMMAND_VIEW_ALL_EXPENSES = "view-all-expense";
    public static final String COMMAND_FIND_EXPENSE = "find-expense";



    private List<Income> incomes;
    private List<Expense> expenses; // (Not directly used, since we use ExpenseList)
    private ExpenseList expenseList;
    private Scanner scanner;
    private Storage storage;


    public Command getCommandFromInput(String input) {
        if (input.startsWith(COMMAND_ADD_INCOME)) {
            return new AddIncomeCommand(input);
        } else if (input.startsWith(COMMAND_LOG_EXPENSE)) {
            return new LogExpenseCommand(input);
        } else if (input.startsWith(COMMAND_DELETE_INCOME)) {
            return new DeleteIncomeCommand(input);
        } else if (COMMAND_LIST_INCOME.equals(input)) {
            listIncome();
        } else if (input.startsWith(COMMAND_DELETE_EXPENSE)) {
            return new DeleteExpenseCommand(input);
        } else if (input.equals(COMMAND_VIEW_ALL_EXPENSES)) {
            viewAllExpenses();
        } else if (input.startsWith(COMMAND_FIND_EXPENSE)) {
            findExpense(input);
        } else {
            System.out.println("I don't understand that command. Try again.");
        }
        return null;
    }

    public void listIncome() {
        if (incomes.isEmpty()) {
            System.out.println("No incomes have been added yet.");
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

    public void viewAllExpenses() {
        if (expenseList.getSize() == 0) {
            System.out.println("No expenses have been logged yet.");
            return;
        }
        System.out.println("Expenses log:");
        for (int i = 0; i < expenseList.getSize(); i++) {
            Expense expense = expenseList.get(i);
            System.out.println((i + 1) + " | " + expense.getCategory() + " | " +
                    expense.getDescription() + " | $" +
                    String.format("%.2f", expense.getAmount()) + " | " +
                    expense.getDate());
        }
        System.out.println("Total Expenses: $" + String.format("%.2f", expenseList.getTotalExpenses()));
    }

    public void findExpense(String input) {
        String keyword = "";
        if (input.startsWith(COMMAND_FIND_EXPENSE)) {
            keyword += input.substring(COMMAND_FIND_EXPENSE.length()).trim();
        }
        if (keyword.isEmpty()) {
            System.out.println("Error: Missing keyword");
            return;
        }
        ExpenseList matchingExpenses = expenseList.get(keyword);
        if (matchingExpenses.getSize() == 0) {
            System.out.println("Sorry, I cannot find any expenses matching your keyword: " + keyword);
        } else {
            System.out.println("Here are all matching expenses: ");
            System.out.print(matchingExpenses);
        }
    }
}

