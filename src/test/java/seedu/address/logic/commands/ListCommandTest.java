package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.ArchivedBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

/**
 * Contains integration tests (interaction with the Model) and unit tests for ListCommand.
 */
public class ListCommandTest {

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs(), new ArchivedBook());
        expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs(), new ArchivedBook());
    }

    @Test
    public void execute_listIsNotFiltered_showsSameList() {
        // Pass an empty string as argument
        assertCommandSuccess(new ListCommand(""), model, ListCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_listIsFiltered_showsEverything() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        // Pass an empty string as argument
        assertCommandSuccess(new ListCommand(""), model, ListCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void executeListWithArgumentsFailure() {
        assertThrows(CommandException.class, () -> new ListCommand("list extra").execute(model));
    }
}
