package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.person.Appointment;
import seedu.address.model.person.EmergencyPerson;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;

/**
 * Represents the in-memory model of the address book data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final AddressBook addressBook;
    private final UserPrefs userPrefs;
    private final FilteredList<Person> filteredPersons;


    /**
     * Initializes a ModelManager with the given addressBook and userPrefs.
     */
    public ModelManager(ReadOnlyAddressBook addressBook, ReadOnlyUserPrefs userPrefs) {
        requireAllNonNull(addressBook, userPrefs);

        logger.fine("Initializing with address book: " + addressBook + " and user prefs " + userPrefs);

        this.addressBook = new AddressBook(addressBook);
        this.userPrefs = new UserPrefs(userPrefs);
        this.filteredPersons = new FilteredList<>(this.addressBook.getPersonList());
    }

    public ModelManager() {
        this(new AddressBook(), new UserPrefs());
    }

    //=========== UserPrefs ==================================================================================

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getAddressBookFilePath() {
        return userPrefs.getAddressBookFilePath();
    }

    @Override
    public void setAddressBookFilePath(Path addressBookFilePath) {
        requireNonNull(addressBookFilePath);
        userPrefs.setAddressBookFilePath(addressBookFilePath);
    }

    //=========== AddressBook ================================================================================

    @Override
    public void setAddressBook(ReadOnlyAddressBook addressBook) {
        this.addressBook.resetData(addressBook);
    }

    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return addressBook;
    }

    @Override
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return addressBook.hasPerson(person);
    }

    @Override
    public void deletePerson(Person target) {
        addressBook.removePerson(target);
    }

    @Override
    public void addPerson(Person person) {
        addressBook.addPerson(person);
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
    }

    @Override
    public void setPerson(Person target, Person editedPerson) {
        requireAllNonNull(target, editedPerson);

        addressBook.setPerson(target, editedPerson);
    }

    @Override
    public void addEmergencyContactToPerson(Person person, EmergencyPerson emergencyPerson) {
        requireAllNonNull(person, emergencyPerson);
        Person updatedPerson = person.setEmergencyContact(emergencyPerson);
    }

    //=========== Tag Command Methods ========================================================================

    @Override
    public Optional<Person> findPersonByName(Name name) {
        requireNonNull(name);
        return addressBook.getPersonList().stream()
                .filter(person -> person.getName().equals(name))
                .findFirst();
    }

    @Override
    public Person addTagsToPerson(Person person, Set<Tag> tagsToAdd) {
        requireAllNonNull(person, tagsToAdd);

        // Create a new set with all existing tags
        Set<Tag> updatedTags = new HashSet<>(person.getTags());

        // Add the new tags
        updatedTags.addAll(tagsToAdd);

        // Create a new person with the updated tags
        Person updatedPerson = new Person(
                person.getName(),
                person.getPhone(),
                person.getEmail(),
                person.getAddress(),
                updatedTags,
                person.getAppointment(),
                person.getEmergencyContact()
        );

        // Update the person in the address book
        setPerson(person, updatedPerson);

        return updatedPerson;
    }

    @Override
    public Person deleteTagFromPerson(Person person, Set<Tag> tagsToDelete) {
        requireAllNonNull(person, tagsToDelete);

        // Create a new set with all existing tags
        Set<Tag> updatedTags = new HashSet<>(person.getTags());

        // Remove the tags to delete
        updatedTags.removeAll(tagsToDelete);

        // Create a new person with the updated tags
        Person updatedPerson = new Person(
                person.getName(),
                person.getPhone(),
                person.getEmail(),
                person.getAddress(),
                updatedTags,
                person.getAppointment(),
                person.getEmergencyContact()
        );

        // Update the person in the address book
        setPerson(person, updatedPerson);

        return updatedPerson;
    }

    @Override
    public Person editTagForPerson(Person person, Tag oldTag, Tag newTag) {
        requireAllNonNull(person, oldTag, newTag);

        // Create a new set with all existing tags
        Set<Tag> updatedTags = new HashSet<>(person.getTags());

        // Remove the old tag and add the new tag
        updatedTags.remove(oldTag);
        updatedTags.add(newTag);

        // Create a new person with the updated tags
        Person updatedPerson = new Person(
                person.getName(),
                person.getPhone(),
                person.getEmail(),
                person.getAddress(),
                updatedTags,
                person.getAppointment(),
                person.getEmergencyContact()
        );

        // Update the person in the address book
        setPerson(person, updatedPerson);

        return updatedPerson;
    }

    //=========== Filtered Person List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Person} backed by the internal list of
     * {@code versionedAddressBook}
     */
    @Override
    public ObservableList<Person> getFilteredPersonList() {
        return filteredPersons;
    }

    @Override
    public void updateFilteredPersonList(Predicate<Person> predicate) {
        requireNonNull(predicate);
        filteredPersons.setPredicate(predicate);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ModelManager)) {
            return false;
        }

        ModelManager otherModelManager = (ModelManager) other;
        return addressBook.equals(otherModelManager.addressBook)
                && userPrefs.equals(otherModelManager.userPrefs)
                && filteredPersons.equals(otherModelManager.filteredPersons);
    }

    //=========== Schedule method =============================================================


    @Override
    public boolean hasSchedule(Appointment appointment) {
        requireNonNull(appointment);
        return addressBook.getPersonList().stream()
                .anyMatch(person -> person.getAppointment().equals(appointment));
    }
    @Override
    public void sortPersonListByName() {
        addressBook.sortPersonsByName();
    }

    @Override
    public void sortPersonListByAppointment() {
        addressBook.sortPersonsByAppointment();
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
    }
}
