package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ALLERGY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CONDITION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INSURANCE;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
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
    public static final String MESSAGE_INVALID_PERSON_INDEX = "The person index is invalid.";
    public static final String MESSAGE_MULTIPLE_OPERATIONS = "Only one tag operation (add, delete, or edit) "
            + "can be performed at a time";

    private static final Logger logger = LogsCenter.getLogger(TagCommand.class);

    private final Index targetIndex;
    private final Set<Tag> allergies;
    private final Set<Tag> conditions;
    private final Set<Tag> insurances;
    private final Set<Tag> tagsToDelete;

    /**
     * Constructs a TagCommand object to add, delete, or edit tags for a person at the specified index.
     *
     * @param targetIndex The index of the person in the filtered person list to add, delete, or edit tags.
     * @param allergies Set of allergy tags to be added to the person.
     * @param conditions Set of condition tags to be added to the person.
     * @param insurances Set of insurance tags to be added to the person.
     * @param tagsToDelete Set of tags to be deleted from the person.
     */
    public TagCommand(Index targetIndex, Set<Tag> allergies, Set<Tag> conditions, Set<Tag> insurances,
                      Set<Tag> tagsToDelete) {
        requireNonNull(targetIndex);
        requireNonNull(allergies);
        requireNonNull(conditions);
        requireNonNull(insurances);
        requireNonNull(tagsToDelete);

        // Validate operation exclusivity
        validateOperationExclusivity(allergies, conditions, insurances, tagsToDelete, oldTag, newTag);

        this.targetIndex = targetIndex;
        this.allergies = allergies;
        this.conditions = conditions;
        this.insurances = insurances;
        this.tagsToDelete = tagsToDelete;
    }

    /**
     * Validates that only one tag operation (add, delete, or edit) is requested at a time.
     * Throws IllegalArgumentException if multiple operations are detected.
     */
    private void validateOperationExclusivity(Set<Tag> allergies, Set<Tag> conditions, Set<Tag> insurances,
                                              Set<Tag> tagsToDelete, Tag oldTag, Tag newTag) {
        boolean isAddOperation = !allergies.isEmpty() || !conditions.isEmpty() || !insurances.isEmpty();
        boolean isDeleteOperation = !tagsToDelete.isEmpty();
        boolean isEditOperation = oldTag != null && newTag != null;

        int operationCount = (isAddOperation ? 1 : 0) + (isDeleteOperation ? 1 : 0) + (isEditOperation ? 1 : 0);

        if (operationCount > 1) {
            logger.warning("Multiple tag operations requested simultaneously");
            throw new IllegalArgumentException(MESSAGE_MULTIPLE_OPERATIONS);
        }
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
        logTagCommandExecution();

        Person personToTag = getPersonToTag(model);
        Person updatedPerson = modifyPersonTags(model, personToTag);
        String resultMessage = createResultMessage(updatedPerson);

        logTagCommandSuccess();
        return new CommandResult(resultMessage);
    }

    /**
     * Logs the start of the tag command execution.
     */
    private void logTagCommandExecution() {
        logger.info("Executing tag command for index " + targetIndex.getOneBased());
    }

    /**
     * Retrieves the Person object to modify tags for, based on the target index.
     *
     * @param model The model of the application.
     * @return The Person object to modify.
     * @throws CommandException If the target index is invalid.
     */
    private Person getPersonToTag(Model model) throws CommandException {
        List<Person> lastShownList = model.getFilteredPersonList();
        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            logger.warning("Invalid person index: " + targetIndex.getOneBased());
            throw new CommandException(MESSAGE_INVALID_PERSON_INDEX);
        }
        return lastShownList.get(targetIndex.getZeroBased());
    }

    /**
     * Modifies the tags of the given Person object based on the command's parameters.
     *
     * @param model       The model of the application.
     * @param personToTag The Person object to modify.
     * @return The updated Person object.
     * @throws CommandException If an error occurs during tag modification.
     */
    private Person modifyPersonTags(Model model, Person personToTag) throws CommandException {
        try {
            if (!tagsToDelete.isEmpty()) {
                return handleTagDeletion(model, personToTag);
            } else if (oldTag != null && newTag != null) {
                return handleTagEditing(model, personToTag);
            } else {
                return handleTagAddition(model, personToTag);
            }
        } catch (CommandException e) {
            logger.warning("Error executing tag command: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Creates the result message based on the type of tag modification.
     *
     * @param updatedPerson The updated Person object.
     * @return The result message.
     */
    private String createResultMessage(Person updatedPerson) {
        if (!tagsToDelete.isEmpty()) {
            return String.format(MESSAGE_DELETE_SUCCESS, updatedPerson);
        } else if (oldTag != null && newTag != null) {
            return String.format(MESSAGE_EDIT_SUCCESS, updatedPerson);
        } else {
            return String.format(MESSAGE_ADD_SUCCESS, updatedPerson);
        }
    }

    /**
     * Logs the successful execution of the tag command.
     */
    private void logTagCommandSuccess() {
        logger.info("Tag command executed successfully");
    }

    /**
     * Handles the deletion of tags from a person.
     *
     * @param model The model of the application that contains the data.
     * @param personToTag The person from whom tags will be deleted.
     * @return The updated Person object after tag deletion.
     * @throws CommandException If a tag to be deleted is not found.
     */
    private Person handleTagDeletion(Model model, Person personToTag) throws CommandException {
        assert !tagsToDelete.isEmpty() : "tagsToDelete should not be empty when handling tag deletion";

        for (Tag tagToDelete : tagsToDelete) {
            if (!personToTag.getTags().contains(tagToDelete)) {
                logger.warning("Tag not found: " + tagToDelete);
                throw new CommandException(MESSAGE_TAG_NOT_FOUND);
            }
            logger.fine("Deleting tag: " + tagToDelete + " from person: " + personToTag.getName());
            personToTag = model.deleteTagFromPerson(personToTag, Collections.singleton(tagToDelete));
        }
        return personToTag;
    }

    /**
     * Handles the editing of a tag for a person.
     *
     * @param model The model of the application that contains the data.
     * @param personToTag The person whose tag will be edited.
     * @return The updated Person object after tag editing.
     * @throws CommandException If the tag to be edited is not found.
     */
    private Person handleTagEditing(Model model, Person personToTag) throws CommandException {
        assert oldTag != null && newTag != null : "oldTag and newTag should not be null when handling tag editing";

        if (!personToTag.getTags().contains(oldTag)) {
            logger.warning("Tag not found for editing: " + oldTag);
            throw new CommandException(MESSAGE_TAG_NOT_FOUND);
        }

        logger.fine("Editing tag: " + oldTag + " to: " + newTag + " for person: " + personToTag.getName());
        return model.editTagForPerson(personToTag, oldTag, newTag);
    }

    /**
     * Handles the addition of tags to a person.
     *
     * @param model The model of the application that contains the data.
     * @param personToTag The person to whom tags will be added.
     * @return The updated Person object after tag addition.
     * @throws CommandException If duplicate tags are detected.
     */
    private Person handleTagAddition(Model model, Person personToTag) throws CommandException {
        Set<Tag> allTags = mergeTags();

        // Check for duplicate tags before adding
        if (!Collections.disjoint(allTags, personToTag.getTags())) {
            logger.warning("Duplicate tags detected when adding to person: " + personToTag.getName());
            throw new CommandException(MESSAGE_DUPLICATE_TAGS);
        }
      
        Person updatedPerson = model.addTagsToPerson(personToTag, allergies, conditions, insurances);
        return new CommandResult(String.format(MESSAGE_ADD_SUCCESS, updatedPerson));
        logger.fine("Adding tags: " + allTags + " to person: " + personToTag.getName());
        return model.addTagsToPerson(personToTag, allTags);
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
                && insurances.equals(otherTagCommand.insurances)
                && tagsToDelete.equals(otherTagCommand.tagsToDelete)
                && ((oldTag == null && otherTagCommand.oldTag == null) || (oldTag != null
                && oldTag.equals(otherTagCommand.oldTag)))
                && ((newTag == null && otherTagCommand.newTag == null) || (newTag != null
                && newTag.equals(otherTagCommand.newTag)));
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
                .add("tagsToDelete", tagsToDelete)
                .add("oldTag", oldTag)
                .add("newTag", newTag)
                .toString();
    }
}
