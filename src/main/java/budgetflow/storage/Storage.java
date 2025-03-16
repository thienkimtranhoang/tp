package budgetflow.storage;

import budgetflow.Expense;
import budgetflow.ExpenseList;
import budgetflow.Income;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Handles the storage operations for the BudgetFlow application.
 * Responsible for saving and loading finance data to/from persistent storage.
 */
public class Storage {
    private static final String DATA_FILE_PATH = "./data/budgetflow.txt";

    /**
     * Saves all income and expense data to a file.
     *
     * This method writes all income and expense records to the file specified by DATA_FILE_PATH.
     * Each record is written on a separate line with fields separated by the '|' character.
     * Income records begin with "INCOME" and expense records begin with "EXPENSE".
     * The method creates the data directory if it doesn't exist.
     *
     * Income format: INCOME|category|amount|date
     * Expense format: EXPENSE|category|description|amount|date
     *
     * @param incomes List of income records to save
     * @param expenseList List of expense records to save
     * @throws IOException if an error occurs during file writing, which is caught
     *                     and logged to the console
     */
    public void saveData(List<Income> incomes, ExpenseList expenseList) {
        try {
            File directory = new File("./data");
            if (!directory.exists()) {
                directory.mkdirs();
            }

            FileWriter fileWriter = new FileWriter(DATA_FILE_PATH);
            BufferedWriter writer = new BufferedWriter(fileWriter);

            for (Income income : incomes) {
                writer.write("INCOME|" +
                        income.getCategory() + "|" +
                        income.getAmount() + "|" +
                        income.getDate());
                writer.newLine();
            }

            for (int i = 0; i < expenseList.getSize(); i++) {
                Expense expense = expenseList.get(i);
                writer.write("EXPENSE|" +
                        expense.getCategory() + "|" +
                        expense.getDescription() + "|" +
                        expense.getAmount() + "|" +
                        expense.getDate());
                writer.newLine();
            }

            writer.close();
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    /**
     * Loads income and expense data from a file.
     *
     * This method reads records from the file specified by DATA_FILE_PATH and populates
     * the incomes list and expenseList with the data. The method expects each record to be
     * on a separate line with fields separated by the '|' character.
     *
     * The method recognizes two types of records:
     * - Income records: start with "INCOME" and have 4 parts (type|category|amount|date)
     * - Expense records: start with "EXPENSE" and have 5 parts (type|category|description|amount|date)
     *
     * If the data file doesn't exist, the method returns without taking any action.
     *
     * @param incomes List where loaded income records will be added
     * @param expenseList List where loaded expense records will be added
     * @throws IOException if an error occurs while reading the file
     * @throws NumberFormatException if amount values cannot be parsed as doubles
     * Both exceptions are caught and logged to the console
     */
    public void loadData(List<Income> incomes, ExpenseList expenseList) {
        File file = new File(DATA_FILE_PATH);
        if (!file.exists()) {
            return;
        }

        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader reader = new BufferedReader(fileReader);

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");

                if (parts[0].equals("INCOME") && parts.length == 4) {
                    String category = parts[1];
                    double amount = Double.parseDouble(parts[2]);
                    String date = parts[3];

                    Income income = new Income(category, amount, date);
                    incomes.add(income);
                } else if (parts[0].equals("EXPENSE") && parts.length == 5) {
                    String category = parts[1];
                    String description = parts[2];
                    double amount = Double.parseDouble(parts[3]);
                    String date = parts[4];

                    Expense expense = new Expense(category, description, amount, date);
                    expenseList.add(expense);
                }
            }

            reader.close();
            System.out.println("Data loaded successfully from " + DATA_FILE_PATH);
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }
}