package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.RELATIONSHIP_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_RELATIONSHIP_AMY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_RELATIONSHIP;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EmergencyCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Relationship;

public class EmergencyCommandParserTest {

    private EmergencyCommandParser parser = new EmergencyCommandParser();

    @Test
    public void parse_allFieldsPresent_success() throws ParseException {
        Index targetIndex = INDEX_FIRST_PERSON;
        String userInput = targetIndex.getOneBased() + NAME_DESC_AMY + PHONE_DESC_AMY + RELATIONSHIP_DESC_AMY;

        EmergencyCommand expectedCommand = new EmergencyCommand(targetIndex,
                new Name(VALID_NAME_AMY), new Phone(VALID_PHONE_AMY), new Relationship(VALID_RELATIONSHIP_AMY));

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_missingParts_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, EmergencyCommand.MESSAGE_USAGE);

        // no index specified
        assertParseFailure(parser, NAME_DESC_AMY + PHONE_DESC_AMY + RELATIONSHIP_DESC_AMY, expectedMessage);

        // no name specified
        assertParseFailure(parser, "1" + PHONE_DESC_AMY + RELATIONSHIP_DESC_AMY, expectedMessage);

        // no phone specified
        assertParseFailure(parser, "1" + NAME_DESC_AMY + RELATIONSHIP_DESC_AMY, expectedMessage);

        // no relationship specified
        assertParseFailure(parser, "1" + NAME_DESC_AMY + PHONE_DESC_AMY, expectedMessage);

        // no prefix specified
        assertParseFailure(parser, "1" + VALID_NAME_AMY + VALID_PHONE_AMY + VALID_RELATIONSHIP_AMY, expectedMessage);
    }

    @Test
    public void parse_invalidPreamble_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, EmergencyCommand.MESSAGE_USAGE);

        // negative index
        assertParseFailure(parser, "-5" + NAME_DESC_AMY + PHONE_DESC_AMY + RELATIONSHIP_DESC_AMY, expectedMessage);

        // zero index
        assertParseFailure(parser, "0" + NAME_DESC_AMY + PHONE_DESC_AMY + RELATIONSHIP_DESC_AMY, expectedMessage);

        // invalid arguments being parsed as preamble
        assertParseFailure(parser, "1 some random string" + NAME_DESC_AMY + PHONE_DESC_AMY + RELATIONSHIP_DESC_AMY,
                expectedMessage);

        // invalid prefix being parsed as preamble
        assertParseFailure(parser, "1 i/ string" + NAME_DESC_AMY + PHONE_DESC_AMY + RELATIONSHIP_DESC_AMY,
                expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid name
        assertParseFailure(parser, "1" + " n/James&" + PHONE_DESC_AMY + RELATIONSHIP_DESC_AMY,
                Name.MESSAGE_CONSTRAINTS);

        // invalid phone
        assertParseFailure(parser, "1" + NAME_DESC_AMY + " p/abc" + RELATIONSHIP_DESC_AMY,
                Phone.MESSAGE_CONSTRAINTS);

        // invalid relationship
        assertParseFailure(parser, "1" + NAME_DESC_AMY + PHONE_DESC_AMY + " r/",
                Relationship.MESSAGE_CONSTRAINTS);

        // two invalid values, only first invalid value reported
        assertParseFailure(parser, "1" + " n/James&" + " p/abc" + RELATIONSHIP_DESC_AMY,
                Name.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_allFieldsSpecified_success() throws ParseException {
        Index targetIndex = INDEX_FIRST_PERSON;
        String userInput = targetIndex.getOneBased() + NAME_DESC_AMY + PHONE_DESC_AMY + RELATIONSHIP_DESC_AMY;

        EmergencyCommand expectedCommand = new EmergencyCommand(targetIndex,
                new Name(VALID_NAME_AMY), new Phone(VALID_PHONE_AMY), new Relationship(VALID_RELATIONSHIP_AMY));

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_multipleRepeatedFields_failure() {
        // More extensive testing of duplicate parameter detections done by
        // AddCommandParserTest#parse_repeatedNonTagValue_failure()

        // Testing duplicate fields
        assertParseFailure(parser, "1" + NAME_DESC_AMY + PHONE_DESC_AMY + RELATIONSHIP_DESC_AMY + NAME_DESC_AMY
                + PHONE_DESC_AMY + RELATIONSHIP_DESC_AMY,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, EmergencyCommand.MESSAGE_USAGE));

        // invalid followed by valid
        assertParseFailure(parser, "1" + " n/James&" + PHONE_DESC_AMY + RELATIONSHIP_DESC_AMY + NAME_DESC_AMY
                + PHONE_DESC_AMY + RELATIONSHIP_DESC_AMY,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, EmergencyCommand.MESSAGE_USAGE));

        // valid followed by invalid
        assertParseFailure(parser, "1" + NAME_DESC_AMY + PHONE_DESC_AMY + RELATIONSHIP_DESC_AMY + " n/James&"
                + PHONE_DESC_AMY + RELATIONSHIP_DESC_AMY,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, EmergencyCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_resetTags_success() throws ParseException {
        Index targetIndex = INDEX_FIRST_PERSON;
        String userInput = targetIndex.getOneBased() + NAME_DESC_AMY + PHONE_DESC_AMY + RELATIONSHIP_DESC_AMY;

        EmergencyCommand expectedCommand = new EmergencyCommand(targetIndex,
                new Name(VALID_NAME_AMY), new Phone(VALID_PHONE_AMY), new Relationship(VALID_RELATIONSHIP_AMY));

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    /**
     * Asserts that the parsing of {@code userInput} is successful and the result matches {@code expectedCommand}
     */
    private void assertParseSuccess(EmergencyCommandParser parser, String userInput, EmergencyCommand expectedCommand) {
        try {
            EmergencyCommand command = parser.parse(userInput);
            assertEquals(expectedCommand, command);
        } catch (ParseException pe) {
            throw new IllegalArgumentException("Invalid userInput.", pe);
        }
    }

    /**
     * Asserts that the parsing of {@code userInput} is unsuccessful and the error message
     * equals to {@code expectedMessage}
     */
    private void assertParseFailure(EmergencyCommandParser parser, String userInput, String expectedMessage) {
        assertThrows(ParseException.class, expectedMessage, () -> parser.parse(userInput));
    }
}
