package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.TagCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.tag.Tag;

/**
 * Parses input arguments and creates a new TagCommand object.
 */
public class TagCommandParser implements Parser<TagCommand> {

  /**
   * Parses the given {@code String} of arguments in the context of the TagCommand
   * and returns a TagCommand object for execution.
   * @throws ParseException if the user input does not conform to the expected format.
   */
  public TagCommand parse(String args) throws ParseException {
    ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_TAG);

    if (argMultimap.getPreamble().isEmpty() || !argMultimap.getValue(PREFIX_TAG).isPresent()) {
      throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, TagCommand.MESSAGE_USAGE));
    }

    try {
      Index index = ParserUtil.parseIndex(argMultimap.getPreamble());
      Set<Tag> tagList = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));

      if (tagList.isEmpty()) {
        throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, TagCommand.MESSAGE_USAGE));
      }

      return new TagCommand(index, tagList);
    } catch (ParseException e) {
      throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, TagCommand.MESSAGE_USAGE));
    }
  }
}
