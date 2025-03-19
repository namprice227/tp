package seedu.address.model.person.exceptions;

/**
 * Signals that the appointment being removed or updated was not found.
 */
public class AppointmentNotFoundException extends RuntimeException {
    public AppointmentNotFoundException() {
        super("Appointment not found");
    }
}
