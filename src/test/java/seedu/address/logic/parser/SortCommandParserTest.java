package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.SortCommand;

public class SortCommandParserTest {

    private SortCommandParser parser = new SortCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validArgs_returnsSortCommand() {
        SortCommand expectedNameCommand = new SortCommand("name");
        assertParseSuccess(parser, "name", expectedNameCommand);
        assertParseSuccess(parser, "NAME", expectedNameCommand);
        assertParseSuccess(parser, "  name  ", expectedNameCommand);

        SortCommand expectedAppointmentCommand = new SortCommand("appointment");
        assertParseSuccess(parser, "appointment", expectedAppointmentCommand);
        assertParseSuccess(parser, "APPOINTMENT", expectedAppointmentCommand);
        assertParseSuccess(parser, "  appointment  ", expectedAppointmentCommand);
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        // invalid fields
        assertParseFailure(parser, "age",
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
        assertParseFailure(parser, "phone",
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
        assertParseFailure(parser, "123",
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
    }
}
