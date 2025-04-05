package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalPersons.AMY;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;
import static seedu.address.testutil.TypicalPersons.getTypicalArchivedBook;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;

public class UndoCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs(), getTypicalArchivedBook());
    private Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs(), getTypicalArchivedBook());

    @Test
    public void execute_undoAddCommand_success() throws CommandException {
        // First, add a person to the model
        Person personToAdd = new Person(
                new Name(VALID_NAME_AMY),
                new Phone(VALID_PHONE_AMY),
                AMY.getEmail(),
                AMY.getAddress(),
                AMY.getAllergyTags(),
                AMY.getConditionTags(),
                AMY.getInsuranceTags(),
                AMY.getAppointment(),
                AMY.getEmergencyContact());

        // Add the person
        AddCommand addCommand = new AddCommand(personToAdd);
        addCommand.execute(model);

        // Verify the person was added
        assertTrue(model.hasPerson(personToAdd));

        // Execute undo command
        UndoCommand undoCommand = new UndoCommand(model);
        undoCommand.execute(model);

        // Verify the person was removed (model returned to initial state)
        assertFalse(model.hasPerson(personToAdd));
    }

    @Test
    public void execute_undoDeleteCommand_success() throws CommandException {
        // Get the first person from the model
        Person personToDelete = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        // Delete the person
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIRST_PERSON);
        deleteCommand.setConfirmation(false); // Skip confirmation
        deleteCommand.execute(model);

        // Verify the person was deleted
        assertFalse(model.hasPerson(personToDelete));

        // Execute undo command
        UndoCommand undoCommand = new UndoCommand(model);
        undoCommand.execute(model);

        // Verify the person was restored
        assertTrue(model.hasPerson(personToDelete));
    }

    @Test
    public void execute_undoNoCommand_failure() {
        UndoCommand undoCommand = new UndoCommand(model);
        assertThrows(CommandException.class, UndoCommand.MESSAGE_FAILURE, () -> undoCommand.execute(model));
    }

    @Test
    public void equals() {
        UndoCommand undoFirstCommand = new UndoCommand(model);
        UndoCommand undoSecondCommand = new UndoCommand(model);

        // same object -> returns true
        assertTrue(undoFirstCommand.equals(undoFirstCommand));

        // same type -> returns true
        assertTrue(undoFirstCommand.equals(undoSecondCommand));

        // null -> returns false
        assertFalse(undoFirstCommand.equals(null));

        // different types -> returns false
        assertFalse(undoFirstCommand.equals(1));
    }
}
