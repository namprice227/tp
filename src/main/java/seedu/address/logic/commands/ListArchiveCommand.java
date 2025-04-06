package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

/**
 * Lists all archived contacts in the address book.
 */
public class ListArchiveCommand extends Command {

    public static final String COMMAND_WORD = "listarchive";
    public static final String MESSAGE_SUCCESS = "Listed all archived patients.";
    public static final String MESSAGE_INVALID_COMMAND = "Listarchive command does not accept additional parameters.";
    private final String args;
    /**
     * Creates a ListArchiveCommand to list all archived persons
     * @param args arguments provided after the command word
     */
    public ListArchiveCommand(String args) {
        this.args = args;
    }
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        if ((args != null) && !args.trim().isEmpty()) {
            throw new CommandException(MESSAGE_INVALID_COMMAND);
        }
        model.setArchiveMode(true);
        model.updateArchivedFilteredPersonList(Model.PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(MESSAGE_SUCCESS, CommandResult.ListType.ARCHIVE);
    }
}
