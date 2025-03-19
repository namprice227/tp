package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

/**
 * Represents a Relation for an EmergencyContact in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Relationship {

    public static final String MESSAGE_CONSTRAINTS =
            "Invalid relationship, and it should not be blank";

    /**
     * Every field must be present and not null.
     */
    public Relationship(String relation) {
        requireNonNull(relation);
    }

    /**
     * Returns true if relation provided exists as an enum.
     */
    public static boolean isValidRelation(String relation) {
        for (RelationshipType type : RelationshipType.values()) {
            if (type.toString().equalsIgnoreCase(relation)) {
                return true;
            }
        }
        return false;
    }

    /**
     * All possible and valid relation types.
     */
    public enum RelationshipType {
        Mother,
        Father,
        Brother,
        Sister,
        Son,
        Daughter,
        Grandfather,
        Grandmother,
        Aunt,
        Uncle,
        Cousin,
        Husband,
        Wife,
        Boyfriend,
        Girlfriend,
        Friend,
        Guardian,
        Other
    }
}

