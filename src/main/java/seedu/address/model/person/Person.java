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

    // Static fields
    public static final EmergencyPerson NIL_EMERGENCY_CONTACT = new EmergencyPerson(
        new Name("NIL"), new Phone("00000000"), new Relationship("NIL"));

    // Identity fields
    private final Name name;
    private final Phone phone;
    private final Email email;

    // Data fields
    private final Address address;
    private final Set<Tag> tags = new HashSet<>();
    private EmergencyPerson emergencyContact;
    private final Appointment appointment;
    /**
     * Every field must be present and not null.
     */
    public Person(Name name, Phone phone, Email email, Address address, Set<Tag> tags,
                  Appointment appointment, EmergencyPerson emergencyContact) {
        requireAllNonNull(name, phone, email, address, tags);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.tags.addAll(tags);
        if (appointment == null) {
            this.appointment = new Appointment();
        } else {
            this.appointment = appointment;
        }
        if (emergencyContact == null) {
            this.emergencyContact = NIL_EMERGENCY_CONTACT;
        } else {
            this.emergencyContact = emergencyContact;
        }
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

    public EmergencyPerson getEmergencyContact() {
        return emergencyContact;
    }

    public Person setEmergencyContact(EmergencyPerson emergencyContact) {
        Person newPerson = new Person(name, phone, email, address, tags, appointment, emergencyContact);
        return newPerson;
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
     * @param appointment Appointment containing date and time
     * @return A new {@code Person} instance with the updated appointment.
     */
    public Person withAppointment(Appointment appointment) {
        return new Person(name, phone, email, address, tags, appointment, emergencyContact);
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
     * Returns the earliest appointment date for this person.
     * Returns a far future date if no appointments exist.
     */
    public DateTime getEarliestAppointment() {
        if (appointment == null || appointment.getDateTime() == null) {
            // Create a far future date that will sort after all real dates
            return new DateTime("31-12-9999 23:59");
        }
        return appointment.getDateTime();
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
                && address.equals(otherPerson.address)
                && tags.equals(otherPerson.tags)
                && emergencyContact.equals(otherPerson.emergencyContact)
                && Objects.equals(appointment, otherPerson.appointment);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, phone, email, address, tags, emergencyContact);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("phone", phone)
                .add("email", email)
                .add("address", address)
                .add("tags", tags)
                .add("emergencyContact", emergencyContact)
                .add("appointment", appointment)
                .toString();
    }
}
