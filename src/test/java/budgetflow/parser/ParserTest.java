package budgetflow.parser;

import budgetflow.command.AddIncomeCommand;
import budgetflow.command.Command;
import budgetflow.command.CompareExpenseCommand;
import budgetflow.command.DeleteExpenseCommand;
import budgetflow.command.DeleteIncomeCommand;
import budgetflow.command.ExitCommand;
import budgetflow.command.FilterIncomeByAmountCommand;
import budgetflow.command.FilterIncomeByCategoryCommand;
import budgetflow.command.FilterIncomeByDateCommand;
import budgetflow.command.FilterIncomeCommand;
import budgetflow.command.FindExpenseCommand;
import budgetflow.command.HelpCommand;
import budgetflow.command.ListIncomeCommand;
import budgetflow.command.LogExpenseCommand;
import budgetflow.command.SetSavingGoalCommand;
import budgetflow.command.UpdateExpenseCommand;
import budgetflow.command.UpdateIncomeCommand;
import budgetflow.command.ViewAllExpensesCommand;
import budgetflow.exception.UnknownCommandException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ParserTest {
    // @@author(IgoyAI) (MODIFIED)
    @Test
    void testAddIncomeCommand() throws UnknownCommandException {
        String input = "add category/Salary amt/5000 d/15-03-2023";
        Command command = Parser.getCommandFromInput(input);
        assertInstanceOf(AddIncomeCommand.class, command);
    }

    @Test
    void testSetSavingGoalCommand() throws UnknownCommandException {
        String input = "set-saving-goal 10000";
        Command command = Parser.getCommandFromInput(input);
        assertInstanceOf(SetSavingGoalCommand.class, command);
    }

    @Test
    void testLogExpenseCommand() throws UnknownCommandException {
        String input = "log-expense category/Food desc/Lunch amt/15 d/20-03-2023";
        Command command = Parser.getCommandFromInput(input);
        assertInstanceOf(LogExpenseCommand.class, command);
    }

    @Test
    void testDeleteIncomeCommand() throws UnknownCommandException {
        String input = "delete-income 1";
        Command command = Parser.getCommandFromInput(input);
        assertInstanceOf(DeleteIncomeCommand.class, command);
    }

    @Test
    void testListIncomeCommand() throws UnknownCommandException {
        String input = "list income";
        Command command = Parser.getCommandFromInput(input);
        assertInstanceOf(ListIncomeCommand.class, command);
    }

    @Test
    void testDeleteExpenseCommand() throws UnknownCommandException {
        String input = "delete-expense 2";
        Command command = Parser.getCommandFromInput(input);
        assertInstanceOf(DeleteExpenseCommand.class, command);
    }

    @Test
    void testViewAllExpensesCommand() throws UnknownCommandException {
        String input = "list-expense";
        Command command = Parser.getCommandFromInput(input);
        assertInstanceOf(ViewAllExpensesCommand.class, command);
    }

    @Test
    void testFindExpenseCommand() throws UnknownCommandException {
        String input = "filter-expense /desc Lunch";
        Command command = Parser.getCommandFromInput(input);
        assertInstanceOf(FindExpenseCommand.class, command);
    }

    @Test
    void testExitCommand() throws UnknownCommandException {
        String input = "exit";
        Command command = Parser.getCommandFromInput(input);
        assertInstanceOf(ExitCommand.class, command);
    }

    @Test
    void testCompareExpenseCommand() throws UnknownCommandException {
        String input = "compare 03-2023 04-2023";
        Command command = Parser.getCommandFromInput(input);
        assertInstanceOf(CompareExpenseCommand.class, command);
    }

    @Test
    void testUpdateExpenseCommand() throws UnknownCommandException {
        String input = "update-expense 2 desc/Brunch amt/20 d/21-03-2023";
        Command command = Parser.getCommandFromInput(input);
        assertInstanceOf(UpdateExpenseCommand.class, command);
    }

    @Test
    void testUpdateIncomeCommand() throws UnknownCommandException {
        String input = "update-income amt/5500";
        Command command = Parser.getCommandFromInput(input);
        assertInstanceOf(UpdateIncomeCommand.class, command);
    }

    @Test
    void testFilterIncomeCommandGeneral() throws UnknownCommandException {
        String input = "filter-income";
        Command command = Parser.getCommandFromInput(input);
        assertInstanceOf(FilterIncomeCommand.class, command);
    }

    @Test
    void testFilterIncomeByDateCommand() throws UnknownCommandException {
        String input = "filter-income date from/01-03-2023 to/31-03-2023";
        Command command = Parser.getCommandFromInput(input);
        assertInstanceOf(FilterIncomeByDateCommand.class, command);
    }

    @Test
    void testFilterIncomeByAmountCommand() throws UnknownCommandException {
        String input = "filter-income amount from/1000 to/5000";
        Command command = Parser.getCommandFromInput(input);
        assertInstanceOf(FilterIncomeByAmountCommand.class, command);
    }

    @Test
    void testFilterIncomeByCategoryCommand() throws UnknownCommandException {
        String input = "filter-income category Salary";
        Command command = Parser.getCommandFromInput(input);
        assertInstanceOf(FilterIncomeByCategoryCommand.class, command);
    }

    @Test
    void testHelpCommand() throws UnknownCommandException {
        String input = "help";
        Command command = Parser.getCommandFromInput(input);
        assertInstanceOf(HelpCommand.class, command);
    }

    @Test
    void testUnknownCommand() {
        String input = "foo";
        UnknownCommandException exception = assertThrows(
                UnknownCommandException.class,
                () -> Parser.getCommandFromInput(input)
        );
        String expectedMessage = "I don't understand that command. Try again.";
        assertEquals(expectedMessage, exception.getMessage());
    }
}
