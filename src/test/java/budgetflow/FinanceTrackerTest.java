package budgetflow;

import budgetflow.expense.Expense;
import budgetflow.expense.ExpenseList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;

//Currently not used for testing

class FinanceTrackerTest {
    public static final String EMPTY_EXPENSE_LIST_MESSAGE =
            "There is currently no expense in your list right now. Please add more expenses to continue";

    /**
     * Empties the persistent file "data/budgetflow.txt" and resets any static fields before each test.
     */
    @BeforeEach
    void setUp() throws Exception {
        // Set the persistent file path relative to the project root.
        Path dataDir = Paths.get("data");
        if (!Files.exists(dataDir)) {
            Files.createDirectories(dataDir);
        }
        Path filePath = dataDir.resolve("budgetflow.txt");
        if (Files.exists(filePath)) {
            Files.write(filePath, new byte[0]);
        } else {
            Files.createFile(filePath);
        }
        System.out.println("Cleared file: " + filePath.toAbsolutePath());

        // Reset static fields in ExpenseList, if they exist.
        try {
            Field innerListField = ExpenseList.class.getDeclaredField("innerList");
            innerListField.setAccessible(true);
            if (Modifier.isStatic(innerListField.getModifiers())) {
                innerListField.set(null, new ArrayList<Expense>());
            }
        } catch (NoSuchFieldException e) {
            // No static innerList found.
        }
        try {
            Field totalExpensesField = ExpenseList.class.getDeclaredField("totalExpenses");
            totalExpensesField.setAccessible(true);
            if (Modifier.isStatic(totalExpensesField.getModifiers())) {
                totalExpensesField.set(null, 0.0);
            }
        } catch (NoSuchFieldException e) {
            // No static totalExpenses found.
        }
    }
}
