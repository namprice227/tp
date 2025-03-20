package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_RELATIONSHIP;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.EmergencyPerson;
import seedu.address.model.person.Person;

/**
 * Adds a person to the address book.
 */
public class EmergencyCommand extends Command {

    public static final String COMMAND_WORD = "emergency";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a patient's emergency contact to HealthSync.\n"
            + "Parameters: "
            + "INDEX (must be a positive integer) "
            + PREFIX_NAME + "NAME "
            + PREFIX_PHONE + "PHONE "
            + PREFIX_RELATIONSHIP + "RELATIONSHIP "
            + "Example: " + COMMAND_WORD + " " + "1"
            + PREFIX_NAME + "John Doe "
            + PREFIX_PHONE + "98765432 "
            + PREFIX_RELATIONSHIP + "Husband ";

    public static final String MESSAGE_EMERGENCY_PERSON_SUCCESS = "Emergency contact added: %1$s";

    private final Index targetIndex;
    private final EmergencyPerson personToAdd;

    /**
     * Creates an EmergencyCommand to add the specified {@code EmergencyPerson}
     *
     * @param person The emergency contact person to be added.
     * @param targetIndex The index at which the person should be added.
     */
    public EmergencyCommand(EmergencyPerson person, Index targetIndex) {
        requireNonNull(person);

        this.targetIndex = targetIndex;
        personToAdd = person;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        List<Person> lastShownList = model.getFilteredPersonList();
        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToUpdate = lastShownList.get(targetIndex.getZeroBased());
        model.addEmergencyContactToPerson(personToUpdate, personToAdd);

        return new CommandResult(String.format(MESSAGE_EMERGENCY_PERSON_SUCCESS, Messages.format(personToAdd)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddCommand)) {
            return false;
        }

        EmergencyCommand otherEmergencyCommand = (EmergencyCommand) other;
        return personToAdd.equals(otherEmergencyCommand.personToAdd);
    }
}

