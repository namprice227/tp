package seedu.address.logic.parser;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.UnarchiveCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new UnarchiveCommand object.
 */
public class UnarchiveCommandParser implements Parser<UnarchiveCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the UnarchiveCommand
     * and returns an UnarchiveCommand object for execution.
     * @throws ParseException if the user input does not conform to the expected format.
     */
    @Override
    public UnarchiveCommand parse(String args) throws ParseException {
        try {
            Index index = ParserUtil.parseIndex(args);
            return new UnarchiveCommand(index.getOneBased());
        } catch (ParseException pe) {
            throw new ParseException(
                String.format("Invalid command format! \n%s", UnarchiveCommand.MESSAGE_USAGE), pe);
        }
    }
}
