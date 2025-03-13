package seedu.address.logic.commands;

import seedu.address.model.Model;

import static java.util.Objects.requireNonNull;

/**
 * Terminates the program.
 */
public class ExitCommand extends Command {

    public static final String COMMAND_WORD = "exit";
    public static final String COMMAND_WORD_ALTERNATIVE = "Exit";
    public static final String CONFIRMATION_MESSAGE = "Are you sure? [Y/N]";
    public static final String EXIT_CANCELLED = "Exit cancelled.";
    public static final String MESSAGE_EXIT_ACKNOWLEDGEMENT = "Goodbye!";

    private boolean isConfirmed;

    public ExitCommand() {
        this.isConfirmed = false;
    }

    public ExitCommand(boolean isConfirmed) {
        this.isConfirmed = isConfirmed;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        if (!isConfirmed) {
            isConfirmed = true;
            return new CommandResult(CONFIRMATION_MESSAGE, false, false, true);
        }
        return new CommandResult(MESSAGE_EXIT_ACKNOWLEDGEMENT, false, true, false);
    }
}
