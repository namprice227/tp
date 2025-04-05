package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.model.AddressBook;
import seedu.address.model.ArchivedBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

public class ClearCommandTest {
    @Test
    public void execute_needsConfirmation_confirmationMessage() {
        Model model = new ModelManager(new AddressBook(), new UserPrefs(), new ArchivedBook());
        ClearCommand clearCommand = new ClearCommand();

        CommandResult result = clearCommand.execute(model);

        // Ensure that the message is asking for confirmation
        assertEquals(ClearCommand.MESSAGE_CONFIRMATION, result.getFeedbackToUser());
    }

    @Test
    public void execute_afterConfirmation_clearSuccess() {
        Model model = new ModelManager(new AddressBook(), new UserPrefs(), new ArchivedBook());
        ClearCommand clearCommand = new ClearCommand(false);

        CommandResult result = clearCommand.execute(model);

        // Ensure that the patient list is cleared
        assertEquals(ClearCommand.MESSAGE_SUCCESS, result.getFeedbackToUser());

        // Check if the model's address book is cleared (empty)
        assertEquals(new AddressBook(), model.getAddressBook());
    }

    @Test
    public void execute_shouldClearModel_afterConfirmation() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs(), new ArchivedBook());
        ClearCommand clearCommand = new ClearCommand(false);

        CommandResult result = clearCommand.execute(model);

        // Ensure that the patient list is cleared
        assertEquals(ClearCommand.MESSAGE_SUCCESS, result.getFeedbackToUser());

        // Check that the address book is now empty
        assertEquals(new AddressBook(), model.getAddressBook());
    }
}
