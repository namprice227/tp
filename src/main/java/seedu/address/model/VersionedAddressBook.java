package seedu.address.model;

import java.util.ArrayList;
import java.util.List;

import seedu.address.logic.commands.exceptions.CommandException;

/**
 * Wraps an AddressBook with undo/redo functionality.
 */
public class VersionedAddressBook extends AddressBook {
  private final List<ReadOnlyAddressBook> addressBookStateList;
  private int currentStatePointer;

  public VersionedAddressBook(ReadOnlyAddressBook initialState) {
    super(initialState);
    addressBookStateList = new ArrayList<>();
    addressBookStateList.add(new AddressBook(initialState));
    currentStatePointer = 0;
  }

  /**
   * Saves a copy of the current address book state.
   */
  public void commit() {
    // Remove any states after current pointer
    while (currentStatePointer < addressBookStateList.size() - 1) {
      addressBookStateList.remove(addressBookStateList.size() - 1);
    }

    // Create and add a deep copy of the current state
    addressBookStateList.add(new AddressBook(this));
    currentStatePointer++;
  }

  /**
   * Restores the previous address book state.
   */
  public void undo() throws CommandException {
    if (!canUndo()) {
      throw new CommandException("Cannot undo");
    }
    currentStatePointer--;
    resetData(addressBookStateList.get(currentStatePointer));
  }

  /**
   * Returns true if undo is possible.
   */
  public boolean canUndo() {
    return currentStatePointer > 0;
  }
}
