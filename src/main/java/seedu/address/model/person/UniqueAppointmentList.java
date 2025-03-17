package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.person.exceptions.AppointmentNotFoundException;
import seedu.address.model.person.exceptions.DuplicateAppointmentException;

/**
 * A list of appointments that enforces uniqueness between its elements and does not allow nulls.
 * An appointment is considered unique by comparing using {@code Appointment#equals(Object)}.
 * The list is maintained in sorted order based on the appointment's dateTime, then person's name, then description.
 */
public class UniqueAppointmentList implements Iterable<Appointment> {

    private final TreeSet<Appointment> internalSet = new TreeSet<>(Comparator
            .comparing(Appointment::getDateTime)
            .thenComparing(a -> a.getPerson().getName().fullName)
            .thenComparing(Appointment::getDescription));

    /**
     * Returns true if the list contains an equivalent appointment as the given argument.
     */
    public boolean contains(Appointment toCheck) {
        requireNonNull(toCheck);
        return internalSet.contains(toCheck);
    }

    /**
     * Adds an appointment to the list.
     * The appointment must not already exist in the list.
     */
    public void add(Appointment toAdd) {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicateAppointmentException();
        }
        internalSet.add(toAdd);
    }

    /**
     * Removes the equivalent appointment from the list.
     * The appointment must exist in the list.
     */
    public void remove(Appointment toRemove) {
        requireNonNull(toRemove);
        if (!internalSet.remove(toRemove)) {
            throw new AppointmentNotFoundException();
        }
    }

    /**
     * Replaces the contents of this list with {@code appointments}.
     * {@code appointments} must not contain duplicate appointments.
     */
    public void setAppointments(List<Appointment> appointments) {
        requireNonNull(appointments);
        if (!appointmentsAreUnique(appointments)) {
            throw new DuplicateAppointmentException();
        }
        internalSet.clear();
        internalSet.addAll(appointments);
    }

    /**
     * Returns the appointments as an unmodifiable observable list.
     */
    public ObservableList<Appointment> asUnmodifiableObservableList() {
        return FXCollections.unmodifiableObservableList(FXCollections.observableArrayList(internalSet));
    }

    /**
     * Returns true if {@code appointments} contains only unique appointments.
     */
    private boolean appointmentsAreUnique(List<Appointment> appointments) {
        for (int i = 0; i < appointments.size() - 1; i++) {
            for (int j = i + 1; j < appointments.size(); j++) {
                if (appointments.get(i).equals(appointments.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public Iterator<Appointment> iterator() {
        return internalSet.iterator();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof UniqueAppointmentList)) {
            return false;
        }

        UniqueAppointmentList otherList = (UniqueAppointmentList) other;
        return internalSet.equals(otherList.internalSet);
    }

    @Override
    public int hashCode() {
        return internalSet.hashCode();
    }

    @Override
    public String toString() {
        return internalSet.toString();
    }
}
