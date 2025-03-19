package seedu.address.model.person.exceptions;

/**
 * Signals that the operation would result in duplicate appointments.
 */
public class DuplicateAppointmentException extends RuntimeException {
    public DuplicateAppointmentException() {
        super("Operation would result in duplicate appointments");
    }
}
