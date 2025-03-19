package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Represents a EmergencyPerson for a Person in HealthSync
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class EmergencyPerson {
    private final Name name;
    private final Phone phone;
    private final Relationship relationship;

    /**
     *
     */
    public EmergencyPerson(Name name, Phone phone, Relationship relationship) {
        requireAllNonNull(name, phone, relationship);
        this.name = name;
        this.phone = phone;
        this.relationship = relationship;
    }

    public Name getName() {
        return name;
    }

    public Phone getPhone() {
        return phone;
    }

    public String getRelationship() {
        return relationship.toString();
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

        if (!(other instanceof Person)) {
            return false;
        }
        EmergencyPerson otherPerson = (EmergencyPerson) other;
        return name.equals(otherPerson.name)
                && phone.equals(otherPerson.phone)
                && relationship.equals(otherPerson.relationship);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("phone", phone)
                .add("relationship", relationship)
                .toString();
    }
}


