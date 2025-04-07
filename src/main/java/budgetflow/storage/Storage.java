package budgetflow.storage;

import budgetflow.expense.Expense;
import budgetflow.expense.ExpenseList;
import budgetflow.income.Income;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
//@@author thienkimtranhoang
/**
 * Handles the storage operations for the BudgetFlow application.
 * Responsible for saving and loading finance data (incomes and expenses)
 * to and from persistent storage in a plain text format.
 */
//@@author thienkimtranhoang
public class Storage {
    private static final String DATA_FILE_PATH = "./data/budgetflow.txt";

    /**
     * Saves the list of incomes and expenses to a file.
     * Creates the data directory if it does not already exist.
     * Each income and expense is written in a pipe-delimited format.
     *
     * @param incomes     The list of income entries to be saved.
     * @param expenseList The list of expense entries to be saved.
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
     * Loads income and expense data from a file and populates the provided lists.
     * If the data file does not exist, the method exits silently.
     * Lines that do not conform to the expected format are skipped.
     *
     * @param incomes     The list to populate with loaded income entries.
     * @param expenseList The list to populate with loaded expense entries.
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
