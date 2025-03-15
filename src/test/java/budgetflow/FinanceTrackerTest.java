package budgetflow;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;



class FinanceTrackerTest {
    public static final String EMPTY_EXPENSE_LIST_MESSAGE =
            "There is currently no expense in your list right now. Please add more expenses to continue";
    private FinanceTracker financeTracker;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    @Test
    void viewAllExpenses_normalTest() {

        // Create FinanceTracker with predefined expenses
        FinanceTracker financeTracker = getFinanceTracker3Expenses();

        // Capture system output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        // Act
        financeTracker.viewAllExpenses();

        // Reset System.out
        System.setOut(originalOut);

        // Assert
        String expectedOutput = "Expenses log: " + System.lineSeparator() +
                "Lunch | $12.50 | 2025-03-13" + System.lineSeparator() +
                "Transport | $3.20 | 2025-03-12" + System.lineSeparator() +
                "Groceries | $25.00 | 2025-03-11" + System.lineSeparator() + System.lineSeparator() +
                "Total Expenses: $40.70" + System.lineSeparator();

        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    void viewAllExpenses_emptyList() {
        FinanceTracker financeTracker = new FinanceTracker(new Scanner(System.in));
        // Capture system output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        // Act
        financeTracker.viewAllExpenses();

        // Reset System.out
        System.setOut(originalOut);

        // Assert
        String expectedOutput = "Expenses log: " + System.lineSeparator() +
                EMPTY_EXPENSE_LIST_MESSAGE + System.lineSeparator() +
                "Total Expenses: $0.00" + System.lineSeparator();

        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    void findExpense_found1Expense() {
        // Create FinanceTracker with predefined expenses
        FinanceTracker financeTracker = getFinanceTracker3Expenses();

        // Capture system output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        // Act
        financeTracker.findExpense("find-expense Lunch");

        // Reset System.out
        System.setOut(originalOut);
        // Assert
        String expectedOutput = "Here are all matching expenses: " + System.lineSeparator() +
                "Lunch | $12.50 | 2025-03-13" + System.lineSeparator();
        assertEquals(expectedOutput, outContent.toString());
    }

    private static FinanceTracker getFinanceTracker3Expenses() {
        FinanceTracker financeTracker = new FinanceTracker(new Scanner(System.in));
        financeTracker.logExpense("log-expense desc/Lunch amt/12.5 d/2025-03-13");
        financeTracker.logExpense("log-expense desc/Transport amt/3.2 d/2025-03-12");
        financeTracker.logExpense("log-expense desc/Groceries amt/25.0 d/2025-03-11");
        return financeTracker;
    }

    @Test
    void findExpense_foundMultipleExpenses() {
        // Create FinanceTracker with predefined expenses
        FinanceTracker financeTracker = getFinanceTracker5Expenses();

        // Capture system output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        // Act
        financeTracker.findExpense("find-expense Lunch");

        // Reset System.out
        System.setOut(originalOut);
        // Assert
        String expectedOutput = "Here are all matching expenses: " + System.lineSeparator() +
                "Lunch | $12.50 | 2025-03-13" + System.lineSeparator() +
                "LateLunch | $13.50 | 2025-03-14" + System.lineSeparator() +
                "ExpensiveLunch | $30.00 | 2025-03-15" + System.lineSeparator();
        assertEquals(expectedOutput, outContent.toString());
    }

    private static FinanceTracker getFinanceTracker5Expenses() {
        FinanceTracker financeTracker = new FinanceTracker(new Scanner(System.in));
        financeTracker.logExpense("log-expense desc/Lunch amt/12.5 d/2025-03-13");
        financeTracker.logExpense("log-expense desc/Transport amt/3.2 d/2025-03-12");
        financeTracker.logExpense("log-expense desc/LateLunch amt/13.5 d/2025-03-14");
        financeTracker.logExpense("log-expense desc/Groceries amt/25.0 d/2025-03-11");
        financeTracker.logExpense("log-expense desc/ExpensiveLunch amt/30.0 d/2025-03-15");
        return financeTracker;
    }

    @Test
    void findExpense_foundNoMatchingExpense() {
        // Create FinanceTracker with predefined expenses
        FinanceTracker financeTracker = getFinanceTracker3Expenses();

        // Capture system output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        // Act
        financeTracker.findExpense("find-expense IAmDummy");

        // Reset System.out
        System.setOut(originalOut);
        // Assert
        String expectedOutput = "Sorry, I cannot find any expenses matching your keyword: IAmDummy" +
                System.lineSeparator();
        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    void findExpenses_noKeyword() {
        // Create FinanceTracker with predefined expenses
        FinanceTracker financeTracker = getFinanceTracker3Expenses();

        // Capture system output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        // Act
        financeTracker.findExpense("find-expense");
        // Reset System.out
        System.setOut(originalOut);
        // Assert
        String expectedOutput = "Error: Missing keyword" +
                System.lineSeparator();
        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    void addIncome_normalTest() {
        // Create FinanceTracker
        FinanceTracker financeTracker = new FinanceTracker(new Scanner(System.in));

        // Capture system output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        // Act
        financeTracker.addIncome("add category/Salary amt/2500.50 d/2025-03-15");

        // Reset System.out
        System.setOut(originalOut);

        // Assert
        String expectedOutput = "Income added: Salary, Amount: $2500.50, Date: 2025-03-15" + System.lineSeparator();
        assertEquals(expectedOutput, outContent.toString());

        // Verify income was added (through output verification only since we can't access private list)
        assertTrue(outContent.toString().contains("Income added: Salary"));
        assertTrue(outContent.toString().contains("Amount: $2500.50"));
        assertTrue(outContent.toString().contains("Date: 2025-03-15"));
    }

    @Test
    void addIncome_missingCategory() {
        // Create FinanceTracker
        FinanceTracker financeTracker = new FinanceTracker(new Scanner(System.in));

        // Capture system output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        // Act
        financeTracker.addIncome("add amt/1000.00 d/2025-03-16");

        // Reset System.out
        System.setOut(originalOut);

        // Assert
        String expectedOutput = "Error: Income category is required." + System.lineSeparator();
        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    void addIncome_missingAmount() {
        // Create FinanceTracker
        FinanceTracker financeTracker = new FinanceTracker(new Scanner(System.in));

        // Capture system output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        // Act
        financeTracker.addIncome("add category/Bonus d/2025-03-17");

        // Reset System.out
        System.setOut(originalOut);

        // Assert
        String expectedOutput = "Error: Income amount is required." + System.lineSeparator();
        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    void addIncome_missingDate() {
        // Create FinanceTracker
        FinanceTracker financeTracker = new FinanceTracker(new Scanner(System.in));

        // Capture system output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        // Act
        financeTracker.addIncome("add category/Freelance amt/500.00");

        // Reset System.out
        System.setOut(originalOut);

        // Assert
        String expectedOutput = "Error: Income date is required." + System.lineSeparator();
        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    void addIncome_invalidAmount() {
        // Create FinanceTracker
        FinanceTracker financeTracker = new FinanceTracker(new Scanner(System.in));

        // Capture system output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        // Act
        financeTracker.addIncome("add category/Refund amt/abc d/2025-03-18");

        // Reset System.out
        System.setOut(originalOut);

        // Assert
        String expectedOutput = "Error: Income amount is required." + System.lineSeparator();
        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    void logExpense_normalTest() {
        // Create FinanceTracker
        FinanceTracker financeTracker = new FinanceTracker(new Scanner(System.in));

        // Capture system output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        // Act
        financeTracker.logExpense("log-expense desc/Groceries amt/85.75 d/2025-03-19");

        // Reset System.out
        System.setOut(originalOut);

        // Assert
        String expectedOutput = "Expense logged: Groceries, Amount: $85.75, Date: 2025-03-19" + System.lineSeparator();
        assertEquals(expectedOutput, outContent.toString());

        // Verify expense was added (through output verification only since we can't access private list)
        assertTrue(outContent.toString().contains("Expense logged: Groceries"));
        assertTrue(outContent.toString().contains("Amount: $85.75"));
        assertTrue(outContent.toString().contains("Date: 2025-03-19"));
    }

    @Test
    void logExpense_missingDescription() {
        // Create FinanceTracker
        FinanceTracker financeTracker = new FinanceTracker(new Scanner(System.in));

        // Capture system output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        // Act
        financeTracker.logExpense("log-expense amt/45.50 d/2025-03-20");

        // Reset System.out
        System.setOut(originalOut);

        // Assert
        String expectedOutput = "Error: Expense description is required." + System.lineSeparator();
        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    void logExpense_missingAmount() {
        // Create FinanceTracker
        FinanceTracker financeTracker = new FinanceTracker(new Scanner(System.in));

        // Capture system output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        // Act
        financeTracker.logExpense("log-expense desc/Restaurant d/2025-03-21");

        // Reset System.out
        System.setOut(originalOut);

        // Assert
        String expectedOutput = "Error: Expense amount is required." + System.lineSeparator();
        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    void logExpense_missingDate() {
        // Create FinanceTracker
        FinanceTracker financeTracker = new FinanceTracker(new Scanner(System.in));

        // Capture system output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        // Act
        financeTracker.logExpense("log-expense desc/Coffee amt/5.25");

        // Reset System.out
        System.setOut(originalOut);

        // Assert
        String expectedOutput = "Error: Expense date is required." + System.lineSeparator();
        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    void logExpense_invalidAmount() {
        // Create FinanceTracker
        FinanceTracker financeTracker = new FinanceTracker(new Scanner(System.in));

        // Capture system output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        // Act
        financeTracker.logExpense("log-expense desc/Books amt/twenty d/2025-03-22");

        // Reset System.out
        System.setOut(originalOut);

        // Assert
        String expectedOutput = "Error: Expense amount is required." + System.lineSeparator();
        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    void logExpense_thenViewExpenses() {
        // Create FinanceTracker
        FinanceTracker financeTracker = new FinanceTracker(new Scanner(System.in));

        // Add expense
        financeTracker.logExpense("log-expense desc/Dinner amt/45.50 d/2025-03-23");

        // Capture system output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        // View expenses to verify
        financeTracker.viewAllExpenses();

        // Reset System.out
        System.setOut(originalOut);

        // Assert the expense was added by checking the viewAllExpenses output
        assertTrue(outContent.toString().contains("Dinner | $45.50 | 2025-03-23"));
        assertTrue(outContent.toString().contains("Total Expenses: $45.50"));
    }

    @Test
    void addIncome_thenAddExpense_thenViewExpenses() {
        // Create FinanceTracker
        FinanceTracker financeTracker = new FinanceTracker(new Scanner(System.in));

        // Add income and expense
        financeTracker.addIncome("add category/Salary amt/1000.00 d/2025-03-24");
        financeTracker.logExpense("log-expense desc/Rent amt/500.00 d/2025-03-24");
        financeTracker.logExpense("log-expense desc/Utilities amt/100.00 d/2025-03-24");

        // Capture system output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        // View expenses to verify
        financeTracker.viewAllExpenses();

        // Reset System.out
        System.setOut(originalOut);

        // Assert the expenses were added by checking the viewAllExpenses output
        assertTrue(outContent.toString().contains("Rent | $500.00 | 2025-03-24"));
        assertTrue(outContent.toString().contains("Utilities | $100.00 | 2025-03-24"));
        assertTrue(outContent.toString().contains("Total Expenses: $600.00"));
    }

}
