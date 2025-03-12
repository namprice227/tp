package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.model.AddressBook;
import seedu.address.model.Model;


/**
 * Clears the address book.
 */
public class ClearCommand extends Command {

    public static final String COMMAND_WORD = "clear";
    public static final String MESSAGE_CONFIRMATION = "Are you sure you want to clear the address book? (y/n)";
    public static final String MESSAGE_CANCELLED = "Clear command cancelled.";
    public static final String MESSAGE_SUCCESS = "Address book has been cleared!";

    private boolean needsConfirmation;

    public ClearCommand() {
        this.needsConfirmation = true;
    }

    /**
     * Creates a clear command with confirmation status specified.
     * @param needsConfirmation true if confirmation is needed, false if already confirmed
     */
    public ClearCommand(boolean needsConfirmation) {
        this.needsConfirmation = needsConfirmation;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ClearCommand that = (ClearCommand) obj;
        return needsConfirmation == that.needsConfirmation;
    }

    /**
     * @param model {@code Model} which the command should operate on.
     * @return
     */
    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        if (needsConfirmation) {
            needsConfirmation = false;
            return new CommandResult(MESSAGE_CONFIRMATION, false, false, true);
        }
        model.setAddressBook(new AddressBook());
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
