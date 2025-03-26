package seedu.address.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.ArchivedBook;
import seedu.address.model.ReadOnlyArchivedBook;
import seedu.address.model.person.Person;

/**
 * An Immutable ArchivedBook that is serializable to JSON format.
 */
@JsonRootName(value = "archivedbook")
class JsonSerializableArchivedBook {
    private final List<JsonAdaptedPerson> persons;

    /**
     * Constructs a {@code JsonSerializableArchivedBook} with the given persons.
     */
    @JsonCreator
    public JsonSerializableArchivedBook(@JsonProperty("persons") List<JsonAdaptedPerson> persons) {
        this.persons = persons != null ? persons : new ArrayList<>();
    }

    /**
     * Converts a given {@code ReadOnlyArchivedBook} into this class for Jackson use.
     *
     * @param source future changes to this will not affect the created {@code JsonSerializableArchivedBook}.
     */
    public JsonSerializableArchivedBook(ReadOnlyArchivedBook source) {
        this.persons = source.getArchivedContactList().stream()
            .map(JsonAdaptedPerson::new)
            .collect(Collectors.toList());
    }

    /**
     * Converts this archived book into the model's {@code ArchivedBook} object.
     *
     * @throws IllegalValueException if there were any data constraints violated.
     */
    public ArchivedBook toModelType() throws IllegalValueException {
        ArchivedBook archivedBook = new ArchivedBook();
        for (JsonAdaptedPerson jsonAdaptedPerson : persons) {
            Person person = jsonAdaptedPerson.toModelType();
            archivedBook.addArchivedPerson(person);
        }
        return archivedBook;
    }
}
