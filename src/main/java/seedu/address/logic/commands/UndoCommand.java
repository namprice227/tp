package seedu.address.logic.commands;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

/**
 * Undoes the most recent command that modified the address book.
 */
public class UndoCommand extends Command {
    public static final String COMMAND_WORD = "undo";

    public static final String MESSAGE_USAGE = COMMAND_WORD
          + ": Undoes the last command that modified the address book.\n"
          + "Example: " + COMMAND_WORD;

    public static final String MESSAGE_SUCCESS = "Undone: Previous command reversed.";
    public static final String MESSAGE_FAILURE = "No more commands to undo.";
    public static final String MESSAGE_NO_SUPPORT = "Undo is not supported for archive commands.";

    private final Model model;

    public UndoCommand(Model model) {
        this.model = model;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        if (model.isLastCommandArchiveRelated()) {
            throw new CommandException(MESSAGE_NO_SUPPORT);
        }

        // Check if undo is possible
        if (!model.canUndoAddressBook()) {
            throw new CommandException(MESSAGE_FAILURE);
        }

        // Perform the undo operation
        model.undoAddressBook();
        return new CommandResult(MESSAGE_SUCCESS);
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof UndoCommand;
    }
}
