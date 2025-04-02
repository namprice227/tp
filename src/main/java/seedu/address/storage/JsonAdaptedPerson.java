package seedu.address.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Appointment;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;

/**
 * Jackson-friendly version of {@link Person}.
 */
class JsonAdaptedPerson {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Person's %s field is missing!";

    private final String name;
    private final String phone;
    private final String email;
    private final String address;
    private final String appointment;
    private final JsonAdaptedEmergencyPerson emergencyContact;
    private final List<JsonAdaptedTag> allergyTags = new ArrayList<>();
    private final List<JsonAdaptedTag> conditionTags = new ArrayList<>();
    private final List<JsonAdaptedTag> insuranceTags = new ArrayList<>();

    /**
     * Constructs a {@code JsonAdaptedPerson} with the given person details.
     */
    @JsonCreator
    public JsonAdaptedPerson(@JsonProperty("name") String name, @JsonProperty("phone") String phone,
            @JsonProperty("email") String email, @JsonProperty("address") String address,
            @JsonProperty("allergyTags") List<JsonAdaptedTag> allergyTags,
            @JsonProperty("conditionTags") List<JsonAdaptedTag> conditionTags,
                             @JsonProperty("insuranceTags") List<JsonAdaptedTag> insuranceTags,
            @JsonProperty("appointment") String appointment,
                             @JsonProperty("emergencyContact") JsonAdaptedEmergencyPerson emergencyContact) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.appointment = appointment;
        this.emergencyContact = emergencyContact;
        if (allergyTags != null) {
            this.allergyTags.addAll(allergyTags);
        }
        if (conditionTags != null) {
            this.conditionTags.addAll(conditionTags);
        }
        if (insuranceTags != null) {
            this.insuranceTags.addAll(insuranceTags);
        }
    }

    /**
     * Converts a given {@code Person} into this class for Jackson use.
     */
    public JsonAdaptedPerson(Person source) {
        name = source.getName().toString();
        phone = source.getPhone().toString();
        email = source.getEmail().toString();
        address = source.getAddress().toString();
        appointment = source.getAppointment().toString();
        emergencyContact = new JsonAdaptedEmergencyPerson(source.getEmergencyContact());
        allergyTags.addAll(source.getAllergyTags().stream()
                .map(JsonAdaptedTag::new)
                .toList());
        conditionTags.addAll(source.getConditionTags().stream()
                .map(JsonAdaptedTag::new)
                .toList());
        insuranceTags.addAll(source.getInsuranceTags().stream()
                .map(JsonAdaptedTag::new)
                .toList());
    }

    /**
     * Converts this Jackson-friendly adapted person object into the model's {@code Person} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person.
     */
    public Person toModelType() throws IllegalValueException {

        final List<Tag> allergyTags = new ArrayList<>();
        for (JsonAdaptedTag tag: this.allergyTags) {
            allergyTags.add(tag.toModelType());
        }

        final Set<Tag> modelAllergyTags = new HashSet<>(allergyTags);

        final List<Tag> conditionTags = new ArrayList<>();
        for (JsonAdaptedTag tag: this.conditionTags) {
            conditionTags.add(tag.toModelType());
        }

        final Set<Tag> modelConditionTags = new HashSet<>(conditionTags);


        final List<Tag> insuranceTags = new ArrayList<>();
        for (JsonAdaptedTag tag: this.insuranceTags) {
            insuranceTags.add(tag.toModelType());
        }

        final Set<Tag> modelInsuranceTags = new HashSet<>(insuranceTags);

        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }
        if (!Name.isValidName(name)) {
            throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
        }
        final Name modelName = new Name(name);

        if (phone == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName()));
        }
        if (!Phone.isValidPhone(phone)) {
            throw new IllegalValueException(Phone.MESSAGE_CONSTRAINTS);
        }
        final Phone modelPhone = new Phone(phone);

        if (email == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName()));
        }
        if (!Email.isValidEmail(email)) {
            throw new IllegalValueException(Email.MESSAGE_CONSTRAINTS);
        }
        final Email modelEmail = new Email(email);

        if (address == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Address.class.getSimpleName()));
        }
        if (!Address.isValidAddress(address)) {
            throw new IllegalValueException(Address.MESSAGE_CONSTRAINTS);
        }
        final Address modelAddress = new Address(address);

        if (appointment == null) {
            throw new IllegalValueException(
                    String.format(MISSING_FIELD_MESSAGE_FORMAT, Appointment.class.getSimpleName()));
        }

        if ((appointment != "") && !Appointment.isValid(appointment)) {
            throw new IllegalValueException(Appointment.MESSAGE_CONSTRAINTS);
        }
        final Appointment modelAppointment = new Appointment(appointment);

        Person person = new Person(modelName, modelPhone, modelEmail, modelAddress,
                modelAllergyTags, modelConditionTags, modelInsuranceTags, modelAppointment, null);

        if (emergencyContact != null) {
            return person.setEmergencyContact(emergencyContact.toModelType());
        }

        return person;

    }
}
