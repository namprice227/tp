package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.UndoCommand.MESSAGE_USAGE;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.UndoCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.testutil.TypicalPersons;

public class UndoCommandParserTest {
    private Model model = new ModelManager(TypicalPersons.getTypicalAddressBook(), new UserPrefs(),
        TypicalPersons.getTypicalArchivedBook());
    private UndoCommandParser parser = new UndoCommandParser(model);

    @Test
    public void parse_emptyArgs_success() throws ParseException {
        UndoCommand expectedCommand = new UndoCommand(model);
        assertParseSuccess(parser, "", expectedCommand);
    }

    @Test
    public void parse_whitespaceArgs_success() throws ParseException {
        UndoCommand expectedCommand = new UndoCommand(model);
        assertParseSuccess(parser, "   ", expectedCommand);
    }

    @Test
    public void parse_invalidArgs_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, MESSAGE_USAGE);
        assertParseFailure(parser, "some random string", expectedMessage);
    }

    /**
     * Asserts that the parsing of {@code userInput} is successful and the result matches {@code expectedCommand}
     */
    private void assertParseSuccess(UndoCommandParser parser, String userInput, UndoCommand expectedCommand) {
        try {
            UndoCommand command = parser.parse(userInput);
            assertEquals(expectedCommand, command);
        } catch (ParseException pe) {
            throw new IllegalArgumentException("Invalid userInput.", pe);
        }
    }

    /**
     * Asserts that the parsing of {@code userInput} is unsuccessful and the error message
     * equals to {@code expectedMessage}
     */
    private void assertParseFailure(UndoCommandParser parser, String userInput, String expectedMessage) {
        assertThrows(ParseException.class, expectedMessage, () -> parser.parse(userInput));
    }
}
