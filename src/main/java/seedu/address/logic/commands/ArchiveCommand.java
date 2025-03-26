package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

public class ArchiveCommand extends Command {

    public static final String COMMAND_WORD = "archive";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Archives the specified contact. \n"
        + "Parameters: INDEX (must be a positive integer)\n"
        + "Example: " + COMMAND_WORD + " 1";
    public static final String MESSAGE_ARCHIVE_SUCCESS = "Archived Contact : %1$s";
    public static final String MESSAGE_PERSON_NOT_FOUND = "This contact does not exist.";
    public final int targetIndex;

    public ArchiveCommand(int index) {
        this.targetIndex = index;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (targetIndex < 0 || targetIndex >= model.getFilteredPersonList().size()) {
            throw new CommandException(MESSAGE_PERSON_NOT_FOUND);
        }

        Person personToArchive = model.getFilteredPersonList().get(targetIndex);

        model.archivePerson(personToArchive);

        return new CommandResult(String.format(MESSAGE_ARCHIVE_SUCCESS, personToArchive));
    }
}
