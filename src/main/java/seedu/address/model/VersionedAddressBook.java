package seedu.address.model;

import java.util.ArrayList;
import java.util.List;

import seedu.address.logic.commands.exceptions.CommandException;

/**
 * Wraps an AddressBook with undo/redo functionality.
 * Maintains a history of address book states to support undo operations.
 */
public class VersionedAddressBook extends AddressBook {
    private final List<ReadOnlyAddressBook> addressBookStateList;
    private int currentStatePointer;

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
     * Adds the current state to the history and removes any forward states.
     */
    public void commit() {
        while (currentStatePointer < addressBookStateList.size() - 1) {
            addressBookStateList.remove(addressBookStateList.size() - 1);
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
     * Undo is possible if there is a previous state to return to.
     */
    public boolean canUndo() {
        return currentStatePointer > 0;
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
    }

    /**
     * Returns true if redo is possible.
     */
    public boolean canRedo() {
        return currentStatePointer < addressBookStateList.size() - 1;
    }
}
