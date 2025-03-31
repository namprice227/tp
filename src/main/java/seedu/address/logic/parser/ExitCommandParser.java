package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments for the ExitCommand.
 */
public class ExitCommandParser implements Parser<ExitCommand> {

  /**
   * Parses the given {@code String} of arguments in the context of the ExitCommand
   * and returns an ExitCommand object for execution.
   *
   * @throws ParseException if the user input does not conform to the expected format.
   */
  @Override
  public ExitCommand parse(String args) throws ParseException {
    String trimmedArgs = args.trim();

    if (!trimmedArgs.isEmpty()) {
      throw new ParseException(ExitCommand.MESSAGE_INVALID_COMMAND);
    }

    return new ExitCommand(args);
  }
}
