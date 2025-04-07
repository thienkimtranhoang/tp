package budgetflow.storage;

import budgetflow.command.ListIncomeCommand;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Handles the saving and loading of the saving goal for the BudgetFlow application.
 * This class reads and writes the saving goal to a text file located at ./data/saving_goal.txt.
 */
public class SavingGoalManager {
    private static final Logger logger = Logger.getLogger(
            SavingGoalManager.class.getName());
    private static final String SAVING_GOAL_FILE_PATH = "./data/saving_goal.txt";

    /**
     * Saves the specified saving goal to a file.
     * If the file or directory does not exist, it is created.
     *
     * @param savingGoal The saving goal to be saved.
     */
    public void saveSavingGoal(double savingGoal) {
        try {
            File directory = new File("./data");
            if (!directory.exists()) {
                directory.mkdirs();
            }
            FileWriter fileWriter = new FileWriter(SAVING_GOAL_FILE_PATH);
            BufferedWriter writer = new BufferedWriter(fileWriter);
            writer.write(String.valueOf(savingGoal));
            writer.newLine();
            writer.close();
            logger.info("Saving goal saved successfully: " + savingGoal);
        } catch (IOException e) {
            System.out.println("Error saving saving goal: " + e.getMessage());
            logger.severe("Error saving saving goal: " + e.getMessage());
        }
    }

    /**
     * Loads the saving goal from the file and sets it in {@link ListIncomeCommand}.
     * If the file does not exist or contains invalid data, the method handles the error gracefully.
     */
    public void loadSavingGoal() {
        File file = new File(SAVING_GOAL_FILE_PATH);
        if (!file.exists()) {
            logger.info("Saving goal file does not exist");
            return;
        }
        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader reader = new BufferedReader(fileReader);
            String line = reader.readLine();
            if (line != null) {
                try {
                    double savingGoal = Double.parseDouble(line);
                    ListIncomeCommand.setSavingGoal(savingGoal);
                    logger.info("Saving goal loaded successfully: " + savingGoal);
                } catch (NumberFormatException e) {
                    System.out.println("Error parsing saving goal: " + e.getMessage());
                    logger.warning("Error parsing saving goal: " + e.getMessage());
                }
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("Error loading saving goal: " + e.getMessage());
            logger.severe("Error loading saving goal: " + e.getMessage());
        }
    }
}
