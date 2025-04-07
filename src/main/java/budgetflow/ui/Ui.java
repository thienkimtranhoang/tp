package budgetflow.ui;

import java.util.Scanner;

/**
 * Handles basic user interface interaction including reading commands from the user and printing messages.
 *
 * @author QuyDatNguyen
 * @author IgoyAI
 */
public class Ui {
    private static final int SCREEN_WIDTH = 60;
    private static final Scanner SCANNER = new Scanner(System.in);

    //@@author IgoyAI
    /**
     * Displays a robust welcome screen with a prominent header, borders, and clear instructions.
     */
    public void showWelcome() {
        String border = "=".repeat(SCREEN_WIDTH);
        System.out.println(border);
        System.out.println(centerText("Welcome to Budgetflow!", SCREEN_WIDTH));
        System.out.println(border);
        System.out.println();
        System.out.println(centerText("Manage your incomes and expenses with ease.", SCREEN_WIDTH));
        System.out.println(centerText("Achieve your saving goals and plan your future.", SCREEN_WIDTH));
        System.out.println();
        System.out.println(border);
        System.out.println(centerText("Type 'help' to see available commands.", SCREEN_WIDTH));
        System.out.println(border);
    }

    //@@author IgoyAI
    /**
     * Centers the provided text within the given width.
     *
     * @param text  the text to center.
     * @param width the total width for centering.
     * @return a string with the text centered.
     */
    private String centerText(String text, int width) {
        if (text.length() >= width) {
            return text;
        }
        int padding = (width - text.length()) / 2;
        return " ".repeat(padding) + text;
    }

    //@@author QuyDatNguyen
    /**
     * Reads the text entered by the user, ignoring all leading and trailing whitespace.
     *
     * @return the user input.
     */
    public String readCommand() {
        System.out.println("What would you like to do?");
        String input = SCANNER.nextLine().trim();
        while (input.isEmpty()) {
            input = SCANNER.nextLine().trim();
        }
        return input;
    }

    //@@author QuyDatNguyen
    /**
     * Displays an error message to the user.
     *
     * @param error the error message.
     */
    public void printError(String error) {
        System.out.println(error);
    }

    //@@author QuyDatNguyen
    /**
     * Prints out the result or message to the user after successfully executing a command.
     *
     * @param message the message to print.
     */
    public void printCommandMessage(String message) {
        System.out.println(message);
    }
}
