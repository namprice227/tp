package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;

/**
 * A UI component to show the detailed info of a person.
 */
public class PersonDetailPanel extends UiPart<Region> {
    private static final String FXML = "PersonDetailPanel.fxml";

    @FXML
    private Label name;
    @FXML
    private Label phone;
    @FXML
    private Label address;
    @FXML
    private Label email;
    @FXML
    private Label emergencyName;
    @FXML
    private Label emergencyPhone;
    @FXML
    private HBox tagsContainer;

    public PersonDetailPanel() {
        super(FXML);
    }

    /**
     * Populates the panel with details of the selected person.
     */
    public void setPerson(Person person) {
        name.setText(person.getName().value);
        phone.setText("📞 " + person.getPhone().value);
        address.setText("🏠 " + person.getAddress().value);
        email.setText("📧 " + person.getEmail().value);

        emergencyName.setText("👤 " + person.getEmergencyContact().getName());
        emergencyPhone.setText("📱 " + person.getEmergencyContact().getPhone());

        tagsContainer.getChildren().clear();
        for (Tag tag : person.getTags()) {
            Label tagLabel = new Label(tag.tagName);
            tagLabel.getStyleClass().add("tag");

            if (tag.tagName.toLowerCase().contains("peanut") || tag.tagName.toLowerCase().contains("allergy")) {
                tagLabel.getStyleClass().add("tag-allergy");
            } else if (tag.tagName.toLowerCase().contains("insurance")) {
                tagLabel.getStyleClass().add("tag-insurance");
            } else {
                tagLabel.getStyleClass().add("tag-condition");
            }

            tagsContainer.getChildren().add(tagLabel);
        }
    }
}
