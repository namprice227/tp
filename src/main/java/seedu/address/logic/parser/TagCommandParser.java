package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ALLERGY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CONDITION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INSURANCE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG_DELETE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG_EDIT;

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
        // If no arguments are provided after the index, throw invalid command format
        if (args.trim().split("\\s+").length == 1) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, TagCommand.MESSAGE_USAGE));
        }

        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_ALLERGY,
                PREFIX_CONDITION, PREFIX_INSURANCE, PREFIX_TAG_DELETE, PREFIX_TAG_EDIT);

        // Check if any invalid prefixes are present in the command
        checkForInvalidPrefixes(args);

        if (argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, TagCommand.MESSAGE_USAGE));
        }

        // Parse index from the argument preamble
        Index index = ParserUtil.parseIndex(argMultimap.getPreamble());

        // Check if there are tags to add, delete, or edit
        Set<Tag> allergies = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_ALLERGY));
        Set<Tag> conditions = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_CONDITION));
        Set<Tag> insurances = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_INSURANCE));

        // Check for delete tags (td/)
        Set<Tag> tagsToDelete = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG_DELETE));

        // Check for edit tags (te/)
        Tag oldTag = null;
        Tag newTag = null;
        boolean isEditTagPresent = argMultimap.getValue(PREFIX_TAG_EDIT).isPresent();

        if (isEditTagPresent) {
            String editTags = argMultimap.getValue(PREFIX_TAG_EDIT).get();
            String[] split = editTags.split("=");

            if (split.length != 2) {
                throw new ParseException("Invalid format for editing a tag. Correct format: te/OLD_TAG=NEW_TAG");
            }
            oldTag = new Tag(split[0].trim());
            newTag = new Tag(split[1].trim());
        }

        // Ensure no tags are being added, deleted, or edited
        if (allergies.isEmpty() && conditions.isEmpty() && insurances.isEmpty()
                && tagsToDelete.isEmpty() && !isEditTagPresent) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, TagCommand.MESSAGE_USAGE));
        }

        // Ensure adding, deleting, and editing cannot happen together
        if (!tagsToDelete.isEmpty() && (!allergies.isEmpty() || !conditions.isEmpty() || !insurances.isEmpty())) {
            throw new ParseException("Cannot add and delete tags in the same command.");
        }
        if (!tagsToDelete.isEmpty() && isEditTagPresent) {
            throw new ParseException("Cannot delete and edit tags in the same command.");
        }
        if (isEditTagPresent && (!allergies.isEmpty() || !conditions.isEmpty() || !insurances.isEmpty())) {
            throw new ParseException("Cannot edit and add tags in the same command.");
        }

        return new TagCommand(index, allergies, conditions, insurances, tagsToDelete, oldTag, newTag);
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
                        || prefix.equals(PREFIX_INSURANCE.toString())
                        || prefix.equals(PREFIX_TAG_DELETE.toString())
                        || prefix.equals(PREFIX_TAG_EDIT.toString())) {
                    continue;
                }

                // If we get here, we found an invalid prefix
                throw new ParseException(MESSAGE_INVALID_TAG_PREFIX);
            }
        }
    }
}
