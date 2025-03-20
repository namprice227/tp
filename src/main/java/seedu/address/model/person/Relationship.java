package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

/**
 * Represents a Relation for an EmergencyContact in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Relationship {
    public static final String MESSAGE_CONSTRAINTS =
            "Invalid relationship, and it should not be blank";

    private final String value;

    /**
     * Every field must be present and not null.
     */
    public Relationship(String relation) {
        requireNonNull(relation);
        this.value = relation.toLowerCase();
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

    @Override
    public String toString() {
        return value.substring(0, 1).toUpperCase() + value.substring(1);
    }

    /**
     * All possible and valid relation types.
     */
    public enum RelationshipType {
        MOTHER,
        FATHER,
        BROTHER,
        SISTER,
        SON,
        DAUGHTER,
        GRANDFATHER,
        GRANDMOTHER,
        AUNT,
        UNCLE,
        COUSIN,
        HUSBAND,
        WIFE,
        BOYFRIEND,
        GIRLFRIEND,
        FRIEND,
        GUARDIAN,
        SELF,
        OTHER
    }
}

