package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

public class RedoCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs(), getTypicalArchivedBook());
    private Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs(), getTypicalArchivedBook());

    @Test
    public void execute_redoAfterUndo_success() throws CommandException {
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

        // Verify the person was removed
        assertFalse(model.hasPerson(personToAdd));

        // Execute redo command
        RedoCommand redoCommand = new RedoCommand(model);
        redoCommand.execute(model);

        // Verify the person was added back
        assertTrue(model.hasPerson(personToAdd));
    }

    @Test
    public void execute_redoNoCommand_failure() {
        RedoCommand redoCommand = new RedoCommand(model);
        assertThrows(CommandException.class, RedoCommand.MESSAGE_FAILURE, () -> redoCommand.execute(model));
    }

    @Test
    public void execute_redoWithoutUndo_failure() throws CommandException {
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

        // Try to redo without any undo
        RedoCommand redoCommand = new RedoCommand(model);
        assertThrows(CommandException.class, RedoCommand.MESSAGE_FAILURE, () -> redoCommand.execute(model));
    }

    @Test
    public void equals() {
        RedoCommand redoFirstCommand = new RedoCommand(model);
        RedoCommand redoSecondCommand = new RedoCommand(model);

        // same object -> returns true
        assertTrue(redoFirstCommand.equals(redoFirstCommand));

        // same type -> returns true
        assertTrue(redoFirstCommand.equals(redoSecondCommand));

        // null -> returns false
        assertFalse(redoFirstCommand.equals(null));

        // different types -> returns false
        assertFalse(redoFirstCommand.equals(1));
    }
}
