package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.parser.exceptions.ParseException;

public class ClearCommandParserTest {

    private final ClearCommandParser parser = new ClearCommandParser();

    @Test
    public void parse_emptyArgs_returnsClearCommand() throws Exception {
        ClearCommand command = parser.parse("");
        assertEquals(new ClearCommand(), command);
    }

    @Test
    public void parse_nonEmptyArgs_throwsParseException() {
        assertThrows(ParseException.class, () -> parser.parse("extra"));
    }
}
