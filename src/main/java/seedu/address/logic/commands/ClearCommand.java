package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.model.Model;

/**
 * Clears the address book.
 */
public class ClearCommand extends Command {

    public static final String COMMAND_WORD = "clear";
    public static final String MESSAGE_CONFIRMATION = "Are you sure you want to clear the patient list? (y/n)";
    public static final String MESSAGE_CANCELLED = "Clear command cancelled.";
    public static final String MESSAGE_SUCCESS = "Patient list has been cleared!";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Clears all entries from the address book.\n"
            + "This command does not accept any additional parameters.";

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
        return obj instanceof ClearCommand; // Ignore needsConfirmation state
    }

    /**
     * @param model {@code Model} which the command should operate on.
     * @return CommandResult after executing the command
     */
    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        if (needsConfirmation) {
            needsConfirmation = false;
            return new CommandResult(MESSAGE_CONFIRMATION, false, false, true);
        }
        model.setLastCommandArchiveRelated(false);
        model.setAddressBook(model.getEmptyAddressBook());
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
