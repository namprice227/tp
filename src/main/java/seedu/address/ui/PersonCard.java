package seedu.address.ui;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.person.DateTime;
import seedu.address.model.person.Person;

/**
 * A UI component that displays information of a {@code Person}.
 */
public class PersonCard extends UiPart<Region> {

    private static final String FXML = "PersonListCard.fxml";

    public final Person person;
    private final boolean showScheduleMode;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label phone;
    @FXML
    private Label address;
    @FXML
    private Label email;
    @FXML
    private Label schedule; // Label for schedules
    @FXML
    private FlowPane tags;

    /**
     * Creates a {@code PersonCard} with the given {@code Person} and index to display.
     */
    public PersonCard(Person person, int displayedIndex, boolean showScheduleMode) {
        super(FXML);
        this.person = person;
        this.showScheduleMode = showScheduleMode;

        id.setText(displayedIndex + ". ");
        name.setText(person.getName().fullName);
        phone.setText(person.getPhone().value);
        address.setText(person.getAddress().value);
        email.setText(person.getEmail().value);

        person.getTags().stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .forEach(tag -> tags.getChildren().add(new Label(tag.tagName)));

        // Ensure schedule label is properly initialized
        if (schedule == null) {
            schedule = new Label();
            cardPane.getChildren().add(schedule); // Ensure it is added to UI
        }

        if (showScheduleMode) {
            showScheduleView();
        } else {
            showContactView();
        }
    }

    /**
     * Displays the default contact view (hides schedule).
     */
    private void showContactView() {
        schedule.setVisible(false);
        schedule.setManaged(false);
    }

    /**
     * Displays the schedule view (hides normal contact details).
     */
    private void showScheduleView() {
        List<DateTime> schedules = person.getDateTimeList();
        if (schedules.isEmpty()) {
            schedule.setText("No schedules available.");
        } else {
            schedule.setText(schedules.stream()
                    .map(DateTime::toString)
                    .collect(Collectors.joining("\n")));
        }
        schedule.setVisible(true);
        schedule.setManaged(true);
    }
}
