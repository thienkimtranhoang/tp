package budgetflow.ui;

import java.util.Scanner;

public class Ui {
    private static final String LINE = "=============================";
    private static final String LINE_DIVIDER = "___________________________";
    private static final Scanner SCANNER = new Scanner(System.in);

    /** Generates and print out the program message at the beginning of application */
    public void showWelcome() {
        System.out.println("Welcome to Finance Tracker!");
        System.out.println("You can track incomes and expenses here.");
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

    public void printLine() {
        System.out.println(LINE_DIVIDER);
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

