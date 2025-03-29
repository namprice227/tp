package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments for the ClearCommand.
 */
public class ClearCommandParser implements Parser<ClearCommand> {

    /**
   * Parses the given {@code String} of arguments in the context of the ClearCommand
   * and returns a ClearCommand object for execution.
   * @throws ParseException if the user input contains additional parameters
   */
    @Override
    public ClearCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();

        // Check if there are any extra parameters
        if (!trimmedArgs.isEmpty()) {
            throw new ParseException(
              String.format(MESSAGE_INVALID_COMMAND_FORMAT, ClearCommand.MESSAGE_USAGE));
        }

        return new ClearCommand();
    }
}
