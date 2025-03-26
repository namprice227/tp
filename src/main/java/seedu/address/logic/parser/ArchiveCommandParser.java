package seedu.address.logic.parser;

import seedu.address.logic.commands.ArchiveCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.commons.core.index.Index;

/**
 * Parses input arguments and creates a new ArchiveCommand object
 */
public class ArchiveCommandParser implements Parser<ArchiveCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the ArchiveCommand
     * and returns an ArchiveCommand object for execution.
     * @throws ParseException if the user input does not conform to the expected format
     */
    @Override
    public ArchiveCommand parse(String args) throws ParseException {
        try {
            Index index = ParserUtil.parseIndex(args);
            return new ArchiveCommand(index.getZeroBased());
        } catch (ParseException pe) {
            throw new ParseException(
                String.format("Invalid command format! \n%s", ArchiveCommand.MESSAGE_USAGE), pe);
        }
    }
}
