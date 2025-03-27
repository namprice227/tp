package seedu.address.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyArchivedBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.UserPrefs;

/**
 * Manages storage of AddressBook data in local storage.
 */
public class StorageManager implements Storage {

    private static final Logger logger = LogsCenter.getLogger(StorageManager.class);
    private AddressBookStorage addressBookStorage;
    private UserPrefsStorage userPrefsStorage;
    private ArchivedBookStorage archivedBookStorage;

    /**
     * Creates a {@code StorageManager} with the given {@code AddressBookStorage}, {@code UserPrefStorage}
     * and {@code ArchivedBookStorage}.
     */
    public StorageManager(AddressBookStorage addressBookStorage,
                          UserPrefsStorage userPrefsStorage,
                          ArchivedBookStorage archivedBookStorage) {
        this.addressBookStorage = addressBookStorage;
        this.userPrefsStorage = userPrefsStorage;
        this.archivedBookStorage = archivedBookStorage;
    }

    // ================ UserPrefs methods ==============================

    @Override
    public Path getUserPrefsFilePath() {
        return userPrefsStorage.getUserPrefsFilePath();
    }

    @Override
    public Optional<UserPrefs> readUserPrefs() throws DataLoadingException {
        return userPrefsStorage.readUserPrefs();
    }

    @Override
    public void saveUserPrefs(ReadOnlyUserPrefs userPrefs) throws IOException {
        userPrefsStorage.saveUserPrefs(userPrefs);
    }


    // ================ AddressBook methods ==============================

    @Override
    public Path getAddressBookFilePath() {
        return addressBookStorage.getAddressBookFilePath();
    }

    @Override
    public Optional<ReadOnlyAddressBook> readAddressBook() throws DataLoadingException {
        return readAddressBook(addressBookStorage.getAddressBookFilePath());
    }

    @Override
    public Optional<ReadOnlyAddressBook> readAddressBook(Path filePath) throws DataLoadingException {
        logger.fine("Attempting to read data from file: " + filePath);
        return addressBookStorage.readAddressBook(filePath);
    }

    @Override
    public void saveAddressBook(ReadOnlyAddressBook addressBook) throws IOException {
        saveAddressBook(addressBook, addressBookStorage.getAddressBookFilePath());
    }

    @Override
    public void saveAddressBook(ReadOnlyAddressBook addressBook, Path filePath) throws IOException {
        logger.fine("Attempting to write to data file: " + filePath);
        addressBookStorage.saveAddressBook(addressBook, filePath);
    }

    // ================ ArchivedBook methods ==============================

    @Override
    public Path getArchivedContactsFilePath() {
        return archivedBookStorage.getArchivedContactsFilePath();
    }

    @Override
    public Optional<ReadOnlyArchivedBook> readArchivedContacts() throws DataLoadingException {
        return readArchivedContacts(archivedBookStorage.getArchivedContactsFilePath());
    }

    @Override
    public Optional<ReadOnlyArchivedBook> readArchivedContacts(Path filePath) throws DataLoadingException {
        logger.fine("Attempting to read data from file: " + filePath);
        return archivedBookStorage.readArchivedContacts(filePath);
    }

    @Override
    public void saveArchivedContacts(ReadOnlyArchivedBook archivedAddressBook) throws IOException {
        saveArchivedContacts(archivedAddressBook, archivedBookStorage.getArchivedContactsFilePath());
    }

    @Override
    public void saveArchivedContacts(ReadOnlyArchivedBook archivedAddressBook, Path filePath) throws IOException {
        logger.fine("Saving archived contacts..." + filePath);
        archivedBookStorage.saveArchivedContacts(archivedAddressBook, filePath);
    }
}
