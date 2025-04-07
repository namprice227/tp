package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

/**
 * Lists all persons in the address book to the user.
 */
public class ListCommand extends Command {

    public static final String COMMAND_WORD = "list";
    public static final String MESSAGE_SUCCESS = "Listed all patients";
    public static final String MESSAGE_INVALID_COMMAND = "List command does not accept additional parameters.";
    private final String args;

    /**
     * Creates a ListCommand to list all persons
     * @param args arguments provided after the command word
     */
    public ListCommand(String args) {
        this.args = args;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        if ((args != null) && !args.trim().isEmpty()) {
            throw new CommandException(MESSAGE_INVALID_COMMAND);
        }
        model.setArchiveMode(false);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(MESSAGE_SUCCESS, CommandResult.ListType.NORMAL);
    }
}
