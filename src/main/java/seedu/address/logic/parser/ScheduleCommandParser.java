package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.ScheduleCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.DateTime;

/**
 * Parses input arguments and creates a new ScheduleCommand object
 */
public class ScheduleCommandParser implements Parser<ScheduleCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the ScheduleCommand
     * and returns a ScheduleCommand object for execution.
     * @throws ParseException if the user input does not conform to the expected format
     */
    public ScheduleCommand parse(String args) throws ParseException {
        requireNonNull(args);

        String trimmedArgs = args.trim();

        // If user types "schedule -s", show the sorted schedule list
        if (trimmedArgs.equals("-s")) {
            return new ScheduleCommand();
        }

        String[] splitArgs = args.trim().split("\\s+");

        if (splitArgs.length < 3) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ScheduleCommand.MESSAGE_USAGE));
        }

        // Parse patient ID (Index)
        Index index;
        try {
            index = ParserUtil.parseIndex(splitArgs[0]);
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ScheduleCommand.MESSAGE_USAGE), pe);
        }

        // Parse DateTime
        String dateTimeString = splitArgs[1] + " " + splitArgs[2]; // Combine date and time
        if (!DateTime.isValidDateTime(dateTimeString)) {
            throw new ParseException(ScheduleCommand.MESSAGE_INVALID_DATETIME);
        }

        DateTime dateTime = new DateTime(dateTimeString);

        return new ScheduleCommand(index, dateTime);
    }
}
