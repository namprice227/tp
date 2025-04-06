package seedu.address.model;

import java.nio.file.Path;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.Appointment;
import seedu.address.model.person.EmergencyPerson;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;

/**
 * The API of the Model component.
 */
public interface Model {
    /** {@code Predicate} that always evaluate to true */
    Predicate<Person> PREDICATE_SHOW_ALL_PERSONS = unused -> true;

    /**
     * Replaces user prefs data with the data in {@code userPrefs}.
     */
    void setUserPrefs(ReadOnlyUserPrefs userPrefs);

    /**
     * Returns the user prefs.
     */
    ReadOnlyUserPrefs getUserPrefs();

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Sets the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);

    /**
     * Returns the user prefs' address book file path.
     */
    Path getAddressBookFilePath();

    /**
     * Sets the user prefs' address book file path.
     */
    void setAddressBookFilePath(Path addressBookFilePath);

    /**
     * Replaces address book data with the data in {@code addressBook}.
     */
    void setAddressBook(ReadOnlyAddressBook addressBook);

    /** Returns the AddressBook */
    ReadOnlyAddressBook getAddressBook();

    ReadOnlyArchivedBook getArchivedBook();

    /**
     * Returns true if a person with the same identity as {@code person} exists in
     * the address book.
     */
    boolean hasPerson(Person person);

    boolean hasConflictingPerson(Person editedPerson, Person personToEdit);

    /**
     * Deletes the given person.
     * The person must exist in the address book.
     */
    void deletePerson(Person target);

    /**
     * Adds the given person.
     * {@code person} must not already exist in the address book.
     */
    void addPerson(Person person);

    /**
     * Add emergency contact to the given person {@code Person} with
     * {@code emergencyPerson}.
     */
    void addEmergencyContactToPerson(Person person, EmergencyPerson emergencyPerson);

    /**
     * Replaces the given person {@code target} with {@code editedPerson}.
     * {@code target} must exist in the address book.
     * The person identity of {@code editedPerson} must not be the same as another
     * existing person in the address book.
     */
    void setPerson(Person target, Person editedPerson);

    /**
     * Finds a person by their name.
     * @param name the name to search for
     * @return an Optional containing the person if found, or empty if not found
     */
    Optional<Person> findPersonByName(Name name);

    /**
     * Adds tags to the given person.
     * @param person the person to add tags to
     * @return the updated person with the new tags
     */
    Person addTagsToPerson(Person person, Set<Tag> allergies, Set<Tag> conditions,
                           Set<Tag> insurances);

    // Deletes a tag from the person's tags
    Person deleteTagFromPerson(Person person, Set<Tag> tagToDelete);

    /** Returns an unmodifiable view of the filtered person list */
    ObservableList<Person> getFilteredPersonList();

    /**
     * Updates the filter of the filtered person list to filter by the given
     * {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredPersonList(Predicate<Person> predicate);

    /**
     * Checks if the address book can be undone.
     * @return true if undo is possible, false otherwise.
     */
    boolean canUndoAddressBook();

    /**
     * Undoes the last command that modified the address book.
     * Throws CommandException if no undo is possible.
     */
    void undoAddressBook() throws CommandException;

    boolean isLastCommandArchiveRelated();

    void setLastCommandArchiveRelated(boolean isLastCommandArchiveRelated);

        /**
         * Checks if the address book can be redone.
         * @return true if redo is possible, false otherwise.
         */
    boolean canRedoAddressBook();

    /**
     * Redoes the last command that modified the address book.
     * Throws CommandException if no redo is possible.
     */
    void redoAddressBook() throws CommandException;

    /**
     * Commits the current state of the address book.
     */
    void commitAddressBook();

    boolean isArchiveMode();

    void setArchiveMode(boolean isArchiveMode);

    void updateArchivedFilteredPersonList(Predicate<Person> predicate);

    /**
     * Archives the specified person by adding them to the archive list
     */
    void archivePerson(Person person);

    /**
     * Remove the specified person from the archive list
     * and add them back to AddressBook
     */
    void unarchivePerson(Person person);

    /** Returns an unmodifiable view of the filtered archived list */
    ObservableList<Person> getFilteredArchivedPersonList();

    boolean hasSchedule(Appointment appointment);

    /**
     * Sorts the person list by name in alphabetical order.
     */
    void sortPersonListByName();

    /**
     * Sorts the person list by appointment date with earliest first.
     */
    void sortPersonListByAppointment();

    /**
     * Returns an empty AddressBook.
     */
    ReadOnlyAddressBook getEmptyAddressBook();
}
