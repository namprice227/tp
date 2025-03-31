package seedu.address.ui;

import java.util.Comparator;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Region;
import seedu.address.model.person.Person;

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
    private FlowPane tags;
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
        emergencyContactRelationship.setText(" " + person.getEmergencyContact().getRelationship());
        appointment.setText(person.getAppointment().toString());
        tags.getChildren().clear();
        person.getTags().stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .forEach(tag -> {
                    Label tagLabel = new Label(tag.tagName);
                    tagLabel.setStyle("-fx-background-color: " + getColorForTag(tag.tagName) + ";"
                        + "-fx-text-fill: white; -fx-padding: 3px; -fx-background-radius: 5px;");
                    tags.getChildren().add(tagLabel);
                });
    }

    /**
     * Assigns a colour to a tag based on its name.
     */
    private String getColorForTag(String tagName) {
        String[] colors = {"#FF6B6B", "#1DD1A1", "#54A0FF"};
        int index = Math.abs(tagName.hashCode()) % colors.length;
        return colors[index];
    }

}
