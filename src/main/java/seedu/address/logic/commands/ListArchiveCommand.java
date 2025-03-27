package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import seedu.address.model.Model;

/**
 * Lists all archived contacts in the address book.
 */
public class ListArchiveCommand extends Command {

    public static final String COMMAND_WORD = "listarchive";
    public static final String MESSAGE_SUCCESS = "Archived Contacts:\n%1$s";

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateArchivedFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
