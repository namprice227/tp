package seedu.address.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyArchivedBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.UserPrefs;

/**
 * API of the Storage component
 */
public interface Storage extends AddressBookStorage, UserPrefsStorage, ArchivedBookStorage {

    @Override
    Optional<UserPrefs> readUserPrefs() throws DataLoadingException;

    @Override
    void saveUserPrefs(ReadOnlyUserPrefs userPrefs) throws IOException;

    @Override
    Path getAddressBookFilePath();

    @Override
    Optional<ReadOnlyAddressBook> readAddressBook() throws DataLoadingException;

    @Override
    void saveAddressBook(ReadOnlyAddressBook addressBook) throws IOException;

    @Override
    Path getArchivedContactsFilePath();

    @Override
    Optional<ReadOnlyArchivedBook> readArchivedContacts() throws DataLoadingException;

    @Override
    void saveArchivedContacts(ReadOnlyArchivedBook archivedAddressBook) throws IOException;

    @Override
    void saveArchivedContacts(ReadOnlyArchivedBook archivedAddressBook, Path filePath) throws IOException;
}
