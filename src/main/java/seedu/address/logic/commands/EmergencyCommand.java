package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_RELATIONSHIP;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.EmergencyPerson;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Relationship;

/**
 * Edits the emergency contact of an existing person in the address book.
 */
public class EmergencyCommand extends Command {

    public static final String COMMAND_WORD = "emergency";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the emergency contact of the patient identified "
            + "by the index number used in the displayed patient list. "
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_NAME + "NAME "
            + PREFIX_PHONE + "PHONE "
            + PREFIX_RELATIONSHIP + "RELATIONSHIP\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_NAME + "John Doe "
            + PREFIX_PHONE + "98765432 "
            + PREFIX_RELATIONSHIP + "Father";

    public static final String MESSAGE_EDIT_EMERGENCY_SUCCESS = "Updated emergency contact for patient: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";

    private final Index index;
    private final Name emergencyName;
    private final Phone emergencyPhone;
    private final Relationship relationship;

    /**
     * @param index of the person in the filtered person list to edit
     * @param emergencyName name of emergency contact
     * @param emergencyPhone phone of emergency contact
     * @param relationship relationship with emergency contact
     */
    public EmergencyCommand(Index index, Name emergencyName, Phone emergencyPhone, Relationship relationship) {
        requireNonNull(index);
        requireNonNull(emergencyName);
        requireNonNull(emergencyPhone);
        requireNonNull(relationship);

        this.index = index;
        this.emergencyName = emergencyName;
        this.emergencyPhone = emergencyPhone;
        this.relationship = relationship;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToEdit = lastShownList.get(index.getZeroBased());
        EmergencyPerson newEmergencyContact = new EmergencyPerson(emergencyName, emergencyPhone, relationship);
        Person editedPerson = personToEdit.setEmergencyContact(newEmergencyContact);

        model.setPerson(personToEdit, editedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(String.format(MESSAGE_EDIT_EMERGENCY_SUCCESS, editedPerson.getName()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof EmergencyCommand)) {
            return false;
        }

        EmergencyCommand e = (EmergencyCommand) other;
        return index.equals(e.index)
                && emergencyName.equals(e.emergencyName)
                && emergencyPhone.equals(e.emergencyPhone)
                && relationship.equals(e.relationship);
    }
}

