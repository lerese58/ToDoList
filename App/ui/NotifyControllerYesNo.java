package App.ui;

import App.bll.TaskService;
import App.bll.TaskServiceImpl;
import App.model.TaskDTO;
import App.utils.NotifyStatus;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class NotifyControllerYesNo {

    private UITask _uiTask;
    private TaskService _taskService = new TaskServiceImpl(MainController._currentUserID);

    @FXML
    private Button btnYes,
            btnNo;
    @FXML
    private Label labelTitle,
            labelTask;

    @FXML
    private void initialize() {

    }

    public void setUiTask(UITask uiTask) {
        _uiTask = uiTask;
    }

    public void setFields() {
        labelTask.setText(_uiTask.toString());
    }

    @FXML
    public void onButtonClicked(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        if (!(source instanceof Button))
            return;
        Button clickedBtn = (Button) source;
        Map<Long, NotifyStatus> tmp = new HashMap<>(_uiTask.getUserList());
        switch (clickedBtn.getId()) {
            case "btnNo":
                tmp.replace(MainController._currentUserID, NotifyStatus.CANCELLED);
                break;
            case "btnYes":
                tmp.replace(MainController._currentUserID, NotifyStatus.CONFIRMED);
        }
        _uiTask.setUserList(tmp);
        _taskService.update(_uiTask.getId(), new TaskDTO(_uiTask));
        closeDialog();
    }

    private void closeDialog() {
        setUiTask(null);
        ((Stage) btnYes.getScene().getWindow()).close();
    }
}
