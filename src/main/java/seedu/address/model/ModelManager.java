package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.util.*;
import java.util.function.Predicate;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.person.EmergencyPerson;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.exceptions.PersonNotFoundException;
import seedu.address.model.tag.Tag;

/**
 * Represents the in-memory model of the address book data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final AddressBook addressBook;
    private final UserPrefs userPrefs;
    private final ArchivedBook archivedBook;
    private final FilteredList<Person> filteredPersons;
    private final FilteredList<Person> filteredArchivedPersons;
    private boolean showScheduleMode = false;


    /**
     * Initializes a ModelManager with the given addressBook and userPrefs.
     */
    public ModelManager(ReadOnlyAddressBook addressBook, ReadOnlyUserPrefs userPrefs,
                        ReadOnlyArchivedBook archivedBook) {
        requireAllNonNull(addressBook, userPrefs, archivedBook);

        logger.fine("Initializing with address book: " + addressBook
            + ", archived book: " + archivedBook
            + " and user prefs " + userPrefs);

        this.addressBook = new AddressBook(addressBook);
        this.userPrefs = new UserPrefs(userPrefs);
        this.archivedBook = new ArchivedBook(archivedBook);
        this.filteredPersons = new FilteredList<>(this.addressBook.getPersonList());
        this.filteredArchivedPersons = new FilteredList<>(this.archivedBook.getArchivedContactList());
    }

    public ModelManager() {
        this(new AddressBook(), new UserPrefs(), new ArchivedBook());
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
        setPerson(person, updatedPerson);
    }

    @Override
    public void archivePerson(Person person) {
        requireNonNull(person);
        archivedBook.addArchivedPerson(person);
        addressBook.removePerson(person);
    }

    @Override
    public void unarchivePerson(Person person) {
        requireNonNull(person);

        if (!archivedBook.hasPerson(person)) {
            throw new PersonNotFoundException();
        }

        archivedBook.unarchivePerson(person);
        addressBook.addPerson(person);
    }

    @Override
    public List<Person> getArchivedPersonList() {
        return archivedBook.getArchivedContactList().stream()
            .collect(Collectors.toList());

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
                updatedTags
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
    public ObservableList<Person> getFilteredArchivedPersonList() {
        return filteredArchivedPersons;
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
                && filteredPersons.equals(otherModelManager.filteredPersons)
                && archivedBook.equals(otherModelManager.archivedBook)
                && filteredArchivedPersons.equals(otherModelManager.filteredArchivedPersons);
    }

}
