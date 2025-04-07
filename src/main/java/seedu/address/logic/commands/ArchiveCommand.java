package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Represents a command to archive a specific contact in the address book.
 */
public class ArchiveCommand extends Command {

    public static final String COMMAND_WORD = "archive";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Archives the specified contact. \n"
        + "Parameters: INDEX (must be a positive integer)\n"
        + "Example: " + COMMAND_WORD + " 1";
    public static final String MESSAGE_ARCHIVE_SUCCESS = "Archived Patient : %1$s is now in archive list! \n"
        + "Use 'listarchive' to see archive list.";
    public static final String MESSAGE_PERSON_NOT_FOUND = "This contact does not exist.";
    public final int targetIndex;

    /**
     * Construct an ArchiveCommand with the specified index.
     *
     * @param index the index of the contact ot archive
     */
    public ArchiveCommand(int index) {
        this.targetIndex = index;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (targetIndex < 0 || targetIndex >= model.getFilteredPersonList().size()) {
            throw new CommandException(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT,
            ArchiveCommand.MESSAGE_USAGE));
        }

        Person personToArchive = model.getFilteredPersonList().get(targetIndex);

        model.archivePerson(personToArchive);
        model.setLastCommandArchiveRelated(true);

        return new CommandResult(String.format(MESSAGE_ARCHIVE_SUCCESS, Messages.showName(personToArchive)));
    }
}
