package seedu.address.ui;

import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.person.Person;

/**
 * Panel containing the list of persons.
 */
public class PersonListPanel extends UiPart<Region> {
    private static final String FXML = "PersonListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(PersonListPanel.class);
    private Person previousSelectedPerson;

    @FXML
    private ListView<Person> personListView;

    @FXML
    private PersonDetailPanel personDetailPanel;

    /**
     * Creates a {@code PersonListPanel} with the given {@code ObservableList}.
     */
    public PersonListPanel(ObservableList<Person> personList,
                           PersonDetailPanel personDetailPanel) {
        super(FXML);
        this.personDetailPanel = personDetailPanel;
        personListView.setItems(personList);
        personListView.setCellFactory(listView -> new PersonListViewCell());

        personListView.getSelectionModel().selectedItemProperty()
            .addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    previousSelectedPerson = newValue;
                }
                updateDetailPanel(newValue);
            });

        personListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                Person selectedPerson = personListView.getSelectionModel().getSelectedItem();
                updateDetailPanel(selectedPerson);
            }
        });
    }

    private void updateDetailPanel(Person selectedPerson) {
        if (selectedPerson != null) {
            personDetailPanel.showPersonDetail(selectedPerson);
        }
    }

    public void setPersonList(ObservableList<Person> personList) {
        personListView.setItems(personList);
    }

    public void setArchivedPersonList(ObservableList<Person> archivedPersonList) {
        personListView.setItems(archivedPersonList);
    }


    /**
     * Custom {@code ListCell} that displays the graphics of a {@code Person} using a {@code PersonCard}.
     */
    class PersonListViewCell extends ListCell<Person> {
        @Override
        protected void updateItem(Person person, boolean empty) {
            super.updateItem(person, empty);

            if (empty || person == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new PersonCard(person, getIndex() + 1).getRoot());
            }
        }
    }
}
