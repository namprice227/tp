package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Appointment;
import seedu.address.model.person.Person;

/**
 * Represents a command to schedule an appointment for a person in the address book.
 */
public class ScheduleCommand extends Command {
    public static final String COMMAND_WORD = "schedule";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Adds a schedule to the schedule list.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "DD-MM-YYYY HH:MM\n";

    public static final String MESSAGE_SUCCESS = "New schedule added";
    public static final String MESSAGE_SHOW_SCHEDULE = "Show schedule";
    public static final String MESSAGE_INVALID_DATETIME =
            "The format should be schedule DD-MM-YYYY HH:MM";
    public static final String MESSAGE_INVALID_FUTURE =
            "The date must be in the future";
    public static final String MESSAGE_DUPLICATE_APPOINTMENT = "This appointment already exists in the schedule";

    private final Index index;
    private Appointment appointment;

    /**
     * Constructs a {@code ScheduleCommand} to schedule an appointment for a specific person.
     *
     * @param index The index of the person in the filtered person list.
     * @param appointment The appointment date and time.
     */
    public ScheduleCommand(Index index, Appointment appointment) {
        this.index = index;
        this.appointment = appointment;
    }

    /**
     * Executes the schedule command by adding an appointment to a selected person.
     *
     * @param model The model which maintains the list of persons and appointments.
     * @return A CommandResult indicating the outcome of execution.
     * @throws CommandException If the provided index is out of bounds.
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (model.hasSchedule(appointment)) {
            throw new CommandException(MESSAGE_DUPLICATE_APPOINTMENT);
        }

        List<Person> lastShownList = model.getFilteredPersonList();
        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToEdit = lastShownList.get(index.getZeroBased());
        System.out.println(appointment);
        System.out.println(index);

        // Create an updated person instance with the new appointment
        Person editedPerson = personToEdit.withAppointment(appointment);

        // Update the model with the edited person
        model.setPerson(personToEdit, editedPerson);

        return new CommandResult(MESSAGE_SUCCESS);
    }
}
