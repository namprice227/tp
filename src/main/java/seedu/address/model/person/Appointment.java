package seedu.address.model.person;

import java.util.Objects;
import seedu.address.model.person.Person;
import seedu.address.model.person.DateTime;

/**
 * Represents a Schedule in the address book.
 * Guarantees: details are present and not null.
 */
public class Appointment {
    private final Person person;
    private final DateTime dateTime;
    private final String description;

    /**
     * Constructs a {@code Schedule}.
     *
     * @param person The person associated with the schedule.
     * @param dateTime The date and time of the schedule.
     * @param description A brief description of the event.
     */
    public Appointment(Person person, DateTime dateTime, String description) {
        this.person = Objects.requireNonNull(person);
        this.dateTime = Objects.requireNonNull(dateTime);
        this.description = Objects.requireNonNull(description);
    }

    public Person getPerson() {
        return person;
    }

    public DateTime getDateTime() {
        return dateTime;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Returns a formatted string representation of the schedule.
     */
    public String getFormattedDetails() {
        return String.format("%s - %s: %s", dateTime, person.getName(), description);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Appointment)) {
            return false;
        }
        Appointment otherAppointment = (Appointment) other;
        return person.equals(otherAppointment.person)
                && dateTime.equals(otherAppointment.dateTime)
                && description.equals(otherAppointment.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(person, dateTime, description);
    }

    @Override
    public String toString() {
        return getFormattedDetails();
    }
}
