package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.util.List;

import javafx.collections.ObservableList;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.person.Person;
import seedu.address.model.person.UniquePersonList;

/**
 * Represents an ArchivedBook that stores archived contacts.
 * It behaves similarly to AddressBook but is meant for storing archived persons.
 */
public class ArchivedBook implements ReadOnlyArchivedBook {

    private final UniquePersonList archivedPersons;

    {
        archivedPersons = new UniquePersonList();
    }

    /**
     * Creates an empty ArchivedBook.
     */
    public ArchivedBook() {}

    /**
     * Creates an ArchivedBook using the Persons in {@code toBeCopied}.
     */
    public ArchivedBook(ReadOnlyArchivedBook toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    /**
     * Replaces the contents of the archived persons list with {@code persons}.
     */
    public void setPersons(List<Person> persons) {
        archivedPersons.setPersons(persons);
    }

    /**
     * Resets the existing data of this {@code ArchivedBook} with {@code newData}.
     */
    public void resetData(ReadOnlyArchivedBook newData) {
        requireNonNull(newData);
        setPersons(newData.getArchivedContactList());
    }

    /**
     * Adds a person to the archived contacts list.
     */
    public void addArchivedPerson(Person person) {
        requireNonNull(person);
        archivedPersons.add(person);
    }

    /**
     * Removes {@code key} from this {@code ArchivedBook}.
     * {@code key} must exist in the archived book.
     */
    public void unarchivePerson(Person person) {
        requireNonNull(person);
        archivedPersons.remove(person);
    }

    /**
     * Returns true if a person with the same identity as {@code person} exists in the archived book.
     */
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return archivedPersons.contains(person);
    }

    @Override
    public ObservableList<Person> getArchivedContactList() {
        return archivedPersons.asUnmodifiableObservableList();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .add("persons", archivedPersons)
            .toString();
    }

    @Override
    public boolean equals(Object other) {
        return other == this
            || (other instanceof ArchivedBook
            && archivedPersons.equals(other));
    }

    @Override
    public int hashCode() {
        return archivedPersons.hashCode();
    }
}
