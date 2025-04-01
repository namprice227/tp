package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
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
    private final List<Set<Tag>> tags;
    private final Set<Tag> allergies = new HashSet<>();
    private final Set<Tag> conditions = new HashSet<>();
    private final Set<Tag> insurances = new HashSet<>();
    private EmergencyPerson emergencyContact;
    private final Appointment appointment;
    /**
     * Every field must be present and not null.
     */

    public Person(Name name, Phone phone, Email email, Address address, List<Set<Tag>> tags,
                  Appointment appointment, EmergencyPerson emergencyContact) {
        requireAllNonNull(name, phone, email, address, tags);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        if (tags == null) {
            this.tags = new ArrayList<>();
        } else {
            this.allergies.addAll(tags.get(0));
            this.conditions.addAll(tags.get(1));
            this.insurances.addAll(tags.get(2));
            this.tags = tags;
        }
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

    public Person(Name name, Phone phone, Email email, Address address, Set<Tag> allergies, Set<Tag> conditions,
                  Set<Tag> insurances, Appointment appointment, EmergencyPerson emergencyContact) {
        requireAllNonNull(name, phone, email, address, allergies, conditions, insurances);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.allergies.addAll(allergies);
        this.conditions.addAll(conditions);
        this.insurances.addAll(insurances);
        this.tags = List.of(this.allergies, this.conditions, this.insurances);
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
    public List<Set<Tag>> getTags() {
        return List.of(allergies, conditions, insurances);
    }

    public Set<Tag> getAllergyTags() {
        return Collections.unmodifiableSet(allergies);
    }

    public Set<Tag> getConditionTags() {
        return Collections.unmodifiableSet(conditions);
    }

    public Set<Tag> getInsuranceTags() {
        return Collections.unmodifiableSet(insurances);
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
        if (!(other instanceof Person)) {
            return false;
        }
        if (getClass() != other.getClass()) {
            return false;
        }

        Person otherPerson = (Person) other;
        return name.equals(otherPerson.name)
                && phone.equals(otherPerson.phone)
                && email.equals(otherPerson.email)
                && address.equals(otherPerson.address)
                && tags.equals(otherPerson.tags)
                && emergencyContact.equals(otherPerson.emergencyContact)
                && appointment.equals(otherPerson.appointment);
    }

    private Set<Tag> mergeTags() {
        Set<Tag> allTags = new HashSet<>();
        allTags.addAll(allergies);
        allTags.addAll(conditions);
        allTags.addAll(insurances);
        return allTags;
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
