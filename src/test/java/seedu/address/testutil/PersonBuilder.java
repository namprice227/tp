package seedu.address.testutil;

import java.util.HashSet;
import java.util.Set;

import seedu.address.model.person.Address;
import seedu.address.model.person.Appointment;
import seedu.address.model.person.Email;
import seedu.address.model.person.EmergencyPerson;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Relationship;
import seedu.address.model.tag.Tag;
import seedu.address.model.util.SampleDataUtil;

/**
 * A utility class to help with building Person objects.
 */
public class PersonBuilder {

    public static final String DEFAULT_NAME = "Amy Bee";
    public static final String DEFAULT_PHONE = "85355255";
    public static final String DEFAULT_EMAIL = "amy@gmail.com";
    public static final String DEFAULT_ADDRESS = "123, Jurong West Ave 6, #08-111";
    public static final String DEFAULT_EMERGENCY_PERSON = "Mom Mee";
    public static final String DEFAULT_EMERGENCY_PHONE = "85355255";
    public static final String DEFAULT_RELATIONSHIP = "MOTHER";
    public static final String DEFAULT_APPOINTMENT = "31-12-2025 10:00";

    private Name name;
    private Phone phone;
    private Email email;
    private Address address;
    private Set<Tag> allergies = new HashSet<>();
    private Set<Tag> conditions = new HashSet<>();
    private Set<Tag> insurances = new HashSet<>();
    private EmergencyPerson emergencyPerson;
    private Appointment appointment;

    /**
     * Creates a {@code PersonBuilder} with the default details.
     */
    public PersonBuilder() {
        name = new Name(DEFAULT_NAME);
        phone = new Phone(DEFAULT_PHONE);
        email = new Email(DEFAULT_EMAIL);
        address = new Address(DEFAULT_ADDRESS);
        emergencyPerson = new EmergencyPerson(new Name(DEFAULT_EMERGENCY_PERSON),
                new Phone(DEFAULT_EMERGENCY_PHONE), new Relationship(DEFAULT_RELATIONSHIP));
        appointment = new Appointment(DEFAULT_APPOINTMENT);
    }



    /**
     * Initializes the PersonBuilder with the data of {@code personToCopy}.
     */
    public PersonBuilder(Person personToCopy) {
        name = personToCopy.getName();
        phone = personToCopy.getPhone();
        email = personToCopy.getEmail();
        address = personToCopy.getAddress();
        allergies = new HashSet<>(personToCopy.getAllergyTags());
        conditions = new HashSet<>(personToCopy.getConditionTags());
        insurances = new HashSet<>(personToCopy.getInsuranceTags());
        emergencyPerson = personToCopy.getEmergencyContact();
        appointment = personToCopy.getAppointment();
    }

    /**
     * Sets the {@code Name} of the {@code Person} that we are building.
     */
    public PersonBuilder withName(String name) {
        this.name = new Name(name);
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code Person} that we are building.
     */
    public PersonBuilder withPhone(String phone) {
        this.phone = new Phone(phone);
        return this;
    }

    /**
     * Sets the {@code Email} of the {@code Person} that we are building.
     */
    public PersonBuilder withEmail(String email) {
        this.email = new Email(email);
        return this;
    }

    /**
     * Sets the {@code Address} of the {@code Person} that we are building.
     */
    public PersonBuilder withAddress(String address) {
        this.address = new Address(address);
        return this;
    }

    /**
     * Sets the {@code EmergencyPerson} of the {@code Person} that we are building.
     */
    public PersonBuilder withEmergencyPerson(String name, String phone, String relationship) {
        this.emergencyPerson = new EmergencyPerson(new Name(name), new Phone(phone), new Relationship(relationship));
        return this;
    }

    public PersonBuilder withEmergencyContact(EmergencyPerson emergencyPerson) {
        this.emergencyPerson = emergencyPerson;
        return this;
    }

    /**
     * Sets the {@code Appointment} of the {@code Person} that we are building.
     */
    public PersonBuilder withAppointment(String appointment) {
        this.appointment = new Appointment(appointment);
        return this;
    }

    /**
     * Sets the {@code Allergies} of the {@code Person} that we are building.
     */
    public PersonBuilder withAllergies(String... tags) {
        for (String tag : tags) {
            allergies.add(new Tag(tag));
        }
        return this;
    }

    /**
     * Sets the {@code Conditions} of the {@code Person} that we are building.
     */
    public PersonBuilder withConditions(String... tags) {
        for (String tag : tags) {
            conditions.add(new Tag(tag));
        }
        return this;
    }

    /**
     * Sets the {@code Insurances} of the {@code Person} that we are building.
     */
    public PersonBuilder withInsurances(String... tags) {
        for (String tag : tags) {
            insurances.add(new Tag(tag));
        }
        return this;
    }

    /**
     * Builds the {@code Person}.
     */
    public Person build() {
        return new Person(name, phone, email, address, allergies, conditions, insurances, appointment, emergencyPerson);
    }
}
