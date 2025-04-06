
package seedu.address.model;

import java.util.ArrayList;
import java.util.List;

import seedu.address.logic.commands.exceptions.CommandException;

/**
 * Wraps an AddressBook with undo/redo functionality.
 * Undo is limited to one step back only.
 */
public class VersionedAddressBook extends AddressBook {
    private final List<ReadOnlyAddressBook> addressBookStateList;
    private int currentStatePointer;
    private boolean hasUndone = false;

    /**
     * Constructs a {@code VersionedAddressBook} with the given initial state.
     * Initializes the state history with the provided {@code initialState} and
     * sets the current state pointer to the first (and only) state.
     *
     * @param initialState The initial state of the address book.
     */
    public VersionedAddressBook(ReadOnlyAddressBook initialState) {
        super(initialState);
        addressBookStateList = new ArrayList<>();
        addressBookStateList.add(new AddressBook(initialState));
        currentStatePointer = 0;
    }

    /**
     * Saves a copy of the current address book state.
     * If an undo operation has been performed, only keeps the current state
     * and the previous state.
     */
    public void commit() {
        // Remove any states after current pointer
        while (currentStatePointer < addressBookStateList.size() - 1) {
            addressBookStateList.remove(addressBookStateList.size() - 1);
        }

        if (hasUndone) {
            AddressBook currentState = new AddressBook(this);
            addressBookStateList.clear();
            addressBookStateList.add(currentState);
            currentStatePointer = 0;
            hasUndone = false;
        }

        addressBookStateList.add(new AddressBook(this));
        currentStatePointer++;

        while (addressBookStateList.size() > 2) {
            addressBookStateList.remove(0);
            currentStatePointer--;
        }
    }

    /**
     * Restores the previous address book state.
     * Can only be called once before needing to commit again.
     */
    public void undo() throws CommandException {
        if (!canUndo()) {
            throw new CommandException("Cannot undo");
        }
        currentStatePointer--;
        resetData(addressBookStateList.get(currentStatePointer));
        hasUndone = true;
    }

    /**
     * Returns true if undo is possible.
     * Undo is only possible if we haven't already undone and there is a previous state.
     */
    public boolean canUndo() {
        return currentStatePointer > 0 && !hasUndone;
    }

    /**
     * Restores the next address book state.
     */
    public void redo() throws CommandException {
        if (!canRedo()) {
            throw new CommandException("Cannot redo");
        }
        currentStatePointer++;
        resetData(addressBookStateList.get(currentStatePointer));
        hasUndone = false;
    }

    /**
     * Returns true if redo is possible.
     */
    public boolean canRedo() {
        return currentStatePointer < addressBookStateList.size() - 1;
    }
}
