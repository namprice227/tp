package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.HelpCommand.SHOWING_HELP_MESSAGE;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;

public class HelpCommandTest {
    private Model model = new ModelManager();
    private Model expectedModel = new ModelManager();

    @Test
    public void executeHelpNoArgumentsSuccess() {
        CommandResult expectedCommandResult = new CommandResult(SHOWING_HELP_MESSAGE, true, false, false);
        assertCommandSuccess(new HelpCommand("help"), model, expectedCommandResult, expectedModel);
    }

    @Test
    public void executeHelpWithArgumentsFailure() {
        assertThrows(CommandException.class, () -> new HelpCommand("help extra").execute(model));
    }
}
