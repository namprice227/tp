package seedu.address.ui;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.address.model.person.Appointment;

/**
 * Panel containing the list of appointments.
 */
public class AppointmentListPanel extends UiPart<Region> {
    // Change the FXML file name to the new name.
    private static final String FXML = "AppointmentListPanel.fxml";

    @FXML
    private ListView<Appointment> scheduleListView;

    /**
     * Creates an {@code AppointmentListPanel} with the given {@code ObservableList} of appointments.
     */
    public AppointmentListPanel(ObservableList<Appointment> appointmentList) {
        super(FXML);
        scheduleListView.setItems(appointmentList);
        scheduleListView.setCellFactory(listView -> new ScheduleListViewCell());
    }

    /**
     * Updates the list view with a new list of appointments.
     */
    public void updateScheduleList(ObservableList<Appointment> newAppointmentList) {
        scheduleListView.setItems(newAppointmentList);
    }

    /**
     * Custom {@code ListCell} that displays the details of an {@code Appointment} using its formatted details.
     */
    class ScheduleListViewCell extends ListCell<Appointment> {
        @Override
        protected void updateItem(Appointment appointment, boolean empty) {
            super.updateItem(appointment, empty);
            if (empty || appointment == null) {
                setText(null);
                setGraphic(null);
            } else {
                setText(appointment.getFormattedDetails());
            }
        }
    }
}
