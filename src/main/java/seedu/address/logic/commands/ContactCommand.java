package seedu.address.logic.commands;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

public class ContactCommand extends Command {
    public static final String COMMAND_WORD = "contact";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Change to address book\n";
    public static final String MESSAGE_SUCCESS = "Changed to address book";

    @Override
    public CommandResult execute(Model model) throws CommandException {
        model.showContactView(); // Reset UI back to contact mode
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
