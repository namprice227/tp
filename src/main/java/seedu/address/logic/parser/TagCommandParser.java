package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ALLERGY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CONDITION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INSURANCE;

import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.TagCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.tag.Tag;

/**
 * Parses input arguments and creates a new TagCommand object
 */
public class TagCommandParser implements Parser<TagCommand> {

    public static final String MESSAGE_INVALID_TAG_PREFIX = "Invalid tag prefix. Only ta/, tc/, and ti/ are allowed.";

    @Override
    public TagCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_ALLERGY,
                PREFIX_CONDITION, PREFIX_INSURANCE);

        // Check if any invalid prefixes are present in the command
        checkForInvalidPrefixes(args);

        if (argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, TagCommand.MESSAGE_USAGE));
        }

        // Check if the only argument provided is a valid index and there are no tags
        Index index = ParserUtil.parseIndex(argMultimap.getPreamble());
        if (argMultimap.getAllValues(PREFIX_ALLERGY).isEmpty()
                && argMultimap.getAllValues(PREFIX_CONDITION).isEmpty()
                && argMultimap.getAllValues(PREFIX_INSURANCE).isEmpty()) {
            throw new ParseException("Error: Tags must be provided when adding a tag.");
        }

        Set<Tag> allergies = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_ALLERGY));
        Set<Tag> conditions = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_CONDITION));
        Set<Tag> insurances = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_INSURANCE));

        return new TagCommand(index, allergies, conditions, insurances);
    }

    /**
     * Checks if any invalid tag prefixes are present in the command.
     * Only ta/, tc/, and ti/ are valid prefixes.
     *
     * @param args the command arguments
     * @throws ParseException if an invalid prefix is detected
     */
    private void checkForInvalidPrefixes(String args) throws ParseException {
        // Extract all prefixes from the command
        String[] parts = args.trim().split("\\s+");

        for (String part : parts) {
            if (part.contains("/")) {
                String prefix = part.split("/")[0] + "/";

                // Skip if it's a valid prefix
                if (prefix.equals(PREFIX_ALLERGY.toString())
                        || prefix.equals(PREFIX_CONDITION.toString())
                        || prefix.equals(PREFIX_INSURANCE.toString())) {
                    continue;
                }

                // If we get here, we found an invalid prefix
                throw new ParseException(MESSAGE_INVALID_TAG_PREFIX);
            }
        }
    }
}
