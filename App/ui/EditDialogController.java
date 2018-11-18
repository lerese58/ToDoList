package App.ui;

import App.model.TaskCalendar;
import App.utils.Priority;
import App.utils.Status;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tornadofx.control.DateTimePicker;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class EditDialogController {

    private final Pattern correctTime;
    @FXML
    ChoiceBox<Priority> menuPriority;

    @FXML
    private Button
            btnOK,
            btnCancel;
    @FXML
    private TextField txtTitle;
    @FXML
    private DateTimePicker dateTimePicker;
    private UITask _uiTask;

    public EditDialogController() {
        correctTime = Pattern.compile("^\\d{2}:\\d{2} \\d{2}.\\d{2}.\\d{4}$");
    }

    public UITask getUiTask() {
        return _uiTask;
    }

    public void setUiTask(UITask uiTask) {
        if (uiTask != null) {
            txtTitle.setText(uiTask.getTitle());
            dateTimePicker.setDateTimeValue(new TaskCalendar(uiTask.getDeadline()).getDateTime());
            menuPriority.setValue(Priority.valueOf(uiTask.getPrio()));
        } else {
            txtTitle.setText("");
            dateTimePicker.setPromptText("");
            menuPriority.setValue(Priority.DEFAULT);
        }
        this._uiTask = uiTask;
    }

    @FXML
    private void initialize() {
        menuPriority.setItems(FXCollections.observableArrayList(Priority.values()));
        menuPriority.setOnInputMethodTextChanged(event -> menuPriority.setValue(menuPriority.getSelectionModel().getSelectedItem()));
    }

    public boolean editTask(ActionEvent actionEvent) { //TODO : add ability to add other users to executorList
        boolean titleFieldIsEmpty = txtTitle.getText().isEmpty();
        Button source = (Button) actionEvent.getSource();
        if (source.getId().equals("btnCancel")) {
            closeDialog();
            return false;
        } else if (!titleFieldIsEmpty) {
            ArrayList<Long> aloneExecutor = new ArrayList<>();
            aloneExecutor.add(MainDialogController._currentUserID);
            this.setUiTask(new UITask(MainDialogController._currentUserID,          //ownerID
                    aloneExecutor,                                                  //userList(till not available, it'll add only owner to executors)
                    txtTitle.getText(),                                             //title
                    new TaskCalendar(dateTimePicker.getDateTimeValue()).toString(), //Deadline
                    true,                                                   //isPersonal(till not available, by default 'true')
                    Status.IN_PROGRESS.toString(),                                  //status
                    menuPriority.getSelectionModel().getSelectedItem().toString()));//priority
            closeDialog();
            return true;
        } else {
            txtTitle.setPromptText("Input title");
        }
        return false;
    }

    @FXML
    private void closeDialog() {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void onCancelButton() {
        this.setUiTask(null);
        closeDialog();
    }
}
