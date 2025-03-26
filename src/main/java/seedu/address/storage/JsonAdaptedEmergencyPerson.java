package seedu.address.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.EmergencyPerson;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Relationship;

/**
 * Jackson-friendly version of {@link EmergencyPerson}.
 */
public class JsonAdaptedEmergencyPerson {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Emergency contact's %s field is missing!";

    private final String name;
    private final String phone;
    private final String relationship;

    /**
     * Constructs a {@code JsonAdaptedEmergencyPerson} with the given emergency contact details.
     */
    @JsonCreator
    public JsonAdaptedEmergencyPerson(@JsonProperty("name") String name,
                                     @JsonProperty("phone") String phone,
                                     @JsonProperty("relationship") String relationship) {
        this.name = name;
        this.phone = phone;
        this.relationship = relationship;
    }

    /**
     * Converts a given {@code EmergencyPerson} into this class for Jackson use.
     */
    public JsonAdaptedEmergencyPerson(EmergencyPerson source) {
        name = source.getName().toString();
        phone = source.getPhone().toString();
        relationship = source.getRelationship().toString();
    }

    /**
     * Converts this Jackson-friendly adapted emergency person object into the model's {@code EmergencyPerson} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted emergency person.
     */
    public EmergencyPerson toModelType() throws IllegalValueException {
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

        if (relationship == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Relationship.class.getSimpleName()));
        }
        if (!Relationship.isValidRelationship(relationship)) {
            throw new IllegalValueException(Relationship.MESSAGE_CONSTRAINTS);
        }
        final Relationship modelRelationship = new Relationship(relationship);

        return new EmergencyPerson(modelName, modelPhone, modelRelationship);
    }
}
