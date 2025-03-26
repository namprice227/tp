package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_RELATIONSHIP;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EmergencyCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Relationship;

/**
 * Parses input arguments and creates a new EmergencyCommand object
 */
public class EmergencyCommandParser implements Parser<EmergencyCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the EmergencyCommand
     * and returns an EmergencyCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public EmergencyCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_PHONE, PREFIX_RELATIONSHIP);

        Index index;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    EmergencyCommand.MESSAGE_USAGE), pe);
        }

        if (!argMultimap.getValue(PREFIX_NAME).isPresent()
                || !argMultimap.getValue(PREFIX_PHONE).isPresent()
                || !argMultimap.getValue(PREFIX_RELATIONSHIP).isPresent()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    EmergencyCommand.MESSAGE_USAGE));
        }

        Name name = ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get());
        Phone phone = ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE).get());
        Relationship relationship = ParserUtil.parseRelationship(argMultimap.getValue(PREFIX_RELATIONSHIP).get());

        return new EmergencyCommand(index, name, phone, relationship);
    }
}
