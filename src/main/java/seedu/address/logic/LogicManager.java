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
import seedu.address.logic.commands.ListArchiveCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.UnarchiveCommand;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.AddressBookParser;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Person;
import seedu.address.storage.Storage;

/**
 * The LogicManager class handles command execution,
 * manages mode switching between normal and archive modes,
 * and ensures data persistence.
 */
public class LogicManager implements Logic {
    public static final String FILE_OPS_ERROR_FORMAT =
            "Could not save data due to the following error: %s";

    public static final String FILE_OPS_PERMISSION_ERROR_FORMAT =
            "Could not save data to file %s due to insufficient permissions.";

    public static final String MESSAGE_COMMAND_RESTRICTED =
            "The %s command is restricted in %s mode.";

    private final Logger logger = LogsCenter.getLogger(LogicManager.class);
    private final Model model;
    private final Storage storage;
    private final AddressBookParser addressBookParser;

    private Optional<Command> pendingCommand = Optional.empty();
    private boolean isArchiveMode = false;

    /**
     * Constructs a {@code LogicManager} with the given {@code Model} and {@code Storage}.
     *
     * @param model The application's data model.
     * @param storage The storage handler for data persistence.
     */
    public LogicManager(Model model, Storage storage) {
        this.model = model;
        this.storage = storage;
        addressBookParser = new AddressBookParser(model);
    }

    /**
     * Executes the given command.
     *
     * @param commandText The user input command.
     * @return The result of the executed command.
     * @throws CommandException If an error occurs during command execution.
     * @throws ParseException If the command input is invalid.
     */
    @Override
    public CommandResult execute(String commandText) throws CommandException, ParseException {
        logUserCommand(commandText);
        validateCommandText(commandText);

        if (pendingCommand.isPresent()) {
            return handleConfirmation(commandText);
        }

        Command command = parseAndValidateCommand(commandText);
        CommandResult commandResult = executeAndValidateCommand(command);

        handleModeSwitch(commandResult);

        if (commandResult.requiresConfirmation()) {
            pendingCommand = Optional.of(command);
        }

        saveDataSafely();
        return commandResult;
    }

    /**
     * Logs the user command for debugging purposes.
     *
     * @param commandText The full user input command string.
     */
    private void logUserCommand(String commandText) {
        logger.info("----------------[USER COMMAND][" + commandText + "]");
    }

    /**
     * Validates the user command text, ensuring it is not null.
     *
     * @param commandText The full user input command string.
     * @throws IllegalArgumentException If the command text is null.
     */
    private void validateCommandText(String commandText) {
        if (commandText == null) {
            throw new IllegalArgumentException("Command text cannot be null.");
        }
    }

    /**
     * Validates the user command text, ensuring it is not null.
     *
     * @param commandText The full user input command string.
     * @throws IllegalArgumentException If the command text is null.
     */
    private Command parseAndValidateCommand(String commandText) throws ParseException, CommandException {
        Command command = addressBookParser.parseCommand(commandText);
        if (command == null) {
            throw new AssertionError("Parser returned null command.");
        }
        if (!isCommandAllowed(command)) {
            throw new CommandException(String.format(MESSAGE_COMMAND_RESTRICTED,
                    getCommandName(command), isArchiveMode ? "archive" : "normal"));
        }
        return command;
    }

    /**
     * Executes the given Command object and validates the result.
     *
     * @param command The Command object to execute.
     * @return The result of executing the command.
     * @throws CommandException If an error occurs during command execution.
     */
    private CommandResult executeAndValidateCommand(Command command) throws CommandException {
        CommandResult commandResult = command.execute(model);
        if (commandResult == null) {
            throw new AssertionError("Command execution returned null result.");
        }
        return commandResult;
    }

    /**
     * Attempts to save data and logs any exceptions that occur.
     */
    private void saveDataSafely() throws CommandException {
        try {
            saveData();
        } catch (CommandException e) {
            logger.severe("Error saving data: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Handles mode switching based on command execution results.
     *
     * @param commandResult The result of the executed command.
     */
    private void handleModeSwitch(CommandResult commandResult) {
        if (commandResult.getListType() == CommandResult.ListType.ARCHIVE) {
            isArchiveMode = true;
        } else if (commandResult.getListType() == CommandResult.ListType.NORMAL) {
            isArchiveMode = false;
        }
    }

    /**
     * Handles user confirmation input for a pending command.
     *
     * @param userInput The user's response to a confirmation prompt.
     * @return The result of the confirmed or canceled command.
     * @throws CommandException If execution fails.
     */
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

    /**
     * Saves application data to storage.
     *
     * @throws CommandException If there is an issue saving data.
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

    /**
     * Determines whether a command is allowed in the current mode.
     *
     * @param command The command to check.
     * @return True if the command is allowed, false otherwise.
     */
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

    /**
     * Retrieves the command name in lowercase without the "Command" suffix.
     *
     * @param command The command instance.
     * @return The command name in lowercase.
     */
    private String getCommandName(Command command) {
        return command.getClass().getSimpleName().replace("Command", "").toLowerCase();
    }

    /**
     * Returns the current archive mode state.
     *
     * @return True if in archive mode, false otherwise.
     */
    @Override
    public boolean isArchiveMode() {
        return isArchiveMode;
    }

    /**
     * Returns the address book.
     *
     * @return The address book.
     */
    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return model.getAddressBook();
    }

    /**
     * Returns the filtered list of persons in normal mode.
     *
     * @return The filtered person list.
     */
    @Override
    public ObservableList<Person> getFilteredPersonList() {
        return model.getFilteredPersonList();
    }

    /**
     * Returns the filtered list of persons in archive mode.
     *
     * @return The filtered archived person list.
     */
    public ObservableList<Person> getFilteredArchivedPersonList() {
        return model.getFilteredArchivedPersonList();
    }

    /**
     * Returns the file path of the address book.
     *
     * @return The file path of the address book.
     */
    @Override
    public Path getAddressBookFilePath() {
        return model.getAddressBookFilePath();
    }

    /**
     * Returns the GUI settings.
     *
     * @return The GUI settings.
     */
    @Override
    public GuiSettings getGuiSettings() {
        return model.getGuiSettings();
    }

    /**
     * Sets the GUI settings.
     *
     * @param guiSettings The new GUI settings.
     */
    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        model.setGuiSettings(guiSettings);
    }
}
