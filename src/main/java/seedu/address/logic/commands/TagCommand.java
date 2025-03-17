package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ALLERGY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CONDITION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INSURANCE;

import java.util.HashSet;
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
 * Adds different types of tags to a person in the address book.
 */
public class TagCommand extends Command {

  public static final String COMMAND_WORD = "tag";

  public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds different types of tags to an existing patient in HealthSync.\n"
          + "Parameters: "
          + "INDEX (must be a positive integer) "
          + "[" + PREFIX_ALLERGY + "ALLERGY]... "
          + "[" + PREFIX_CONDITION + "CONDITION]... "
          + "[" + PREFIX_INSURANCE + "INSURANCE]...\n"
          + "Example: " + COMMAND_WORD + " 1 "
          + PREFIX_ALLERGY + "peanuts "
          + PREFIX_CONDITION + "asthma "
          + PREFIX_INSURANCE + "medisave";

  public static final String MESSAGE_SUCCESS = "Tags added to patient: %1$s";
  public static final String MESSAGE_DUPLICATE_TAGS = "Some tags are already in the patient's tag list";

  private final Index targetIndex;
  private final Set<Tag> allergies;
  private final Set<Tag> conditions;
  private final Set<Tag> insurances;

  /**
   * Creates a TagCommand to add the specified tags to the person at the specified index.
   */
  public TagCommand(Index targetIndex, Set<Tag> allergies, Set<Tag> conditions, Set<Tag> insurances) {
    requireNonNull(targetIndex);
    requireNonNull(allergies);
    requireNonNull(conditions);
    requireNonNull(insurances);

    this.targetIndex = targetIndex;
    this.allergies = allergies;
    this.conditions = conditions;
    this.insurances = insurances;
  }

  @Override
  public CommandResult execute(Model model) throws CommandException {
    requireNonNull(model);

    List<Person> lastShownList = model.getFilteredPersonList();

    if (targetIndex.getZeroBased() >= lastShownList.size()) {
      throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    Person personToTag = lastShownList.get(targetIndex.getZeroBased());
    Set<Tag> allTags = mergeTags();
    Person updatedPerson = model.addTagsToPerson(personToTag, allTags);

    return new CommandResult(String.format(MESSAGE_SUCCESS, seedu.address.logic.Messages.format(updatedPerson)));
  }

  /**
   * Combines all tag categories into one set.
   */
  private Set<Tag> mergeTags() {
    Set<Tag> allTags = new HashSet<>();
    allTags.addAll(allergies);
    allTags.addAll(conditions);
    allTags.addAll(insurances);
    return allTags;
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
            && allergies.equals(otherTagCommand.allergies)
            && conditions.equals(otherTagCommand.conditions)
            && insurances.equals(otherTagCommand.insurances);
  }

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
