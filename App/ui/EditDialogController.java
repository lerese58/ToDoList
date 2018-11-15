package App.ui;

import App.utils.Priority;
import App.utils.Status;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class EditDialogController {

    private final Pattern correctTime;
    @FXML
    ChoiceBox<Priority> menuPriority;
    private UITask uiTask;
    @FXML
    private Button btnOK,
            btnCancel;
    @FXML
    private TextField txtTitle,
            txtDeadline;

    public EditDialogController() {
        correctTime = Pattern.compile("^\\d{2}:\\d{2} \\d{2}.\\d{2}.\\d{4}$");

    }

    public UITask getUiTask() {
        return uiTask;
    }

    public void setUiTask(UITask uiTask) {
        if (uiTask != null) {
            txtTitle.setText(uiTask.getTitle());
            txtDeadline.setText(uiTask.getDeadline());
            menuPriority.setValue(Priority.valueOf(uiTask.getPrio()));
        } else {
            txtTitle.setText("");
            txtDeadline.setText("");
            menuPriority.setValue(Priority.DEFAULT);
        }
        this.uiTask = uiTask;
    }

    @FXML
    private void initialize() {
        menuPriority.setItems(FXCollections.observableArrayList(Priority.values()));
        menuPriority.setOnInputMethodTextChanged(event -> menuPriority.setValue(menuPriority.getSelectionModel().getSelectedItem()));
    }

    public boolean editTask(ActionEvent actionEvent) { //TODO : add ability to add other users to executorList
        boolean titleFieldIsEmpty = txtTitle.getText().isEmpty();
        boolean deadlineFieldIsEmpty = txtDeadline.getText().isEmpty();
        boolean incorrectTimeFormat = !correctTime.matcher(txtDeadline.getText()).matches();
        Button source = (Button) actionEvent.getSource();
        if (source.getId().equals("btnCancel")) {
            closeDialog();
            return false;
        } else if (!titleFieldIsEmpty && !deadlineFieldIsEmpty && !incorrectTimeFormat) {
            ArrayList<Long> aloneExecutor = new ArrayList<>();
            aloneExecutor.add(MainDialogController._currentUserID);
            this.setUiTask(new UITask(MainDialogController._currentUserID,          //ownerID
                    aloneExecutor,                                                  //userList(till not available, it'll add only owner to executors)
                    txtTitle.getText(),                                             //title
                    txtDeadline.getText(),                                          //Deadline
                    true,                                                   //isPersonal(till not available, by default 'true')
                    Status.IN_PROGRESS.toString(),                                  //status
                    menuPriority.getSelectionModel().getSelectedItem().toString()));//priority
            closeDialog();
            return true;
        }
        if (titleFieldIsEmpty) {
            txtTitle.setPromptText("Input title");
            if (deadlineFieldIsEmpty) {
                txtDeadline.setPromptText("Input time");
            } else if (incorrectTimeFormat) {
                txtDeadline.setPromptText("Time should be in hh:mm dd.MM.yyyy");
            }
        } else if (incorrectTimeFormat)
            txtDeadline.setText("Time should be in hh:mm dd.MM.yyyy");
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
