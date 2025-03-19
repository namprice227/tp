package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.tag.Tag;

/**
 * Represents a Person in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Person {

    // Identity fields
    private final Name name;
    private final Phone phone;
    private final Email email;

    // Data fields
    private final Address address;
    private final Set<Tag> tags = new HashSet<>();
    private Appointment appointment;

    /**
     * Every field must be present and not null.
     */
    public Person(Name name, Phone phone, Email email, Address address, Set<Tag> tags, Appointment appointment) {
        requireAllNonNull(name, phone, email, address, tags);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.tags.addAll(tags);
        this.appointment = appointment;
    }

    /**
     * Constructs a {@code Person} with all specified details, including an appointment.
     *
     * @param name The person's name.
     * @param phone The person's phone number.
     * @param email The person's email address.
     * @param address The person's home address.
     * @param tags The set of tags associated with the person.
     * @param appointment The appointment associated with the person.
     */
    public Person(Name name, Phone phone, Email email, Address address, Set<Tag> tags) {
        requireAllNonNull(name, phone, email, address, tags);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.tags.addAll(tags);
        this.appointment = new Appointment();
    }

    public Name getName() {
        return name;
    }

    public Phone getPhone() {
        return phone;
    }

    public Email getEmail() {
        return email;
    }

    public Address getAddress() {
        return address;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public boolean hasAppointment() {
        return appointment != null;
    }

    /**
     * Returns a new {@code Person} instance with the given appointment date and time.
     * The existing person's details remain unchanged, ensuring immutability.
     *
     * @param dateTime The date and time of the appointment.
     * @return A new {@code Person} instance with the updated appointment.
     */

    public Person withAppointment(DateTime dateTime) {
        Appointment newAppointment = new Appointment(dateTime, "");
        return new Person(name, phone, email, address, tags, newAppointment);
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    /**
     * Returns true if both persons have the same name.
     * This defines a weaker notion of equality between two persons.
     */
    public boolean isSamePerson(Person otherPerson) {
        if (otherPerson == this) {
            return true;
        }

        return otherPerson != null && otherPerson.getName().equals(getName())
                && (otherPerson.getPhone().equals(getPhone())
                || otherPerson.getEmail().equals(getEmail()));
    }

    /**
     * Returns true if both persons have the same identity and data fields.
     * This defines a stronger notion of equality between two persons.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        //if (!(other instanceof Person)) {
        if (other == null || getClass() != other.getClass()) {
            return false;
        }

        Person otherPerson = (Person) other;
        return name.equals(otherPerson.name)
                && phone.equals(otherPerson.phone)
                && email.equals(otherPerson.email)
                && address.equals(otherPerson.address);
        //&& tags.equals(otherPerson.tags);
                && address.equals(otherPerson.address)
                && tags.equals(otherPerson.tags)
                && Objects.equals(appointment, otherPerson.appointment);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, phone, email, address);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("phone", phone)
                .add("email", email)
                .add("address", address)
                .toString();
    }
}
