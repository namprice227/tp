package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Deletes a patient identified using it's displayed index from the address book.
 */
public class DeleteCommand extends Command {

    public static final String COMMAND_WORD = "delete";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the patient identified by the index number used"
            + " in the displayed patient list.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_DELETE_PERSON_SUCCESS = "Deleted Patient : %1$s is removed.";
    public static final String MESSAGE_CONFIRMATION = "Are you sure you want to delete this patient? (y/n)";

    private final Index targetIndex;
    private boolean needsConfirmation;

    /**
     * Constructs a DeleteCommand to delete the person at the specified index.
     *
     * @param targetIndex the index of the person to delete
     */
    public DeleteCommand(Index targetIndex) {
        requireNonNull(targetIndex);

        this.targetIndex = targetIndex;
        this.needsConfirmation = true;

    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        // Use ParserUtil's message for consistency with parser
        if (targetIndex.getZeroBased() >= lastShownList.size() || targetIndex.getZeroBased() < 0) {
            throw new CommandException(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT,
            DeleteCommand.MESSAGE_USAGE));
        }

        if (needsConfirmation) {
            needsConfirmation = false;
            return new CommandResult(MESSAGE_CONFIRMATION, false, false, true);
        }

        Person personToDelete = lastShownList.get(targetIndex.getZeroBased());
        model.setLastCommandArchiveRelated(false);
        model.deletePerson(personToDelete);

        return new CommandResult(String.format(MESSAGE_DELETE_PERSON_SUCCESS, Messages.showName(personToDelete)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DeleteCommand otherDeleteCommand)) {
            return false;
        }

        return targetIndex.equals(otherDeleteCommand.targetIndex);
    }

    public void setConfirmation(boolean needsConfirmation) {
        this.needsConfirmation = needsConfirmation;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .toString();
    }
}
