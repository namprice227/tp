package seedu.address.logic.commands;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

/**
 * Format full help instructions for every command for display.
 */
public class HelpCommand extends Command {

    public static final String COMMAND_WORD = "help";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Shows program usage instructions.\n"
            + "Example: " + COMMAND_WORD;

    public static final String SHOWING_HELP_MESSAGE = "Opened help window.";
    public static final String MESSAGE_INVALID_COMMAND = "Help command does not accept additional parameters.";

    private final String input;

    public HelpCommand(String input) {
        this.input = input;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        String[] parts = input.trim().split("\\s+"); // Split input by spaces

        if (parts.length > 1) { // If there's more than just "help"
            throw new CommandException(MESSAGE_INVALID_COMMAND);
        }

        return new CommandResult(SHOWING_HELP_MESSAGE, true, false, false);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof HelpCommand otherHelpCommand)) {
            return false;
        }

      return input.equals(otherHelpCommand.input);
    }

    @Override
    public String toString() {
        return "HelpCommand{" +
                "input='" + input + '\'' +
                '}';
    }
}
