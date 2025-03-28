package seedu.address.logic;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.AddressBookParser;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Person;
import seedu.address.storage.Storage;

/**
 * The main LogicManager of the app.
 */
public class LogicManager implements Logic {
    public static final String FILE_OPS_ERROR_FORMAT = "Could not save data due to the following error: %s";

    public static final String FILE_OPS_PERMISSION_ERROR_FORMAT =
            "Could not save data to file %s due to insufficient permissions to write to the file or the folder.";

    private final Logger logger = LogsCenter.getLogger(LogicManager.class);

    private final Model model;
    private final Storage storage;
    private final AddressBookParser addressBookParser;

    private Optional<Command> pendingCommand = Optional.empty();

    /**
     * Constructs a {@code LogicManager} with the given {@code Model} and {@code Storage}.
     */
    public LogicManager(Model model, Storage storage) {
        this.model = model;
        this.storage = storage;
        addressBookParser = new AddressBookParser(model);
    }

    @Override
    public CommandResult execute(String commandText) throws CommandException, ParseException {
        logger.info("----------------[USER COMMAND][" + commandText + "]");
        Command command;
        if (pendingCommand.isPresent()) {
            return handleConfirmation(commandText);
        }

        CommandResult commandResult;
        command = addressBookParser.parseCommand(commandText);
        commandResult = command.execute(model);
        if (commandResult.requiresConfirmation()) {
            pendingCommand = Optional.of(command); // Store command for next execution
        }
        saveData();

        return commandResult;
    }

    /**
     * Handles user input when a confirmation command is pending.
     */
    private CommandResult handleConfirmation(String userInput) throws CommandException {
        if (userInput.equalsIgnoreCase("y")) {
            Command confirmedCommand = pendingCommand.get();
            pendingCommand = Optional.empty(); // Reset state
            CommandResult result = confirmedCommand.execute(model);
            saveData();
            return result;
        } else {
            pendingCommand = Optional.empty(); // Reset state
            return new CommandResult("Command cancelled");
        }
    }

    /**
     * Saves data to storage, handling any potential errors.
     */
    private void saveData() throws CommandException {
        try {
            storage.saveAddressBook(model.getAddressBook());
            storage.saveArchivedContacts(model.getArchivedBook());
        } catch (AccessDeniedException e) {
            throw new CommandException(String.format(FILE_OPS_PERMISSION_ERROR_FORMAT, e.getMessage()), e);
        } catch (IOException ioe) {
            throw new CommandException(String.format(FILE_OPS_ERROR_FORMAT, ioe.getMessage()), ioe);
        }
    }


    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return model.getAddressBook();
    }

    @Override
    public ObservableList<Person> getFilteredPersonList() {
        return model.getFilteredPersonList();
    }

    public ObservableList<Person> getFilteredArchivedPersonList() {
        return model.getFilteredArchivedPersonList();
    }

    @Override
    public Path getAddressBookFilePath() {
        return model.getAddressBookFilePath();
    }

    @Override
    public GuiSettings getGuiSettings() {
        return model.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        model.setGuiSettings(guiSettings);
    }

}
