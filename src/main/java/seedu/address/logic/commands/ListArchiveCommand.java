package seedu.address.logic.commands;

import seedu.address.model.Model;
import seedu.address.model.person.Person;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

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
