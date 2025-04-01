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
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.Appointment;
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
    private final VersionedAddressBook versionedAddressBook;
    private final UserPrefs userPrefs;
    private final ArchivedBook archivedBook;
    private final FilteredList<Person> filteredPersons;
    private final FilteredList<Person> filteredArchivedPersons;

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
        this.versionedAddressBook = new VersionedAddressBook(addressBook);
        this.userPrefs = new UserPrefs(userPrefs);
        this.archivedBook = new ArchivedBook(archivedBook);

        this.filteredPersons = new FilteredList<>(this.versionedAddressBook.getPersonList());
        this.filteredArchivedPersons = new FilteredList<>(this.archivedBook.getArchivedContactList());
    }

    public ModelManager() {
        this(new AddressBook(), new UserPrefs(), new ArchivedBook());
    }

    // =========== UserPrefs
    // ==================================================================================

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

    // =========== AddressBook Undo/Redo Methods
    // ==================================================================

    @Override
    public void commitAddressBook() {
        versionedAddressBook.commit();
    }

    @Override
    public void undoAddressBook() throws CommandException {
        versionedAddressBook.undo();
    }

    @Override
    public boolean canUndoAddressBook() {
        return versionedAddressBook.canUndo();
    }

    @Override
    public void redoAddressBook() throws CommandException {
        versionedAddressBook.redo();
    }

    @Override
    public boolean canRedoAddressBook() {
        return versionedAddressBook.canRedo();
    }

    // =========== AddressBook Methods
    // ========================================================================

    @Override
    public void setAddressBook(ReadOnlyAddressBook addressBook) {
        versionedAddressBook.resetData(addressBook);
        commitAddressBook();
    }

    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return versionedAddressBook;
    }

    @Override
    public ReadOnlyArchivedBook getArchivedBook() {
        return archivedBook;
    }

    @Override
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return versionedAddressBook.hasPerson(person);
    }

    @Override
    public void deletePerson(Person target) {
        versionedAddressBook.removePerson(target);
        commitAddressBook();
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
    }

    @Override
    public void addPerson(Person person) {
        versionedAddressBook.addPerson(person);
        commitAddressBook();
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
    }

    @Override
    public void setPerson(Person target, Person editedPerson) {
        requireAllNonNull(target, editedPerson);
        versionedAddressBook.setPerson(target, editedPerson);
        commitAddressBook();
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
        versionedAddressBook.removePerson(person);
        commitAddressBook();
    }

    @Override
    public void unarchivePerson(Person person) {
        requireNonNull(person);

        if (!archivedBook.hasPerson(person)) {
            throw new PersonNotFoundException();
        }

        archivedBook.unarchivePerson(person);
        versionedAddressBook.addPerson(person);
        commitAddressBook();
    }

    // =========== Tag Command Methods
    // ========================================================================

    @Override
    public Optional<Person> findPersonByName(Name name) {
        requireNonNull(name);
        return versionedAddressBook.getPersonList().stream()
                .filter(person -> person.getName().equals(name))
                .findFirst();
    }

    @Override
    public Person addTagsToPerson(Person person, Set<Tag> allergies, Set<Tag> conditions,
                                  Set<Tag> insurances) {
        requireAllNonNull(person);

        Set<Tag> currentAllergies = new HashSet<>(person.getAllergyTags());
        currentAllergies.addAll(allergies);
        Set<Tag> currentConditions = new HashSet<>(person.getConditionTags());
        currentConditions.addAll(conditions);
        Set<Tag> currentInsurances = new HashSet<>(person.getInsuranceTags());
        currentInsurances.addAll(insurances);

        // Create a new person with the updated tags
        Person updatedPerson = new Person(
                person.getName(),
                person.getPhone(),
                person.getEmail(),
                person.getAddress(),
                currentAllergies,
                currentConditions,
                currentInsurances,
                person.getAppointment(),
                person.getEmergencyContact());

        // Update the person in the address book
        setPerson(person, updatedPerson);

        return updatedPerson;
    }

    @Override
    public Person deleteTagFromPerson(Person person, Set<Tag> tagsToDelete) {
        requireAllNonNull(person, tagsToDelete);

        Set<Tag> currentAllergies = new HashSet<>(person.getAllergyTags());
        Set<Tag> currentConditions = new HashSet<>(person.getConditionTags());
        Set<Tag> currentInsurances = new HashSet<>(person.getInsuranceTags());

        // Remove the tags to delete
        currentAllergies.removeAll(tagsToDelete);
        currentConditions.removeAll(tagsToDelete);
        currentInsurances.removeAll(tagsToDelete);

        // Create a new person with the updated tags
        Person updatedPerson = new Person(
                person.getName(),
                person.getPhone(),
                person.getEmail(),
                person.getAddress(),
                currentAllergies,
                currentConditions,
                currentInsurances,
                person.getAppointment(),
                person.getEmergencyContact());

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
                person.getEmergencyContact());

        // Update the person in the address book
        setPerson(person, updatedPerson);

        return updatedPerson;
    }

    // =========== Filtered Person List Accessors
    // =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Person} backed by the
     * internal list of
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
    public void updateArchivedFilteredPersonList(Predicate<Person> predicate) {
        requireNonNull(predicate);
        filteredArchivedPersons.setPredicate(predicate);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ModelManager otherModelManager)) {
            return false;
        }

        return versionedAddressBook.equals(otherModelManager.versionedAddressBook)
                && userPrefs.equals(otherModelManager.userPrefs)
                && filteredPersons.equals(otherModelManager.filteredPersons)
                && archivedBook.equals(otherModelManager.archivedBook)
                && filteredArchivedPersons.equals(otherModelManager.filteredArchivedPersons);
    }

    // =========== Schedule method
    // =============================================================

    @Override
    public boolean hasSchedule(Appointment appointment) {
        requireNonNull(appointment);
        return versionedAddressBook.getPersonList().stream()
                .anyMatch(person -> person.getAppointment().equals(appointment));
    }

    @Override
    public void sortPersonListByName() {
        versionedAddressBook.sortPersonsByName();
        commitAddressBook();
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
    }

    @Override
    public void sortPersonListByAppointment() {
        versionedAddressBook.sortPersonsByAppointment();
        commitAddressBook();
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
    }

    @Override
    public ReadOnlyAddressBook getEmptyAddressBook() {
        return new AddressBook();
    }
}
