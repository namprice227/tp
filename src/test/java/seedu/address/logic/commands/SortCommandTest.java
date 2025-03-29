package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.AddressBook;
import seedu.address.model.ArchivedBook;

public class SortCommandTest {

  private Model model = new ModelManager(new AddressBook(), new UserPrefs(), new ArchivedBook());

  @Test
  public void execute_validSortFieldName_success() {
    SortCommand sortCommand = new SortCommand("name");
    CommandResult commandResult = sortCommand.execute(model);
    assertEquals(String.format(SortCommand.MESSAGE_SUCCESS, "name"), commandResult.getFeedbackToUser());
  }

  @Test
  public void execute_validSortFieldAppointment_success() {
    SortCommand sortCommand = new SortCommand("appointment");
    CommandResult commandResult = sortCommand.execute(model);
    assertEquals(String.format(SortCommand.MESSAGE_SUCCESS, "appointment"), commandResult.getFeedbackToUser());
  }

  @Test
  public void execute_invalidSortField_throwsAssertionError() {
    SortCommand sortCommand = new SortCommand("invalid");
    assertThrows(AssertionError.class, () -> sortCommand.execute(model));
  }

  @Test
  public void equals() {
    SortCommand sortByNameCommand = new SortCommand("name");
    SortCommand sortByAppointmentCommand = new SortCommand("appointment");
    SortCommand sortByNameCommandCopy = new SortCommand("name");

    assertTrue(sortByNameCommand.equals(sortByNameCommand));

    assertTrue(sortByNameCommand.equals(sortByNameCommandCopy));

    assertFalse(sortByNameCommand.equals(1));

    assertFalse(sortByNameCommand.equals(null));

    assertFalse(sortByNameCommand.equals(sortByAppointmentCommand));
  }
}
