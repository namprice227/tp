package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.logic.commands.SortCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new SortCommand object.
 */
public class SortCommandParser implements Parser<SortCommand> {

  /**
   * Parses the given {@code String} of arguments in the context of the SortCommand
   * and returns a SortCommand object for execution.
   * @throws ParseException if the user input does not conform to the expected format.
   */
  @Override
  public SortCommand parse(String args) throws ParseException {
    String trimmedArgs = args.trim();

    if (trimmedArgs.isEmpty()) {
      throw new ParseException(
              String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
    }

    // Check if sort field is valid
    String sortField = trimmedArgs.toLowerCase();
    if (!sortField.equals("name") && !sortField.equals("appointment")) {
      throw new ParseException(
              String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
    }

    return new SortCommand(sortField);
  }
}
