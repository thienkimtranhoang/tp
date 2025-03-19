package budgetflow.ui;

import java.util.Scanner;

public class Ui {
    public static final String WELCOME_MESSAGE = "Welcome to Finance Tracker!" + System.lineSeparator()
            + "You can track incomes and expenses here.";
    private static final Scanner SCANNER = new Scanner(System.in);

    /** Generates and print out the program message at the beginning of application */
    public void showWelcome() {
        System.out.println(WELCOME_MESSAGE);
    }

    /** Read the text entered by user, ignoring all leading and trailing whitespace */
    public String readCommand() {
        System.out.println("What would you like to do?");
        String input = SCANNER.nextLine().trim();
        while (input.isEmpty()) {
            input = SCANNER.nextLine().trim();
        }
        return input;
    }

    /** Show error to user after failing to execute the command*/
    public void printError (String error) {
        System.out.println(error);
    }

    /** Print out the result/ message to use after successfully executing the command */
    public void printCommandMessage (String message) {

        System.out.println(message);
    }
}

