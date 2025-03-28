package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ALLERGY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CONDITION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INSURANCE;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;

/**
 * Command that manages adding, deleting, or editing tags for a person in HealthSync.
 * This command allows for the manipulation of tags such as allergies, conditions, and insurances
 * associated with a specific person in the HealthSync application.
 */
public class TagCommand extends Command {

    public static final String COMMAND_WORD = "tag";
    public static final String MESSAGE_USAGE = COMMAND_WORD
          + ": Adds, deletes, or edits different types of tags to an existing patient in HealthSync.\n"
          + "Parameters:\n"
            + "  Adding tags: INDEX " + PREFIX_ALLERGY + "TAG " + PREFIX_CONDITION + "TAG " + PREFIX_INSURANCE + "TAG\n"
            + "  Deleting a tag: INDEX td/TAG\n"
            + "  Editing a tag: INDEX te/OLD_TAG=NEW_TAG\n"
          + "Example:\n"
            + "  " + COMMAND_WORD + " 1 " + PREFIX_ALLERGY + "Peanuts\n"
            + "  " + COMMAND_WORD + " 1 " + PREFIX_CONDITION + "Asthma\n"
            + "  " + COMMAND_WORD + " 1 " + PREFIX_INSURANCE + "Medisave\n"
            + "  " + COMMAND_WORD + " td/Peanuts\n"
            + "  " + COMMAND_WORD + " te/Medisave=Prudential";

    public static final String MESSAGE_ADD_SUCCESS = "Tags added to patient: %1$s";
    public static final String MESSAGE_EDIT_SUCCESS = "Tag edited to patient: %1$s";
    public static final String MESSAGE_DELETE_SUCCESS = "Tag deleted from patient: %1$s";
    public static final String MESSAGE_DUPLICATE_TAGS = "Some tags are already in the patient's tag list";
    public static final String MESSAGE_TAG_NOT_FOUND = "Tag not found in the patient's tag list";

    private final Index targetIndex;
    private final Set<Tag> allergies;
    private final Set<Tag> conditions;
    private final Set<Tag> insurances;
    private final Set<Tag> tagsToDelete;
    private final Tag oldTag;
    private final Tag newTag;

    /**
     * Constructs a TagCommand object to add, delete, or edit tags for a person at the specified index.
     *
     * @param targetIndex The index of the person in the filtered person list to add, delete, or edit tags.
     * @param allergies Set of allergy tags to be added to the person.
     * @param conditions Set of condition tags to be added to the person.
     * @param insurances Set of insurance tags to be added to the person.
     * @param tagsToDelete Set of tags to be deleted from the person.
     * @param oldTag The existing tag to be edited (if applicable).
     * @param newTag The new tag to replace the old tag (if applicable).
     */
    public TagCommand(Index targetIndex, Set<Tag> allergies, Set<Tag> conditions, Set<Tag> insurances,
                      Set<Tag> tagsToDelete, Tag oldTag, Tag newTag) {
        requireNonNull(targetIndex);
        requireNonNull(allergies);
        requireNonNull(conditions);
        requireNonNull(insurances);

        this.targetIndex = targetIndex;
        this.allergies = allergies;
        this.conditions = conditions;
        this.insurances = insurances;
        this.tagsToDelete = tagsToDelete;
        this.oldTag = oldTag;
        this.newTag = newTag;
    }

    /**
     * Executes the TagCommand, which modifies the tags of a person as specified (add, delete, or edit).
     *
     * @param model The model of the application that contains the data.
     * @return A CommandResult containing the outcome message indicating success.
     * @throws CommandException If an error occurs during the execution (e.g., invalid index).
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        List<Person> lastShownList = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException("The person index is invalid.");
        }

        Person personToTag = lastShownList.get(targetIndex.getZeroBased());

        if (!tagsToDelete.isEmpty()) {
            // Handle delete tag
            for (Tag tagToDelete : tagsToDelete) {
                if (!personToTag.getTags().contains(tagToDelete)) {
                    throw new CommandException(MESSAGE_TAG_NOT_FOUND);
                }
                personToTag = model.deleteTagFromPerson(personToTag, Collections.singleton(tagToDelete));
            }
            return new CommandResult(String.format(MESSAGE_DELETE_SUCCESS, personToTag));
        }

        if (oldTag != null && newTag != null) {
            // Handle edit tag
            if (!personToTag.getTags().contains(oldTag)) {
                throw new CommandException(MESSAGE_TAG_NOT_FOUND);
            }
            personToTag = model.editTagForPerson(personToTag, oldTag, newTag);
            return new CommandResult(String.format(MESSAGE_EDIT_SUCCESS, personToTag));
        }

        Set<Tag> allTags = mergeTags();

        // Check for duplicate tags before adding
        if (!Collections.disjoint(allTags, personToTag.getTags())) {
            throw new CommandException(MESSAGE_DUPLICATE_TAGS);
        }

        Person updatedPerson = model.addTagsToPerson(personToTag, allTags);
        return new CommandResult(String.format(MESSAGE_ADD_SUCCESS, updatedPerson));
    }

    /**
     * Combines all tag categories (allergies, conditions, insurances) into a single set.
     *
     * @return A set containing all the tags to be added to the person.
     */
    private Set<Tag> mergeTags() {
        Set<Tag> allTags = new HashSet<>();
        allTags.addAll(allergies);
        allTags.addAll(conditions);
        allTags.addAll(insurances);
        return allTags;
    }

    /**
     * Compares this TagCommand with another object to determine equality.
     *
     * @param other The object to compare this TagCommand to.
     * @return True if the other object is the same type and contains the same values.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof TagCommand otherTagCommand)) {
            return false;
        }

        return targetIndex.equals(otherTagCommand.targetIndex)
                && allergies.equals(otherTagCommand.allergies)
                && conditions.equals(otherTagCommand.conditions)
                && insurances.equals(otherTagCommand.insurances);
    }

    /**
     * Returns a string representation of the TagCommand.
     *
     * @return A string representation of the TagCommand's state.
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .add("targetIndex", targetIndex)
            .add("allergies", allergies)
            .add("conditions", conditions)
            .add("insurances", insurances)
            .toString();
    }
}
