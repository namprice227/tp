package seedu.address.logic;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.commands.*;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.AddressBookParser;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Person;
import seedu.address.storage.Storage;

public class LogicManager implements Logic {
    public static final String FILE_OPS_ERROR_FORMAT = "Could not save data due to the following error: %s";
    public static final String FILE_OPS_PERMISSION_ERROR_FORMAT = "Could not save data to file %s due to insufficient permissions.";
    public static final String MESSAGE_COMMAND_RESTRICTED = "The %s command is restricted in %s mode.";

    private final Logger logger = LogsCenter.getLogger(LogicManager.class);
    private final Model model;
    private final Storage storage;
    private final AddressBookParser addressBookParser;

    private Optional<Command> pendingCommand = Optional.empty();
    private boolean isArchiveMode = false;

    private static final Set<Class<? extends Command>> ALLOWED_IN_ARCHIVE_MODE = Set.of(
            UnarchiveCommand.class, ListArchiveCommand.class
    );

    public LogicManager(Model model, Storage storage) {
        this.model = model;
        this.storage = storage;
        this.addressBookParser = new AddressBookParser();
    }

    @Override
    public CommandResult execute(String commandText) throws CommandException, ParseException {
        logger.info("----------------[USER COMMAND][" + commandText + "]");

        if (pendingCommand.isPresent()) {
            return handleConfirmation(commandText);
        }

        Command command = addressBookParser.parseCommand(commandText);
        if (!isCommandAllowed(command)) {
            throw new CommandException(String.format(MESSAGE_COMMAND_RESTRICTED, getCommandName(command),
                    isArchiveMode ? "archive" : "normal"));
        }

        CommandResult commandResult = command.execute(model);
        handleModeSwitch(commandResult);

        if (commandResult.requiresConfirmation()) {
            pendingCommand = Optional.of(command);
        }
        saveData();
        return commandResult;
    }

    private void handleModeSwitch(CommandResult commandResult) {
        if (commandResult.getListType() == CommandResult.ListType.ARCHIVE) {
            isArchiveMode = true;
        } else if (commandResult.getListType() == CommandResult.ListType.NORMAL) {
            isArchiveMode = false;
        }
    }

    private CommandResult handleConfirmation(String userInput) throws CommandException {
        if (userInput.equalsIgnoreCase("y")) {
            Command confirmedCommand = pendingCommand.get();
            pendingCommand = Optional.empty();
            CommandResult result = confirmedCommand.execute(model);
            saveData();
            return result;
        }
        pendingCommand = Optional.empty();
        return new CommandResult("Command cancelled");
    }

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

    private boolean isCommandAllowed(Command command) {
        if (isArchiveMode) {
            // Allow UnarchiveCommand, ListArchiveCommand, and ListCommand in archive mode
            return command instanceof UnarchiveCommand
                    || command instanceof ListArchiveCommand
                    || command instanceof ListCommand;
        } else {
            // In normal mode, disallow UnarchiveCommand
            return !(command instanceof UnarchiveCommand);
        }
    }

    private String getCommandName(Command command) {
        return command.getClass().getSimpleName().replace("Command", "").toLowerCase();
    }

    @Override
    public boolean isArchiveMode() {
        return isArchiveMode;
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
