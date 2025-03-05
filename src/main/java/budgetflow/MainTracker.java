package budgetflow;

import java.util.ArrayList;
import java.util.Scanner;

public class MainTracker {
    private static ArrayList<Income> incomes = new ArrayList<>();
    private static ArrayList<Expense> expenses = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    /**
     * Main entry-point for the finance tracker application.
     */
    public static void main(String[] args) {
        System.out.println("Welcome to Finance Tracker!");
        System.out.println("You can track incomes and expenses here.");

        boolean isRunning = true;
        while (isRunning) {
            System.out.println("What would you like to do?");
            String input = scanner.nextLine().trim();

            try {
                if (input.startsWith("add category/")) {
                    addIncome(input);
                } else if (input.startsWith("log-expense ")) {
                    logExpense(input);
                } else if (input.equals("exit")) {
                    isRunning = false;
                    System.out.println("Goodbye!");
                } else {
                    System.out.println("I don't understand that command. Try again.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    /**
     * Adds income to the finance tracker.
     * Format: add category/CATEGORY amt/AMOUNT d/DATE
     * Example: add category/Part-time job amt/300.00 d/June 12
     */
    private static void addIncome(String input) {
        // Remove "add " from the start
        input = input.substring(4);

        // Split the input by spaces
        String[] parts = input.split(" ");

        // Variables to store parsed values
        String category = null;
        Double amount = null;
        String date = null;

        // Parse each part
        for (String part : parts) {
            if (part.startsWith("category/")) {
                category = part.substring(9);
            } else if (part.startsWith("amt/")) {
                try {
                    amount = Double.parseDouble(part.substring(4));
                } catch (NumberFormatException e) {
                    System.out.println("Error: Invalid amount format. Please enter a valid number.");
                    return;
                }
            } else if (part.startsWith("d/")) {
                date = part.substring(2);
            }
        }

        // Validate required fields
        if (category == null) {
            System.out.println("Error: Income category is required");
            return;
        }
        if (amount == null) {
            System.out.println("Error: Income amount is required");
            return;
        }
        if (date == null) {
            System.out.println("Error: Income date is required");
            return;
        }

        // Create and add income
        Income income = new Income(category, amount, date);
        incomes.add(income);

        System.out.println("Income added: " + category + ", Amount: $" + String.format("%.2f", amount) + ", Date: " + date);
    }

    /**
     * Logs an expense in the finance tracker.
     * Format: log-expense desc/DESCRIPTION amt/AMOUNT d/DATE
     * Example: log-expense desc/Lunch at cafe amt/12.00 d/Feb 18
     */
    private static void logExpense(String input) {
        // Remove "log-expense " from the start
        input = input.substring(12);

        // Split the input by spaces
        String[] parts = input.split(" ");

        // Variables to store parsed values
        String description = null;
        Double amount = null;
        String date = null;

        // Parse each part
        for (String part : parts) {
            if (part.startsWith("desc/")) {
                description = part.substring(5);
            } else if (part.startsWith("amt/")) {
                try {
                    amount = Double.parseDouble(part.substring(4));
                } catch (NumberFormatException e) {
                    System.out.println("Error: Invalid amount format. Please enter a valid number.");
                    return;
                }
            } else if (part.startsWith("d/")) {
                date = part.substring(2);
            }
        }

        // Validate required fields
        if (description == null) {
            System.out.println("Error: Expense description is required");
            return;
        }
        if (amount == null) {
            System.out.println("Error: Expense amount is required");
            return;
        }
        if (date == null) {
            System.out.println("Error: Expense date is required");
            return;
        }

        // Create and add expense
        Expense expense = new Expense(description, amount, date);
        expenses.add(expense);

        System.out.println("Expense logged: " + description + ", Amount: $" + String.format("%.2f", amount) + ", Date: " + date);
    }
}