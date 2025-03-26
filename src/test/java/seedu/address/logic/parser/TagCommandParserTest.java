package seedu.address.logic.parser;

import static seedu.address.logic.parser.CliSyntax.PREFIX_ALLERGY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CONDITION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG_DELETE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG_EDIT;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.TagCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.tag.Tag;

import java.util.HashSet;
import java.util.Set;

public class TagCommandParserTest {

    private final TagCommandParser parser = new TagCommandParser();

    // Test adding tags
    @Test
    public void parse_addTags_success() throws ParseException {
        String userInput = "1 " + PREFIX_ALLERGY + "Peanuts " + PREFIX_CONDITION + "Asthma";
        TagCommand tagCommand = parser.parse(userInput);

        Set<Tag> expectedAllergies = new HashSet<>();
        expectedAllergies.add(new Tag("Peanuts"));
        Set<Tag> expectedConditions = new HashSet<>();
        expectedConditions.add(new Tag("Asthma"));

        TagCommand expectedTagCommand = new TagCommand(INDEX_FIRST_PERSON, expectedAllergies, expectedConditions,
                new HashSet<>(), new HashSet<>(), null, null);

        assertEquals(tagCommand, expectedTagCommand);
    }

    // Test deleting a tag
    @Test
    public void parse_deleteTag_success() throws ParseException {
        String userInput = "1 " + PREFIX_TAG_DELETE + "Peanuts";
        TagCommand tagCommand = parser.parse(userInput);

        Set<Tag> tagsToDelete = new HashSet<>();
        tagsToDelete.add(new Tag("Peanuts"));

        TagCommand expectedTagCommand = new TagCommand(INDEX_FIRST_PERSON, new HashSet<>(), new HashSet<>(),
                new HashSet<>(), tagsToDelete, null, null);

        assertEquals(tagCommand, expectedTagCommand);
    }

    // Test editing a tag
    @Test
    public void parse_editTag_success() throws ParseException {
        String userInput = "1 " + PREFIX_TAG_EDIT + "Medisave=Prudential";
        TagCommand tagCommand = parser.parse(userInput);

        Tag oldTag = new Tag("Medisave");
        Tag newTag = new Tag("Prudential");

        TagCommand expectedTagCommand = new TagCommand(INDEX_FIRST_PERSON, new HashSet<>(), new HashSet<>(),
                new HashSet<>(), new HashSet<>(), oldTag, newTag);

        assertEquals(tagCommand, expectedTagCommand);
    }

    // Test invalid format for editing a tag
    @Test
    public void parse_invalidEditTag_throwsParseException() {
        String userInput = "1 " + PREFIX_TAG_EDIT + "Medisave-Prudential";

        // Check if the parser throws a ParseException
        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }

    // Test invalid index format
    @Test
    public void parse_invalidIndex_throwsParseException() {
        String userInput = "abc " + PREFIX_ALLERGY + "Peanuts";

        // Check if the parser throws a ParseException
        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }

    // Test empty input
    @Test
    public void parse_emptyInput_throwsParseException() {
        String userInput = "";

        // Check if the parser throws a ParseException
        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }

    // Test missing prefix for tag (e.g., no allergy prefix)
    @Test
    public void parse_missingPrefix_throwsParseException() {
        String userInput = "1 Peanuts";  // Missing the allergy prefix

        // Check if the parser throws a ParseException
        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }

    // Test invalid command format (multiple commands)
    @Test
    public void parse_invalidCommandFormat_throwsParseException() {
        String userInput = "1 " + PREFIX_ALLERGY + "Peanuts " + PREFIX_TAG_DELETE + "Asthma";

        // Check if the parser throws a ParseException due to invalid command format
        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }

    // Test empty preamble (no index)
    @Test
    public void parse_emptyPreamble_throwsParseException() {
        String userInput = " " + PREFIX_ALLERGY + "Peanuts";

        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }
}
