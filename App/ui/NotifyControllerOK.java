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

public class NotifyControllerOK {

    private final TaskService _taskService = new TaskServiceImpl();
    private UITask _uiTask;

    @FXML
    private Label labelTitle,
            labelTask;
    @FXML
    private Button btnOK;

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
        Map<Long, NotifyStatus> tmp = new HashMap<>(_uiTask.getUserList());
        tmp.put(MainController._currentUser.getId(), NotifyStatus.CONFIRMED);
        _uiTask.setUserList(tmp);
        //_uiTask.getUserList().replace(MainController._currentUser, NotifyStatus.CONFIRMED);
        _taskService.update(_uiTask.getId(), new TaskDTO(_uiTask));
        setUiTask(null);
        ((Stage) btnOK.getScene().getWindow()).close();
    }
}
