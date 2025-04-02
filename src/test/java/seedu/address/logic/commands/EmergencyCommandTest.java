package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;
import static seedu.address.testutil.TypicalPersons.getTypicalArchivedBook;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.ArchivedBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyArchivedBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.EmergencyPerson;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Relationship;

public class EmergencyCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs(), getTypicalArchivedBook());

    @Test
    public void execute_validIndexUnfilteredList_success() throws CommandException {
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        EmergencyCommand emergencyCommand = new EmergencyCommand(INDEX_FIRST_PERSON,
                new Name("John Smith"), new Phone("98765432"), new Relationship("Father"));

        String expectedMessage = String.format(EmergencyCommand.MESSAGE_EDIT_EMERGENCY_SUCCESS, personToEdit.getName());

        ReadOnlyAddressBook addressBook = new AddressBook(model.getAddressBook());
        ReadOnlyArchivedBook archivedBook = new ArchivedBook(model.getArchivedBook());
        ReadOnlyUserPrefs userPrefs = new UserPrefs();
        Model expectedModel = new ModelManager(addressBook, userPrefs, archivedBook);
        
        EmergencyPerson newEmergencyContact = new EmergencyPerson(
                new Name("John Smith"), new Phone("98765432"), new Relationship("Father"));
        Person editedPerson = personToEdit.setEmergencyContact(newEmergencyContact);
        expectedModel.setPerson(personToEdit, editedPerson);

        CommandResult commandResult = emergencyCommand.execute(model);

        assertEquals(expectedMessage, commandResult.getFeedbackToUser());
        assertEquals(expectedModel.getAddressBook().getPersonList(), model.getAddressBook().getPersonList());
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        EmergencyCommand emergencyCommand = new EmergencyCommand(outOfBoundIndex,
                new Name("John Smith"), new Phone("98765432"), new Relationship("Father"));

        assertThrows(CommandException.class, MESSAGE_INVALID_PERSON_DISPLAYED_INDEX, () ->
                emergencyCommand.execute(model));
    }

    @Test
    public void equals() {
        final EmergencyCommand standardCommand = new EmergencyCommand(INDEX_FIRST_PERSON,
                new Name("John Smith"), new Phone("98765432"), new Relationship("Father"));

        // same values -> returns true
        EmergencyCommand commandWithSameValues = new EmergencyCommand(INDEX_FIRST_PERSON,
                new Name("John Smith"), new Phone("98765432"), new Relationship("Father"));
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new EmergencyCommand(INDEX_SECOND_PERSON,
                new Name("John Smith"), new Phone("98765432"), new Relationship("Father"))));

        // different name -> returns false
        assertFalse(standardCommand.equals(new EmergencyCommand(INDEX_FIRST_PERSON,
                new Name("Jane Smith"), new Phone("98765432"), new Relationship("Father"))));

        // different phone -> returns false
        assertFalse(standardCommand.equals(new EmergencyCommand(INDEX_FIRST_PERSON,
                new Name("John Smith"), new Phone("12345678"), new Relationship("Father"))));

        // different relationship -> returns false
        assertFalse(standardCommand.equals(new EmergencyCommand(INDEX_FIRST_PERSON,
                new Name("John Smith"), new Phone("98765432"), new Relationship("Mother"))));
    }
}
