package budgetflow;
import java.util.Collection;

import budgetflow.storage.Storage;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FinanceTracker {
    private static final Logger logger = Logger.getLogger(FinanceTracker.class.getName());

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
        assert scanner != null : "Scanner cannot be null";
        assert expenseList != null : "Expense list cannot be null";

        this.incomes = new ArrayList<>();
        this.scanner = scanner;
        this.expenseList = new ExpenseList(expenseList);
        this.storage = new Storage();
        if (!Boolean.getBoolean("skipPersistentLoad")) {
            storage.loadData(incomes, this.expenseList);
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
        assert input != null : "Command input cannot be null";

        logger.info("Processing command: " + input);

        try {
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
                logger.warning("Unknown command received: " + input);
                System.out.println("I don't understand that command. Try again.");
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error processing command: " + input, e);
            System.out.println("An error occurred while processing the command.");
        }
    }


    public void addIncome(String input) {
        assert input.startsWith(COMMAND_ADD_INCOME) : "Invalid add income command format";

        input = input.substring(ADD_COMMAND_PREFIX_LENGTH).trim();
        logger.info("Adding income with input: " + input);

        String category = extractPattern(input, "category/(.*?) (amt/|d/|$)");
        String amtStr = extractPattern(input, "amt/([0-9]+(\\.[0-9]*)?)");
        String date = extractPattern(input, "d/([^ ]+)");

        if (category == null || category.isEmpty() || amtStr == null || date == null) {
            logger.warning("Invalid income input: " + input);
            throw new IllegalArgumentException("Error: Income category, amount, and date are required.");
        }

        double amount;
        try {
            amount = Double.parseDouble(amtStr);
        } catch (NumberFormatException e) {
            logger.warning("Invalid amount format: " + amtStr);
            throw new IllegalArgumentException("Error: Invalid amount format.");
        }

        Income income = new Income(category, amount, date);
        incomes.add(income);
        logger.info("Income added successfully: " + income);

        storage.saveData(incomes, expenseList);
    }

    public void logExpense(String input) {
        assert input != null && !input.isEmpty() : "Expense input should not be empty";
        assert input.startsWith(LOG_EXPENSE_COMMAND_PREFIX) : "Invalid log expense command format";

        input = input.substring(LOG_EXPENSE_COMMAND_PREFIX_LENGTH).trim();
        logger.info("Logging expense with input: " + input);

        String category = extractPattern(input, "category/(.*?) (desc/|amt/|d/|$)");
        String description = extractPattern(input, "desc/(.*?) (amt/|d/|$)");
        String amtStr = extractPattern(input, "amt/([0-9]+(\\.[0-9]*)?)");
        String date = extractPattern(input, "d/([^ ]+)");

        if (category == null || description == null || amtStr == null || date == null) {
            logger.warning("Invalid expense input: " + input);
            throw new IllegalArgumentException("Error: Expense category, description, amount, and date are required.");
        }

        double amount;
        try {
            amount = Double.parseDouble(amtStr);
        } catch (NumberFormatException e) {
            logger.warning("Invalid amount format: " + amtStr);
            throw new IllegalArgumentException("Error: Invalid amount format.");
        }

        Expense expense = new Expense(category, description, amount, date);
        expenseList.add(expense);
        logger.info("Expense logged successfully: " + expense);

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

    public double viewAllExpenses() {
        if (expenseList.getSize() == 0) {
            System.out.println("No expenses have been logged yet.");
            return 0;
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
        return 0;
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

    public void deleteIncome(String input) {
        assert input.startsWith(COMMAND_DELETE_INCOME) : "Invalid delete income command format";

        String incomeCategory = input.substring(COMMAND_DELETE_INCOME.length()).trim();
        boolean found = incomes.removeIf(income -> income.getCategory().equalsIgnoreCase(incomeCategory));

        if (!found) {
            logger.warning("Attempted to delete non-existent income: " + incomeCategory);
            System.out.println("Income not found: " + incomeCategory);
        } else {
            logger.info("Income deleted: " + incomeCategory);
            storage.saveData(incomes, expenseList);
        }
    }

    public void deleteExpense(String input) {
        assert input.startsWith(COMMAND_DELETE_EXPENSE) : "Invalid delete expense command format";

        String expenseDesc = input.substring(COMMAND_DELETE_EXPENSE.length()).trim();
        boolean found = false;

        for (int i = 0; i < expenseList.getSize(); i++) {
            if (expenseList.get(i).getDescription().equalsIgnoreCase(expenseDesc)) {
                expenseList.delete(i);
                logger.info("Expense deleted: " + expenseDesc);
                found = true;
                break;
            }
        }

        if (!found) {
            logger.warning("Attempted to delete non-existent expense: " + expenseDesc);
            System.out.println("Expense not found: " + expenseDesc);
        } else {
            storage.saveData(incomes, expenseList);
        }
    }

    private String extractPattern(String input, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        return matcher.find() ? matcher.group(1).trim() : null;
    }
}
