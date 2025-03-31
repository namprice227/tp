package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

/**
 * Terminates the program.
 */
public class ExitCommand extends Command {

    public static final String COMMAND_WORD = "exit";
    public static final String COMMAND_WORD_ALTERNATIVE = "Exit";
    public static final String CONFIRMATION_MESSAGE = "Are you sure? [Y/N]";
    public static final String EXIT_CANCELLED = "Exit cancelled.";
    public static final String MESSAGE_EXIT_ACKNOWLEDGEMENT = "Goodbye!";
    public static final String MESSAGE_INVALID_COMMAND = "Exit command does not accept additional parameters.";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Exits the program.\n"
            + "Example: " + COMMAND_WORD;

    private boolean isConfirmed;
    private final String input;

    /**
     * Creates an exit command with default parameters.
     */
    public ExitCommand() {
        this.isConfirmed = false;
        this.input = "";
    }

    /**
     * Creates an exit command with the provided input string.
     *
     * @param input The raw input string to be validated
     */
    public ExitCommand(String input) {
        this.isConfirmed = false;
        this.input = input;
    }

    /**
     * Creates an exit command with the specified confirmation state.
     *
     * @param isConfirmed Whether the exit has been confirmed by the user
     */
    public ExitCommand(boolean isConfirmed) {
        this.isConfirmed = isConfirmed;
        this.input = "";
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        String[] parts = input.trim().split("\\s+"); // Split input by spaces

        if (parts.length > 1) {
            throw new CommandException(MESSAGE_INVALID_COMMAND);
        }

        if (!isConfirmed) {
            isConfirmed = true;
            return new CommandResult(CONFIRMATION_MESSAGE, false, false, true);
        }
        return new CommandResult(MESSAGE_EXIT_ACKNOWLEDGEMENT, false, true, false);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof ExitCommand otherExitCommand)) {
            return false;
        }

        return isConfirmed == otherExitCommand.isConfirmed
                && input.equals(otherExitCommand.input);
    }

    @Override
    public String toString() {
        return "ExitCommand{"
                + "isConfirmed=" + isConfirmed
                + ", input='" + input + '\''
                + '}';
    }
}
