package seedu.address.storage;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.FileUtil;
import seedu.address.commons.util.JsonUtil;
import seedu.address.model.ReadOnlyArchivedBook;

/**
 * A class to access archived contacts stored as a JSON file on the hard disk.
 */
public class JsonArchivedBookStorage implements ArchivedBookStorage {
    private static final Logger logger = LogsCenter.getLogger(JsonArchivedBookStorage.class);

    private final Path filePath;

    public JsonArchivedBookStorage(Path filePath) {
        this.filePath = filePath;
    }

    @Override
    public Path getArchivedContactsFilePath() {
        return filePath;
    }

    @Override
    public Optional<ReadOnlyArchivedBook> readArchivedContacts() throws DataLoadingException {
        return readArchivedContacts(filePath);
    }

    /**
     * Reads the archived contacts from a specified file path.
     *
     * @param filePath location of the data. Cannot be null.
     * @throws DataLoadingException if loading the data from storage failed.
     */
    @Override
    public Optional<ReadOnlyArchivedBook> readArchivedContacts(Path filePath) throws DataLoadingException {
        requireNonNull(filePath);

        Optional<JsonSerializableArchivedBook> jsonArchivedBook = JsonUtil.readJsonFile(
            filePath, JsonSerializableArchivedBook.class);
        if (!jsonArchivedBook.isPresent()) {
            return Optional.empty();
        }

        try {
            return Optional.of(jsonArchivedBook.get().toModelType());
        } catch (IllegalValueException ive) {
            logger.info("Illegal values found in " + filePath + ": " + ive.getMessage());
            throw new DataLoadingException(ive);
        }
    }

    @Override
    public void saveArchivedContacts(ReadOnlyArchivedBook archivedBook) throws IOException {
        saveArchivedContacts(archivedBook, filePath);
    }

    /**
     * Saves the archived contacts to a specified file path.
     *
     * @param filePath location of the data. Cannot be null.
     */
    @Override
    public void saveArchivedContacts(ReadOnlyArchivedBook archivedBook, Path filePath) throws IOException {
        requireNonNull(archivedBook);
        requireNonNull(filePath);

        FileUtil.createIfMissing(filePath);
        JsonUtil.saveJsonFile(new JsonSerializableArchivedBook(archivedBook), filePath);
    }
}
