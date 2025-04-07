package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Moves a person from ArchivedBook back to AddressBook.
 */
public class UnarchiveCommand extends Command {

    public static final String COMMAND_WORD = "unarchive";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Restores the specified archived contact. \n"
        + "Parameters: INDEX (must be a positive integer)\n"
        + "Example: " + COMMAND_WORD + " 1";
    public static final String MESSAGE_SUCCESS = "Restored Contact: %1$s is added back to patient list! \n"
        + "Use 'list' to see patient list.";
    public static final String MESSAGE_PERSON_NOT_FOUND = "Person not found in archive.";
    public final int targetIndex;

    public UnarchiveCommand(int targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (targetIndex <= 0 || targetIndex > model.getFilteredArchivedPersonList().size()) {
            throw new CommandException(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT,
            UnarchiveCommand.MESSAGE_USAGE));
        }

        Person personToRestore = model.getFilteredArchivedPersonList().get(targetIndex - 1);
        model.unarchivePerson(personToRestore);
        model.setLastCommandArchiveRelated(true);

        return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.showName(personToRestore)),
        CommandResult.ListType.ARCHIVE);
    }
}
