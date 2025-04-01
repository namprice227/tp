package seedu.address.ui;

import java.util.Comparator;
import java.util.Set;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Region;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;

/**
 * A UI component to show the detailed info of a person.
 */
public class PersonDetail extends UiPart<Region> {
    private static final String FXML = "PersonDetail.fxml";

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label phone;
    @FXML
    private Label address;
    @FXML
    private Label email;
    @FXML
    private FlowPane allergyTagsPane;
    @FXML
    private FlowPane conditionTagsPane;
    @FXML
    private FlowPane insuranceTagsPane;
    @FXML
    private Label appointment;
    @FXML
    private Label emergencyContactName;
    @FXML
    private Label emergencyContactPhone;
    @FXML
    private Label emergencyContactRelationship;
    @FXML
    private VBox tagSection;
    @FXML
    private VBox emergencyContactSection;

    public PersonDetail() {
        super(FXML);
        cardPane.setVisible(false);
        cardPane.setManaged(false);
    }

    /**
     * Populates the panel with details of the selected person.
     */
    public void setPerson(Person person) {
        cardPane.setVisible(true);
        cardPane.setManaged(true);
        name.setText(person.getName().value);
        phone.setText("📞 " + person.getPhone().value);
        address.setText("🏠 " + person.getAddress().value);
        email.setText("📧 " + person.getEmail().value);
        emergencyContactName.setText("👤 " + person.getEmergencyContact().getName());
        emergencyContactPhone.setText("📱 " + person.getEmergencyContact().getPhone());
        emergencyContactRelationship.setText("(" + person.getEmergencyContact().getRelationship() + ")");
        appointment.setText(person.getAppointment().toString());
        allergyTagsPane.getChildren().clear();
        conditionTagsPane.getChildren().clear();
        insuranceTagsPane.getChildren().clear();
        // Populate tags by category
        populateTags(allergyTagsPane, person.getAllergyTags(), "#FF6B6B"); // Allergy tags in red
        populateTags(conditionTagsPane, person.getConditionTags(), "#1DD1A1"); // Condition tags in green
        populateTags(insuranceTagsPane, person.getInsuranceTags(), "#54A0FF"); // Insurance tags in blue
    }

    /**
     * Populates a FlowPane with tags from the given set, using the specified color.
     *
     * @param flowPane The FlowPane to populate.
     * @param tags     The set of tags to add.
     * @param color    The background color for the tags.
     */
    private void populateTags(FlowPane flowPane, Set<Tag> tags, String color) {
        flowPane.getChildren().clear(); // Clear existing tags

        // Sort and process the tags
        tags.stream()
                .sorted(Comparator.comparing(tag -> tag.tagName)) // Sort tags alphabetically by name
                .forEach(tag -> {
                    // Extract the tag name
                    String tagName = tag.tagName;

                    // Create a Label for the tag
                    Label tagLabel = new Label(tagName);
                    tagLabel.setStyle("-fx-background-color: " + color + ";"
                            + "-fx-text-fill: white; -fx-padding: 1px; -fx-background-radius: 1px;");

                    // Add the Label to the FlowPane
                    flowPane.getChildren().add(tagLabel);
                });
    }
}

