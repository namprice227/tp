package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import seedu.address.model.Model;

/**
 * Sorts all persons in the address book by the specified field.
 */
public class SortCommand extends Command {
    public static final String COMMAND_WORD = "sort";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Sorts all patients in the address book "
          + "by the specified field.\n"
          + "Parameters: FIELD (must be either \"name\" or \"appointment\")\n"
          + "Example: " + COMMAND_WORD + " name";

    public static final String MESSAGE_SUCCESS = "Sorted all patients by %s!";

    private final String sortField;

    public SortCommand(String sortField) {
        this.sortField = sortField;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);

        switch (sortField.toLowerCase()) {
        case "name":
            model.sortPersonListByName();
            break;
        case "appointment":
            model.sortPersonListByAppointment();
            break;
        default:
            throw new AssertionError("Unknown sort field: " + sortField);
        }

        model.setLastCommandArchiveRelated(false);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(String.format(MESSAGE_SUCCESS, sortField));
    }

    @Override
    public boolean equals(Object other) {
        return other == this
            || (other instanceof SortCommand
            && sortField.equals(((SortCommand) other).sortField));
    }
}
