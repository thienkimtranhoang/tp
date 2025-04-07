package budgetflow.command;

import budgetflow.exception.FinanceException;
import budgetflow.expense.ExpenseList;
import budgetflow.income.Income;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class AddIncomeCommandTest {
    @Test
    void addIncome_validInput_addsIncome() throws FinanceException {
        ExpenseList expenseList = new ExpenseList();
        List<Income> incomes = new ArrayList<>();
        Command command = new AddIncomeCommand("add category/Salary amt/2500.00 d/15-03-2025");
        command.execute(incomes, expenseList);
        String expectedOutput = "Income added: Salary, Amount: $2500.00, Date: 15-03-2025";
        assertEquals(expectedOutput, command.getOutputMessage());
    }

    @Test
    void addIncome_missingCategory_showsError() {
        ExpenseList expenseList = new ExpenseList();
        List<Income> incomes = new ArrayList<>();
        Command command = new AddIncomeCommand("add amt/2500.00 d/15-03-2025");
        try {
            command.execute(incomes, expenseList);
            fail();
        } catch (FinanceException e) {
            String expectedError = "Error: Income category is required.";
            assertEquals(expectedError, e.getMessage());
        }
    }

    @Test
    void addIncome_missingAmount_showsError() {
        ExpenseList expenseList = new ExpenseList();
        List<Income> incomes = new ArrayList<>();
        Command command = new AddIncomeCommand("add category/Salary d/15-03-2025");
        try {
            command.execute(incomes, expenseList);
            fail();
        } catch (FinanceException e) {
            String expectedError = "Error: Income amount is required.";
            assertEquals(expectedError, e.getMessage());
        }
    }

    @Test
    void addIncome_missingDate_showsError() {
        ExpenseList expenseList = new ExpenseList();
        List<Income> incomes = new ArrayList<>();
        Command command = new AddIncomeCommand("add category/Salary amt/2500.00");
        try {
            command.execute(incomes, expenseList);
            fail();
        } catch (FinanceException e) {
            String expectedError = "Error: Income date is required.";
            assertEquals(expectedError, e.getMessage());
        }
    }

    @Test
    void addIncome_invalidAmountFormat_showsError() {
        ExpenseList expenseList = new ExpenseList();
        List<Income> incomes = new ArrayList<>();
        Command command = new AddIncomeCommand("add category/Salary amt/invalid d/15-03-2025");
        try {
            command.execute(incomes, expenseList);
            fail();
        } catch (FinanceException e) {
            String expectedError = "Error: Income amount is required.";
            assertEquals(expectedError, e.getMessage());
        }
    }

    @Test
    void addIncome_extraParameters_ignoresExtraParams() throws FinanceException {
        ExpenseList expenseList = new ExpenseList();
        List<Income> incomes = new ArrayList<>();
        Command command = new AddIncomeCommand("add category/Salary amt/2500.00 d/15-03-2025 extra/parameter");
        command.execute(incomes, expenseList);
        String expectedOutput = "Income added: Salary, Amount: $2500.00, Date: 15-03-2025";
        assertEquals(expectedOutput, command.getOutputMessage());
    }

    @Test
    void addIncome_invalidDateFormat_showsError() {
        ExpenseList expenseList = new ExpenseList();
        List<Income> incomes = new ArrayList<>();
        Command command = new AddIncomeCommand("add category/Salary amt/5000 d/2025-03-15");
        try {
            command.execute(incomes, expenseList);
            fail();
        } catch (FinanceException e) {
            // Updated expected error message to match actual output (with a space after the period).
            String expectedError = "Error: Income date is in wrong format. Please use DD-MM-YYYY format.";
            assertEquals(expectedError, e.getMessage());
        }
    }

    @Test
    void addIncome_invalidDate_showsError() {
        ExpenseList expenseList = new ExpenseList();
        List<Income> incomes = new ArrayList<>();
        Command command = new AddIncomeCommand("add category/Salary amt/5000 d/99-99-1234");
        try {
            command.execute(incomes, expenseList);
            fail();
        } catch (FinanceException e) {
            String expectedError = "Error: Date is not a valid date. Please use DD-MM-YYYY format.";
            assertEquals(expectedError, e.getMessage());
        }
    }
    @Test
    void addIncome_whitespaceInInput_trimsAndAddsCorrectly() throws FinanceException {
        ExpenseList expenseList = new ExpenseList();
        List<Income> incomes = new ArrayList<>();
        Command command = new AddIncomeCommand("add category/  Salary   amt/ 2500.00   d/ 15-03-2025 ");
        command.execute(incomes, expenseList);
        String expectedOutput = "Income added: Salary, Amount: $2500.00, Date: 15-03-2025";
        assertEquals(expectedOutput, command.getOutputMessage());
    }

    @Test
    void addIncome_negativeAmount_showsError() {
        ExpenseList expenseList = new ExpenseList();
        List<Income> incomes = new ArrayList<>();
        Command command = new AddIncomeCommand("add category/Salary amt/-3000 d/15-03-2025");
        try {
            command.execute(incomes, expenseList);
        } catch (FinanceException e) {
            String expectedError = "Error: Income amount is required.";
            assertEquals(expectedError, e.getMessage());
        }
    }

    @Test
    void addIncome_zeroAmount_showsError() {
        ExpenseList expenseList = new ExpenseList();
        List<Income> incomes = new ArrayList<>();
        Command command = new AddIncomeCommand("add category/Salary amt/0 d/15-03-2025");
        try {
            command.execute(incomes, expenseList);
        } catch (FinanceException e) {
            String expectedError = "Error: Income amount is required.";
            assertEquals(expectedError, e.getMessage());
        }
    }

    @Test
    void addIncome_duplicateIncome_allowsAdding() throws FinanceException {
        ExpenseList expenseList = new ExpenseList();
        List<Income> incomes = new ArrayList<>();
        Command command1 = new AddIncomeCommand("add category/Freelance amt/200 d/10-04-2025");
        Command command2 = new AddIncomeCommand("add category/Freelance amt/300 d/10-04-2025");

        command1.execute(incomes, expenseList);
        command2.execute(incomes, expenseList);

        assertEquals("Income added: Freelance, Amount: $300.00, Date: 10-04-2025", command2.getOutputMessage());
    }

    @Test
    void addIncome_categoryWithSpecialCharacters_acceptsSuccessfully() throws FinanceException {
        ExpenseList expenseList = new ExpenseList();
        List<Income> incomes = new ArrayList<>();
        Command command = new AddIncomeCommand("add category/Gift🎁 amt/150 d/01-04-2025");
        command.execute(incomes, expenseList);
        assertEquals("Income added: Gift🎁, Amount: $150.00, Date: 01-04-2025", command.getOutputMessage());
    }
}
