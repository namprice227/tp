package seedu.address.logic.commands;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.DateTime;
import seedu.address.model.person.Person;

import java.util.List;

import static java.util.Objects.requireNonNull;

public class ScheduleCommand extends Command{
    public static final String COMMAND_WORD = "schedule";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Adds a schedule to the schedule list.\n"
            + "Parameters: INDEX (must be a positive integer)"
            + "DD-MM-YYYY HH:MM\n";
    public static final String MESSAGE_SUCCESS = "New schedule added";
    public static final String MESSAGE_INVALID_DATETIME = ""
            + "The format should be schedule DD-MM-YYYY HH:MM";
    private final Index index;
    private DateTime dateTime;
    private final boolean showAllSchedules; // Flag to show all schedules


    public ScheduleCommand(Index index, DateTime dateTime) {
        this.index = index;
        this.dateTime = dateTime;
        this.showAllSchedules = false;
    }

    public ScheduleCommand() {
        this.index = null;
        this.dateTime = null;
        this.showAllSchedules = true;
    }

    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToEdit = lastShownList.get(index.getZeroBased());
        personToEdit.addDateTime(dateTime);
        model.updateFilteredPersonList(p -> true);;
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
