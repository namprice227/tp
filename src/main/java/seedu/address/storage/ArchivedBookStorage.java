package seedu.address.storage;

import java.nio.file.Path;
import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyArchivedBook;

import java.io.IOException;
import java.util.Optional;

/**
 * Represents a storage for archived contacts.
 */
public interface ArchivedBookStorage {

    /**
     * Returns the file path of the archived contacts data file.
     */
    Path getArchivedContactsFilePath();

    /**
     * Returns archived contacts as a {@link ReadOnlyArchivedBook}.
     * Returns {@code Optional.empty()} if the archive storage file is not found.
     *
     * @throws DataLoadingException if loading the data from storage failed.
     */
    Optional<ReadOnlyArchivedBook> readArchivedContacts() throws DataLoadingException;

    Optional<ReadOnlyArchivedBook> readArchivedContacts(Path filePath) throws DataLoadingException;

    /**
     * Saves the given {@link ReadOnlyArchivedBook} to the storage.
     * @param archivedAddressBook cannot be null.
     * @throws IOException if there was any problem writing to the file.
     */
    void saveArchivedContacts(ReadOnlyArchivedBook archivedAddressBook) throws IOException;


    /**
     * Saves the given {@link ReadOnlyAddressBook} to the archived contacts storage.
     * @param archivedAddressBook cannot be null.
     * @throws IOException if there was any problem writing to the file.
     */
    void saveArchivedContacts(ReadOnlyArchivedBook archivedAddressBook, Path filePath) throws IOException;
}
