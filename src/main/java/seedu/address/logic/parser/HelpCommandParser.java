package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments for the HelpCommand.
 */
public class HelpCommandParser implements Parser<HelpCommand> {

    /**
   * Parses the given {@code String} of arguments in the context of the HelpCommand
   * and returns a HelpCommand object for execution.
   * @throws ParseException if the user input does not conform to the expected format.
   */
    @Override
    public HelpCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();

        if (!trimmedArgs.isEmpty()) {
            throw new ParseException(
              String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }

        return new HelpCommand(args);
    }
}
