package seedu.address.logic.commands;

import seedu.address.model.Model;
import seedu.address.model.person.Person;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

/**
 * Lists all archived contacts in the address book.
 */
public class ListArchiveCommand extends Command {

    public static final String COMMAND_WORD = "listarchive";
    public static final String MESSAGE_SUCCESS = "Archived Contacts:\n%1$s";

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        List<Person> archivedContacts = model.getArchivedPersonList();

        if (archivedContacts.isEmpty()) {
            return new CommandResult("No archived contacts found.");
        }

        String archivedList = archivedContacts.stream()
            .map(Person::toString)
            .collect(Collectors.joining("\n"));

        return new CommandResult(String.format(MESSAGE_SUCCESS, archivedList));
    }
}
