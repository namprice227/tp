package seedu.address.model.person;

import java.util.Objects;

/**
 * Represents a Schedule in the address book.
 * Guarantees: details are present and not null.
 */
public class Appointment implements Comparable<Appointment> {
    private final DateTime dateTime;
    private final String description;

    /**
     * Constructs a {@code Schedule}.
     *
     * @param dateTime The date and time of the schedule.
     * @param description A brief description of the event.
     */
    public Appointment(DateTime dateTime, String description) {
        this.dateTime = Objects.requireNonNull(dateTime);
        this.description = Objects.requireNonNull(description);
    }

    /**
     * Constructs a {@code Schedule}.
     */
    public Appointment() {
        this.dateTime = null;
        this.description = "";
    }

    public DateTime getDateTime() {
        return dateTime;
    }

    public String getDescription() {
        return description;
    }

    public int compareTo(Appointment other) {
        return this.dateTime.compareTo(other.dateTime);
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
        if (dateTime == null && otherAppointment.dateTime == null) {
            return true;
        }

        return dateTime != null && dateTime.equals(otherAppointment.dateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dateTime, description);
    }

    @Override
    public String toString() {
        if (dateTime == null) {
            return "";
        }
        return dateTime.toString();
    }
}
