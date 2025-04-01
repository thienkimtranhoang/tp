package budgetflow.parser;

import budgetflow.command.Command;
import budgetflow.command.ListIncomeCommand;
import budgetflow.exception.UnknownCommandException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest {

    @Test
    void getCommandFromInput_successTest() throws UnknownCommandException {
        String input = "list income";
        Command command = Parser.getCommandFromInput(input);
        assertInstanceOf(ListIncomeCommand.class, command);
    }

    @Test
    void getCommandFromInput_unknownInputTest() {
        String input = "foo ";
        try {
            Command command = Parser.getCommandFromInput(input);
            fail();
        } catch (UnknownCommandException e) {
            String expectedError = "I don't understand that command. Try again.";
            assertEquals(expectedError, e.getMessage());
        }
    }
}
