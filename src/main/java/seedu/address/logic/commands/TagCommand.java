package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.List;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;

/**
 * Adds tags to a person in the address book.
 */
public class TagCommand extends Command {

  public static final String COMMAND_WORD = "tag";

  public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds tags to an existing patient in HealthSync.\n"
          + "Parameters: "
          + "INDEX (must be a positive integer) "
          + "[" + PREFIX_TAG + "TAG]...\n"
          + "Example: " + COMMAND_WORD + " 1 "
          + PREFIX_TAG + "diabetes "
          + PREFIX_TAG + "allergy";

  public static final String MESSAGE_SUCCESS = "Tags added to patient: %1$s";
  public static final String MESSAGE_DUPLICATE_TAGS = "Some tags are already in the patient's tag list";

  private final Index targetIndex;
  private final Set<Tag> tagsToAdd;

  /**
   * Creates a TagCommand to add the specified {@code Tag}s to the person at the specified {@code Index}.
   */
  public TagCommand(Index targetIndex, Set<Tag> tagsToAdd) {
    requireAllNonNull(targetIndex, tagsToAdd);
    this.targetIndex = targetIndex;
    this.tagsToAdd = tagsToAdd;
  }

  @Override
  public CommandResult execute(Model model) throws CommandException {
    requireNonNull(model);

    List<Person> lastShownList = model.getFilteredPersonList();

    if (targetIndex.getZeroBased() >= lastShownList.size()) {
      throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    Person personToTag = lastShownList.get(targetIndex.getZeroBased());
    Person updatedPerson = model.addTagsToPerson(personToTag, tagsToAdd);

    return new CommandResult(String.format(MESSAGE_SUCCESS, seedu.address.logic.Messages.format(updatedPerson)));
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) {
      return true;
    }
    if (!(other instanceof TagCommand)) {
      return false;
    }
    TagCommand otherTagCommand = (TagCommand) other;
    return targetIndex.equals(otherTagCommand.targetIndex)
            && tagsToAdd.equals(otherTagCommand.tagsToAdd);
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
            .add("targetIndex", targetIndex)
            .add("tagsToAdd", tagsToAdd)
            .toString();
  }
}
