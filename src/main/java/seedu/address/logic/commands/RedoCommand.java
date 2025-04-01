package seedu.address.logic.commands;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

/**
 * Redoes the most recent command that modified the address book.
 */
public class RedoCommand extends Command {
    public static final String COMMAND_WORD = "redo";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Redoes the last command that modified the address book.\n"
            + "Example: " + COMMAND_WORD;

    public static final String MESSAGE_SUCCESS = "Redone: Previous command reversed.";
    public static final String MESSAGE_FAILURE = "No more commands to redo.";

    private final Model model;

    /**
     * Creates a RedoCommand to redo the last command that modified the address
     * book.
     *
     * @param model The model to be used for the command.
     */
    public RedoCommand(Model model) {
        this.model = model;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        // Check if undo is possible
        if (!model.canRedoAddressBook()) {
            throw new CommandException(MESSAGE_FAILURE);
        }

        // Perform the undo operation
        model.redoAddressBook();
        return new CommandResult(MESSAGE_SUCCESS);
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof RedoCommand;
    }
}
