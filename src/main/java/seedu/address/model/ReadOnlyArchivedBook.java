package seedu.address.model;

import javafx.collections.ObservableList;
import seedu.address.model.person.Person;

/**
 * Unmodifiable view of an archived contacts book.
 */
public interface ReadOnlyArchivedBook {
    /**
     * Returns an unmodifiable list of archived contacts.
     */
    ObservableList<Person> getArchivedContactList();
}
