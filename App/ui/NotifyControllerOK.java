package App.ui;

import App.bll.TaskService;
import App.bll.TaskServiceImpl;
import App.bll.UserService;
import App.bll.UserServiceImpl;
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

    private final TaskService _taskService = new TaskServiceImpl(MainController._currentUserID);
    private final UserService _userService = new UserServiceImpl();
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
        labelTitle.setText("\"" + _userService.getById(_uiTask.getOwner()).getLogin() + "\" has added you to executors for task: ");
        labelTask.setText(_uiTask.toString());
    }

    @FXML
    public void onButtonClicked(ActionEvent actionEvent) {
        Map<Long, NotifyStatus> tmp = new HashMap<>(_uiTask.getUserList());
        tmp.replace(MainController._currentUserID, NotifyStatus.CONFIRMED);
        _uiTask.setUserList(tmp);
        _taskService.update(_uiTask.getId(), new TaskDTO(_uiTask));
        closeDialog();
    }

    private void closeDialog() {
        setUiTask(null);
        ((Stage) btnOK.getScene().getWindow()).close();
    }
}
