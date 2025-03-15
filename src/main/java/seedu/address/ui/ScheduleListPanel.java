package seedu.address.ui;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;

public class ScheduleListPanel extends UiPart<Region> {
    private static final String FXML = "ScheduleListPanel.fxml";

    @FXML
    private ListView<seedu.address.model.schedule.Schedule> scheduleListView;

    public ScheduleListPanel(ObservableList<seedu.address.model.schedule.Schedule> scheduleList) {
        super(FXML);
        scheduleListView.setItems(scheduleList);
        scheduleListView.setCellFactory(listView -> new ScheduleListViewCell());
    }

    public void updateScheduleList(ObservableList<seedu.address.model.schedule.Schedule> newScheduleList) {
        scheduleListView.setItems(newScheduleList);
    }

    class ScheduleListViewCell extends ListCell<seedu.address.model.schedule.Schedule> {
        @Override
        protected void updateItem(seedu.address.model.schedule.Schedule schedule, boolean empty) {
            super.updateItem(schedule, empty);
            if (empty || schedule == null) {
                setText(null);
                setGraphic(null);
            } else {
                setText(schedule.getFormattedDetails());
            }
        }
    }
}
