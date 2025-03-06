package budgetflow;

import java.util.Scanner;

public class MainTracker {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        FinanceTracker financeTracker = new FinanceTracker(scanner);

        System.out.println("Welcome to Finance Tracker!");
        System.out.println("You can track incomes and expenses here.");

        while (true) {
            System.out.println("What would you like to do?");
            String input = scanner.nextLine().trim();

            if (FinanceTracker.COMMAND_EXIT.equals(input)) {
                System.out.println("Goodbye!");
                break;
            }

            financeTracker.processCommand(input);
        }

        scanner.close();
    }
}
