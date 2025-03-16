package budgetflow;
import java.util.Collection;

import budgetflow.storage.Storage;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FinanceTracker {

    // Command constants
    public static final String COMMAND_ADD_INCOME = "add category/";
    public static final String COMMAND_LOG_EXPENSE = "log-expense ";
    public static final String COMMAND_LIST_INCOME = "list income";
    public static final String COMMAND_EXIT = "exit";
    public static final String COMMAND_DELETE_INCOME = "delete-income ";
    public static final String COMMAND_DELETE_EXPENSE = "delete-expense ";
    public static final String COMMAND_VIEW_ALL_EXPENSES = "view-all-expense";
    public static final String COMMAND_FIND_EXPENSE = "find-expense";

    private static final String ADD_COMMAND_PREFIX = "add ";
    private static final int ADD_COMMAND_PREFIX_LENGTH = ADD_COMMAND_PREFIX.length();
    private static final String LOG_EXPENSE_COMMAND_PREFIX = "log-expense ";
    private static final int LOG_EXPENSE_COMMAND_PREFIX_LENGTH = LOG_EXPENSE_COMMAND_PREFIX.length();

    private List<Income> incomes;
    private List<Expense> expenses; // (Not directly used, since we use ExpenseList)
    private ExpenseList expenseList;
    private Scanner scanner;
    private Storage storage;

    public FinanceTracker(Collection<Expense> expenseList, Scanner scanner) {
        this.incomes = new ArrayList<>();
        this.scanner = scanner;
        this.expenseList = new ExpenseList(expenseList);
        this.storage = new Storage();
        if (!Boolean.getBoolean("skipPersistentLoad")) {
            this.storage.loadData(incomes, this.expenseList);
        }
    }

    public FinanceTracker(Scanner scanner) {
        this.incomes = new ArrayList<>();
        this.scanner = scanner;
        this.expenseList = new ExpenseList();
        this.storage = new Storage();
        if (!Boolean.getBoolean("skipPersistentLoad")) {
            this.storage.loadData(incomes, this.expenseList);
        }
    }

    public void processCommand(String input) {
        if (input.startsWith(COMMAND_ADD_INCOME)) {
            addIncome(input);
        } else if (input.startsWith(COMMAND_LOG_EXPENSE)) {
            logExpense(input);
        } else if (input.startsWith(COMMAND_DELETE_INCOME)) {
            deleteIncome(input);
        } else if (COMMAND_LIST_INCOME.equals(input)) {
            listIncome();
        } else if (input.startsWith(COMMAND_DELETE_EXPENSE)) {
            deleteExpense(input);
        } else if (input.equals(COMMAND_VIEW_ALL_EXPENSES)) {
            viewAllExpenses();
        } else if (input.startsWith(COMMAND_FIND_EXPENSE)) {
            findExpense(input);
        } else {
            System.out.println("I don't understand that command. Try again.");
        }
    }

    public void addIncome(String input) {
        input = input.substring(ADD_COMMAND_PREFIX_LENGTH).trim();

        String category = null;
        Double amount = null;
        String date = null;

        String categoryPattern = "category/(.*?) (amt/|d/|$)";
        String amtPattern = "amt/([0-9]+(\\.[0-9]*)?)";
        String datePattern = "d/([^ ]+)";

        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(categoryPattern);
        java.util.regex.Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            category = matcher.group(1).trim();
        }
        pattern = java.util.regex.Pattern.compile(amtPattern);
        matcher = pattern.matcher(input);
        if (matcher.find()) {
            try {
                amount = Double.parseDouble(matcher.group(1));
            } catch (NumberFormatException e) {
                System.out.println("Error: Invalid amount format. Please enter a valid number.");
                return;
            }
        }
        pattern = java.util.regex.Pattern.compile(datePattern);
        matcher = pattern.matcher(input);
        if (matcher.find()) {
            date = matcher.group(1).trim();
        }

        if (category == null || category.isEmpty()) {
            System.out.println("Error: Income category is required.");
            return;
        }
        if (amount == null) {
            System.out.println("Error: Income amount is required.");
            return;
        }
        if (date == null) {
            System.out.println("Error: Income date is required.");
            return;
        }

        Income income = new Income(category, amount, date);
        incomes.add(income);
        System.out.println("Income added: " + category + ", Amount: $" +
                String.format("%.2f", amount) + ", Date: " + date);
        storage.saveData(incomes, expenseList);
    }

    public void logExpense(String input) {
        input = input.substring(LOG_EXPENSE_COMMAND_PREFIX_LENGTH).trim();

        String category = null;
        String description = null;
        Double amount = null;
        String date = null;

        String categoryPattern = "category/(.*?) (desc/|amt/|d/|$)";
        String descPattern = "desc/(.*?) (amt/|d/|$)";
        String amtPattern = "amt/([0-9]+(\\.[0-9]*)?)";
        String datePattern = "d/([^ ]+)";

        java.util.regex.Pattern pattern;
        java.util.regex.Matcher matcher;

        pattern = java.util.regex.Pattern.compile(categoryPattern);
        matcher = pattern.matcher(input);
        if (matcher.find()) {
            category = matcher.group(1).trim();
        }
        pattern = java.util.regex.Pattern.compile(descPattern);
        matcher = pattern.matcher(input);
        if (matcher.find()) {
            description = matcher.group(1).trim();
        }
        pattern = java.util.regex.Pattern.compile(amtPattern);
        matcher = pattern.matcher(input);
        if (matcher.find()) {
            try {
                amount = Double.parseDouble(matcher.group(1));
            } catch (NumberFormatException e) {
                System.out.println("Error: Invalid amount format. Please enter a valid number.");
                return;
            }
        }
        pattern = java.util.regex.Pattern.compile(datePattern);
        matcher = pattern.matcher(input);
        if (matcher.find()) {
            date = matcher.group(1).trim();
        }

        if (category == null || category.isEmpty()) {
            System.out.println("Error: Expense category is required.");
            return;
        }
        if (description == null || description.isEmpty()) {
            System.out.println("Error: Expense description is required.");
            return;
        }
        if (amount == null) {
            System.out.println("Error: Expense amount is required.");
            return;
        }
        if (date == null) {
            System.out.println("Error: Expense date is required.");
            return;
        }

        Expense expense = new Expense(category, description, amount, date);
        expenseList.add(expense);
        System.out.println("Expense logged: " + category + " | " + description +
                " | $" + String.format("%.2f", amount) + " | " + date);
        storage.saveData(incomes, expenseList);
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

    public void deleteIncome(String income) {
        if (income.startsWith(COMMAND_DELETE_INCOME)) {
            income = income.substring(COMMAND_DELETE_INCOME.length()).trim();
        }
        boolean found = false;
        for (int i = 0; i < incomes.size(); i++) {
            if (incomes.get(i).getCategory().equalsIgnoreCase(income)) {
                incomes.remove(i);
                System.out.println("Income deleted: " + income);
                found = true;
                break;
            }
        }
        if (!found) {
            System.out.println("Income not found: " + income);
        } else {
            storage.saveData(incomes, expenseList);
        }
    }

    public void deleteExpense(String input) {
        if (input.startsWith(COMMAND_DELETE_EXPENSE)) {
            input = input.substring(COMMAND_DELETE_EXPENSE.length()).trim();
        }
        boolean found = false;
        for (int i = 0; i < expenseList.getSize(); i++) {
            if (expenseList.get(i).getDescription().equalsIgnoreCase(input)) {
                expenseList.delete(i);
                System.out.println("Expense deleted: " + input);
                found = true;
                break;
            }
        }
        if (!found) {
            System.out.println("Expense not found: " + input);
        } else {
            storage.saveData(incomes, expenseList);
        }
    }
}
